"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { HexagonIcon } from "lucide-react";
import { ThemeToggle } from "@/shared/components/theme/theme-toggle";
import { useAuth } from "@/features/auth/context";
import { LoginForm } from "@/features/auth/components/login-form";

export default function AuthPage() {
  const router = useRouter();
  const { user, isLoading } = useAuth();

  // Redirigir al dashboard si ya esta autenticado
  useEffect(() => {
    if (!isLoading && user) {
      router.replace("/dashboard");
    }
  }, [user, isLoading, router]);

  // Mostrar loading mientras se verifica sesion
  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="flex flex-col items-center gap-4">
          <span
            className="size-8 border-3 border-primary/20 border-t-primary rounded-full animate-spin"
            aria-label="Verificando sesión"
          />
          <span className="text-sm text-muted-foreground">
            Verificando sesión...
          </span>
        </div>
      </div>
    );
  }

  // Si ya autenticado, no renderizar el form (el useEffect redirige)
  if (user) return null;

  return (
    <div className="min-h-screen grid grid-cols-1 lg:grid-cols-2 bg-background text-foreground selection:bg-primary/20">
      {/* Decorative panel — always dark, institutional */}
      <div
        className="dark relative hidden lg:flex flex-col overflow-hidden"
        style={{ background: "oklch(0.14 0.025 255)" }}
      >
        <div className="absolute inset-0">
          <Image
            src="https://images.unsplash.com/photo-1524661135-423995f22d0b?q=80&w=2074&auto=format&fit=crop"
            alt="Análisis espacial y georeferenciación"
            fill
            className="object-cover opacity-30 mix-blend-luminosity"
            priority
          />
          <div className="absolute inset-0 bg-gradient-to-br from-[oklch(0.14_0.025_255)]/95 via-[oklch(0.14_0.025_255)]/70 to-[oklch(0.42_0.09_160)]/10" />
        </div>

        {/* Content over image */}
        <div className="relative z-10 p-10 flex flex-col h-full justify-between">
          <div className="flex items-center gap-2.5">
            <div className="flex items-center justify-center size-10 rounded-lg bg-[oklch(0.58_0.10_160)]">
              <HexagonIcon className="size-5 text-white" aria-hidden="true" />
            </div>
            <div className="flex flex-col">
              <span className="text-lg font-bold tracking-widest text-white">
                <span className="text-[#C9A24D]">GEO</span>
                <span className="text-white">PIX</span>
              </span>
              <span className="text-[10px] font-medium text-white/30 tracking-wide uppercase">
                Sistema de Gestión Predial
              </span>
            </div>
          </div>

          <div className="max-w-md">
            <h2 className="text-3xl font-bold text-white mb-4 leading-tight tracking-tight">
              Gestión territorial centralizada y precisa.
            </h2>
            <p className="text-white/45 text-base leading-relaxed">
              Herramientas de vanguardia para la estructuración de información
              predial y análisis económico de manera eficiente.
            </p>
          </div>

          <p className="text-white/20 text-xs">
            &copy; {new Date().getFullYear()} GEOPIX. Plataforma institucional.
          </p>
        </div>
      </div>

      {/* Auth Form Side — follows theme */}
      <div className="relative flex flex-1 flex-col justify-center px-6 py-12 sm:px-12 lg:px-20 xl:px-24">
        {/* Theme toggle */}
        <div className="absolute top-4 right-4">
          <ThemeToggle />
        </div>

        <div className="mx-auto w-full max-w-sm flex relative flex-col items-center lg:items-start">
          {/* Mobile Branding */}
          <div className="flex flex-col items-center lg:hidden mb-8">
            <div className="flex items-center justify-center size-12 rounded-xl bg-primary/10 border border-primary/15 mb-4">
              <HexagonIcon className="size-6 text-primary" aria-hidden="true" />
            </div>
            <h1 className="text-2xl font-bold tracking-widest text-primary">
              <span className="text-[#C9A24D]">GEO</span>
              <span className="text-primary">PIX</span>
            </h1>
          </div>

          <div className="text-center lg:text-left mb-8 w-full">
            <h2 className="text-2xl font-bold tracking-tight text-foreground">
              Iniciar Sesión
            </h2>
            <p className="text-sm text-muted-foreground mt-2">
              Ingresa tus credenciales de acceso a la plataforma.
            </p>
          </div>

          <LoginForm />

          <p className="text-center lg:text-left text-xs mt-10 flex gap-1.5 text-muted-foreground/60 w-full justify-center">
            &copy; {new Date().getFullYear()}{" "}
            <span>
              <span className="text-[#C9A24D]">GEO</span>
              <span className="text-primary">PIX</span>. Todos los derechos
            </span>
            reservados.
          </p>
        </div>
      </div>
    </div>
  );
}
