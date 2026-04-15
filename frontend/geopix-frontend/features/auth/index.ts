// features/auth — barrel export
export { AuthProvider, useAuth } from "./context"
export { AuthGuard } from "./components/auth-guard"
export { LoginForm } from "./components/login-form"
export { canAccessRoute, filterNavByRoles, getRoleLabel } from "./lib/rbac"
export type { NavItem } from "./lib/rbac"
export type { AuthUser, Role, AuthResponse, LoginCredentials } from "./lib/types"
