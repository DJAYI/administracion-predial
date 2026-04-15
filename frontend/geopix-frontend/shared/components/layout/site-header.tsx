"use client";

import { usePathname } from "next/navigation";
import { Separator } from "@/shared/ui/separator";
import { SidebarTrigger } from "@/shared/ui/sidebar";
import { ThemeToggle } from "@/shared/components/theme/theme-toggle";

const pageTitles: Record<string, string> = {
  "/dashboard": "Inicio",
  "/dashboard/creacion-de-predios": "Creación de Predios",
  "/dashboard/reporte-economico": "Reporte Económico",
  "/dashboard/georeferenciacion": "Georeferenciación",
  "/dashboard/configuracion": "Configuración",
};

export function SiteHeader() {
  const pathname = usePathname();
  const title = pageTitles[pathname] || "GEOPIX";

  return (
    <header
      role="banner"
      className="flex h-(--header-height) shrink-0 items-center gap-2 border-b border-border transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-(--header-height) bg-background"
    >
      <div className="flex w-full items-center gap-1 px-4 lg:gap-2 lg:px-6">
        <SidebarTrigger
          className="-ml-1 text-foreground/60 hover:text-foreground"
          aria-label="Abrir o cerrar menu lateral"
        />
        <Separator
          orientation="vertical"
          className="mx-2 data-[orientation=vertical]:h-4"
          aria-hidden="true"
        />
        <h1 className="text-sm font-semibold text-foreground">{title}</h1>
        <div className="ml-auto">
          <ThemeToggle />
        </div>
      </div>
    </header>
  );
}
