"use client"

import { useState, useCallback, useEffect, forwardRef, useImperativeHandle } from "react"
import { Field, FieldLabel, FieldError } from "@/shared/ui/field"
import { Input } from "@/shared/ui/input"
import { Button } from "@/shared/ui/button"
import { Separator } from "@/shared/ui/separator"
import { Badge } from "@/shared/ui/badge"
import {
  AlertCircleIcon,
  PlusIcon,
  Trash2Icon,
  EditIcon,
  CheckIcon,
  XIcon,
} from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { cn } from "@/shared/lib/utils"
import {
  getVigencias,
  createVigencia,
  updateVigencia,
  deleteVigencia,
} from "@/features/predios/lib/api"
import type {
  PredioVigencia,
  CreateVigenciaRequest,
  UpdateVigenciaRequest,
  ApiErrorResponse,
} from "@/features/predios/lib/types"
import { createVigenciaSchema } from "@/features/predios/lib/schemas"
import { estadoVigenciaOptions, ESTADO_VIGENCIA_COLORS } from "@/features/predios/lib/constants"
import { z } from "zod/v4"

/* ------------------------------------------------------------------ */
/*  Types                                                              */
/* ------------------------------------------------------------------ */

interface InfoEconomicaFormProps {
  /** UUID del predio */
  predioId: string
  /** Callback cuando se guarda exitosamente */
  onSaved: () => void
  /** Callback para ir al paso anterior */
  onBack: () => void
}

type VigenciaFormData = z.infer<typeof createVigenciaSchema>

interface FormErrors {
  vigencia?: string
  numeroInmueble?: string
  fechaRefPago?: string
  referenciaPagos?: string
  avaluoCatastral?: string
  impuestoPredial?: string
  interesesPredial?: string
  sac?: string
  interesesSac?: string
  descuentos?: string
  sobretasaAmbiental?: string
  tarifas?: string
  general?: string
}

/* ------------------------------------------------------------------ */
/*  Initial state                                                      */
/* ------------------------------------------------------------------ */

const INITIAL_FORM: VigenciaFormData = {
  vigencia: new Date().getFullYear(),
  numeroInmueble: "",
  fechaRefPago: null,
  referenciaPagos: null,
  avaluoCatastral: 0,
  impuestoPredial: 0,
  interesesPredial: 0,
  sac: 0,
  interesesSac: 0,
  descuentos: 0,
  sobretasaAmbiental: 0,
  tarifas: 0,
}

/* ------------------------------------------------------------------ */
/*  Helpers                                                            */
/* ------------------------------------------------------------------ */

function formatCurrency(value: number): string {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(value)
}

function calcularTotal(data: VigenciaFormData): number {
  return (
    (data.impuestoPredial ?? 0) +
    (data.interesesPredial ?? 0) +
    (data.sac ?? 0) +
    (data.interesesSac ?? 0) +
    (data.sobretasaAmbiental ?? 0) -
    (data.descuentos ?? 0)
  )
}

/* ------------------------------------------------------------------ */
/*  Component                                                          */
/* ------------------------------------------------------------------ */

export const InfoEconomicaForm = forwardRef<{ triggerSave: () => Promise<void> }, InfoEconomicaFormProps>(
  function InfoEconomicaForm({ predioId, onSaved, onBack }, _ref) {
  const [vigencias, setVigencias] = useState<PredioVigencia[]>([])
  const [isLoadingVigencias, setIsLoadingVigencias] = useState(true)
  const [editingVigenciaId, setEditingVigenciaId] = useState<number | null>(null)
  const [isCreatingNew, setIsCreatingNew] = useState(false)
  const [form, setForm] = useState<VigenciaFormData>(INITIAL_FORM)
  const [errors, setErrors] = useState<FormErrors>({})
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [generalError, setGeneralError] = useState<string | null>(null)

  /* ---- Cargar vigencias ---- */
  useEffect(() => {
    let cancelled = false
    async function load() {
      try {
        const data = await getVigencias(predioId)
        if (!cancelled) setVigencias(data)
      } catch {
        if (!cancelled) notify.error("Error al cargar vigencias")
      } finally {
        if (!cancelled) setIsLoadingVigencias(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [predioId])

  /* ---- Helpers ---- */
  const set = useCallback(
    <K extends keyof VigenciaFormData>(key: K, value: string | number | null) => {
      const numValue =
        typeof value === "string" && [
          "avaluoCatastral",
          "impuestoPredial",
          "interesesPredial",
          "sac",
          "interesesSac",
          "descuentos",
          "sobretasaAmbiental",
          "tarifas",
        ].includes(key)
          ? parseFloat(value) || 0
          : value
      setForm((prev) => ({ ...prev, [key]: numValue }))
      setErrors((prev) => ({ ...prev, [key]: undefined }))
    },
    [],
  )

  const validateForm = useCallback((): boolean => {
    const result = createVigenciaSchema.safeParse(form)
    if (result.success) return true

    const newErrors: FormErrors = {}
    for (const issue of result.error.issues) {
      const path = issue.path[0] as keyof FormErrors
      if (typeof path === "string") {
        newErrors[path] = issue.message
      }
    }
    setErrors(newErrors)
    return false
  }, [form])

  const resetForm = useCallback(() => {
    setForm(INITIAL_FORM)
    setErrors({})
    setIsCreatingNew(false)
  }, [])

  /* ---- Acciones de vigencia ---- */
  const handleEdit = useCallback((vigencia: PredioVigencia) => {
    setEditingVigenciaId(vigencia.id)
    setForm({
      vigencia: vigencia.vigencia,
      numeroInmueble: vigencia.numeroInmueble,
      fechaRefPago: vigencia.fechaRefPago,
      referenciaPagos: vigencia.referenciaPagos,
      avaluoCatastral: vigencia.avaluoCatastral,
      impuestoPredial: vigencia.impuestoPredial,
      interesesPredial: vigencia.interesesPredial,
      sac: vigencia.sac,
      interesesSac: vigencia.interesesSac,
      descuentos: vigencia.descuentos,
      sobretasaAmbiental: vigencia.sobretasaAmbiental,
      tarifas: vigencia.tarifas,
    })
    setErrors({})
    setIsCreatingNew(false)
  }, [])

  const handleCancelEdit = useCallback(() => {
    setEditingVigenciaId(null)
    resetForm()
  }, [resetForm])

  const handleDelete = useCallback(
    async (vigenciaId: number) => {
      if (!confirm("Esta seguro de eliminar esta vigencia?")) return

      try {
        await deleteVigencia(predioId, vigenciaId)
        setVigencias((prev) => prev.filter((v) => v.id !== vigenciaId))
        notify.success("Vigencia eliminada")
      } catch {
        notify.error("Error al eliminar vigencia")
      }
    },
    [predioId],
  )

  const handleSubmit = useCallback(async (e: React.FormEvent) => {
    e.preventDefault()
    setGeneralError(null)

    if (!validateForm()) {
      notify.error("Formulario incompleto", {
        description: "Revise los campos marcados en rojo",
      })
      return
    }

    setIsSubmitting(true)
    try {
      if (editingVigenciaId !== null) {
        // Update existing
        const updates: UpdateVigenciaRequest = {
          vigencia: form.vigencia,
          numeroInmueble: form.numeroInmueble,
          fechaRefPago: form.fechaRefPago,
          referenciaPagos: form.referenciaPagos,
          avaluoCatastral: form.avaluoCatastral,
          impuestoPredial: form.impuestoPredial,
          interesesPredial: form.interesesPredial,
          sac: form.sac,
          interesesSac: form.interesesSac,
          descuentos: form.descuentos,
          sobretasaAmbiental: form.sobretasaAmbiental,
          tarifas: form.tarifas,
        }
        const updated = await updateVigencia(
          predioId,
          editingVigenciaId,
          updates,
        )
        setVigencias((prev) =>
          prev.map((v) => (v.id === editingVigenciaId ? updated : v)),
        )
        notify.success("Vigencia actualizada")
      } else {
        // Create new
        const request: CreateVigenciaRequest = {
          vigencia: form.vigencia,
          numeroInmueble: form.numeroInmueble,
          fechaRefPago: form.fechaRefPago,
          referenciaPagos: form.referenciaPagos,
          avaluoCatastral: form.avaluoCatastral,
          impuestoPredial: form.impuestoPredial,
          interesesPredial: form.interesesPredial,
          sac: form.sac,
          interesesSac: form.interesesSac,
          descuentos: form.descuentos,
          sobretasaAmbiental: form.sobretasaAmbiental,
          tarifas: form.tarifas,
        }
        const created = await createVigencia(predioId, request)
        setVigencias((prev) => [...prev, created])
        notify.success("Vigencia creada")
      }
      resetForm()
    } catch (error) {
      if (error instanceof Error) {
        try {
          const raw = error.message.replace(/^API error \d+: /, "")
          const apiError: ApiErrorResponse = JSON.parse(raw)
          setGeneralError(apiError.message)
          notify.error("Error al guardar", {
            description: apiError.message,
          })
        } catch {
          setGeneralError("Ocurrio un error al guardar. Intente de nuevo.")
          notify.error("Error al guardar", {
            description: "Ocurrio un error inesperado",
          })
        }
      } else {
        setGeneralError("Ocurrio un error inesperado.")
        notify.error("Error al guardar", {
          description: "Ocurrio un error inesperado",
        })
      }
    } finally {
      setIsSubmitting(false)
    }
  }, [form, editingVigenciaId, validateForm, updateVigencia, createVigencia, resetForm])

  /* ---- Expose triggerSave via ref ---- */
  useImperativeHandle(_ref, () => ({ triggerSave: async () => { await handleSubmit({ preventDefault: () => {} } as React.FormEvent) } }), [handleSubmit])

  const total = calcularTotal(form)

  return (
    <div className="space-y-8">
      {/* ---------------------------------------------------------------- */}
      {/*  Instrucciones                                                    */}
      {/* ---------------------------------------------------------------- */}
      <div className="rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-700 dark:border-blue-800 dark:bg-blue-950/40 dark:text-blue-300">
        <p className="font-medium">Gestion de Vigencias Economicas</p>
        <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">
          Ingrese la informacion de avaluos e impuestos para cada vigencia anual del predio.
        </p>
      </div>

      {/* ---------------------------------------------------------------- */}
      {/*  Lista de Vigencias existentes                                   */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div className="flex items-center justify-between">
          <div>
            <h3 className="text-base font-semibold">Vigencias Registradas</h3>
            <p className="text-xs text-muted-foreground">
              {vigencias.length} vigencia(s) encontrada(s)
            </p>
          </div>
          {!isCreatingNew && editingVigenciaId === null && (
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => setIsCreatingNew(true)}
            >
              <PlusIcon className="size-4 mr-1" />
              Nueva Vigencia
            </Button>
          )}
        </div>

        {isLoadingVigencias ? (
          <div className="flex items-center justify-center py-8">
            <span className="size-6 border-2 border-muted-foreground/30 border-t-muted-foreground rounded-full animate-spin" />
          </div>
        ) : vigencias.length === 0 ? (
          <div className="rounded-lg border border-dashed border-border py-8 text-center">
            <p className="text-sm text-muted-foreground">
              No hay vigencias registradas
            </p>
            <Button
              type="button"
              variant="link"
              onClick={() => setIsCreatingNew(true)}
              className="mt-2"
            >
              Agregar la primera vigencia
            </Button>
          </div>
        ) : (
          <div className="rounded-lg border border-border overflow-hidden">
            <table className="w-full text-sm">
              <thead className="bg-muted/50">
                <tr>
                  <th className="px-4 py-3 text-left font-medium">Vigencia</th>
                  <th className="px-4 py-3 text-right font-medium">Avaluo</th>
                  <th className="px-4 py-3 text-right font-medium">Impuesto</th>
                  <th className="px-4 py-3 text-right font-medium">Total</th>
                  <th className="px-4 py-3 text-center font-medium">Estado</th>
                  <th className="px-4 py-3 text-center font-medium">Acciones</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border">
                {vigencias.map((v) => (
                  <tr key={v.id} className="hover:bg-muted/30">
                    <td className="px-4 py-3 font-medium">{v.vigencia}</td>
                    <td className="px-4 py-3 text-right tabular-nums">
                      {formatCurrency(v.avaluoCatastral)}
                    </td>
                    <td className="px-4 py-3 text-right tabular-nums">
                      {formatCurrency(v.impuestoPredial)}
                    </td>
                    <td className="px-4 py-3 text-right tabular-nums font-medium">
                      {formatCurrency(v.total)}
                    </td>
                    <td className="px-4 py-3 text-center">
                      <Badge
                        className={cn(
                          "text-xs",
                          ESTADO_VIGENCIA_COLORS[v.estado] ||
                            "bg-gray-100 text-gray-700",
                        )}
                      >
                        {estadoVigenciaOptions.find(
                          (o) => o.value === v.estado,
                        )?.label || v.estado}
                      </Badge>
                    </td>
                    <td className="px-4 py-3 text-center">
                      <div className="flex items-center justify-center gap-1">
                        <Button
                          type="button"
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEdit(v)}
                          disabled={
                            editingVigenciaId !== null || isCreatingNew
                          }
                        >
                          <EditIcon className="size-4" />
                        </Button>
                        <Button
                          type="button"
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDelete(v.id)}
                          disabled={
                            editingVigenciaId !== null || isCreatingNew
                          }
                          className="text-destructive hover:text-destructive"
                        >
                          <Trash2Icon className="size-4" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Formulario de vigencia (crear/editar)                          */}
      {/* ---------------------------------------------------------------- */}
      {(isCreatingNew || editingVigenciaId !== null) && (
        <section className="space-y-4">
          <div>
            <h3 className="text-base font-semibold">
              {editingVigenciaId !== null
                ? `Editar Vigencia ${editingVigenciaId}`
                : "Nueva Vigencia"}
            </h3>
            <Separator className="mt-2" />
          </div>

          <form onSubmit={handleSubmit} noValidate className="space-y-6">
            {/* Error general */}
            {generalError && (
              <div className="flex items-start gap-3 rounded-lg border border-destructive/30 bg-destructive/10 px-4 py-3 text-sm text-destructive">
                <AlertCircleIcon className="mt-0.5 size-4 shrink-0" />
                <span>{generalError}</span>
              </div>
            )}

            {/* ---------------------------------------------------------------- */}
            {/*  Campos basicos                                               */}
            {/* ---------------------------------------------------------------- */}
            <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2 lg:grid-cols-3">
              {/* Vigencia */}
              <Field>
                <FieldLabel htmlFor="vigencia">Ano de Vigencia</FieldLabel>
                <Input
                  id="vigencia"
                  type="number"
                  min={1900}
                  max={2100}
                  value={form.vigencia}
                  onChange={(e) => set("vigencia", e.target.value)}
                  className={cn(errors.vigencia && "border-destructive")}
                />
                <FieldError>{errors.vigencia}</FieldError>
              </Field>

              {/* Numero de Inmueble */}
              <Field>
                <FieldLabel htmlFor="numeroInmueble">Numero de Inmueble</FieldLabel>
                <Input
                  id="numeroInmueble"
                  value={form.numeroInmueble}
                  onChange={(e) => set("numeroInmueble", e.target.value)}
                  placeholder="Numero catastral"
                  className={cn(errors.numeroInmueble && "border-destructive")}
                />
                <FieldError>{errors.numeroInmueble}</FieldError>
              </Field>

              {/* Fecha de Referencia de Pago */}
              <Field>
                <FieldLabel htmlFor="fechaRefPago">
                  Fecha de Referencia de Pago
                </FieldLabel>
                <Input
                  id="fechaRefPago"
                  type="date"
                  value={form.fechaRefPago ?? ""}
                  onChange={(e) =>
                    set("fechaRefPago", e.target.value || null)
                  }
                  className={cn(errors.fechaRefPago && "border-destructive")}
                />
                <FieldError>{errors.fechaRefPago}</FieldError>
              </Field>

              {/* Referencia de Pagos */}
              <Field className="md:col-span-2">
                <FieldLabel htmlFor="referenciaPagos">
                  Referencias de Pago
                </FieldLabel>
                <Input
                  id="referenciaPagos"
                  value={form.referenciaPagos ?? ""}
                  onChange={(e) =>
                    set("referenciaPagos", e.target.value || null)
                  }
                  placeholder="Referencias de pago separadas por coma"
                  className={cn(errors.referenciaPagos && "border-destructive")}
                />
                <FieldError>{errors.referenciaPagos}</FieldError>
              </Field>
            </div>

            {/* ---------------------------------------------------------------- */}
            {/*  Valores economicos                                           */}
            {/* ---------------------------------------------------------------- */}
            <div>
              <h4 className="text-sm font-medium text-muted-foreground mb-3">
                Valores Monetarios (COP)
              </h4>
              <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2 lg:grid-cols-3">
                {/* Avaluo Catastral */}
                <Field>
                  <FieldLabel htmlFor="avaluoCatastral">
                    Avaluo Catastral
                  </FieldLabel>
                  <Input
                    id="avaluoCatastral"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.avaluoCatastral}
                    onChange={(e) => set("avaluoCatastral", e.target.value)}
                    className={cn(errors.avaluoCatastral && "border-destructive")}
                  />
                  <FieldError>{errors.avaluoCatastral}</FieldError>
                </Field>

                {/* Impuesto Predial */}
                <Field>
                  <FieldLabel htmlFor="impuestoPredial">
                    Impuesto Predial
                  </FieldLabel>
                  <Input
                    id="impuestoPredial"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.impuestoPredial}
                    onChange={(e) => set("impuestoPredial", e.target.value)}
                    className={cn(errors.impuestoPredial && "border-destructive")}
                  />
                  <FieldError>{errors.impuestoPredial}</FieldError>
                </Field>

                {/* Intereses Predial */}
                <Field>
                  <FieldLabel htmlFor="interesesPredial">
                    Intereses Predial
                  </FieldLabel>
                  <Input
                    id="interesesPredial"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.interesesPredial}
                    onChange={(e) => set("interesesPredial", e.target.value)}
                    className={cn(errors.interesesPredial && "border-destructive")}
                  />
                  <FieldError>{errors.interesesPredial}</FieldError>
                </Field>

                {/* SAC */}
                <Field>
                  <FieldLabel htmlFor="sac">SAC (Sobretasa Bomberil)</FieldLabel>
                  <Input
                    id="sac"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.sac}
                    onChange={(e) => set("sac", e.target.value)}
                    className={cn(errors.sac && "border-destructive")}
                  />
                  <FieldError>{errors.sac}</FieldError>
                </Field>

                {/* Intereses SAC */}
                <Field>
                  <FieldLabel htmlFor="interesesSac">
                    Intereses SAC
                  </FieldLabel>
                  <Input
                    id="interesesSac"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.interesesSac}
                    onChange={(e) => set("interesesSac", e.target.value)}
                    className={cn(errors.interesesSac && "border-destructive")}
                  />
                  <FieldError>{errors.interesesSac}</FieldError>
                </Field>

                {/* Descuentos */}
                <Field>
                  <FieldLabel htmlFor="descuentos">Descuentos</FieldLabel>
                  <Input
                    id="descuentos"
                    type="number"
                    min={0}
                    step={1000}
                    value={form.descuentos}
                    onChange={(e) => set("descuentos", e.target.value)}
                    className={cn(errors.descuentos && "border-destructive")}
                  />
                  <FieldError>{errors.descuentos}</FieldError>
                </Field>
              </div>
            </div>

            {/* ---------------------------------------------------------------- */}
            {/*  Porcentajes                                                  */}
            {/* ---------------------------------------------------------------- */}
            <div>
              <h4 className="text-sm font-medium text-muted-foreground mb-3">
                Tarifas y Porcentajes
              </h4>
              <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
                {/* Sobretasa Ambiental */}
                <Field>
                  <FieldLabel htmlFor="sobretasaAmbiental">
                    Sobretasa Ambiental (%)
                  </FieldLabel>
                  <Input
                    id="sobretasaAmbiental"
                    type="number"
                    min={0}
                    max={100}
                    step={0.01}
                    value={form.sobretasaAmbiental}
                    onChange={(e) =>
                      set("sobretasaAmbiental", e.target.value)
                    }
                    className={cn(
                      errors.sobretasaAmbiental && "border-destructive",
                    )}
                  />
                  <FieldError>{errors.sobretasaAmbiental}</FieldError>
                </Field>

                {/* Tarifas */}
                <Field>
                  <FieldLabel htmlFor="tarifas">Tarifas (%)</FieldLabel>
                  <Input
                    id="tarifas"
                    type="number"
                    min={0}
                    max={100}
                    step={0.01}
                    value={form.tarifas}
                    onChange={(e) => set("tarifas", e.target.value)}
                    className={cn(errors.tarifas && "border-destructive")}
                  />
                  <FieldError>{errors.tarifas}</FieldError>
                </Field>
              </div>
            </div>

            {/* ---------------------------------------------------------------- */}
            {/*  Total calculado                                              */}
            {/* ---------------------------------------------------------------- */}
            <div className="rounded-lg border border-border bg-muted/30 p-4">
              <div className="flex items-center justify-between">
                <span className="text-sm font-medium text-muted-foreground">
                  Total a Pagar (calculado)
                </span>
                <span className="text-xl font-bold tabular-nums">
                  {formatCurrency(total)}
                </span>
              </div>
              <p className="mt-1 text-xs text-muted-foreground">
                = Impuesto + Intereses + SAC + Intereses SAC + Sobretasa - Descuentos
              </p>
            </div>

            {/* ---------------------------------------------------------------- */}
            {/*  Botones                                                      */}
            {/* ---------------------------------------------------------------- */}
            <div className="flex items-center justify-end gap-3">
              <Button
                type="button"
                variant="outline"
                onClick={() => {
                  if (editingVigenciaId !== null) {
                    handleCancelEdit()
                  } else {
                    resetForm()
                  }
                }}
                disabled={isSubmitting}
              >
                <XIcon className="size-4 mr-1" />
                Cancelar
              </Button>
              <Button type="submit" disabled={isSubmitting}>
                {isSubmitting ? (
                  <span className="flex items-center gap-2">
                    <span className="size-4 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin" />
                    Guardando...
                  </span>
                ) : (
                  <span className="flex items-center gap-2">
                    <CheckIcon className="size-4" />
                    {editingVigenciaId !== null
                      ? "Actualizar Vigencia"
                      : "Crear Vigencia"}
                  </span>
                )}
              </Button>
            </div>
          </form>
        </section>
      )}

      {/* ---------------------------------------------------------------- */}
      {/*  Botones de navegacion del wizard                               */}
      {/* ---------------------------------------------------------------- */}
      <div className="flex items-center justify-end gap-3 border-t border-border pt-6">
        <Button type="button" variant="outline" onClick={onBack}>
          Anterior
        </Button>
        <Button type="button" onClick={onSaved}>
          <CheckIcon className="size-4 mr-1" />
          Continuar
        </Button>
      </div>
    </div>
  )
})
