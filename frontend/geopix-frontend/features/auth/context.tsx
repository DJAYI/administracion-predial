"use client"

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react"
import { useRouter } from "next/navigation"
import type { AuthUser, LoginCredentials } from "@/features/auth/lib/types"
import {
  login as apiLogin,
  logout as apiLogout,
  getMe,
  UnauthenticatedError,
} from "@/features/auth/lib/api"

/* ------------------------------------------------------------------ */
/*  Context shape                                                      */
/* ------------------------------------------------------------------ */

interface AuthContextValue {
  /** Usuario actual — null si no autenticado, undefined mientras carga */
  user: AuthUser | null | undefined
  /** True mientras se verifica la sesion inicial */
  isLoading: boolean
  /** Login con credenciales */
  login: (credentials: LoginCredentials) => Promise<AuthUser>
  /** Logout y redireccion a /auth */
  logout: () => Promise<void>
}

const AuthContext = createContext<AuthContextValue | null>(null)

/* ------------------------------------------------------------------ */
/*  Provider                                                           */
/* ------------------------------------------------------------------ */

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const router = useRouter()
  const [user, setUser] = useState<AuthUser | null | undefined>(undefined)
  const [isLoading, setIsLoading] = useState(true)

  // Verificar sesion al montar
  useEffect(() => {
    let cancelled = false

    async function checkSession() {
      try {
        const me = await getMe()
        if (!cancelled) setUser(me)
      } catch {
        if (!cancelled) setUser(null)
      } finally {
        if (!cancelled) setIsLoading(false)
      }
    }

    checkSession()
    return () => {
      cancelled = true
    }
  }, [])

  const login = useCallback(
    async (credentials: LoginCredentials): Promise<AuthUser> => {
      const response = await apiLogin(credentials)
      const authUser: AuthUser = {
        username: response.username,
        email: response.email,
        roles: response.roles,
      }
      setUser(authUser)
      return authUser
    },
    [],
  )

  const logout = useCallback(async () => {
    await apiLogout()
    setUser(null)
    router.push("/auth")
  }, [router])

  const value = useMemo<AuthContextValue>(
    () => ({ user, isLoading, login, logout }),
    [user, isLoading, login, logout],
  )

  return <AuthContext value={value}>{children}</AuthContext>
}

/* ------------------------------------------------------------------ */
/*  Hook                                                               */
/* ------------------------------------------------------------------ */

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error("useAuth debe usarse dentro de <AuthProvider>")
  }
  return ctx
}
