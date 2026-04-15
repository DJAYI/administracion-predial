import type {
  AuthUser,
  AuthResponse,
  LoginCredentials,
} from "@/features/auth/lib/types";

// const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080"
const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost";

/**
 * Error lanzado cuando el usuario no tiene sesion valida
 * y el refresh tambien fallo.
 */
export class UnauthenticatedError extends Error {
  constructor() {
    super("No autenticado");
    this.name = "UnauthenticatedError";
  }
}

/* ------------------------------------------------------------------ */
/*  Refresh mutex — evita multiples refreshes simultaneos              */
/* ------------------------------------------------------------------ */

let refreshPromise: Promise<AuthUser> | null = null;

async function refreshToken(): Promise<AuthUser> {
  if (refreshPromise) return refreshPromise;

  refreshPromise = (async () => {
    const res = await fetch(`${API_BASE}/api/auth/refresh`, {
      method: "POST",
      credentials: "include",
    });
    if (!res.ok) throw new UnauthenticatedError();
    const data: AuthResponse = await res.json();
    return { username: data.username, email: data.email, roles: data.roles };
  })();

  try {
    return await refreshPromise;
  } finally {
    refreshPromise = null;
  }
}

/* ------------------------------------------------------------------ */
/*  Fetch wrapper con credentials:include y retry tras refresh         */
/* ------------------------------------------------------------------ */

type FetchOptions = RequestInit & {
  /** Si es true, no intenta refresh en 401 (evita loop) */
  _skipRefresh?: boolean;
};

async function apiFetch<T>(
  path: string,
  options: FetchOptions = {},
): Promise<T> {
  const { _skipRefresh, ...init } = options;
  const url = `${API_BASE}${path}`;

  const res = await fetch(url, {
    ...init,
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      ...init.headers,
    },
  });

  if (res.ok) return res.json() as Promise<T>;

  const body = await res.text().catch(() => "");
  console.error(`[apiFetch] ${res.status} ${path} → ${body || res.statusText}`);

  // 401/403 → intentar refresh una sola vez
  if ((res.status === 401 || res.status === 403) && !_skipRefresh) {
    try {
      await refreshToken();
      return apiFetch<T>(path, { ...options, _skipRefresh: true });
    } catch {
      throw new UnauthenticatedError();
    }
  }

  throw new Error(`API error ${res.status}: ${body || res.statusText}`);
}

/* ------------------------------------------------------------------ */
/*  Funciones de autenticacion                                         */
/* ------------------------------------------------------------------ */

/** POST /api/auth/login */
export async function login(
  credentials: LoginCredentials,
): Promise<AuthResponse> {
  return apiFetch<AuthResponse>("/api/auth/login", {
    method: "POST",
    body: JSON.stringify(credentials),
    _skipRefresh: true, // No hay token aun, no intentar refresh
    credentials: "include",
  });
}

/** GET /api/auth/me */
export async function getMe(): Promise<AuthUser> {
  return apiFetch<AuthUser>("/api/auth/me", {
    method: "GET",
    credentials: "include",
  });
}

/** POST /api/auth/logout — siempre 200 */
export async function logout(): Promise<void> {
  await fetch(`${API_BASE}/api/auth/logout`, {
    method: "POST",
    credentials: "include",
  });
}

export { apiFetch };
