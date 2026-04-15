"use client";

import { useState, useMemo } from "react";
import { useRouter } from "next/navigation";
import { Input } from "@/shared/ui/input";
import { SearchIcon, HexagonIcon } from "lucide-react";
import { DataTable } from "@/shared/ui/data-table";
import { columns } from "@/features/dashboard/lib/columns";
import type { Predio } from "@/features/dashboard/lib/types";

const SAMPLE_DATA: Predio[] = [];

export function DashboardHome() {
  const router = useRouter();
  const [searchQuery, setSearchQuery] = useState("");

  const filteredData = useMemo(() => {
    if (!searchQuery.trim()) return SAMPLE_DATA;
    const q = searchQuery.toLowerCase();
    return SAMPLE_DATA.filter(
      (p) =>
        p.codigoPredial.toLowerCase().includes(q) ||
        p.direccion.toLowerCase().includes(q) ||
        p.propietario.toLowerCase().includes(q) ||
        p.nit.toLowerCase().includes(q),
    );
  }, [searchQuery]);

  return (
    <div className="flex flex-1 flex-col items-center justify-start py-12 px-4 sm:px-6 lg:px-8 bg-background">
      <div className="w-full max-w-2xl flex flex-col items-center gap-10">
        {/* Branding & Greeting */}
        <div className="flex flex-col items-center text-center">
          <div className="flex items-center justify-center size-14 rounded-xl bg-primary/8 mb-5 border border-primary/10">
            <HexagonIcon className="size-7 text-primary" aria-hidden="true" />
          </div>
          <h1 className="text-2xl sm:text-3xl font-bold tracking-tight text-foreground mb-2">
            Bienvenido a <span className="text-[#C9A24D]">GEO</span>PIX
          </h1>
          <p className="text-sm text-muted-foreground max-w-md leading-relaxed">
            Busque, consulte y gestione la información de predios del sistema de
            manera centralizada.
          </p>
        </div>

        {/* Search */}
        <div className="w-full max-w-lg relative">
          <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
            <SearchIcon
              className="size-4 text-muted-foreground/60"
              aria-hidden="true"
            />
          </div>
          <Input
            id="global-search"
            type="search"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Código predial, dirección, propietario o NIT..."
            className="block w-full pl-10 pr-4 py-2 h-10 text-sm bg-card border-border shadow-sm focus-visible:ring-2 focus-visible:ring-[#C9A24D]/30 focus-visible:border-[#C9A24D] rounded-lg transition-all"
            aria-label="Búsqueda global de predios"
          />
        </div>
      </div>

      {/* Data table */}
      <div className="w-full max-w-5xl mt-8">
        <DataTable
          columns={columns}
          data={filteredData}
          onRowClick={() => router.push("/dashboard")}
        />
      </div>
    </div>
  );
}
