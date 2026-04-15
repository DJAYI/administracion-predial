"use client";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/shared/ui/dialog";
import { Button } from "@/shared/ui/button";
import { Badge } from "@/shared/ui/badge";
import { Separator } from "@/shared/ui/separator";
import { getRoleLabel } from "@/features/auth/lib/rbac";
import type { UserResponse } from "@/features/usuarios/lib/types";

interface UserDetailDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  user: UserResponse | null;
}

function formatDate(dateStr: string | null): string {
  if (!dateStr) return "—";
  return new Intl.DateTimeFormat("es-CO", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(dateStr));
}

function DetailRow({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <div className="flex flex-col gap-1 sm:flex-row sm:items-start sm:gap-4">
      <span className="text-sm font-medium text-muted-foreground sm:w-32 shrink-0">
        {label}
      </span>
      <div className="text-sm text-foreground">{children}</div>
    </div>
  );
}

export function UserDetailDialog({
  open,
  onOpenChange,
  user,
}: UserDetailDialogProps) {
  if (!user) return null;

  const isDeleted = user.deletedAt !== null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Detalle de usuario</DialogTitle>
          <DialogDescription>
            Informacion completa del usuario &ldquo;{user.username}&rdquo;.
          </DialogDescription>
        </DialogHeader>

        <div className="flex flex-col gap-3">
          <DetailRow label="Usuario">
            <span className="font-medium">{user.username}</span>
          </DetailRow>

          <DetailRow label="Email">{user.email}</DetailRow>

          <DetailRow label="Roles">
            <div className="flex flex-wrap gap-1">
              {user.roles.map((role) => (
                <Badge key={role} variant="secondary" className="text-xs">
                  {getRoleLabel(role)}
                </Badge>
              ))}
            </div>
          </DetailRow>

          <DetailRow label="Estado">
            {isDeleted ? (
              <Badge variant="destructive" className="text-xs">
                Eliminado
              </Badge>
            ) : user.enabled ? (
              <Badge variant="default" className="text-xs">
                Activo
              </Badge>
            ) : (
              <Badge variant="secondary" className="text-xs">
                Inactivo
              </Badge>
            )}
          </DetailRow>

          <Separator />

          <DetailRow label="Creado">{formatDate(user.createdAt)}</DetailRow>

          <DetailRow label="Actualizado">
            {formatDate(user.updatedAt)}
          </DetailRow>

          {isDeleted && (
            <>
              <DetailRow label="Eliminado">
                {formatDate(user.deletedAt)}
              </DetailRow>
              <DetailRow label="Eliminado por">
                <span className="font-medium text-destructive">
                  {user.deletedBy ?? "—"}
                </span>
              </DetailRow>
            </>
          )}
        </div>

        <DialogFooter showCloseButton>
          {/* The showCloseButton adds a Close button automatically */}
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
