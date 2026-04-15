"use client"

import { useState } from "react"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogMedia,
} from "@/shared/ui/alert-dialog"
import { Trash2Icon } from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { deleteUser } from "@/features/usuarios/lib/api"
import type { UserResponse } from "@/features/usuarios/lib/types"

interface UserDeleteDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  user: UserResponse | null
  onSuccess: () => void
}

export function UserDeleteDialog({
  open,
  onOpenChange,
  user,
  onSuccess,
}: UserDeleteDialogProps) {
  const [isDeleting, setIsDeleting] = useState(false)

  async function handleDelete() {
    if (!user) return

    setIsDeleting(true)
    try {
      await deleteUser(user.id)
      notify.success("Usuario eliminado", {
        description: `El usuario "${user.username}" ha sido desactivado correctamente`,
      })
      onOpenChange(false)
      onSuccess()
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Error inesperado"
      notify.error("Error al eliminar", { description: message })
    } finally {
      setIsDeleting(false)
    }
  }

  if (!user) return null

  return (
    <AlertDialog open={open} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogMedia className="bg-destructive/10">
            <Trash2Icon className="text-destructive" />
          </AlertDialogMedia>
          <AlertDialogTitle>Eliminar usuario</AlertDialogTitle>
          <AlertDialogDescription>
            Esta a punto de desactivar al usuario{" "}
            <strong className="text-foreground">{user.username}</strong>. El
            usuario no podra iniciar sesion pero sus datos se conservaran y
            podra ser restaurado posteriormente.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel disabled={isDeleting}>
            Cancelar
          </AlertDialogCancel>
          <AlertDialogAction
            variant="destructive"
            onClick={handleDelete}
            disabled={isDeleting}
          >
            {isDeleting ? (
              <span className="flex items-center gap-2">
                <span
                  className="size-4 border-2 border-destructive-foreground/30 border-t-destructive-foreground rounded-full animate-spin"
                  aria-hidden="true"
                />
                Eliminando...
              </span>
            ) : (
              "Eliminar"
            )}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}
