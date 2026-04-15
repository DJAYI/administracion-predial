import { apiFetch } from "@/features/auth/lib/api"
import type {
  PredioResponse,
  CreatePredioRequest,
  CreatePredioRequestLegacy,
  UpdatePredioRequest,
  CheckMatriculaResponse,
  PredioVigencia,
  CreateVigenciaRequest,
  BatchVigenciaRequest,
  UpdateVigenciaRequest,
  MaestroOption,
  DepartamentoDANE,
  MunicipioDANE,
  DatosBasicos,
} from "@/features/predios/lib/types"

/* ------------------------------------------------------------------ */
/*  Tipos para tablas maestras (legacy)                                 */
/* ------------------------------------------------------------------ */

export type CategoriaMaestro =
  | "TIPO_PREDIO"
  | "TIPO_SERVIDUMBRE"
  | "TIPO_SUELO"
  | "DESTINO_PREDIO"
  | "TIPO_TITULARIDAD"
  | "TIPO_EXPEDIENTE"

/* ------------------------------------------------------------------ */
/*  API de Predios — v1                                                 */
/* ------------------------------------------------------------------ */

/**
 * POST /api/v1/predios
 * Crea un nuevo borrador de predio (Paso 1 - Datos Básicos).
 */
export async function createPredio(
  data: CreatePredioRequest,
): Promise<PredioResponse> {
  return apiFetch<PredioResponse>("/api/v1/predios", {
    method: "POST",
    body: JSON.stringify(data),
  })
}

/**
 * GET /api/v1/predios/{id}
 * Obtiene un predio por su ID.
 *
 * @param id - UUID del predio
 */
export async function getPredioById(id: string): Promise<PredioResponse> {
  return apiFetch<PredioResponse>(`/api/v1/predios/${encodeURIComponent(id)}`)
}

/**
 * PUT /api/v1/predios/{id}?paso=N
 * Actualiza un paso específico del wizard.
 *
 * @param id - UUID del predio
 * @param paso - Número del paso (2, 3, o 4)
 * @param data - Datos a actualizar
 */
export async function updatePredioPaso<T extends UpdatePredioRequest>(
  id: string,
  paso: number,
  data: T,
): Promise<PredioResponse> {
  return apiFetch<PredioResponse>(
    `/api/v1/predios/${encodeURIComponent(id)}?paso=${paso}`,
    {
      method: "PUT",
      body: JSON.stringify(data),
    },
  )
}

/**
 * GET /api/v1/predios/check-matricula?matricula={matricula}
 * Verifica si una matrícula inmobiliaria ya existe.
 *
 * @param matricula - Matrícula a verificar
 */
export async function checkMatricula(
  matricula: string,
): Promise<CheckMatriculaResponse> {
  return apiFetch<CheckMatriculaResponse>(
    `/api/v1/predios/check-matricula?matricula=${encodeURIComponent(matricula)}`,
  )
}

/**
 * POST /api/v1/predios/{id}/completar
 * Finaliza el registro del predio, marcando pasoActual = 4.
 *
 * @param id - UUID del predio
 */
export async function completarPredio(id: string): Promise<PredioResponse> {
  return apiFetch<PredioResponse>(
    `/api/v1/predios/${encodeURIComponent(id)}/completar`,
    {
      method: "POST",
    },
  )
}

/* ------------------------------------------------------------------ */
/*  API de Vigencias                                                    */
/* ------------------------------------------------------------------ */

/**
 * GET /api/v1/predios/{id}/vigencias
 * Obtiene todas las vigencias económicas de un predio.
 *
 * @param id - UUID del predio
 */
export async function getVigencias(id: string): Promise<PredioVigencia[]> {
  return apiFetch<PredioVigencia[]>(
    `/api/v1/predios/${encodeURIComponent(id)}/vigencias`,
  )
}

/**
 * POST /api/v1/predios/{id}/vigencias
 * Crea una vigencia económica individual.
 *
 * @param id - UUID del predio
 * @param data - Datos de la vigencia
 */
export async function createVigencia(
  id: string,
  data: CreateVigenciaRequest,
): Promise<PredioVigencia> {
  return apiFetch<PredioVigencia>(
    `/api/v1/predios/${encodeURIComponent(id)}/vigencias`,
    {
      method: "POST",
      body: JSON.stringify(data),
    },
  )
}

/**
 * POST /api/v1/predios/{id}/vigencias/batch
 * Crea múltiples vigencias económicas de forma batch.
 *
 * @param id - UUID del predio
 * @param data - Array de vigencias a crear
 */
export async function createVigenciasBatch(
  id: string,
  data: BatchVigenciaRequest[],
): Promise<PredioVigencia[]> {
  return apiFetch<PredioVigencia[]>(
    `/api/v1/predios/${encodeURIComponent(id)}/vigencias/batch`,
    {
      method: "POST",
      body: JSON.stringify(data),
    },
  )
}

/**
 * PUT /api/v1/predios/{id}/vigencias/{vigenciaId}
 * Actualiza una vigencia económica existente.
 *
 * @param id - UUID del predio
 * @param vigenciaId - ID numérico de la vigencia
 * @param data - Datos a actualizar
 */
export async function updateVigencia(
  id: string,
  vigenciaId: number,
  data: UpdateVigenciaRequest,
): Promise<PredioVigencia> {
  return apiFetch<PredioVigencia>(
    `/api/v1/predios/${encodeURIComponent(id)}/vigencias/${vigenciaId}`,
    {
      method: "PUT",
      body: JSON.stringify(data),
    },
  )
}

/**
 * DELETE /api/v1/predios/{id}/vigencias/{vigenciaId}
 * Elimina una vigencia económica (solo ADMIN).
 *
 * @param id - UUID del predio
 * @param vigenciaId - ID numérico de la vigencia
 */
export async function deleteVigencia(
  id: string,
  vigenciaId: number,
): Promise<{ message: string }> {
  return apiFetch<{ message: string }>(
    `/api/v1/predios/${encodeURIComponent(id)}/vigencias/${vigenciaId}`,
    {
      method: "DELETE",
    },
  )
}

/* ------------------------------------------------------------------ */
/*  Funciones legacy (para compatibilidad con código existente)          */
/* ------------------------------------------------------------------ */

/** Alias para getPredioById — usa UUID en vez de número */
export const getPredio = getPredioById

/** Alias para updatePredioPaso con paso=1 (Datos Básicos) */
export async function updatePredioDatosBasicos(
  id: string,
  datosBasicos: CreatePredioRequest["datosBasicos"],
): Promise<PredioResponse> {
  return updatePredioPaso(id, 1, { datosBasicos })
}

/** Alias para updatePredioPaso con paso=2 (Datos Físicos) */
export async function updatePredioDatosFisicos(
  id: string,
  datosFisicos: NonNullable<UpdatePredioRequest["datosFisicos"]>,
): Promise<PredioResponse> {
  return updatePredioPaso(id, 2, { datosFisicos })
}

/** Alias para updatePredioPaso con paso=3 (Titularidad) */
export async function updatePredioTitularidad(
  id: string,
  titularidad: NonNullable<UpdatePredioRequest["titularidad"]>,
): Promise<PredioResponse> {
  return updatePredioPaso(id, 3, { titularidad })
}

/* ------------------------------------------------------------------ */
/*  Funciones legacy DANE (para compatibilidad con componentes existentes) */
/* ------------------------------------------------------------------ */

/** GET /api/dane/departamentos — Listar todos los departamentos */
export async function getDepartamentos(): Promise<DepartamentoDANE[]> {
  return apiFetch<DepartamentoDANE[]>("/api/dane/departamentos")
}

/** GET /api/dane/municipios/{codigoDepartamento} — Municipios de un departamento */
export async function getMunicipios(
  codigoDepartamento: string,
): Promise<MunicipioDANE[]> {
  return apiFetch<MunicipioDANE[]>(
    `/api/dane/municipios/${encodeURIComponent(codigoDepartamento)}`,
  )
}

/** GET /api/maestros?categoria={categoria} — Cargar opciones de un maestro */
export async function getMaestro(
  categoria: CategoriaMaestro,
): Promise<MaestroOption[]> {
  return apiFetch<MaestroOption[]>(
    `/api/maestros?categoria=${encodeURIComponent(categoria)}`,
  )
}

/* ------------------------------------------------------------------ */
/*  Funciones legacy para compatibilidad con formularios existentes      */
/* ------------------------------------------------------------------ */

/**
 * Convierte el formato legacy plano a DatosBasicos anidado.
 * @deprecated Usar createPredio con DatosBasicos directamente
 */
function legacyToDatosBasicos(
  data: CreatePredioRequestLegacy,
): DatosBasicos {
  return {
    matriculaInmobiliaria: data.matriculaInmobiliaria,
    npn: data.npn ?? null,
    referenciaCatastral: data.referenciaCatastral ?? null,
    departamento: data.departamentoCodigo,
    municipio: data.municipioCodigo,
    tipoPredio: (data.tipoPredio[0] ?? "NPH") as DatosBasicos["tipoPredio"],
    destino: data.destino as DatosBasicos["destino"],
    direccion: data.direccion,
    tipoSuelo: data.tipoSuelo as DatosBasicos["tipoSuelo"],
    servidumbre: data.servidumbre as DatosBasicos["servidumbre"],
    afectacion: data.afectacionAmbiental as DatosBasicos["afectacion"],
    alias: data.alias || null,
    observaciones: data.observaciones ?? null,
    expediente: data.expediente as DatosBasicos["expediente"],
  }
}

/**
 * POST /api/v1/predios — Crea un nuevo borrador con formato legacy.
 * Transforma el formato plano al formato anidado esperado por el backend.
 * @deprecated Usar createPredio con CreatePredioRequest
 */
export async function createPredioLegacy(
  data: CreatePredioRequestLegacy,
): Promise<PredioResponse> {
  const datosBasicos = legacyToDatosBasicos(data)
  return createPredio({ datosBasicos })
}
