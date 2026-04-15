"use client"

import { useState, useCallback, useEffect, forwardRef, useImperativeHandle } from "react"
import { Field, FieldLabel, FieldError } from "@/shared/ui/field"
import { Input } from "@/shared/ui/input"
import { Button } from "@/shared/ui/button"
import { Separator } from "@/shared/ui/separator"
import { AlertCircleIcon, SaveIcon, PlusIcon, Trash2Icon } from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { cn } from "@/shared/lib/utils"
import {
  updatePredioTitularidad,
  getPredioById,
} from "@/features/predios/lib/api"
import type {
  Titular,
  Titularidad,
  PredioResponse,
  ApiErrorResponse,
} from "@/features/predios/lib/types"
import { titularidadSchema } from "@/features/predios/lib/schemas"
import { tipoDocumentoOptions, tipoTitularOptions } from "@/features/predios/lib/constants"
import { z } from "zod/v4"

/* ------------------------------------------------------------------ */
/*  Types                                                              */
/* ------------------------------------------------------------------ */

interface TitularidadFormProps {
  /** UUID del predio */
  predioId: string
  /** Datos iniciales para editar */
  initialData?: Titularidad
  /** Callback cuando se guarda exitosamente */
  onSaved: () => void
  /** Callback para ir al paso anterior */
  onBack: () => void
}

type TitularFormData = z.infer<typeof titularidadSchema>

interface FormErrors {
  titulares?: string
  general?: string
  [key: string]: string | undefined
}

/* ------------------------------------------------------------------ */
/*  Initial state                                                      */
/* ------------------------------------------------------------------ */

function createEmptyTitular(): Titular {
  return {
    tipoDocumento: "CC",
    numeroDocumento: "",
    nombre: "",
    correo: null,
    telefono: null,
    direccion: null,
    participacion: 100,
    tipoTitular: "PROPIETARIO",
  }
}

/* ------------------------------------------------------------------ */
/*  Validation                                                         */
/* ------------------------------------------------------------------ */

function validateForm(data: TitularFormData): FormErrors {
  const result = titularidadSchema.safeParse(data)

  if (result.success) {
    // Additional validation: sum of participations
    const totalParticipacion = data.titulares.reduce(
      (sum, t) => sum + t.participacion,
      0,
    )
    if (totalParticipacion > 100) {
      return {
        titulares: `La suma de participaciones (${totalParticipacion}%) no puede exceder 100%`,
      }
    }
    return {}
  }

  const errors: FormErrors = {}
  for (const issue of result.error.issues) {
    if (issue.path[0] === "titulares" && issue.path[1] !== undefined) {
      // Field-level errors within array
      const index = issue.path[1] as number
      const field = issue.path[2] as string
      errors[`titular_${index}_${field}`] = issue.message
    } else {
      const path = issue.path[0] as keyof FormErrors
      if (typeof path === "string") {
        errors[path] = issue.message
      }
    }
  }

  return errors
}

/* ------------------------------------------------------------------ */
/*  Component                                                          */
/* ------------------------------------------------------------------ */

export const TitularidadForm = forwardRef<{ triggerSave: () => Promise<void> }, TitularidadFormProps>(
  function TitularidadForm({ predioId, initialData, onSaved, onBack }, _ref) {
  const [titulares, setTitulares] = useState<Titular[]>(
    initialData?.titulares?.length
      ? initialData.titulares
      : [createEmptyTitular()],
  )
  const [errors, setErrors] = useState<FormErrors>({})
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [generalError, setGeneralError] = useState<string | null>(null)

  // Load existing data if not provided
  useEffect(() => {
    if (initialData || !predioId) return

    let cancelled = false
    async function load() {
      try {
        const response: PredioResponse = await getPredioById(predioId)
        if (cancelled || !response.titularidad?.titulares?.length) return

        setTitulares(response.titularidad.titulares)
      } catch {
        // Silently fail
      }
    }
    load()
    return () => {
      cancelled = true
    }
  }, [predioId, initialData])

  /* ---- Helpers ---- */
  const updateTitular = useCallback(
    (index: number, field: keyof Titular, value: string | number | null) => {
      setTitulares((prev) => {
        const updated = [...prev]
        updated[index] = { ...updated[index], [field]: value }
        return updated
      })
      // Clear field error
      setErrors((prev) => ({
        ...prev,
        [`titular_${index}_${field}`]: undefined,
      }))
    },
    [],
  )

  const addTitular = useCallback(() => {
    setTitulares((prev) => [...prev, createEmptyTitular()])
  }, [])

  const removeTitular = useCallback((index: number) => {
    setTitulares((prev) => prev.filter((_, i) => i !== index))
  }, [])

  const getFieldError = (index: number, field: string): string | undefined => {
    return errors[`titular_${index}_${field}`]
  }

  const totalParticipacion = titulares.reduce(
    (sum, t) => sum + t.participacion,
    0,
  )

  /* ---- Submit ---- */
  const doSubmit = useCallback(async () => {
    setGeneralError(null)

    const data: TitularFormData = { titulares }
    const validationErrors = validateForm(data)

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      notify.error("Formulario incompleto", {
        description: "Revise los campos marcados en rojo",
      })
      return
    }

    setIsSubmitting(true)
    try {
      await updatePredioTitularidad(predioId, { titulares })
      notify.success("Titularidad guardada", {
        description: "La informacion de titulares ha sido actualizada",
      })
      onSaved()
    } catch (error) {
      if (error instanceof Error) {
        try {
          const raw = error.message.replace(/^API error \d+: /, "")
          const apiError: ApiErrorResponse = JSON.parse(raw)
          if (apiError.fieldErrors) {
            setErrors((prev) => ({ ... prev, ...apiError.fieldErrors }))
          }
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
  }, [predioId, titulares, onSaved])

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
      {/*  Instrucciones                                                    */}
      {/* ---------------------------------------------------------------- */}
      <div className="rounded-lg border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-700 dark:border-blue-800 dark:bg-blue-950/40 dark:text-blue-300">
        <p className="font-medium">Agregue los titulares del predio</p>
        <p className="mt-1 text-xs text-blue-600 dark:text-blue-400">
          La suma de las participaciones no puede exceder 100%. Al menos un titular es requerido.
        </p>
      </div>

      {/* ---------------------------------------------------------------- */}
      {/*  Resumen de participaciones                                     */}
      {/* ---------------------------------------------------------------- */}
      <div className="flex items-center justify-between rounded-lg border border-border bg-muted/30 px-4 py-3">
        <span className="text-sm text-muted-foreground">
          Total de participacion:
        </span>
        <span
          className={cn(
            "text-lg font-semibold",
            totalParticipacion > 100
              ? "text-destructive"
              : totalParticipacion === 100
                ? "text-green-600 dark:text-green-400"
                : "text-muted-foreground",
          )}
        >
          {totalParticipacion}%
        </span>
      </div>

      {/* ---------------------------------------------------------------- */}
      {/*  Lista de Titulares                                             */}
      {/* ---------------------------------------------------------------- */}
      <div className="space-y-6">
        {titulares.map((titular, index) => (
          <section
            key={index}
            className="space-y-4 rounded-lg border border-border p-4"
          >
            <div className="flex items-center justify-between">
              <h3 className="text-sm font-semibold">
                Titular {index + 1}
              </h3>
              {titulares.length > 1 && (
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={() => removeTitular(index)}
                  className="text-destructive hover:text-destructive hover:bg-destructive/10"
                >
                  <Trash2Icon className="size-4 mr-1" />
                  Eliminar
                </Button>
              )}
            </div>

            <div className="grid grid-cols-1 gap-x-6 gap-y-5 md:grid-cols-2 lg:grid-cols-3">
              {/* Tipo de Documento */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-tipoDocumento`}>
                  Tipo de Documento
                </FieldLabel>
                <select
                  id={`titular-${index}-tipoDocumento`}
                  value={titular.tipoDocumento}
                  onChange={(e) =>
                    updateTitular(index, "tipoDocumento", e.target.value)
                  }
                  className={cn(
                    "flex h-9 w-full rounded-md border border-input bg-background px-3 py-1 text-sm shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50",
                    getFieldError(index, "tipoDocumento") && "border-destructive",
                  )}
                >
                  {tipoDocumentoOptions.map((opt) => (
                    <option key={opt.value} value={opt.value}>
                      {opt.label}
                    </option>
                  ))}
                </select>
                <FieldError>{getFieldError(index, "tipoDocumento")}</FieldError>
              </Field>

              {/* Numero de Documento */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-numeroDocumento`}>
                  Numero de Documento
                </FieldLabel>
                <Input
                  id={`titular-${index}-numeroDocumento`}
                  value={titular.numeroDocumento}
                  onChange={(e) =>
                    updateTitular(index, "numeroDocumento", e.target.value)
                  }
                  placeholder="Sin puntos ni espacios"
                  className={cn(
                    getFieldError(index, "numeroDocumento") && "border-destructive",
                  )}
                />
                <FieldError>{getFieldError(index, "numeroDocumento")}</FieldError>
              </Field>

              {/* Nombre */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-nombre`}>
                  Nombre Completo
                </FieldLabel>
                <Input
                  id={`titular-${index}-nombre`}
                  value={titular.nombre}
                  onChange={(e) =>
                    updateTitular(index, "nombre", e.target.value)
                  }
                  placeholder="Nombre completo del titular"
                  className={cn(
                    getFieldError(index, "nombre") && "border-destructive",
                  )}
                />
                <FieldError>{getFieldError(index, "nombre")}</FieldError>
              </Field>

              {/* Correo */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-correo`}>
                  Correo Electronico
                  <span className="text-xs font-normal text-muted-foreground ml-1">
                    (Opcional)
                  </span>
                </FieldLabel>
                <Input
                  id={`titular-${index}-correo`}
                  type="email"
                  value={titular.correo ?? ""}
                  onChange={(e) =>
                    updateTitular(index, "correo", e.target.value || null)
                  }
                  placeholder="correo@ejemplo.com"
                  className={cn(
                    getFieldError(index, "correo") && "border-destructive",
                  )}
                />
                <FieldError>{getFieldError(index, "correo")}</FieldError>
              </Field>

              {/* Telefono */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-telefono`}>
                  Telefono
                  <span className="text-xs font-normal text-muted-foreground ml-1">
                    (Opcional)
                  </span>
                </FieldLabel>
                <Input
                  id={`titular-${index}-telefono`}
                  value={titular.telefono ?? ""}
                  onChange={(e) =>
                    updateTitular(index, "telefono", e.target.value || null)
                  }
                  placeholder="Numero de telefono"
                />
                <FieldError>{getFieldError(index, "telefono")}</FieldError>
              </Field>

              {/* Tipo de Titular */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-tipoTitular`}>
                  Tipo de Titular
                </FieldLabel>
                <select
                  id={`titular-${index}-tipoTitular`}
                  value={titular.tipoTitular}
                  onChange={(e) =>
                    updateTitular(index, "tipoTitular", e.target.value)
                  }
                  className={cn(
                    "flex h-9 w-full rounded-md border border-input bg-background px-3 py-1 text-sm shadow-sm transition-colors file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50",
                    getFieldError(index, "tipoTitular") && "border-destructive",
                  )}
                >
                  {tipoTitularOptions.map((opt) => (
                    <option key={opt.value} value={opt.value}>
                      {opt.label}
                    </option>
                  ))}
                </select>
                <FieldError>{getFieldError(index, "tipoTitular")}</FieldError>
              </Field>

              {/* Participacion */}
              <Field>
                <FieldLabel htmlFor={`titular-${index}-participacion`}>
                  Participacion (%)
                </FieldLabel>
                <Input
                  id={`titular-${index}-participacion`}
                  type="number"
                  min={0}
                  max={100}
                  step={0.01}
                  value={titular.participacion}
                  onChange={(e) =>
                    updateTitular(index, "participacion", parseFloat(e.target.value) || 0)
                  }
                  className={cn(
                    getFieldError(index, "participacion") && "border-destructive",
                  )}
                />
                <FieldError>{getFieldError(index, "participacion")}</FieldError>
              </Field>

              {/* Direccion */}
              <Field className="md:col-span-2">
                <FieldLabel htmlFor={`titular-${index}-direccion`}>
                  Direccion de Notificaciones
                  <span className="text-xs font-normal text-muted-foreground ml-1">
                    (Opcional)
                  </span>
                </FieldLabel>
                <Input
                  id={`titular-${index}-direccion`}
                  value={titular.direccion ?? ""}
                  onChange={(e) =>
                    updateTitular(index, "direccion", e.target.value || null)
                  }
                  placeholder="Direccion para notificaciones"
                />
                <FieldError>{getFieldError(index, "direccion")}</FieldError>
              </Field>
            </div>
          </section>
        ))}
      </div>

      {/* ---------------------------------------------------------------- */}
      {/*  Agregar titular                                                 */}
      {/* ---------------------------------------------------------------- */}
      <Button
        type="button"
        variant="outline"
        onClick={addTitular}
        className="w-full"
      >
        <PlusIcon className="size-4 mr-2" />
        Agregar Titular
      </Button>

      {/* ---------------------------------------------------------------- */}
      {/*  Errores de validacion                                          */}
      {/* ---------------------------------------------------------------- */}
      {errors.titulares && (
        <div className="flex items-start gap-3 rounded-lg border border-destructive/30 bg-destructive/10 px-4 py-3 text-sm text-destructive">
          <AlertCircleIcon className="mt-0.5 size-4 shrink-0" />
          <span>{errors.titulares}</span>
        </div>
      )}

      {/* ---------------------------------------------------------------- */}
      {/*  Botones de navegacion                                          */}
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
