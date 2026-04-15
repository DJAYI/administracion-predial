"use client";

import { useEffect, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/shared/ui/dialog";
import { Badge } from "@/shared/ui/badge";
import { Separator } from "@/shared/ui/separator";
import { Skeleton } from "@/shared/ui/skeleton";
import {
  PlusCircleIcon,
  PencilIcon,
  Trash2Icon,
  RotateCcwIcon,
  ClockIcon,
} from "lucide-react";
import { getUserHistory } from "@/features/usuarios/lib/api";
import type {
  AuditLogEntry,
  UserResponse,
} from "@/features/usuarios/lib/types";

interface UserHistoryDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  user: UserResponse | null;
}

const ACTION_CONFIG: Record<
  AuditLogEntry["action"],
  {
    label: string;
    icon: React.ElementType;
    variant: "default" | "secondary" | "destructive";
  }
> = {
  CREATE: { label: "Creacion", icon: PlusCircleIcon, variant: "default" },
  UPDATE: { label: "Modificacion", icon: PencilIcon, variant: "secondary" },
  DELETE: { label: "Eliminacion", icon: Trash2Icon, variant: "destructive" },
  RESTORE: { label: "Restauracion", icon: RotateCcwIcon, variant: "default" },
};

function formatDate(dateStr: string): string {
  return new Intl.DateTimeFormat("es-CO", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(new Date(dateStr));
}

function HistoryEntry({ entry }: { entry: AuditLogEntry }) {
  const config = ACTION_CONFIG[entry.action];
  const Icon = config.icon;

  return (
    <div className="flex gap-3">
      <div className="flex flex-col items-center">
        <div className="flex items-center justify-center size-8 rounded-full bg-muted shrink-0">
          <Icon className="size-3.5 text-muted-foreground" />
        </div>
        <div className="w-px flex-1 bg-border/50 mt-1" />
      </div>
      <div className="flex-1 pb-4">
        <div className="flex items-center gap-2 mb-1">
          <Badge variant={config.variant} className="text-xs">
            {config.label}
          </Badge>
          <span className="text-xs text-muted-foreground">
            por <span className="font-medium">{entry.performedBy}</span>
          </span>
        </div>
        <div className="flex items-center gap-1.5 text-xs text-muted-foreground/70 mb-2">
          <ClockIcon className="size-3" />
          {formatDate(entry.timestamp)}
        </div>
        {entry.changesDescription && (
          <p className="text-xs text-muted-foreground leading-relaxed bg-muted/40 rounded-md px-2.5 py-1.5">
            {entry.changesDescription}
          </p>
        )}
      </div>
    </div>
  );
}

function HistorySkeleton() {
  return (
    <div className="flex flex-col gap-4">
      {[1, 2, 3].map((i) => (
        <div key={i} className="flex gap-3">
          <Skeleton className="size-8 rounded-full shrink-0" />
          <div className="flex-1 flex flex-col gap-2">
            <Skeleton className="h-4 w-32" />
            <Skeleton className="h-3 w-48" />
          </div>
        </div>
      ))}
    </div>
  );
}

export function UserHistoryDialog({
  open,
  onOpenChange,
  user,
}: UserHistoryDialogProps) {
  const [history, setHistory] = useState<AuditLogEntry[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!open || !user) return;

    let cancelled = false;
    setIsLoading(true);
    setError(null);

    getUserHistory(user.id)
      .then((data) => {
        if (!cancelled) setHistory(data);
      })
      .catch((err) => {
        if (!cancelled) {
          setError(
            err instanceof Error ? err.message : "Error al cargar historial",
          );
        }
      })
      .finally(() => {
        if (!cancelled) setIsLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, [open, user]);

  if (!user) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md max-h-[80vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Historial de auditoria</DialogTitle>
          <DialogDescription>
            Registro de cambios para el usuario &ldquo;{user.username}&rdquo;.
          </DialogDescription>
        </DialogHeader>

        <Separator />

        {isLoading && <HistorySkeleton />}

        {error && (
          <div
            className="rounded-lg bg-destructive/10 px-3 py-2 text-sm text-destructive"
            role="alert"
          >
            {error}
          </div>
        )}

        {!isLoading && !error && history.length === 0 && (
          <div className="flex flex-col items-center justify-center gap-2 py-8 text-center">
            <ClockIcon className="size-5 text-muted-foreground/40" />
            <p className="text-sm text-muted-foreground">
              No se encontraron registros de auditoria.
            </p>
          </div>
        )}

        {!isLoading && !error && history.length > 0 && (
          <div className="flex flex-col">
            {history.map((entry) => (
              <HistoryEntry key={entry.id} entry={entry} />
            ))}
          </div>
        )}

        <DialogFooter showCloseButton />
      </DialogContent>
    </Dialog>
  );
}
