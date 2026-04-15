"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { useAuth } from "@/features/auth/context"

export default function Home() {
  const { user, isLoading } = useAuth()
  const router = useRouter()

  useEffect(() => {
    if (isLoading) return
    if (user) {
      router.replace("/dashboard")
    } else {
      router.replace("/auth")
    }
  }, [user, isLoading, router])

  return (
    <div className="min-h-screen flex items-center justify-center bg-background">
      <span
        className="size-8 border-3 border-primary/20 border-t-primary rounded-full animate-spin"
        aria-label="Cargando"
      />
    </div>
  )
}
