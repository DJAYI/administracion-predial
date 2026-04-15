"use client"

import Link from "next/link"
import { SettingsIcon, UsersIcon, ChevronRightIcon } from "lucide-react"
import { Card, CardHeader, CardTitle, CardDescription } from "@/shared/ui/card"

const configSections = [
  {
    title: "Gestion de usuarios",
    description:
      "Cree, edite, desactive y restaure cuentas de usuario del sistema",
    icon: UsersIcon,
    href: "/dashboard/configuracion/usuarios",
  },
]

export function ConfiguracionHome() {
  return (
    <div className="flex flex-1 flex-col py-8 px-4 sm:px-6 lg:px-8 bg-background">
      <div className="w-full max-w-4xl mx-auto flex flex-col gap-6">
        {/* Header */}
        <div className="flex items-center gap-3">
          <div className="flex items-center justify-center size-10 rounded-lg bg-primary/8 border border-primary/10">
            <SettingsIcon
              className="size-5 text-primary"
              aria-hidden="true"
            />
          </div>
          <div>
            <h1 className="text-xl font-bold text-foreground tracking-tight">
              Configuracion
            </h1>
            <p className="text-sm text-muted-foreground">
              Administre los parametros del sistema
            </p>
          </div>
        </div>

        {/* Sections grid */}
        <div className="grid gap-4 sm:grid-cols-2">
          {configSections.map((section) => (
            <Link key={section.href} href={section.href}>
              <Card className="group transition-colors hover:border-primary/30 hover:bg-muted/30 cursor-pointer">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex items-center justify-center size-9 rounded-lg bg-primary/8 mb-2">
                      <section.icon
                        className="size-4.5 text-primary"
                        aria-hidden="true"
                      />
                    </div>
                    <ChevronRightIcon className="size-4 text-muted-foreground/40 group-hover:text-primary transition-colors" />
                  </div>
                  <CardTitle className="text-sm">{section.title}</CardTitle>
                  <CardDescription className="text-xs leading-relaxed">
                    {section.description}
                  </CardDescription>
                </CardHeader>
              </Card>
            </Link>
          ))}
        </div>
      </div>
    </div>
  )
}
