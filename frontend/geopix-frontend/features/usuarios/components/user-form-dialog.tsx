"use client"

import { useState, useEffect } from "react"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/shared/ui/dialog"
import { Button } from "@/shared/ui/button"
import { Input } from "@/shared/ui/input"
import { Switch } from "@/shared/ui/switch"
import { Checkbox } from "@/shared/ui/checkbox"
import {
  Field,
  FieldLabel,
  FieldGroup,
  FieldError,
  FieldSet,
  FieldLegend,
} from "@/shared/ui/field"
import { EyeIcon, EyeOffIcon } from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { createUser, updateUser } from "@/features/usuarios/lib/api"
import type {
  UserResponse,
  CreateUserRequest,
  UpdateUserRequest,
  ApiErrorResponse,
} from "@/features/usuarios/lib/types"
import type { Role } from "@/features/auth/lib/types"
import { getRoleLabel } from "@/features/auth/lib/rbac"

const ALL_ROLES: Role[] = ["ADMINISTRADOR", "CONTADOR", "EJECUTOR_INTEGRAL"]

interface UserFormDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  user?: UserResponse | null
  onSuccess: () => void
}

interface FormErrors {
  username?: string
  email?: string
  password?: string
  roles?: string
  general?: string
}

export function UserFormDialog({
  open,
  onOpenChange,
  user,
  onSuccess,
}: UserFormDialogProps) {
  const isEditing = !!user

  const [username, setUsername] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [roles, setRoles] = useState<Role[]>([])
  const [enabled, setEnabled] = useState(true)
  const [showPassword, setShowPassword] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [errors, setErrors] = useState<FormErrors>({})

  // Sync form state when dialog opens or user prop changes
  useEffect(() => {
    if (!open) return

    if (user) {
      setUsername(user.username)
      setEmail(user.email)
      setRoles([...user.roles])
      setEnabled(user.enabled)
    } else {
      setUsername("")
      setEmail("")
      setRoles([])
      setEnabled(true)
    }
    setPassword("")
    setShowPassword(false)
    setErrors({})
  }, [open, user])

  function toggleRole(role: Role) {
    setRoles((prev) =>
      prev.includes(role)
        ? prev.filter((r) => r !== role)
        : [...prev, role],
    )
  }

  function validate(): boolean {
    const newErrors: FormErrors = {}

    if (!isEditing || username !== user?.username) {
      if (!username.trim()) {
        newErrors.username = "El nombre de usuario es requerido"
      } else if (username.length < 3 || username.length > 50) {
        newErrors.username = "Debe tener entre 3 y 50 caracteres"
      }
    }

    if (!isEditing || email !== user?.email) {
      if (!email.trim()) {
        newErrors.email = "El email es requerido"
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        newErrors.email = "Formato de email invalido"
      }
    }

    if (!isEditing && !password.trim()) {
      newErrors.password = "La contrasena es requerida"
    } else if (password && (password.length < 8 || password.length > 100)) {
      newErrors.password = "Debe tener entre 8 y 100 caracteres"
    }

    if (roles.length === 0) {
      newErrors.roles = "Debe seleccionar al menos un rol"
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    if (!validate()) return

    setIsSubmitting(true)
    setErrors({})

    try {
      if (isEditing && user) {
        const updates: UpdateUserRequest = {}
        if (username !== user.username) updates.username = username
        if (email !== user.email) updates.email = email
        if (password) updates.password = password
        if (JSON.stringify([...roles].sort()) !== JSON.stringify([...user.roles].sort())) {
          updates.roles = roles
        }
        if (enabled !== user.enabled) updates.enabled = enabled

        await updateUser(user.id, updates)
        notify.success("Usuario actualizado", {
          description: `El usuario "${username}" ha sido modificado correctamente`,
        })
      } else {
        const data: CreateUserRequest = {
          username,
          email,
          password,
          roles,
          enabled,
        }
        await createUser(data)
        notify.success("Usuario creado", {
          description: `El usuario "${username}" ha sido registrado correctamente`,
        })
      }
      onOpenChange(false)
      onSuccess()
    } catch (err) {
      const message = err instanceof Error ? err.message : "Error inesperado"

      // Try to parse field errors from API response
      try {
        const parsed: ApiErrorResponse = JSON.parse(
          message.replace(/^API error \d+: /, ""),
        )
        if (parsed.fieldErrors) {
          setErrors(parsed.fieldErrors as unknown as FormErrors)
        } else {
          setErrors({ general: parsed.message || message })
        }
        notify.error(isEditing ? "Error al actualizar" : "Error al crear", {
          description: parsed.message || message,
        })
      } catch {
        setErrors({ general: message })
        notify.error(isEditing ? "Error al actualizar" : "Error al crear", {
          description: message,
        })
      }
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>
            {isEditing ? "Editar usuario" : "Crear usuario"}
          </DialogTitle>
          <DialogDescription>
            {isEditing
              ? `Modifique los campos que desea actualizar para "${user?.username}".`
              : "Complete los datos para registrar un nuevo usuario en el sistema."}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          {errors.general && (
            <div className="rounded-lg bg-destructive/10 px-3 py-2 text-sm text-destructive" role="alert">
              {errors.general}
            </div>
          )}

          <FieldGroup>
            <Field data-invalid={!!errors.username || undefined}>
              <FieldLabel htmlFor="user-username">Usuario</FieldLabel>
              <Input
                id="user-username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="nombre.usuario"
                aria-invalid={!!errors.username || undefined}
                autoComplete="off"
              />
              {errors.username && <FieldError>{errors.username}</FieldError>}
            </Field>

            <Field data-invalid={!!errors.email || undefined}>
              <FieldLabel htmlFor="user-email">Email</FieldLabel>
              <Input
                id="user-email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="usuario@geopix.com"
                aria-invalid={!!errors.email || undefined}
                autoComplete="off"
              />
              {errors.email && <FieldError>{errors.email}</FieldError>}
            </Field>

            <Field data-invalid={!!errors.password || undefined}>
              <FieldLabel htmlFor="user-password">
                {isEditing ? "Nueva contrasena (opcional)" : "Contrasena"}
              </FieldLabel>
              <div className="relative">
                <Input
                  id="user-password"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder={
                    isEditing
                      ? "Dejar vacio para no cambiar"
                      : "Minimo 8 caracteres"
                  }
                  aria-invalid={!!errors.password || undefined}
                  autoComplete="new-password"
                  className="pr-11"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-0 top-0 h-full w-11 flex items-center justify-center text-muted-foreground/60 hover:text-foreground transition-colors"
                  aria-label={
                    showPassword ? "Ocultar contrasena" : "Mostrar contrasena"
                  }
                >
                  {showPassword ? (
                    <EyeOffIcon className="size-4" />
                  ) : (
                    <EyeIcon className="size-4" />
                  )}
                </button>
              </div>
              {errors.password && <FieldError>{errors.password}</FieldError>}
            </Field>

            <FieldSet>
              <FieldLegend variant="label">Roles</FieldLegend>
              {errors.roles && (
                <FieldError>{errors.roles}</FieldError>
              )}
              <FieldGroup className="gap-3">
                {ALL_ROLES.map((role) => (
                  <Field key={role} orientation="horizontal">
                    <Checkbox
                      id={`role-${role}`}
                      checked={roles.includes(role)}
                      onCheckedChange={() => toggleRole(role)}
                    />
                    <FieldLabel htmlFor={`role-${role}`} className="font-normal">
                      {getRoleLabel(role)}
                    </FieldLabel>
                  </Field>
                ))}
              </FieldGroup>
            </FieldSet>

            <Field orientation="horizontal">
              <FieldLabel htmlFor="user-enabled" className="flex-1">
                Habilitado
              </FieldLabel>
              <Switch
                id="user-enabled"
                checked={enabled}
                onCheckedChange={setEnabled}
              />
            </Field>
          </FieldGroup>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={isSubmitting}
            >
              Cancelar
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? (
                <span className="flex items-center gap-2">
                  <span
                    className="size-4 border-2 border-primary-foreground/30 border-t-primary-foreground rounded-full animate-spin"
                    aria-hidden="true"
                  />
                  {isEditing ? "Guardando..." : "Creando..."}
                </span>
              ) : isEditing ? (
                "Guardar cambios"
              ) : (
                "Crear usuario"
              )}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}
