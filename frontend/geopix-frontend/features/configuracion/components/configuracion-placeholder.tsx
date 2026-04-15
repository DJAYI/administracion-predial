import { SettingsIcon } from "lucide-react"

export function ConfiguracionPlaceholder() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center p-4 lg:p-6">
      <div className="text-center max-w-sm">
        <div className="flex items-center justify-center size-14 rounded-xl bg-primary/8 border border-primary/15 mx-auto mb-5">
          <SettingsIcon className="size-7 text-primary" aria-hidden="true" />
        </div>
        <h1 className="text-lg font-bold text-foreground mb-1.5">
          Configuracion
        </h1>
        <p className="text-sm text-muted-foreground leading-relaxed">
          Esta seccion se encuentra en desarrollo. Las opciones de
          configuracion estaran disponibles proximamente.
        </p>
      </div>
    </div>
  )
}
