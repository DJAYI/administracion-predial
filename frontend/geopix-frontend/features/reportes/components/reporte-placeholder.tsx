import { BarChart3Icon } from "lucide-react"

export function ReportePlaceholder() {
  return (
    <div className="flex flex-1 flex-col items-center justify-center p-4 lg:p-6">
      <div className="text-center max-w-sm">
        <div className="flex items-center justify-center size-14 rounded-xl bg-primary/8 border border-primary/15 mx-auto mb-5">
          <BarChart3Icon className="size-7 text-primary" aria-hidden="true" />
        </div>
        <h1 className="text-lg font-bold text-foreground mb-1.5">
          Reporte Economico
        </h1>
        <p className="text-sm text-muted-foreground leading-relaxed">
          Esta seccion se encuentra en desarrollo. Los reportes economicos
          estaran disponibles proximamente.
        </p>
      </div>
    </div>
  )
}
