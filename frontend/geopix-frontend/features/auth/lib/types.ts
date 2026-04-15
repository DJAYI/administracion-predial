/** Roles del sistema GEOPIX */
export type Role = "ADMIN" | "CONTADOR" | "EJECUTOR_INTEGRAL";

/** Usuario autenticado (devuelto por /api/auth/me y /api/auth/login) */
export interface AuthUser {
  username: string;
  email: string;
  roles: Role[];
}

/** Respuesta del endpoint POST /api/auth/login */
export interface AuthResponse extends AuthUser {
  message: string;
}

/** Credenciales para login */
export interface LoginCredentials {
  username: string;
  password: string;
}
