"use client"

import { useState, useCallback, useEffect, forwardRef, useImperativeHandle } from "react"
import { Field, FieldLabel, FieldError } from "@/shared/ui/field"
import { Input } from "@/shared/ui/input"
import { Button } from "@/shared/ui/button"
import { Separator } from "@/shared/ui/separator"
import { AlertCircleIcon, SaveIcon } from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { cn } from "@/shared/lib/utils"
import { updatePredioDatosFisicos, getPredioById } from "@/features/predios/lib/api"
import type { DatosFisicos, PredioResponse, ApiErrorResponse } from "@/features/predios/lib/types"
import { datosFisicosSchema } from "@/features/predios/lib/schemas"
import { z } from "zod/v4"

/* ------------------------------------------------------------------ */
/*  Types                                                              */
/* ------------------------------------------------------------------ */

interface DatosFisicosFormProps {
  /** UUID del predio */
  predioId: string
  /** Datos iniciales para editar */
  initialData?: DatosFisicos
  /** Callback cuando se guarda exitosamente */
  onSaved: () => void
  /** Callback para ir al paso anterior */
  onBack: () => void
}

type FormData = z.infer<typeof datosFisicosSchema>

interface FormErrors {
  areaVurHa?: string
  areaVurM2?: string
  areaCatastroHa?: string
  areaCatastroM2?: string
  areaEscriturasHa?: string
  areaEscriturasM2?: string
  areaMedicacionHa?: string
  areaMedicacionM2?: string
  general?: string
}

/* ------------------------------------------------------------------ */
/*  Initial state                                                      */
/* ------------------------------------------------------------------ */

const INITIAL_FORM: FormData = {
  areaVurHa: 0,
  areaVurM2: 0,
  areaCatastroHa: 0,
  areaCatastroM2: 0,
  areaEscriturasHa: 0,
  areaEscriturasM2: 0,
  areaMedicacionHa: 0,
  areaMedicacionM2: 0,
}

/* ------------------------------------------------------------------ */
/*  Validation                                                         */
/* ------------------------------------------------------------------ */

function validateForm(data: FormData): FormErrors {
  const result = datosFisicosSchema.safeParse(data)

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

export const DatosFisicosForm = forwardRef<{ triggerSave: () => Promise<void> }, DatosFisicosFormProps>(
  function DatosFisicosForm({ predioId, initialData, onSaved, onBack }, _ref) {
  const merged: FormData = {
    ...INITIAL_FORM,
    ...initialData,
  }

  const [form, setForm] = useState<FormData>(merged)
  const [errors, setErrors] = useState<FormErrors>({})
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [generalError, setGeneralError] = useState<string | null>(null)

  /* ---- Cargar datos existentes ---- */
  useEffect(() => {
    if (initialData) return

    let cancelled = false
    async function load() {
      try {
        const response: PredioResponse = await getPredioById(predioId)
        if (cancelled || !response.datosFisicos) return

        const df = response.datosFisicos
        setForm({
          areaVurHa: df.areaVurHa,
          areaVurM2: df.areaVurM2,
          areaCatastroHa: df.areaCatastroHa,
          areaCatastroM2: df.areaCatastroM2,
          areaEscriturasHa: df.areaEscriturasHa,
          areaEscriturasM2: df.areaEscriturasM2,
          areaMedicacionHa: df.areaMedicacionHa,
          areaMedicacionM2: df.areaMedicacionM2,
        })
      } catch {
        // Silently fail - form will use initial values
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [predioId, initialData])

  /* ---- Helpers ---- */
  const set = useCallback(
    <K extends keyof FormData>(key: K, value: string | number) => {
      // Convert string to number for numeric fields
      const numValue = typeof value === "string" ? parseFloat(value) || 0 : value
      setForm((prev) => ({ ...prev, [key]: numValue }))
      setErrors((prev) => ({ ...prev, [key]: undefined }))
    },
    [],
  )

  /* ---- Submit ---- */
  const doSubmit = useCallback(async () => {
    setGeneralError(null)

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
      await updatePredioDatosFisicos(predioId, form)
      notify.success("Datos físicos guardados", {
        description: "La información de áreas ha sido actualizada",
      })
      onSaved()
    } catch (error) {
      if (error instanceof Error) {
        try {
          const raw = error.message.replace(/^API error \d+: /, "")
          const apiError: ApiErrorResponse = JSON.parse(raw)
          if (apiError.fieldErrors) {
            setErrors((prev) => ({ ...prev, ...apiError.fieldErrors }))
          }
          setGeneralError(apiError.message)
          notify.error("Error al guardar", {
            description: apiError.message,
          })
        } catch {
          setGeneralError("Ocurrió un error al guardar. Intente de nuevo.")
          notify.error("Error al guardar", {
            description: "Ocurrió un error inesperado",
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
  }, [predioId, form, validateForm, updatePredioDatosFisicos, onSaved])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    await doSubmit()
  }

  /* ---- Expose triggerSave via ref ---- */
  useImperativeHandle(_ref, () => ({ triggerSave: async () => { await doSubmit() } }), [doSubmit])

  /* ---- Render area row ---- */
  const renderAreaRow = (
    id: keyof FormData,
    label: string,
    helpText?: string,
  ) => (
    <Field>
      <FieldLabel htmlFor={id}>
        {label}
        {helpText && (
          <span className="text-xs font-normal text-muted-foreground ml-1">
            ({helpText})
          </span>
        )}
      </FieldLabel>
      <Input
        id={id}
        type="number"
        min={0}
        step={0.01}
        value={form[id]}
        onChange={(e) => set(id, e.target.value)}
        className={cn(errors[id] && "border-destructive")}
      />
      <FieldError>{errors[id]}</FieldError>
    </Field>
  )

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
      {/*  Instrucciones                                                    */}
      {/* ---------------------------------------------------------------- */}
      <div className="rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-700 dark:border-blue-800 dark:bg-blue-950/40 dark:text-blue-300">
        <p className="font-medium">Ingrese las áreas en hectáreas (Ha) y metros cuadrados (m²)</p>
        <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">
          Use el punto (.) como separador decimal. Las áreas deben ser valores positivos o cero.
        </p>
      </div>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 1: Área VUR                                            */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Área VUR (Valor Unitario de Suelo)</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {renderAreaRow("areaVurHa", "Hectáreas", "Ha")}
          {renderAreaRow("areaVurM2", "Metros cuadrados", "m²")}
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 2: Área Catastro                                        */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Área de Catastro</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {renderAreaRow("areaCatastroHa", "Hectáreas", "Ha")}
          {renderAreaRow("areaCatastroM2", "Metros cuadrados", "m²")}
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 3: Área Escrituras                                      */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Área de Escrituras</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {renderAreaRow("areaEscriturasHa", "Hectáreas", "Ha")}
          {renderAreaRow("areaEscriturasM2", "Metros cuadrados", "m²")}
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Sección 4: Área Medición                                       */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Área de Mediación / Levantamiento</h3>
          <Separator className="mt-2" />
        </div>

        <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2">
          {renderAreaRow("areaMedicacionHa", "Hectáreas", "Ha")}
          {renderAreaRow("areaMedicacionM2", "Metros cuadrados", "m²")}
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Resumen de áreas                                               */}
      {/* ---------------------------------------------------------------- */}
      <section className="space-y-4">
        <div>
          <h3 className="text-base font-semibold">Resumen de Áreas</h3>
          <Separator className="mt-2" />
        </div>

        <div className="rounded-lg border border-border bg-muted/30 p-4">
          <dl className="grid grid-cols-2 gap-x-8 gap-y-2 text-sm md:grid-cols-4">
            <div>
              <dt className="text-muted-foreground">Total VUR</dt>
              <dd className="font-medium">
                {(form.areaVurHa + form.areaVurM2 / 10000).toFixed(4)} Ha
              </dd>
            </div>
            <div>
              <dt className="text-muted-foreground">Total Catastro</dt>
              <dd className="font-medium">
                {(form.areaCatastroHa + form.areaCatastroM2 / 10000).toFixed(4)} Ha
              </dd>
            </div>
            <div>
              <dt className="text-muted-foreground">Total Escrituras</dt>
              <dd className="font-medium">
                {(form.areaEscriturasHa + form.areaEscriturasM2 / 10000).toFixed(4)} Ha
              </dd>
            </div>
            <div>
              <dt className="text-muted-foreground">Total Medición</dt>
              <dd className="font-medium">
                {(form.areaMedicacionHa + form.areaMedicacionM2 / 10000).toFixed(4)} Ha
              </dd>
            </div>
          </dl>
        </div>
      </section>

      {/* ---------------------------------------------------------------- */}
      {/*  Botones de navegación                                          */}
      {/* ---------------------------------------------------------------- */}
      <div className="flex items-center justify-end gap-3 border-t border-border pt-6">
        <Button
          type="button"
          variant="outline"
          onClick={onBack}
          disabled={isSubmitting}
        >
          Anterior
        </Button>
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
