import type { Role } from "@/features/auth/lib/types"

/* ------------------------------------------------------------------ */
/*  Respuesta del servidor                                             */
/* ------------------------------------------------------------------ */

/** Respuesta del endpoint GET/POST/PUT /api/users */
export interface UserResponse {
  id: number
  username: string
  email: string
  enabled: boolean
  roles: Role[]
  createdAt: string
  updatedAt: string
  deletedAt: string | null
  deletedBy: string | null
}

/* ------------------------------------------------------------------ */
/*  Peticiones al servidor                                             */
/* ------------------------------------------------------------------ */

/** Body para POST /api/users */
export interface CreateUserRequest {
  username: string
  email: string
  password: string
  roles: Role[]
  enabled: boolean
}

/** Body para PUT /api/users/{id} — todos los campos son opcionales */
export interface UpdateUserRequest {
  username?: string
  email?: string
  password?: string
  roles?: Role[]
  enabled?: boolean
}

/* ------------------------------------------------------------------ */
/*  Historial de auditoria                                             */
/* ------------------------------------------------------------------ */

export interface AuditLogEntry {
  id: number
  action: "CREATE" | "UPDATE" | "DELETE" | "RESTORE"
  entityType: string
  entityId: number
  performedBy: string
  previousValue: string | null
  newValue: string | null
  changesDescription: string | null
  timestamp: string
}

/* ------------------------------------------------------------------ */
/*  Error del servidor                                                 */
/* ------------------------------------------------------------------ */

export interface ApiErrorResponse {
  timestamp: string
  status: number
  error: string
  message: string
  fieldErrors?: Record<string, string>
}
