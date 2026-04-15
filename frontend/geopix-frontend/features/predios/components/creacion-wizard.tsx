"use client"

import { useState, useRef } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/shared/ui/button"
import { Card, CardContent } from "@/shared/ui/card"
import { CheckIcon, ChevronLeftIcon, ChevronRightIcon, LoaderIcon } from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { steps } from "@/features/predios/lib/steps"
import { DatosBasicosForm } from "@/features/predios/components/datos-basicos-form"
import { DatosFisicosForm } from "@/features/predios/components/datos-fisicos-form"
import { TitularidadForm } from "@/features/predios/components/titularidad-form"
import { InfoEconomicaForm } from "@/features/predios/components/info-economica-form"
import { completarPredio } from "@/features/predios/lib/api"
import type { PredioResponse } from "@/features/predios/lib/types"

type FormHandle = { triggerSave: () => Promise<void> }

export function CreacionWizard() {
  const router = useRouter()
  const [currentStep, setCurrentStep] = useState(1)
  const [predioId, setPredioId] = useState<string | null>(null)
  const [isCompleting, setIsCompleting] = useState(false)
  const [isSaving, setIsSaving] = useState(false)

  const form1Ref = useRef<FormHandle>(null)
  const form2Ref = useRef<FormHandle>(null)
  const form3Ref = useRef<FormHandle>(null)
  const form4Ref = useRef<FormHandle>(null)

  const formRefs = [form1Ref, form2Ref, form3Ref, form4Ref]

  const goToStep = (step: number) => {
    if (step >= 1 && step <= steps.length) {
      setCurrentStep(step)
    }
  }

  const handleSiguiente = async () => {
    if (currentStep < 1 || currentStep > 4) return
    setIsSaving(true)
    try {
      await formRefs[currentStep - 1].current?.triggerSave()
    } finally {
      setIsSaving(false)
    }
  }

  const handleDatosBasicosSaved = (response: PredioResponse) => {
    setPredioId(response.id)
    goToStep(2)
  }

  const handlePasoCompletado = () => {
    // Solo avanzar para pasos 2 y 3
    // El paso 4 (Info Economica) se maneja internamente
    if (currentStep < steps.length) {
      goToStep(currentStep + 1)
    }
  }

  const handleFinalizar = async () => {
    if (!predioId) {
      notify.error("Error", {
        description: "No se encontro el ID del predio",
      })
      return
    }

    setIsCompleting(true)
    try {
      await completarPredio(predioId)
      notify.success("Predio completado", {
        description: "El expediente predial ha sido marcado como completo",
      })
      router.push("/dashboard/predios")
    } catch (error) {
      const message = error instanceof Error ? error.message : "Error inesperado"
      notify.error("Error al completar", {
        description: message,
      })
    } finally {
      setIsCompleting(false)
    }
  }

  const currentStepData = steps.find((s) => s.id === currentStep)!

  /* ---- Renderizar contenido del paso ---- */
  const renderStepContent = () => {
    switch (currentStep) {
      case 1:
        return (
          <DatosBasicosForm
            ref={form1Ref}
            onSaved={handleDatosBasicosSaved}
          />
        )

      case 2:
        if (!predioId) {
          return (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <p className="text-muted-foreground">
                Primero debe guardar los datos basicos del predio.
              </p>
              <Button
                variant="outline"
                onClick={() => goToStep(1)}
                className="mt-4"
              >
                <ChevronLeftIcon className="size-4 mr-1" />
                Volver al Paso 1
              </Button>
            </div>
          )
        }
        return (
          <DatosFisicosForm
            ref={form2Ref}
            predioId={predioId}
            onSaved={handlePasoCompletado}
            onBack={() => goToStep(1)}
          />
        )

      case 3:
        if (!predioId) {
          return (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <p className="text-muted-foreground">
                Primero debe guardar los datos basicos del predio.
              </p>
              <Button
                variant="outline"
                onClick={() => goToStep(1)}
                className="mt-4"
              >
                <ChevronLeftIcon className="size-4 mr-1" />
                Volver al Paso 1
              </Button>
            </div>
          )
        }
        return (
          <TitularidadForm
            ref={form3Ref}
            predioId={predioId}
            onSaved={handlePasoCompletado}
            onBack={() => goToStep(2)}
          />
        )

      case 4:
        if (!predioId) {
          return (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <p className="text-muted-foreground">
                Primero debe guardar los datos basicos del predio.
              </p>
              <Button
                variant="outline"
                onClick={() => goToStep(1)}
                className="mt-4"
              >
                <ChevronLeftIcon className="size-4 mr-1" />
                Volver al Paso 1
              </Button>
            </div>
          )
        }
        return (
          <InfoEconomicaForm
            ref={form4Ref}
            predioId={predioId}
            onSaved={handlePasoCompletado}
            onBack={() => goToStep(3)}
          />
        )

      case 5:
      case 6:
        // Pasos 5 y 6 aun no implementados
        return (
          <div className="min-h-75 flex items-center justify-center rounded-lg border border-dashed border-border bg-muted/30">
            <div className="flex flex-col items-center text-center max-w-sm px-4">
              <div className="flex items-center justify-center size-14 rounded-full bg-muted mb-4">
                <currentStepData.icon
                  className="size-6 text-muted-foreground/50"
                  aria-hidden="true"
                />
              </div>
              <p className="text-sm font-semibold text-foreground">
                {currentStepData.title}
              </p>
              <p className="text-xs text-muted-foreground mt-1.5 leading-relaxed">
                Seccion para capturar los campos correspondientes a{" "}
                {currentStepData.description.toLowerCase()}.
              </p>
              <p className="mt-4 text-xs text-muted-foreground">
                Esta seccion aun no esta disponible.
              </p>
            </div>
          </div>
        )

      default:
        return null
    }
  }

  return (
    <div className="flex flex-1 flex-col py-8 px-4 sm:px-6 lg:px-8 bg-background">
      <div className="max-w-5xl w-full mx-auto flex flex-col gap-8">
        {/* Page Header */}
        <div>
          <h1 className="text-xl font-bold tracking-tight text-foreground">
            Creacion de Predio
          </h1>
          <p className="text-sm text-muted-foreground mt-1">
            Complete los 6 pasos para estructurar el expediente predial.
          </p>
        </div>

        {/* --- Stepper UI (Desktop) --- */}
        <nav
          aria-label="Pasos del formulario"
          className="hidden lg:block relative"
        >
          {/* Background line */}
          <div
            className="absolute z-0 top-5 left-[calc(100%/12)] right-[calc(100%/12)] h-0.5 bg-border z-0"
            aria-hidden="true"
          />
          {/* Active progress line */}
          <div
            className="absolute top-5 left-[calc(100%/12)] h-0.5 bg-[#C9A24D] z-0 transition-all duration-500 ease-out"
            style={{
              width: `${((currentStep - 1) / (steps.length - 1)) * (100 - 100 / 6)}%`,
            }}
            aria-hidden="true"
          />

          <ol className="relative z-10 flex items-start justify-between">
            {steps.map((step) => {
              const isCurrent = step.id === currentStep
              const isCompleted = step.id < currentStep

              return (
                <li
                  key={step.id}
                  className="flex flex-col items-center gap-2"
                  style={{ width: `${100 / steps.length}%` }}
                >
                  <button
                    onClick={() => goToStep(step.id)}
                    aria-current={isCurrent ? "step" : undefined}
                    aria-label={`Paso ${step.id}: ${step.title}`}
                    disabled={step.id > 4}
                    className={`
                      flex items-center justify-center size-10 rounded-full border-2 font-semibold text-sm transition-all outline-none focus-visible:ring-2 focus-visible:ring-primary/30 focus-visible:ring-offset-2 focus-visible:ring-offset-background
                      ${
                        isCurrent
                          ? "bg-[#C9A24D] border-[#C9A24D] text-primary-foreground shadow-sm"
                          : isCompleted
                            ? "bg-[#C9A24D] border-[#C9A24D] text-primary-foreground"
                            : "bg-card border-border text-muted-foreground hover:border-muted-foreground"
                      }
                      ${step.id > 4 ? "opacity-50 cursor-not-allowed" : ""}
                    `}
                  >
                    {isCompleted ? (
                      <CheckIcon className="size-4" />
                    ) : (
                      <step.icon className="size-4" aria-hidden="true" />
                    )}
                  </button>
                  <span
                    className={`text-xs text-center leading-tight ${isCurrent ? "text-foreground font-semibold" : isCompleted ? "text-foreground/60 font-medium" : "text-muted-foreground font-medium"}`}
                  >
                    {step.shortTitle}
                  </span>
                </li>
              )
            })}
          </ol>
        </nav>

        {/* --- Stepper UI (Mobile/Tablet) --- */}
        <div
          className="lg:hidden"
          role="group"
          aria-label="Progreso del formulario"
        >
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm font-semibold text-foreground">
              {currentStepData.title}
            </span>
            <span className="text-xs font-medium text-muted-foreground">
              Paso {currentStep} de {steps.length}
            </span>
          </div>
          <div
            className="h-1.5 w-full bg-muted rounded-full overflow-hidden"
            role="progressbar"
            aria-valuenow={currentStep}
            aria-valuemin={1}
            aria-valuemax={steps.length}
          >
            <div
              className="h-full bg-[#C9A24D] transition-all duration-500 ease-out rounded-full"
              style={{ width: `${(currentStep / steps.length) * 100}%` }}
            />
          </div>
        </div>

        {/* --- Content Area --- */}
        <Card className="border-border shadow-sm bg-card">
          <CardContent className="p-4 sm:p-6 lg:p-8 pb-0 lg:pb-0">
            {renderStepContent()}
          </CardContent>

          {/* Navigation Footer - Siempre visible */}
          <div className="flex items-center justify-between px-4 sm:px-6 lg:px-8 py-3 sm:py-4 border-t border-border/50 bg-muted/20 mt-4 lg:mt-0">
            <Button
              variant="outline"
              size="sm"
              onClick={() => goToStep(currentStep - 1)}
              disabled={currentStep === 1 || isSaving || isCompleting}
              className="gap-1.5"
              aria-label="Ir al paso anterior"
            >
              <ChevronLeftIcon className="size-3.5" aria-hidden="true" />
              <span className="hidden xs:inline">Anterior</span>
            </Button>

            {/* Mobile step indicators */}
            <div className="flex gap-1.5 items-center sm:hidden">
              {steps.map((s) => (
                <div
                  key={s.id}
                  className={`h-1.5 rounded-full transition-all ${s.id === currentStep ? "w-4 bg-[#C9A24D]" : s.id < currentStep ? "w-1.5 bg-[#C9A24D]/40" : "w-1.5 bg-muted-foreground/20"}`}
                />
              ))}
            </div>

            {currentStep < steps.length ? (
              <Button
                size="sm"
                className="gap-1.5"
                aria-label="Guardar y avanzar al siguiente paso"
                onClick={handleSiguiente}
                disabled={isSaving || isCompleting}
              >
                {isSaving ? (
                  <>
                    <span className="size-3.5 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin" />
                    <span className="hidden xs:inline">Guardando...</span>
                    <span className="xs:hidden">Guard...</span>
                  </>
                ) : (
                  <>
                    <span className="hidden xs:inline">Siguiente</span>
                    <span className="xs:hidden">Sig.</span>
                    <ChevronRightIcon className="size-3.5" aria-hidden="true" />
                  </>
                )}
              </Button>
            ) : (
              <Button
                size="sm"
                className="gap-1.5"
                aria-label="Finalizar registro del predio"
                onClick={handleFinalizar}
                disabled={isCompleting || !predioId}
              >
                {isCompleting ? (
                  <>
                    <LoaderIcon className="size-3.5 animate-spin" />
                    <span className="hidden xs:inline">Finalizando...</span>
                    <span className="xs:hidden">Fin...</span>
                  </>
                ) : (
                  <>
                    <CheckIcon className="size-3.5" />
                    <span className="hidden xs:inline">Finalizar</span>
                    <span className="xs:hidden">Fin</span>
                  </>
                )}
              </Button>
            )}
          </div>
        </Card>
      </div>
    </div>
  )
}
