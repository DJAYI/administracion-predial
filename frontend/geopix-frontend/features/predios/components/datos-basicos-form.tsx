"use client";

import { useState, useCallback, useEffect, forwardRef, useImperativeHandle } from "react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { Field, FieldLabel, FieldError } from "@/shared/ui/field";
import { Input } from "@/shared/ui/input";
import { Textarea } from "@/shared/ui/textarea";
import { Badge } from "@/shared/ui/badge";
import { Button } from "@/shared/ui/button";
import { Separator } from "@/shared/ui/separator";
import { AlertCircleIcon, SaveIcon } from "lucide-react";
import { notify } from "@/shared/lib/toast";
import { cn } from "@/shared/lib/utils";
import {
  createPredio,
  getDepartamentos,
  getMunicipios,
  checkMatricula,
} from "@/features/predios/lib/api";
import type {
  CreatePredioRequest,
  PredioResponse,
  ApiErrorResponse,
  DepartamentoDANE,
  MunicipioDANE,
} from "@/features/predios/lib/types";
import {
  tipoPredioOptions,
  destinoOptions,
  tipoSueloOptions,
  expedienteOptions,
  SI_NO_LABELS,
} from "@/features/predios/lib/constants";
import { datosBasicosSchema } from "@/features/predios/lib/schemas";
import { z } from "zod/v4";
import { Skeleton } from "@/shared/ui/skeleton";

/* ------------------------------------------------------------------ */
/*  Types                                                              */
/* ------------------------------------------------------------------ */

interface DatosBasicosFormProps {
  /** Callback when data is saved successfully, receives the created PredioResponse */
  onSaved?: (predio: PredioResponse) => void;
  /** Initial data for editing an existing predio */
  initialData?: Partial<z.infer<typeof datosBasicosSchema>>;
}

type FormData = z.infer<typeof datosBasicosSchema>

interface FormErrors {
  matriculaInmobiliaria?: string;
  npn?: string;
  referenciaCatastral?: string;
  departamento?: string;
  municipio?: string;
  tipoPredio?: string;
  destino?: string;
  direccion?: string;
  tipoSuelo?: string;
  servidumbre?: string;
  afectacion?: string;
  expediente?: string;
  alias?: string;
  observaciones?: string;
  general?: string;
}

/* ------------------------------------------------------------------ */
/*  Initial state                                                      */
/* ------------------------------------------------------------------ */

const INITIAL_FORM: FormData = {
  matriculaInmobiliaria: "",
  npn: null,
  referenciaCatastral: null,
  departamento: "",
  municipio: "",
  tipoPredio: "NPH",
  destino: "HABITACIONAL",
  direccion: "",
  tipoSuelo: "URBANO",
  servidumbre: "NO",
  afectacion: "NO",
  alias: null,
  observaciones: null,
  expediente: "DIGITAL",
}

/* ------------------------------------------------------------------ */
/*  Validation                                                         */
/* ------------------------------------------------------------------ */

function validateForm(data: FormData): FormErrors {
  const result = datosBasicosSchema.safeParse(data)
  
  if (result.success) {
    return {}
  }
  
  const errors: FormErrors = {}
  for (const issue of result.error.issues) {
    const path = issue.path[0] as keyof FormErrors
    if (typeof path === "string") {
      errors[path] = issue.message
    }
  }
  
  return errors
}

/* ------------------------------------------------------------------ */
/*  Component                                                          */
/* ------------------------------------------------------------------ */

export const DatosBasicosForm = forwardRef<{ triggerSave: () => Promise<void> }, DatosBasicosFormProps>(
  function DatosBasicosForm({ onSaved, initialData }, _ref) {
  const merged: FormData = {
    ...INITIAL_FORM,
    ...initialData,
  }

  const [form, setForm] = useState<FormData>(merged)
  const [errors, setErrors] = useState<FormErrors>({})
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [generalError, setGeneralError] = useState<string | null>(null)
  const [matriculaChecking, setMatriculaChecking] = useState(false)
  const [matriculaExists, setMatriculaExists] = useState(false)

  /* ---- Catalogos DANE ---- */
  const [departamentos, setDepartamentos] = useState<DepartamentoDANE[]>([])
  const [municipios, setMunicipios] = useState<MunicipioDANE[]>([])
  const [isLoadingDptos, setIsLoadingDptos] = useState(true)
  const [isLoadingMuns, setIsLoadingMuns] = useState(false)

  /* ---- Cargar departamentos ---- */
  useEffect(() => {
    let cancelled = false
    async function load() {
      try {
        const depts = await getDepartamentos()
        if (!cancelled) setDepartamentos(depts)
      } catch (err) {
        console.error("Error cargando departamentos:", err)
        if (!cancelled) {
          setDepartamentos([])
          notify.error("Error al cargar departamentos", {
            description: err instanceof Error ? err.message : "No se pudieron cargar los departamentos",
          })
        }
      } finally {
        if (!cancelled) setIsLoadingDptos(false)
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [])

  /* ---- Cargar municipios al cambiar departamento ---- */
  useEffect(() => {
    if (!form.departamento) {
      setMunicipios([])
      return
    }
    let cancelled = false
    setIsLoadingMuns(true)
    getMunicipios(form.departamento)
      .then((data) => {
        if (!cancelled) setMunicipios(data)
      })
      .catch((err) => {
        console.error("Error cargando municipios:", err)
        if (!cancelled) {
          setMunicipios([])
          notify.error("Error al cargar municipios", {
            description: err instanceof Error ? err.message : "No se pudieron cargar los municipios",
          })
        }
      })
      .finally(() => {
        if (!cancelled) setIsLoadingMuns(false)
      })
    return () => {
      cancelled = true
    }
  }, [form.departamento])

  /* ---- Helpers ---- */
  const set = useCallback(
    <K extends keyof FormData>(key: K, value: FormData[K]) => {
      setForm((prev) => ({ ...prev, [key]: value }))
      setErrors((prev) => ({ ...prev, [key]: undefined }))
      // Reset matricula check when changing
      if (key === "matriculaInmobiliaria") {
        setMatriculaExists(false)
      }
    },
    [],
  )

  const handleDepartamentoChange = useCallback(
    (codigo: string) => {
      const dept = departamentos.find((d) => d.codigo === codigo)
      setForm((prev) => ({
        ...prev,
        departamento: codigo,
        municipio: "", // Reset municipio
      }))
      setErrors((prev) => ({
        ...prev,
        departamento: undefined,
        municipio: undefined,
      }))
    },
    [departamentos],
  )

  const handleMunicipioChange = useCallback(
    (codigo: string) => {
      setForm((prev) => ({ ...prev, municipio: codigo }))
      setErrors((prev) => ({ ...prev, municipio: undefined }))
    },
    [],
  )

  const handleMatriculaBlur = useCallback(
    async (matricula: string) => {
      // Validate format first
      const MATRICULA_PATTERN = /^\d{2}-\d{2}-\d{4}-\d{4}-\d{3}$/
      if (!MATRICULA_PATTERN.test(matricula)) {
        return // Let Zod validation show the error
      }

      setMatriculaChecking(true)
      try {
        const result = await checkMatricula(matricula)
        setMatriculaExists(result.exists)
        if (result.exists) {
          setErrors((prev) => ({
            ...prev,
            matriculaInmobiliaria: "Esta matrícula ya existe en el sistema",
          }))
        }
      } catch {
        // Silently fail - don't block the user
      } finally {
        setMatriculaChecking(false)
      }
    },
    [],
  )

  /* ---- Submit ---- */
  const doSubmit = useCallback(async () => {
    setGeneralError(null)

    if (matriculaExists) {
      notify.error("Matrícula duplicada", {
        description: "Esta matrícula ya existe en el sistema",
      })
      return
    }

    const validationErrors = validateForm(form)
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      notify.error("Formulario incompleto", {
        description: "Revise los campos marcados en rojo",
      })
      return
    }

    setIsSubmitting(true)
    try {
      const request: CreatePredioRequest = {
        datosBasicos: {
          matriculaInmobiliaria: form.matriculaInmobiliaria,
          npn: form.npn ?? null,
          referenciaCatastral: form.referenciaCatastral ?? null,
          departamento: form.departamento,
          municipio: form.municipio,
          tipoPredio: form.tipoPredio,
          destino: form.destino ?? null,
          direccion: form.direccion ?? null,
          tipoSuelo: form.tipoSuelo ?? null,
          servidumbre: form.servidumbre ?? null,
          afectacion: form.afectacion ?? null,
          alias: form.alias ?? null,
          observaciones: form.observaciones ?? null,
          expediente: form.expediente ?? null,
        },
      }

      const response = await createPredio(request)
      notify.success("Datos básicos guardados", {
        description: "El predio ha sido creado exitosamente",
      })
      onSaved?.(response)
    } catch (error) {
      console.error("Error al guardar:", error)
      if (error instanceof Error) {
        try {
          const raw = error.message.replace(/^API error \d+: /, "")
          const apiError: ApiErrorResponse = JSON.parse(raw)
          if (apiError.fieldErrors) {
            setErrors((prev) => ({ ...prev, ...apiError.fieldErrors }))
          }
          setGeneralError(apiError.message)
          notify.error("Error al guardar", {
            description: `${apiError.message} [HTTP ${error.message.match(/^API error (\d+)/)?.[1] ?? "?"}]`,
          })
        } catch {
          setGeneralError("Ocurrió un error al guardar. Intente de nuevo.")
          notify.error("Error al guardar", {
            description: `${error.message}`,
          })
        }
      } else {
        setGeneralError("Ocurrió un error inesperado.")
        notify.error("Error al guardar", {
          description: "Ocurrió un error inesperado",
        })
      }
    } finally {
      setIsSubmitting(false)
    }
  }, [matriculaExists, form, validateForm, createPredio, onSaved])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    await doSubmit()
  }

  /* ---- Expose triggerSave via ref ---- */
  useImperativeHandle(_ref, () => ({ triggerSave: async () => { await doSubmit() } }), [doSubmit])

  return (
    <form onSubmit={handleSubmit} noValidate className="space-y-8">
      {/* General error banner */}
      {generalError && (
        <div className="flex items-start gap-3 rounded-lg border border-destructive/30 bg-destructive/10 px-4 py-3 text-sm text-destructive">
          <AlertCircleIcon className="mt-0.5 size-4 shrink-0" />
          <span>{generalError}</span>
        </div>
      )}

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 1: Identificación del Predio                           */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Identificación del Predio</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {/* Matrícula Inmobiliaria */}
          <Field>
            <FieldLabel htmlFor="matriculaInmobiliaria">
              Matrícula Inmobiliaria
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <div className="relative">
              <Input
                id="matriculaInmobiliaria"
                placeholder="00-00-0000-0000-000"
                value={form.matriculaInmobiliaria}
                onChange={(e) => set("matriculaInmobiliaria", e.target.value)}
                onBlur={(e) => handleMatriculaBlur(e.target.value)}
                className={cn(
                  errors.matriculaInmobiliaria && "border-destructive",
                  matriculaChecking && "pr-10",
                )}
                aria-describedby="matricula-help"
              />
              {matriculaChecking && (
                <span className="absolute right-3 top-1/2 -translate-y-1/2 size-4 border-2 border-muted-foreground/30 border-t-muted-foreground rounded-full animate-spin" />
              )}
            </div>
            <p id="matricula-help" className="text-xs text-muted-foreground">
              Formato: 00-00-0000-0000-000 (departamento-municipio-corregimiento-sección-predio)
            </p>
            <FieldError>{errors.matriculaInmobiliaria}</FieldError>
          </Field>

          {/* NPN */}
          <Field>
            <FieldLabel htmlFor="npn">
              NPN
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Opcional)
              </span>
            </FieldLabel>
            <Input
              id="npn"
              placeholder="15 dígitos numéricos"
              value={form.npn ?? ""}
              onChange={(e) => set("npn", e.target.value || null)}
              className={cn(errors.npn && "border-destructive")}
            />
            <FieldError>{errors.npn}</FieldError>
          </Field>

          {/* Referencia Catastral */}
          <Field>
            <FieldLabel htmlFor="referenciaCatastral">
              Referencia Catastral
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Opcional)
              </span>
            </FieldLabel>
            <Input
              id="referenciaCatastral"
              placeholder="15 dígitos numéricos"
              value={form.referenciaCatastral ?? ""}
              onChange={(e) => set("referenciaCatastral", e.target.value || null)}
              className={cn(errors.referenciaCatastral && "border-destructive")}
            />
            <FieldError>{errors.referenciaCatastral}</FieldError>
          </Field>

          {/* Expediente */}
          <Field>
            <FieldLabel htmlFor="expediente">
              Expediente
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.expediente}
              onValueChange={(v) => set("expediente", v as FormData["expediente"])}
            >
              <SelectTrigger
                id="expediente"
                className={cn(errors.expediente && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                {expedienteOptions.map((opt) => (
                  <SelectItem key={opt.value} value={opt.value}>
                    {opt.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <FieldError>{errors.expediente}</FieldError>
          </Field>
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 2: Ubicación                                           */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Ubicación</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {/* Departamento */}
          <Field>
            <FieldLabel htmlFor="departamento">
              Departamento
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            {isLoadingDptos ? (
              <Skeleton className="h-9 w-full rounded-md" />
            ) : (
              <Select
                value={form.departamento}
                onValueChange={handleDepartamentoChange}
              >
                <SelectTrigger
                  id="departamento"
                  className={cn(errors.departamento && "border-destructive")}
                >
                  <SelectValue placeholder="Seleccione un departamento..." />
                </SelectTrigger>
                <SelectContent>
                  {departamentos.map((dept) => (
                    <SelectItem key={dept.codigo} value={dept.codigo}>
                      {dept.nombre}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            )}
            <FieldError>{errors.departamento}</FieldError>
          </Field>

          {/* Municipio */}
          <Field>
            <FieldLabel htmlFor="municipio">
              Municipio
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.municipio}
              onValueChange={handleMunicipioChange}
              disabled={!form.departamento || isLoadingMuns}
            >
              <SelectTrigger
                id="municipio"
                className={cn(errors.municipio && "border-destructive")}
              >
                <SelectValue
                  placeholder={
                    isLoadingMuns
                      ? "Cargando municipios..."
                      : form.departamento
                        ? "Seleccione un municipio..."
                        : "Seleccione primero un departamento"
                  }
                />
              </SelectTrigger>
              <SelectContent>
                {municipios.map((mun) => (
                  <SelectItem key={mun.codigo} value={mun.codigo}>
                    {mun.nombre}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <FieldError>{errors.municipio}</FieldError>
          </Field>
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 3: Caracterización del Predio                          */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Caracterización del Predio</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {/* Tipo del Predio */}
          <Field>
            <FieldLabel htmlFor="tipoPredio">
              Tipo de Predio
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.tipoPredio}
              onValueChange={(v) => set("tipoPredio", v as FormData["tipoPredio"])}
            >
              <SelectTrigger
                id="tipoPredio"
                className={cn(errors.tipoPredio && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                {tipoPredioOptions.map((opt) => (
                  <SelectItem key={opt.value} value={opt.value}>
                    {opt.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <FieldError>{errors.tipoPredio}</FieldError>
          </Field>

          {/* Destino */}
          <Field>
            <FieldLabel htmlFor="destino">
              Destino
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.destino}
              onValueChange={(v) => set("destino", v as FormData["destino"])}
            >
              <SelectTrigger
                id="destino"
                className={cn(errors.destino && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                {destinoOptions.map((opt) => (
                  <SelectItem key={opt.value} value={opt.value}>
                    {opt.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            <FieldError>{errors.destino}</FieldError>
          </Field>

          {/* Tipo de Suelo */}
          <Field>
            <FieldLabel htmlFor="tipoSuelo">
              Tipo de Suelo
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.tipoSuelo}
              onValueChange={(v) => set("tipoSuelo", v as FormData["tipoSuelo"])}
              disabled={!form.municipio}
            >
              <SelectTrigger
                id="tipoSuelo"
                className={cn(errors.tipoSuelo && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                {tipoSueloOptions.map((opt) => (
                  <SelectItem key={opt.value} value={opt.value}>
                    {opt.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
            {!form.municipio && (
              <p className="text-xs text-muted-foreground">
                Seleccione primero un municipio
              </p>
            )}
            <FieldError>{errors.tipoSuelo}</FieldError>
          </Field>

          {/* Servidumbre */}
          <Field>
            <FieldLabel htmlFor="servidumbre">
              Servidumbre
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.servidumbre}
              onValueChange={(v) => set("servidumbre", v as FormData["servidumbre"])}
            >
              <SelectTrigger
                id="servidumbre"
                className={cn(errors.servidumbre && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="SI">Sí</SelectItem>
                <SelectItem value="NO">No</SelectItem>
              </SelectContent>
            </Select>
            <FieldError>{errors.servidumbre}</FieldError>
          </Field>

          {/* Afectación */}
          <Field>
            <FieldLabel htmlFor="afectacion">
              Afectación
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Select
              value={form.afectacion}
              onValueChange={(v) => set("afectacion", v as FormData["afectacion"])}
            >
              <SelectTrigger
                id="afectacion"
                className={cn(errors.afectacion && "border-destructive")}
              >
                <SelectValue placeholder="Seleccione..." />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="SI">Sí</SelectItem>
                <SelectItem value="NO">No</SelectItem>
              </SelectContent>
            </Select>
            <FieldError>{errors.afectacion}</FieldError>
          </Field>
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 4: Descripción                                         */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Descripción</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {/* Dirección */}
          <Field>
            <FieldLabel htmlFor="direccion">
              Dirección
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Obligatorio)
              </span>
            </FieldLabel>
            <Input
              id="direccion"
              placeholder="Dirección completa del predio"
              value={form.direccion}
              onChange={(e) => set("direccion", e.target.value)}
              className={cn(errors.direccion && "border-destructive")}
            />
            <FieldError>{errors.direccion}</FieldError>
          </Field>

          {/* Alias */}
          <Field>
            <FieldLabel htmlFor="alias">
              Alias
              <span className="text-xs font-normal text-muted-foreground ml-1">
                (Opcional)
              </span>
            </FieldLabel>
            <Input
              id="alias"
              placeholder="Nombre alterno o apodo del predio"
              value={form.alias ?? ""}
              onChange={(e) => set("alias", e.target.value || null)}
              className={cn(errors.alias && "border-destructive")}
            />
            <FieldError>{errors.alias}</FieldError>
          </Field>

          {/* Observaciones — full width */}
          <Field className="md:col-span-2">
            <FieldLabel htmlFor="observaciones">Observaciones</FieldLabel>
            <Textarea
              id="observaciones"
              rows={3}
              placeholder="Observaciones adicionales (opcional)"
              value={form.observaciones ?? ""}
              onChange={(e) => set("observaciones", e.target.value || null)}
              className={cn(
                "resize-none",
                errors.observaciones && "border-destructive",
              )}
            />
            <FieldError>{errors.observaciones}</FieldError>
          </Field>
        </div>
      </section>

      {/* Submit button */}
      <div className="flex items-center justify-end gap-3 border-t border-border pt-6">
        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? (
            <span className="flex items-center gap-2">
              <span className="size-4 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin" />
              Guardando...
            </span>
          ) : (
            <span className="flex items-center gap-2">
              <SaveIcon className="size-4" />
              Guardar y Continuar
            </span>
          )}
        </Button>
      </div>
    </form>
  )
})
