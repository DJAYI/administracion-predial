import { apiFetch } from "@/features/auth/lib/api"
import type { DepartamentoDANE, MunicipioDANE } from "@/features/predios/lib/types"

/* ------------------------------------------------------------------ */
/*  Catálogos DANE                                                      */
/* ------------------------------------------------------------------ */

/**
 * Obtiene la lista de todos los departamentos de Colombia.
 * GET /api/dane/departamentos
 */
export async function fetchDepartamentos(): Promise<DepartamentoDANE[]> {
  return apiFetch<DepartamentoDANE[]>("/api/dane/departamentos")
}

/**
 * Obtiene la lista de municipios de un departamento específico.
 * GET /api/dane/municipios/{codigo}
 *
 * @param codigo - Código DANE del departamento (ej: "05" para Antioquia)
 */
export async function fetchMunicipios(codigo: string): Promise<MunicipioDANE[]> {
  return apiFetch<MunicipioDANE[]>(`/api/dane/municipios/${encodeURIComponent(codigo)}`)
}

/* ------------------------------------------------------------------ */
/*  Caché local para optimizar llamadas                                */
/* ------------------------------------------------------------------ */

let departamentosCache: DepartamentoDANE[] | null = null
let municipiosCache: Map<string, MunicipioDANE[]> = new Map()

/**
 * Obtiene departamentos con caché en memoria.
 * Útil para evitar múltiples llamadas durante la sesión.
 */
export async function getDepartamentosCached(): Promise<DepartamentoDANE[]> {
  if (departamentosCache === null) {
    departamentosCache = await fetchDepartamentos()
  }
  return departamentosCache
}

/**
 * Obtiene municipios con caché en memoria por departamento.
 * Útil para evitar múltiples llamadas durante la sesión.
 *
 * @param codigo - Código DANE del departamento
 */
export async function getMunicipiosCached(codigo: string): Promise<MunicipioDANE[]> {
  if (!municipiosCache.has(codigo)) {
    const municipios = await fetchMunicipios(codigo)
    municipiosCache.set(codigo, municipios)
  }
  return municipiosCache.get(codigo)!
}

/**
 * Limpia la caché de departamentos y municipios.
 * Útil para forzar una actualización de datos.
 */
export function clearCatalogosCache(): void {
  departamentosCache = null
  municipiosCache.clear()
}
