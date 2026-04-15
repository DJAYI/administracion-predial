"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/shared/ui/button";
import { Input } from "@/shared/ui/input";
import { Label } from "@/shared/ui/label";
import { EyeIcon, EyeOffIcon } from "lucide-react";
import { notify } from "@/shared/lib/toast";
import { useAuth } from "@/features/auth/context";

export function LoginForm() {
  const router = useRouter();
  const { login } = useAuth();
  const [showPassword, setShowPassword] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setIsSubmitting(true);

    const formData = new FormData(e.currentTarget);
    const username = formData.get("username") as string;
    const password = formData.get("password") as string;

    try {
      await login({ username, password });
      notify.success("Bienvenido a GEOPIX", {
        description: "Sesión iniciada correctamente",
      });
      router.push("/dashboard");
    } catch (err) {
      const message =
        err instanceof Error
          ? err.message.includes("401")
            ? "Credenciales inválidas"
            : "Error de conexión con el servidor"
          : "Error inesperado";
      setError(message);
      notify.error("Error de autenticación", { description: message });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="space-y-5 w-full"
      aria-label="Formulario de inicio de sesión"
    >
      <div className="space-y-2">
        <Label
          htmlFor="username"
          className="text-foreground/80 text-sm font-medium"
        >
          Usuario
        </Label>
        <Input
          id="username"
          name="username"
          type="text"
          autoComplete="username"
          placeholder="Ingrese su usuario"
          required
          aria-required="true"
          className="h-11 bg-background border-border text-foreground placeholder:text-muted-foreground/40 focus-visible:border-primary focus-visible:ring-1 focus-visible:ring-primary transition-all"
        />
      </div>

      <div className="space-y-2">
        <div className="flex items-center justify-between">
          <Label
            htmlFor="password"
            className="text-foreground/80 text-sm font-medium"
          >
            Contraseña
          </Label>
          <a
            href="#"
            className="text-xs font-medium text-primary hover:text-primary/80 transition-colors"
          >
            Recuperar acceso
          </a>
        </div>
        <div className="relative">
          <Input
            id="password"
            name="password"
            type={showPassword ? "text" : "password"}
            autoComplete="current-password"
            placeholder="Ingrese su contraseña"
            required
            aria-required="true"
            className="h-11 bg-background border-border text-foreground placeholder:text-muted-foreground/40 pr-11 focus-visible:border-primary focus-visible:ring-1 focus-visible:ring-primary transition-all"
          />
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="absolute right-0 top-0 h-11 w-11 flex items-center justify-center text-muted-foreground/60 hover:text-foreground transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-primary"
            aria-label={
              showPassword ? "Ocultar contraseña" : "Mostrar contraseña"
            }
          >
            {showPassword ? (
              <EyeOffIcon className="size-4" />
            ) : (
              <EyeIcon className="size-4" />
            )}
          </button>
        </div>
      </div>

      {error && (
        <p className="text-sm text-destructive font-medium" role="alert">
          {error}
        </p>
      )}

      <Button
        type="submit"
        className="w-full h-11 font-semibold text-sm tracking-wide"
        disabled={isSubmitting}
      >
        {isSubmitting ? (
          <span className="flex items-center gap-2.5">
            <span
              className="size-4 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin"
              aria-hidden="true"
            />
            Verificando...
          </span>
        ) : (
          "Ingresar a la plataforma"
        )}
      </Button>
    </form>
  );
}
