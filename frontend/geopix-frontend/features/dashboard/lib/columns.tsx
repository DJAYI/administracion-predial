"use client";

import { ColumnDef } from "@tanstack/react-table";
import {
  ArrowUpDown,
  MapPinIcon,
  UserIcon,
  EyeIcon,
  PencilIcon,
} from "lucide-react";
import { Button } from "@/shared/ui/button";
import type { Predio } from "@/features/dashboard/lib/types";

export const columns: ColumnDef<Predio>[] = [
  {
    accessorKey: "codigoPredial",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        className="-ml-3 h-8 text-xs font-semibold uppercase tracking-wider text-muted-foreground hover:text-foreground"
      >
        Código Predial
        <ArrowUpDown className="ml-1.5 size-3 opacity-50" />
      </Button>
    ),
    cell: ({ row }) => (
      <span className="font-mono text-xs tracking-wide text-foreground/90 bg-muted/60 px-2 py-0.5 rounded">
        {row.getValue("codigoPredial")}
      </span>
    ),
  },
  {
    accessorKey: "direccion",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        className="-ml-3 h-8 text-xs font-semibold uppercase tracking-wider text-muted-foreground hover:text-foreground"
      >
        Dirección
        <ArrowUpDown className="ml-1.5 size-3 opacity-50" />
      </Button>
    ),
    cell: ({ row }) => (
      <span className="flex items-center gap-2 text-sm text-foreground/80">
        <MapPinIcon
          className="size-3.5 text-muted-foreground/50 shrink-0"
          aria-hidden="true"
        />
        {row.getValue("direccion")}
      </span>
    ),
  },
  {
    accessorKey: "propietario",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        className="-ml-3 h-8 text-xs font-semibold uppercase tracking-wider text-muted-foreground hover:text-foreground"
      >
        Propietario
        <ArrowUpDown className="ml-1.5 size-3 opacity-50" />
      </Button>
    ),
    cell: ({ row }) => (
      <span className="flex items-center gap-2 text-sm font-medium text-foreground">
        <span className="flex items-center justify-center size-6 rounded-full bg-primary/8 shrink-0">
          <UserIcon className="size-3 text-primary/60" aria-hidden="true" />
        </span>
        {row.getValue("propietario")}
      </span>
    ),
  },
  {
    accessorKey: "nit",
    header: () => (
      <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
        NIT
      </span>
    ),
    cell: ({ row }) => (
      <span className="text-sm tabular-nums text-muted-foreground">
        {row.getValue("nit")}
      </span>
    ),
  },
  {
    id: "actions",
    header: () => (
      <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
        Acciones
      </span>
    ),
    cell: ({ row }) => (
      <div className="flex items-center gap-1">
        <Button
          variant="ghost"
          size="icon"
          className="size-8 text-muted-foreground hover:text-foreground hover:bg-muted/50"
          onClick={(e) => {
            e.stopPropagation();
            // TODO: navegar a detalle del predio
          }}
          aria-label={`Ver predio ${row.getValue("codigoPredial")}`}
        >
          <EyeIcon className="size-4" />
        </Button>
        <Button
          variant="ghost"
          size="icon"
          className="size-8 text-muted-foreground hover:text-foreground hover:bg-muted/50"
          onClick={(e) => {
            e.stopPropagation();
            // TODO: navegar a edición del predio
          }}
          aria-label={`Editar predio ${row.getValue("codigoPredial")}`}
        >
          <PencilIcon className="size-3.5" />
        </Button>
      </div>
    ),
    enableSorting: false,
    enableHiding: false,
  },
];
