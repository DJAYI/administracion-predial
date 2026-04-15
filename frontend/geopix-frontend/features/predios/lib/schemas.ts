import { z } from "zod/v4"
import {
  TIPO_PREDIO,
  DESTINO,
  TIPO_SUELO,
  SI_NO,
  EXPEDIENTE,
  ESTADO_REGISTRO,
  ESTADO_VIGENCIA,
  TIPO_DOCUMENTO,
  TIPO_TITULAR,
} from "./constants"

/* ------------------------------------------------------------------ */
/*  Datos Básicos                                                      */
/* ------------------------------------------------------------------ */

/**
 * Formato de matrícula inmobiliaria colombiana:
 * 01-01-0001-0001-000 (departamento-municipio-corregimiento-seccion-predio)
 * - 2 dígitos departamento
 * - 2 dígitos municipio
 * - 4 dígitos corregimiento
 * - 4 dígitos sección
 * - 3 dígitos número de predio
 */
const MATRICULA_PATTERN = /^\d{2}-\d{2}-\d{4}-\d{4}-\d{3}$/

/**
 * Formato NPN (Número Predial Nacional):
 * 15 dígitos numéricos
 */
const NPN_PATTERN = /^\d{15}$/

/**
 * Formato Referencia Catastral:
 * 15 dígitos numéricos
 */
const REFERENCIA_CATASTRAL_PATTERN = /^\d{15}$/

export const datosBasicosSchema = z.object({
  matriculaInmobiliaria: z
    .string()
    .min(1, "La matrícula inmobiliaria es requerida")
    .regex(MATRICULA_PATTERN, {
      message: "Formato inválido. Use: 01-01-0001-0001-000",
    }),

  npn: z
    .string()
    .regex(NPN_PATTERN, {
      message: "El NPN debe tener exactamente 15 dígitos numéricos",
    })
    .nullable()
    .optional(),

  referenciaCatastral: z
    .string()
    .regex(REFERENCIA_CATASTRAL_PATTERN, {
      message: "La referencia catastral debe tener exactamente 15 dígitos numéricos",
    })
    .nullable()
    .optional(),

  departamento: z
    .string()
    .min(1, "El departamento es requerido"),

  municipio: z
    .string()
    .min(1, "El municipio es requerido"),

  tipoPredio: z.enum(TIPO_PREDIO, {
    error: "Seleccione un tipo de predio válido",
  }),

  destino: z.enum(DESTINO, {
    error: "Seleccione un destino válido",
  }),

  direccion: z
    .string()
    .min(1, "La dirección es requerida")
    .max(500, "La dirección no puede exceder 500 caracteres"),

  tipoSuelo: z.enum(TIPO_SUELO, {
    error: "Seleccione un tipo de suelo válido",
  }),

  servidumbre: z.enum(SI_NO, {
    error: "Seleccione si tiene servidumbre",
  }),

  afectacion: z.enum(SI_NO, {
    error: "Seleccione si tiene afectación",
  }),

  alias: z
    .string()
    .max(200, "El alias no puede exceder 200 caracteres")
    .nullable()
    .optional(),

  observaciones: z
    .string()
    .max(2000, "Las observaciones no pueden exceder 2000 caracteres")
    .nullable()
    .optional(),

  expediente: z.enum(EXPEDIENTE, {
    error: "Seleccione un tipo de expediente válido",
  }),
})

export type DatosBasicosInput = z.infer<typeof datosBasicosSchema>

/* ------------------------------------------------------------------ */
/*  Datos Físicos                                                      */
/* ------------------------------------------------------------------ */

export const datosFisicosSchema = z.object({
  areaVurHa: z.number().min(0, "El área no puede ser negativa"),
  areaVurM2: z.number().min(0, "El área no puede ser negativa"),
  areaCatastroHa: z.number().min(0, "El área no puede ser negativa"),
  areaCatastroM2: z.number().min(0, "El área no puede ser negativa"),
  areaEscriturasHa: z.number().min(0, "El área no puede ser negativa"),
  areaEscriturasM2: z.number().min(0, "El área no puede ser negativa"),
  areaMedicacionHa: z.number().min(0, "El área no puede ser negativa"),
  areaMedicacionM2: z.number().min(0, "El área no puede ser negativa"),
})

export type DatosFisicosInput = z.infer<typeof datosFisicosSchema>

/* ------------------------------------------------------------------ */
/*  Titularidad                                                        */
/* ------------------------------------------------------------------ */

export const titularSchema = z.object({
  tipoDocumento: z.enum(TIPO_DOCUMENTO, {
    error: "Seleccione un tipo de documento válido",
  }),

  numeroDocumento: z
    .string()
    .min(5, "El número de documento debe tener al menos 5 caracteres")
    .max(20, "El número de documento no puede exceder 20 caracteres"),

  nombre: z
    .string()
    .min(3, "El nombre debe tener al menos 3 caracteres")
    .max(200, "El nombre no puede exceder 200 caracteres"),

  correo: z
    .string()
    .email("Ingrese un correo electrónico válido")
    .max(255, "El correo no puede exceder 255 caracteres")
    .nullable()
    .optional(),

  telefono: z
    .string()
    .max(20, "El teléfono no puede exceder 20 caracteres")
    .nullable()
    .optional(),

  direccion: z
    .string()
    .max(500, "La dirección no puede exceder 500 caracteres")
    .nullable()
    .optional(),

  participacion: z
    .number()
    .min(0, "La participación no puede ser menor a 0")
    .max(100, "La participación no puede exceder 100"),

  tipoTitular: z.enum(TIPO_TITULAR, {
    error: "Seleccione un tipo de titular válido",
  }),
})

export type TitularInput = z.infer<typeof titularSchema>

export const titularidadSchema = z.object({
  titulares: z
    .array(titularSchema)
    .min(1, "Debe agregar al menos un titular"),
})

export type TitularidadInput = z.infer<typeof titularidadSchema>

/* ------------------------------------------------------------------ */
/*  Vigencias                                                          */
/* ------------------------------------------------------------------ */

export const createVigenciaSchema = z.object({
  vigencia: z
    .number()
    .int("La vigencia debe ser un año válido")
    .min(1900, "Año inválido")
    .max(2100, "Año inválido"),

  numeroInmueble: z
    .string()
    .max(50, "El número de inmueble no puede exceder 50 caracteres"),

  fechaRefPago: z
    .string()
    .regex(/^\d{4}-\d{2}-\d{2}$/, "Formato de fecha inválido (YYYY-MM-DD)")
    .nullable()
    .optional(),

  referenciaPagos: z
    .string()
    .max(100, "La referencia de pagos no puede exceder 100 caracteres")
    .nullable()
    .optional(),

  avaluoCatastral: z
    .number()
    .min(0, "El avalúo no puede ser negativo"),

  impuestoPredial: z
    .number()
    .min(0, "El impuesto no puede ser negativo"),

  interesesPredial: z
    .number()
    .min(0, "Los intereses no pueden ser negativos")
    .optional(),

  sac: z
    .number()
    .min(0, "El SAC no puede ser negativo")
    .optional(),

  interesesSac: z
    .number()
    .min(0, "Los intereses del SAC no pueden ser negativos")
    .optional(),

  descuentos: z
    .number()
    .min(0, "Los descuentos no pueden ser negativos")
    .optional(),

  sobretasaAmbiental: z
    .number()
    .min(0, "La sobretasa no puede ser negativa")
    .optional(),

  tarifas: z
    .number()
    .min(0, "Las tarifas no pueden ser negativas")
    .optional(),
})

export type CreateVigenciaInput = z.infer<typeof createVigenciaSchema>

export const batchVigenciaSchema = z.object({
  vigencia: z
    .number()
    .int("La vigencia debe ser un año válido")
    .min(1900, "Año inválido")
    .max(2100, "Año inválido"),

  avaluoCatastral: z
    .number()
    .min(0, "El avalúo no puede ser negativo"),

  impuestoPredial: z
    .number()
    .min(0, "El impuesto no puede ser negativo"),
})

export type BatchVigenciaInput = z.infer<typeof batchVigenciaSchema>

export const updateVigenciaSchema = createVigenciaSchema.partial()

export type UpdateVigenciaInput = z.infer<typeof updateVigenciaSchema>

/* ------------------------------------------------------------------ */
/*  Requests completos para cada paso del wizard                        */
/* ------------------------------------------------------------------ */

/** Request para crear un nuevo predio (Paso 1) */
export const createPredioSchema = z.object({
  datosBasicos: datosBasicosSchema,
})

export type CreatePredioInput = z.infer<typeof createPredioSchema>

/** Request para actualizar el Paso 2 (Datos Físicos) */
export const updatePredioPaso2Schema = z.object({
  datosFisicos: datosFisicosSchema,
})

export type UpdatePredioPaso2Input = z.infer<typeof updatePredioPaso2Schema>

/** Request para actualizar el Paso 3 (Titularidad) */
export const updatePredioPaso3Schema = z.object({
  titularidad: titularidadSchema,
})

export type UpdatePredioPaso3Input = z.infer<typeof updatePredioPaso3Schema>

/** Request para verificar matrícula */
export const checkMatriculaSchema = z.object({
  matricula: z.string().regex(MATRICULA_PATTERN, {
    message: "Formato inválido. Use: 01-01-0001-0001-000",
  }),
})

export type CheckMatriculaInput = z.infer<typeof checkMatriculaSchema>
