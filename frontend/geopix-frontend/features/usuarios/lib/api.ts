import { apiFetch } from "@/features/auth/lib/api"
import type {
  UserResponse,
  CreateUserRequest,
  UpdateUserRequest,
  AuditLogEntry,
} from "@/features/usuarios/lib/types"

/* ------------------------------------------------------------------ */
/*  Listar usuarios                                                    */
/* ------------------------------------------------------------------ */

/** GET /api/users — lista usuarios activos */
export async function getUsers(): Promise<UserResponse[]> {
  return apiFetch<UserResponse[]>("/api/users")
}

/** GET /api/users?includeDeleted=true — lista todos los usuarios */
export async function getAllUsers(): Promise<UserResponse[]> {
  return apiFetch<UserResponse[]>("/api/users?includeDeleted=true")
}

/* ------------------------------------------------------------------ */
/*  CRUD de usuarios                                                   */
/* ------------------------------------------------------------------ */

/** GET /api/users/{id} — detalle de un usuario */
export async function getUserById(id: number): Promise<UserResponse> {
  return apiFetch<UserResponse>(`/api/users/${id}`)
}

/** POST /api/users — crear usuario */
export async function createUser(
  data: CreateUserRequest,
): Promise<UserResponse> {
  return apiFetch<UserResponse>("/api/users", {
    method: "POST",
    body: JSON.stringify(data),
  })
}

/** PUT /api/users/{id} — actualizar usuario */
export async function updateUser(
  id: number,
  data: UpdateUserRequest,
): Promise<UserResponse> {
  return apiFetch<UserResponse>(`/api/users/${id}`, {
    method: "PUT",
    body: JSON.stringify(data),
  })
}

/** DELETE /api/users/{id} — borrado logico */
export async function deleteUser(
  id: number,
): Promise<{ message: string }> {
  return apiFetch<{ message: string }>(`/api/users/${id}`, {
    method: "DELETE",
  })
}

/* ------------------------------------------------------------------ */
/*  Restaurar y auditoria                                              */
/* ------------------------------------------------------------------ */

/** POST /api/users/{id}/restore — restaurar usuario eliminado */
export async function restoreUser(id: number): Promise<UserResponse> {
  return apiFetch<UserResponse>(`/api/users/${id}/restore`, {
    method: "POST",
  })
}

/** GET /api/users/{id}/history — historial de auditoria */
export async function getUserHistory(
  id: number,
): Promise<AuditLogEntry[]> {
  return apiFetch<AuditLogEntry[]>(`/api/users/${id}/history`)
}
