// features/predios — barrel export

/* ------------------------------------------------------------------ */
/*  Componentes                                                        */
/* ------------------------------------------------------------------ */

export { CreacionWizard } from "./components/creacion-wizard"
export { DatosBasicosForm } from "./components/datos-basicos-form"
export { DatosFisicosForm } from "./components/datos-fisicos-form"
export { TitularidadForm } from "./components/titularidad-form"
export { InfoEconomicaForm } from "./components/info-economica-form"
export { steps } from "./lib/steps"

/* ------------------------------------------------------------------ */
/*  Tipos                                                              */
/* ------------------------------------------------------------------ */

// Enums y tipos base
export type {
  TipoPredio,
  Destino,
  TipoSuelo,
  SiNo,
  TipoExpediente,
  EstadoRegistro,
  EstadoVigencia,
  TipoDocumento,
  TipoTitular,
} from "./lib/types"

// Datos básicos
export type {
  DatosBasicos,
  DatosBasicosFormData,
  DatosBasicosFormErrors,
} from "./lib/types"
export { INITIAL_DATOS_BASICOS } from "./lib/types"

// Datos físicos
export type { DatosFisicos } from "./lib/types"

// Titularidad
export type { Titular, Titularidad } from "./lib/types"

// Vigencias
export type {
  PredioVigencia,
  CreateVigenciaRequest,
  BatchVigenciaRequest,
  UpdateVigenciaRequest,
} from "./lib/types"

// Requests y Responses
export type {
  CreatePredioRequest,
  CreatePredioRequestLegacy,
  UpdatePredioRequest,
  PredioResponse,
  CheckMatriculaResponse,
  ApiErrorResponse,
} from "./lib/types"

// DANE
export type {
  MaestroOption,
  DepartamentoDANE,
  MunicipioDANE,
} from "./lib/types"

/* ------------------------------------------------------------------ */
/*  Constantes (catálogos)                                             */
/* ------------------------------------------------------------------ */

export {
  TIPO_PREDIO,
  TIPO_PREDIO_LABELS,
  DESTINO,
  DESTINO_LABELS,
  TIPO_SUELO,
  TIPO_SUELO_LABELS,
  SI_NO,
  SI_NO_LABELS,
  EXPEDIENTE,
  EXPEDIENTE_LABELS,
  ESTADO_REGISTRO,
  ESTADO_REGISTRO_LABELS,
  ESTADO_REGISTRO_COLORS,
  ESTADO_VIGENCIA,
  ESTADO_VIGENCIA_LABELS,
  ESTADO_VIGENCIA_COLORS,
  TIPO_DOCUMENTO,
  TIPO_DOCUMENTO_LABELS,
  TIPO_TITULAR,
  TIPO_TITULAR_LABELS,
} from "./lib/constants"

export {
  tipoPredioOptions,
  destinoOptions,
  tipoSueloOptions,
  siNoOptions,
  expedienteOptions,
  tipoDocumentoOptions,
  tipoTitularOptions,
  estadoVigenciaOptions,
  // Legacy
  OPCIONES_SERVIDUMBRE,
  OPCIONES_AFECTACION,
} from "./lib/constants"

/* ------------------------------------------------------------------ */
/*  Esquemas Zod                                                       */
/* ------------------------------------------------------------------ */

export {
  // Datos básicos
  datosBasicosSchema,
  type DatosBasicosInput,
  // Datos físicos
  datosFisicosSchema,
  type DatosFisicosInput,
  // Titularidad
  titularSchema,
  type TitularInput,
  titularidadSchema,
  type TitularidadInput,
  // Vigencias
  createVigenciaSchema,
  type CreateVigenciaInput,
  batchVigenciaSchema,
  type BatchVigenciaInput,
  updateVigenciaSchema,
  type UpdateVigenciaInput,
  // Requests completos
  createPredioSchema,
  type CreatePredioInput,
  updatePredioPaso2Schema,
  type UpdatePredioPaso2Input,
  updatePredioPaso3Schema,
  type UpdatePredioPaso3Input,
  checkMatriculaSchema,
  type CheckMatriculaInput,
} from "./lib/schemas"

/* ------------------------------------------------------------------ */
/*  API                                                                */
/* ------------------------------------------------------------------ */

export {
  // Predios
  createPredio,
  createPredioLegacy,
  getPredioById,
  getPredio,
  updatePredioPaso,
  updatePredioDatosBasicos,
  updatePredioDatosFisicos,
  updatePredioTitularidad,
  checkMatricula,
  completarPredio,
  // Vigencias
  getVigencias,
  createVigencia,
  createVigenciasBatch,
  updateVigencia,
  deleteVigencia,
  // Legacy DANE
  getDepartamentos,
  getMunicipios,
  getMaestro,
  type CategoriaMaestro,
} from "./lib/api"

/* ------------------------------------------------------------------ */
/*  Catálogos DANE                                                     */
/* ------------------------------------------------------------------ */

export {
  fetchDepartamentos,
  fetchMunicipios,
  getDepartamentosCached,
  getMunicipiosCached,
  clearCatalogosCache,
} from "./lib/catalogos"
