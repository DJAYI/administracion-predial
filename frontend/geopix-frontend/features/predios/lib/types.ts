/* ------------------------------------------------------------------ */
/*  Tipos comunes de tablas maestras y DANE                             */
/* ------------------------------------------------------------------ */

/** Opción genérica de lista parametrizable (tabla maestra) */
export interface MaestroOption {
  value: string
  label: string
}

/** Departamento DANE */
export interface DepartamentoDANE {
  codigo: string
  nombre: string
}

/** Municipio DANE */
export interface MunicipioDANE {
  codigo: string
  nombre: string
  departamentoCodigo?: string
}

/* ------------------------------------------------------------------ */
/*  Enums y tipos para catálogos hardcoded                              */
/* ------------------------------------------------------------------ */

/** Tipo de predio */
export const TIPO_PREDIO = ["NPH", "PH"] as const
export type TipoPredio = (typeof TIPO_PREDIO)[number]

/** Destino del predio */
export const DESTINO = [
  "HABITACIONAL",
  "COMERCIAL",
  "INDUSTRIAL",
  "AGROPECUARIO",
  "OTRO",
] as const
export type Destino = (typeof DESTINO)[number]

/** Tipo de suelo */
export const TIPO_SUELO = ["URBANO", "RURAL", "EXPANSION_URBANA"] as const
export type TipoSuelo = (typeof TIPO_SUELO)[number]

/** Servidumbre / Afectación */
export const SI_NO = ["SI", "NO"] as const
export type SiNo = (typeof SI_NO)[number]

/** Expediente */
export const EXPEDIENTE = ["DIGITAL", "FISICO", "MIXTO"] as const
export type TipoExpediente = (typeof EXPEDIENTE)[number]

/** Estado del registro */
export const ESTADO_REGISTRO = [
  "BORRADOR",
  "COMPLETO",
  "EN_REVISION",
  "APROBADO",
] as const
export type EstadoRegistro = (typeof ESTADO_REGISTRO)[number]

/** Estado de vigencia */
export const ESTADO_VIGENCIA = ["EN_DEUDA", "PAGADO", "ACOGIDO", "EXENTO"] as const
export type EstadoVigencia = (typeof ESTADO_VIGENCIA)[number]

/** Tipo de documento */
export const TIPO_DOCUMENTO = [
  "CC",
  "CE",
  "NIT",
  "RC",
  "TI",
  "PA",
  "PEP",
  "NIP",
  "NUIP",
] as const
export type TipoDocumento = (typeof TIPO_DOCUMENTO)[number]

/** Tipo de titular */
export const TIPO_TITULAR = [
  "PROPIETARIO",
  "POSEEDOR",
  "TENEDOR",
  "ARRENDATARIO",
  "USUFRUCTUARIO",
] as const
export type TipoTitular = (typeof TIPO_TITULAR)[number]

/* ------------------------------------------------------------------ */
/*  Datos Básicos del Predio                                           */
/* ------------------------------------------------------------------ */

export interface DatosBasicos {
  /** Matrícula Inmobiliaria */
  matriculaInmobiliaria: string
  /** Número Predial Nacional */
  npn: string | null
  /** Referencia Catastral */
  referenciaCatastral: string | null
  /** Código DANE del departamento */
  departamento: string
  /** Nombre del municipio */
  municipio: string
  /** Tipo de predio (NPH o PH) */
  tipoPredio: TipoPredio
  /** Destino del predio */
  destino: Destino
  /** Dirección completa */
  direccion: string
  /** Tipo de suelo */
  tipoSuelo: TipoSuelo
  /** Servidumbre */
  servidumbre: SiNo
  /** Afectación */
  afectacion: SiNo
  /** Alias / nombre alterno */
  alias: string | null
  /** Observaciones */
  observaciones: string | null
  /** Tipo de expediente */
  expediente: TipoExpediente
}

/* ------------------------------------------------------------------ */
/*  Datos Físicos del Predio                                           */
/* ------------------------------------------------------------------ */

export interface DatosFisicos {
  /** Área VUR en hectáreas */
  areaVurHa: number
  /** Área VUR en metros cuadrados */
  areaVurM2: number
  /** Área Catastro en hectáreas */
  areaCatastroHa: number
  /** Área Catastro en metros cuadrados */
  areaCatastroM2: number
  /** Área Escrituras en hectáreas */
  areaEscriturasHa: number
  /** Área Escrituras en metros cuadrados */
  areaEscriturasM2: number
  /** Área Mediación en hectáreas */
  areaMedicacionHa: number
  /** Área Mediación en metros cuadrados */
  areaMedicacionM2: number
}

/* ------------------------------------------------------------------ */
/*  Titularidad del Predio                                             */
/* ------------------------------------------------------------------ */

export interface Titular {
  /** Tipo de documento de identidad */
  tipoDocumento: TipoDocumento
  /** Número de documento */
  numeroDocumento: string
  /** Nombre completo */
  nombre: string
  /** Correo electrónico */
  correo: string | null
  /** Teléfono de contacto */
  telefono: string | null
  /** Dirección de notificaciones */
  direccion: string | null
  /** Porcentaje de participación (0-100) */
  participacion: number
  /** Tipo de titular */
  tipoTitular: TipoTitular
}

export interface Titularidad {
  /** Lista de titulares del predio */
  titulares: Titular[]
}

/* ------------------------------------------------------------------ */
/*  Vigencias del Predio                                              */
/* ------------------------------------------------------------------ */

export interface PredioVigencia {
  /** ID único de la vigencia */
  id: number
  /** ID del predio padre */
  predioId: string
  /** Año de la vigencia */
  vigencia: number
  /** Número de inmueble */
  numeroInmueble: string
  /** Fecha de referencia de pago */
  fechaRefPago: string | null
  /** Referencias de pago */
  referenciaPagos: string | null
  /** Avalúo catastral */
  avaluoCatastral: number
  /** Impuesto predial */
  impuestoPredial: number
  /** Intereses del impuesto predial */
  interesesPredial: number
  /** SAC (Sobretasa Bomberil) */
  sac: number
  /** Intereses del SAC */
  interesesSac: number
  /** Descuentos aplicados */
  descuentos: number
  /** Sobretasa ambiental */
  sobretasaAmbiental: number
  /** Tarifas */
  tarifas: number
  /** Valor total calculado */
  total: number
  /** Estado de la vigencia */
  estado: EstadoVigencia
  /** Fecha de creación */
  createdAt: string
  /** Fecha de última actualización */
  updatedAt: string
}

/** Request para crear una vigencia individual */
export interface CreateVigenciaRequest {
  vigencia: number
  numeroInmueble: string
  fechaRefPago?: string | null
  referenciaPagos?: string | null
  avaluoCatastral: number
  impuestoPredial: number
  interesesPredial?: number
  sac?: number
  interesesSac?: number
  descuentos?: number
  sobretasaAmbiental?: number
  tarifas?: number
}

/** Request para crear vigencias en batch */
export interface BatchVigenciaRequest {
  vigencia: number
  avaluoCatastral: number
  impuestoPredial: number
}

/** Request para actualizar una vigencia */
export type UpdateVigenciaRequest = Partial<CreateVigenciaRequest>

/* ------------------------------------------------------------------ */
/*  Peticiones y respuestas de la API                                  */
/* ------------------------------------------------------------------ */

/** Body para POST /api/v1/predios — Crear borrador (Paso 1) */
export interface CreatePredioRequest {
  datosBasicos: DatosBasicos
}

/** Request legacy para POST /api/predios (formato plano) */
export interface CreatePredioRequestLegacy {
  matriculaInmobiliaria: string
  npn?: string
  referenciaCatastral?: string
  referenciaNpnGeo?: string
  departamentoCodigo: string
  municipioCodigo: string
  tipoPredio: string[]
  servidumbre: string
  tipoServidumbre?: string
  tipoSuelo: string
  destino: string
  tipoTitularidad: string
  expediente: string
  direccion: string
  alias: string
  afectacionAmbiental: string
  observaciones?: string
}

/** Body para PUT /api/v1/predios/{id}?paso=N — Actualizar paso N */
export interface UpdatePredioRequest {
  datosBasicos?: Partial<DatosBasicos>
  datosFisicos?: DatosFisicos
  titularidad?: Titularidad
}

/** Respuesta completa del servidor para un predio */
export interface PredioResponse {
  /** ID único del predio (UUID) */
  id: string
  /** Estado del registro */
  estadoRegistro: EstadoRegistro
  /** Paso actual en el wizard */
  pasoActual: number
  /** Datos básicos */
  datosBasicos: DatosBasicos
  /** Datos físicos */
  datosFisicos: DatosFisicos | null
  /** Titularidad */
  titularidad: Titularidad | null
  /** Lista de vigencias económicas */
  vigencias: PredioVigencia[]
  /** Timestamps */
  createdAt: string
  updatedAt: string
}

/** Respuesta de verificación de matrícula */
export interface CheckMatriculaResponse {
  /** Indica si la matrícula ya existe */
  exists: boolean
}

/** Respuesta de error genérica del backend */
export interface ApiErrorResponse {
  message: string
  status?: number
  fieldErrors?: Record<string, string>
}

/* ------------------------------------------------------------------ */
/*  Tipos legacy (para compatibilidad con formularios existentes)         */
/* ------------------------------------------------------------------ */

/** Tipo de servidumbre */
export type TipoServidumbre =
  | "PASO_PEATONAL_VEHICULAR"
  | "TUBERIAS_ACUEDUCTO_ALCANTARILLADO"
  | "LINEAS_ELECTRICAS_TELECOMUNICACIONES"
  | "DRENAJES_CANALES_AGUAS"

/** Tipo de titularidad (legacy) */
export type TipoTitularidadLegacy =
  | "TITULAR"
  | "INFORMALIDAD"
  | "TENENCIA_POR_DEFINIR"
  | "PROMESA"
  | "FIDEICOMISO"

/** Tipo de afectación ambiental (legacy) */
export type AfectacionAmbiental = "SI" | "NO"

/** Form data para el formulario de datos básicos (legacy) */
export interface DatosBasicosFormData {
  /** Matrícula Inmobiliaria */
  matriculaInmobiliaria: string
  /** Matrícula de confirmación (solo en formulario) */
  matriculaConfirmacion: string
  /** Número Predial Nacional */
  npn: string
  /** Referencia Catastral (15 dígitos) */
  referenciaCatastral: string
  /** Referencia NPN GEO */
  referenciaNpnGeo: string
  /** Código DANE del departamento (legacy) */
  departamentoCodigo: string
  /** Nombre del departamento (para display) */
  departamentoNombre: string
  /** Código DANE del municipio (legacy) */
  municipioCodigo: string
  /** Nombre del municipio (para display) */
  municipioNombre: string
  /** Código del departamento (nuevo spec) */
  departamento: string
  /** Nombre del municipio (nuevo spec) */
  municipio: string
  /** Condiciones del predio (multi-selección legacy) */
  tipoPredio: string[]
  /** Servidumbre */
  servidumbre: string
  /** Tipo de servidumbre (condicional) */
  tipoServidumbre: string
  /** Tipo de suelo */
  tipoSuelo: string
  /** Destino del predio */
  destino: string
  /** Tipo de titularidad (legacy) */
  tipoTitularidad: string
  /** Expediente */
  expediente: string
  /** Dirección completa */
  direccion: string
  /** Alias / nombre alterno */
  alias: string
  /** Afectación ambiental (legacy) */
  afectacionAmbiental: string
  /** Afectación (nuevo spec) */
  afectacion: SiNo
  /** Observaciones (opcional) */
  observaciones: string
}

/** Errores de validación de formulario */
export type DatosBasicosFormErrors = Partial<
  Record<keyof DatosBasicosFormData | "general", string>
>

/** Estado inicial del formulario de datos básicos */
export const INITIAL_DATOS_BASICOS: DatosBasicosFormData = {
  matriculaInmobiliaria: "",
  matriculaConfirmacion: "",
  npn: "",
  referenciaCatastral: "",
  referenciaNpnGeo: "",
  departamentoCodigo: "",
  departamentoNombre: "",
  municipioCodigo: "",
  municipioNombre: "",
  departamento: "",
  municipio: "",
  tipoPredio: [],
  servidumbre: "",
  tipoServidumbre: "",
  tipoSuelo: "",
  destino: "",
  tipoTitularidad: "",
  expediente: "",
  direccion: "",
  alias: "",
  afectacionAmbiental: "",
  afectacion: "NO",
  observaciones: "",
}
