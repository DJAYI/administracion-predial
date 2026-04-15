"use client"

import { useEffect } from "react"
import { useRouter, usePathname } from "next/navigation"
import { useAuth } from "@/features/auth/context"
import { canAccessRoute } from "@/features/auth/lib/rbac"
import { notify } from "@/shared/lib/toast"

/**
 * Protege las rutas del dashboard:
 * 1. Redirige a /auth si no hay sesion
 * 2. Redirige a /dashboard si el usuario no tiene permiso para la ruta actual
 */
export function AuthGuard({ children }: { children: React.ReactNode }) {
  const { user, isLoading } = useAuth()
  const router = useRouter()
  const pathname = usePathname()

  useEffect(() => {
    if (isLoading) return

    // No autenticado → login
    if (!user) {
      router.replace("/auth")
      return
    }

    // Autenticado pero sin permiso para esta ruta
    if (!canAccessRoute(pathname, user.roles)) {
      notify.warning("Acceso restringido", {
        description: "No tienes permisos para acceder a esta sección",
      })
      router.replace("/dashboard")
    }
  }, [user, isLoading, pathname, router])

  // Mientras carga, mostrar spinner
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="flex flex-col items-center gap-4">
          <span
            className="size-8 border-3 border-primary/20 border-t-primary rounded-full animate-spin"
            aria-label="Cargando"
          />
          <span className="text-sm text-muted-foreground">
            Cargando plataforma...
          </span>
        </div>
      </div>
    )
  }

  // No autenticado — no renderizar nada (el useEffect redirige)
  if (!user) return null

  // Sin permiso — no renderizar (el useEffect redirige)
  if (!canAccessRoute(pathname, user.roles)) return null

  return <>{children}</>
}
