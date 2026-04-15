import type { Role } from "@/features/auth/lib/types";

/* ------------------------------------------------------------------ */
/*  Mapeo de rutas a roles autorizados                                 */
/* ------------------------------------------------------------------ */

/**
 * Define que roles pueden acceder a cada ruta del dashboard.
 * Si una ruta no aparece aqui, se considera accesible por todos los
 * usuarios autenticados (ej. /dashboard = Inicio).
 */
const ROUTE_ROLES: Record<string, Role[]> = {
  "/dashboard/configuracion": ["ADMIN"],
  "/dashboard/reporte-economico": ["CONTADOR", "ADMIN"],
  "/dashboard/georeferenciacion": ["EJECUTOR_INTEGRAL", "ADMIN"],
  "/dashboard/creacion-de-predios": ["EJECUTOR_INTEGRAL", "ADMIN"],
};

/**
 * Verifica si un usuario con los roles dados puede acceder a una ruta.
 * Rutas no mapeadas son accesibles por cualquier rol.
 */
export function canAccessRoute(pathname: string, userRoles: Role[]): boolean {
  // Buscar la regla mas especifica que coincida
  const matchedRoute = Object.keys(ROUTE_ROLES).find((route) =>
    pathname.startsWith(route),
  );

  // Si no hay regla, la ruta es publica para autenticados
  if (!matchedRoute) return true;

  const allowedRoles = ROUTE_ROLES[matchedRoute];
  return userRoles.some((role) => allowedRoles.includes(role));
}

/* ------------------------------------------------------------------ */
/*  Navegacion filtrada por rol                                        */
/* ------------------------------------------------------------------ */

export interface NavItem {
  title: string;
  url: string;
  icon: React.ComponentType<{ className?: string }>;
  /** Roles que pueden ver este item. Si no se define, todos lo ven. */
  roles?: Role[];
}

/**
 * Filtra items de navegacion segun los roles del usuario.
 * Items sin `roles` definido son visibles para todos.
 */
export function filterNavByRoles(
  items: NavItem[],
  userRoles: Role[],
): NavItem[] {
  return items.filter((item) => {
    if (!item.roles) return true;
    return userRoles.some((role) => item.roles!.includes(role));
  });
}

/**
 * Obtiene la primera ruta accesible para un usuario.
 * Se usa para redirigir cuando intenta acceder a una ruta no autorizada.
 */
export function getDefaultRoute(_userRoles: Role[]): string {
  return "/dashboard";
}

/**
 * Obtiene un label amigable para un rol.
 */
export function getRoleLabel(role: Role): string {
  const labels: Record<Role, string> = {
    ADMIN: "Administrador",
    CONTADOR: "Contador",
    EJECUTOR_INTEGRAL: "Ejecutor Integral",
  };
  return labels[role] ?? role;
}
