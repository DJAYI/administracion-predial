"use client";

import { ColumnDef } from "@tanstack/react-table";
import {
  ArrowUpDown,
  EyeIcon,
  PencilIcon,
  Trash2Icon,
  RotateCcwIcon,
  HistoryIcon,
} from "lucide-react";
import { Button } from "@/shared/ui/button";
import { Badge } from "@/shared/ui/badge";
import { getRoleLabel } from "@/features/auth/lib/rbac";
import type { UserResponse } from "@/features/usuarios/lib/types";

interface ColumnActions {
  onView: (user: UserResponse) => void;
  onEdit: (user: UserResponse) => void;
  onDelete: (user: UserResponse) => void;
  onRestore: (user: UserResponse) => void;
  onHistory: (user: UserResponse) => void;
}

export function getUserColumns(
  actions: ColumnActions,
): ColumnDef<UserResponse>[] {
  return [
    {
      accessorKey: "username",
      header: ({ column }) => (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="-ml-3 h-8 text-xs font-semibold uppercase tracking-wider text-muted-foreground hover:text-foreground"
        >
          Usuario
          <ArrowUpDown className="ml-1.5 size-3 opacity-50" />
        </Button>
      ),
      cell: ({ row }) => (
        <span className="text-sm font-medium text-foreground truncate block max-w-[150px]">
          {row.getValue("username")}
        </span>
      ),
    },
    {
      accessorKey: "email",
      header: ({ column }) => (
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
          className="-ml-3 h-8 text-xs font-semibold uppercase tracking-wider text-muted-foreground hover:text-foreground"
        >
          Email
          <ArrowUpDown className="ml-1.5 size-3 opacity-50" />
        </Button>
      ),
      cell: ({ row }) => (
        <span className="text-sm text-muted-foreground truncate block max-w-[200px]">
          {row.getValue("email")}
        </span>
      ),
    },
    {
      accessorKey: "roles",
      header: () => (
        <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
          Roles
        </span>
      ),
      cell: ({ row }) => {
        const roles = row.getValue("roles") as string[];
        return (
          <div className="flex flex-wrap gap-1">
            {roles.map((role) => (
              <Badge
                key={role}
                variant="secondary"
                className="text-[10px] leading-tight px-1.5 py-0.5"
              >
                {getRoleLabel(
                  role as "ADMINISTRADOR" | "CONTADOR" | "EJECUTOR_INTEGRAL",
                )}
              </Badge>
            ))}
          </div>
        );
      },
      enableSorting: false,
    },
    {
      accessorKey: "enabled",
      header: () => (
        <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
          Estado
        </span>
      ),
      cell: ({ row }) => {
        const enabled = row.getValue("enabled") as boolean;
        const isDeleted = row.original.deletedAt !== null;
        if (isDeleted) {
          return (
            <Badge
              variant="destructive"
              className="text-[10px] leading-tight px-1.5 py-0.5"
            >
              Eliminado
            </Badge>
          );
        }
        return enabled ? (
          <Badge
            variant="default"
            className="text-[10px] leading-tight px-1.5 py-0.5"
          >
            Activo
          </Badge>
        ) : (
          <Badge
            variant="secondary"
            className="text-[10px] leading-tight px-1.5 py-0.5"
          >
            Inactivo
          </Badge>
        );
      },
      size: 90,
      enableSorting: false,
    },
    {
      id: "actions",
      header: () => (
        <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
          Acciones
        </span>
      ),
      cell: ({ row }) => {
        const user = row.original;
        const isDeleted = user.deletedAt !== null;

        return (
          <div className="flex items-center gap-0.5">
            <Button
              variant="ghost"
              size="icon"
              className="size-7 text-muted-foreground hover:text-foreground hover:bg-muted/50"
              onClick={(e) => {
                e.stopPropagation();
                actions.onView(user);
              }}
              aria-label={`Ver usuario ${user.username}`}
            >
              <EyeIcon className="size-3.5" />
            </Button>

            {!isDeleted && (
              <>
                <Button
                  variant="ghost"
                  size="icon"
                  className="size-7 text-muted-foreground hover:text-foreground hover:bg-muted/50"
                  onClick={(e) => {
                    e.stopPropagation();
                    actions.onEdit(user);
                  }}
                  aria-label={`Editar usuario ${user.username}`}
                >
                  <PencilIcon className="size-3" />
                </Button>
                <Button
                  variant="ghost"
                  size="icon"
                  className="size-7 text-muted-foreground hover:text-destructive hover:bg-destructive/10"
                  onClick={(e) => {
                    e.stopPropagation();
                    actions.onDelete(user);
                  }}
                  aria-label={`Eliminar usuario ${user.username}`}
                >
                  <Trash2Icon className="size-3" />
                </Button>
              </>
            )}

            {isDeleted && (
              <Button
                variant="ghost"
                size="icon"
                className="size-7 text-muted-foreground hover:text-primary hover:bg-primary/10"
                onClick={(e) => {
                  e.stopPropagation();
                  actions.onRestore(user);
                }}
                aria-label={`Restaurar usuario ${user.username}`}
              >
                <RotateCcwIcon className="size-3" />
              </Button>
            )}

            <Button
              variant="ghost"
              size="icon"
              className="size-7 text-muted-foreground hover:text-foreground hover:bg-muted/50"
              onClick={(e) => {
                e.stopPropagation();
                actions.onHistory(user);
              }}
              aria-label={`Historial de ${user.username}`}
            >
              <HistoryIcon className="size-3" />
            </Button>
          </div>
        );
      },
      size: 140,
      enableSorting: false,
      enableHiding: false,
    },
  ];
}
