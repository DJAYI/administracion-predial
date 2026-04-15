/* ------------------------------------------------------------------ */
/*  Catálogos hardcoded para el módulo de Predios                        */
/* ------------------------------------------------------------------ */

/* ------------------------------------------------------------------ */
/*  Tipo de Predio                                                     */
/* ------------------------------------------------------------------ */

export const TIPO_PREDIO = ["NPH", "PH"] as const

export const TIPO_PREDIO_LABELS: Record<(typeof TIPO_PREDIO)[number], string> = {
  NPH: "No PH (Independiente)",
  PH: "Propiedad Horizontal",
}

/* ------------------------------------------------------------------ */
/*  Destino del Predio                                                 */
/* ------------------------------------------------------------------ */

export const DESTINO = [
  "HABITACIONAL",
  "COMERCIAL",
  "INDUSTRIAL",
  "AGROPECUARIO",
  "OTRO",
] as const

export const DESTINO_LABELS: Record<(typeof DESTINO)[number], string> = {
  HABITACIONAL: "Habitacional",
  COMERCIAL: "Comercial",
  INDUSTRIAL: "Industrial",
  AGROPECUARIO: "Agropecuario",
  OTRO: "Otro",
}

/* ------------------------------------------------------------------ */
/*  Tipo de Suelo                                                      */
/* ------------------------------------------------------------------ */

export const TIPO_SUELO = ["URBANO", "RURAL", "EXPANSION_URBANA"] as const

export const TIPO_SUELO_LABELS: Record<(typeof TIPO_SUELO)[number], string> = {
  URBANO: "Urbano",
  RURAL: "Rural",
  EXPANSION_URBANA: "Expansión Urbana",
}

/* ------------------------------------------------------------------ */
/*  Servidumbre / Afectación                                           */
/* ------------------------------------------------------------------ */

export const SI_NO = ["SI", "NO"] as const

export const SI_NO_LABELS: Record<(typeof SI_NO)[number], string> = {
  SI: "Sí",
  NO: "No",
}

/* ------------------------------------------------------------------ */
/*  Tipo de Expediente                                                 */
/* ------------------------------------------------------------------ */

export const EXPEDIENTE = ["DIGITAL", "FISICO", "MIXTO"] as const

export const EXPEDIENTE_LABELS: Record<(typeof EXPEDIENTE)[number], string> = {
  DIGITAL: "Digital",
  FISICO: "Físico",
  MIXTO: "Mixto (Digital y Físico)",
}

/* ------------------------------------------------------------------ */
/*  Estado del Registro                                                */
/* ------------------------------------------------------------------ */

export const ESTADO_REGISTRO = [
  "BORRADOR",
  "COMPLETO",
  "EN_REVISION",
  "APROBADO",
] as const

export const ESTADO_REGISTRO_LABELS: Record<
  (typeof ESTADO_REGISTRO)[number],
  string
> = {
  BORRADOR: "Borrador",
  COMPLETO: "Completo",
  EN_REVISION: "En Revisión",
  APROBADO: "Aprobado",
}

export const ESTADO_REGISTRO_COLORS: Record<
  (typeof ESTADO_REGISTRO)[number],
  string
> = {
  BORRADOR: "bg-slate-100 text-slate-700 dark:bg-slate-800 dark:text-slate-300",
  COMPLETO: "bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300",
  EN_REVISION: "bg-amber-100 text-amber-700 dark:bg-amber-900 dark:text-amber-300",
  APROBADO: "bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300",
}

/* ------------------------------------------------------------------ */
/*  Estado de Vigencia                                                 */
/* ------------------------------------------------------------------ */

export const ESTADO_VIGENCIA = ["EN_DEUDA", "PAGADO", "ACOGIDO", "EXENTO"] as const

export const ESTADO_VIGENCIA_LABELS: Record<
  (typeof ESTADO_VIGENCIA)[number],
  string
> = {
  EN_DEUDA: "En Deuda",
  PAGADO: "Pagado",
  ACOGIDO: "Acogido",
  EXENTO: "Exento",
}

export const ESTADO_VIGENCIA_COLORS: Record<
  (typeof ESTADO_VIGENCIA)[number],
  string
> = {
  EN_DEUDA: "bg-red-100 text-red-700 dark:bg-red-900 dark:text-red-300",
  PAGADO: "bg-green-100 text-green-700 dark:bg-green-900 dark:text-green-300",
  ACOGIDO: "bg-blue-100 text-blue-700 dark:bg-blue-900 dark:text-blue-300",
  EXENTO: "bg-purple-100 text-purple-700 dark:bg-purple-900 dark:text-purple-300",
}

/* ------------------------------------------------------------------ */
/*  Tipo de Documento                                                 */
/* ------------------------------------------------------------------ */

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

export const TIPO_DOCUMENTO_LABELS: Record<
  (typeof TIPO_DOCUMENTO)[number],
  string
> = {
  CC: "Cédula de Ciudadanía",
  CE: "Cédula de Extranjería",
  NIT: "NIT (Número de Identificación Tributaria)",
  RC: "Registro Civil",
  TI: "Tarjeta de Identidad",
  PA: "Pasaporte",
  PEP: "Permiso Especial de Permanencia",
  NIP: "Número de Identificación Personal",
  NUIP: "NUIP (Número Único de Identificación Personal)",
}

/* ------------------------------------------------------------------ */
/*  Tipo de Titular                                                    */
/* ------------------------------------------------------------------ */

export const TIPO_TITULAR = [
  "PROPIETARIO",
  "POSEEDOR",
  "TENEDOR",
  "ARRENDATARIO",
  "USUFRUCTUARIO",
] as const

export const TIPO_TITULAR_LABELS: Record<(typeof TIPO_TITULAR)[number], string> = {
  PROPIETARIO: "Propietario",
  POSEEDOR: "Poseedor",
  TENEDOR: "Tenedor",
  ARRENDATARIO: "Arrendatario",
  USUFRUCTUARIO: "Usufructuario",
}

/* ------------------------------------------------------------------ */
/*  Exports legacy (para compatibilidad con componentes existentes)       */
/* ------------------------------------------------------------------ */

/** Alias legacy para SI_NO en formato de opciones */
export const OPCIONES_SERVIDUMBRE = SI_NO.map((v) => ({
  value: v,
  label: SI_NO_LABELS[v],
}))

/** Alias legacy para SI_NO en formato de opciones */
export const OPCIONES_AFECTACION = SI_NO.map((v) => ({
  value: v,
  label: SI_NO_LABELS[v],
}))

/* ------------------------------------------------------------------ */
/*  Helpers para selects                                                */
/* ------------------------------------------------------------------ */

/** Opciones para selects de catálogo */
export const tipoPredioOptions = TIPO_PREDIO.map((v) => ({
  value: v,
  label: TIPO_PREDIO_LABELS[v],
}))

export const destinoOptions = DESTINO.map((v) => ({
  value: v,
  label: DESTINO_LABELS[v],
}))

export const tipoSueloOptions = TIPO_SUELO.map((v) => ({
  value: v,
  label: TIPO_SUELO_LABELS[v],
}))

export const siNoOptions = SI_NO.map((v) => ({
  value: v,
  label: SI_NO_LABELS[v],
}))

export const expedienteOptions = EXPEDIENTE.map((v) => ({
  value: v,
  label: EXPEDIENTE_LABELS[v],
}))

export const tipoDocumentoOptions = TIPO_DOCUMENTO.map((v) => ({
  value: v,
  label: TIPO_DOCUMENTO_LABELS[v],
}))

export const tipoTitularOptions = TIPO_TITULAR.map((v) => ({
  value: v,
  label: TIPO_TITULAR_LABELS[v],
}))

export const estadoVigenciaOptions = ESTADO_VIGENCIA.map((v) => ({
  value: v,
  label: ESTADO_VIGENCIA_LABELS[v],
}))
