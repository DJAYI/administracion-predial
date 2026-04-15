# GEOPIX Frontend — Agent Instructions

> Context file for AI coding agents operating in this repository. Read before writing code.

---

## 1. Project Overview

**GEOPIX** is a land management and georeferencing platform with RBAC:

| Role | Access |
|------|--------|
| `ADMINISTRADOR` | Full system |
| `CONTADOR` | Dashboard, Economic Report |
| `EJECUTOR_INTEGRAL` | Dashboard, Property Creation, Georeferencing |

---

## 2. Tech Stack

- **Framework**: Next.js 16 (App Router, RSC, View Transitions)
- **Language**: TypeScript (strict mode)
- **UI**: shadcn/ui (`radix-nova` style), Tailwind CSS v4 (oklch), Lucide React
- **Tables**: @tanstack/react-table v8
- **Validation**: Zod v4 (`zod/v4` import)
- **Toasts**: Sileo via `notify` from `@/shared/lib/toast`
- **Theme**: next-themes (light/dark, `defaultTheme="light"`)
- **Fonts**: Plus Jakarta Sans (UI), Geist Mono (code)

---

## 3. Commands

```bash
npm run dev      # Start dev server
npm run build    # Production build
npm run lint     # ESLint check
npm start        # Start production server
```

---

## 4. Architecture

```
app/           # Routes (thin wrappers only)
features/      # Business modules (auth, dashboard, usuarios, predios, etc.)
shared/        # Reusable: ui/, components/, hooks/, lib/
```

**Rules:**
- `app/` pages only import from `features/` — no business logic
- Features are self-contained: `components/`, `lib/api.ts`, `lib/types.ts`, `index.ts`
- `shared/` never imports from `features/`
- Use `@/` path alias: `@/features/...`, `@/shared/...`

---

## 5. Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Files/folders | `kebab-case` | `user-form-dialog.tsx` |
| Components | `PascalCase` | `UserFormDialog` |
| Hooks | `camelCase` + `use` prefix | `useAuth` |
| Types | `PascalCase` | `AuthUser`, `CreateUserRequest` |
| Functions | `camelCase` | `getUsers`, `canAccessRoute` |
| Constants | `UPPER_SNAKE_CASE` | `ROUTE_ROLES` |

---

## 6. Code Style

- **Quotes**: Double quotes (`"`) in JSX/TS
- **Semicolons**: None at end of statements
- **Component exports**: Named exports (`export function X`), not arrow functions
- **Language**: Code in English. UI text in Spanish.
- **Imports order**: React → External libs → Features → Shared → Local
- **Separators**: Block comments between logical sections:
  ```ts
  /* ------------------------------------------------------------------ */
  /*  Section name                                                       */
  /* ------------------------------------------------------------------ */
  ```

---

## 7. Imports

```typescript
"use client" // Only when needed

// 1. React/Next
import { useState } from "react"
import { useRouter } from "next/navigation"

// 2. External libraries
import { z } from "zod/v4"

// 3. Features (barrel or direct)
import { apiFetch } from "@/features/auth/lib/api"
import type { Role } from "@/features/auth/lib/types"

// 4. Shared
import { Button } from "@/shared/ui/button"
import { notify } from "@/shared/lib/toast"

// 5. Local
import type { UserResponse } from "../lib/types"
```

---

## 8. API Calls

All calls use `apiFetch` from `@/features/auth/lib/api` (handles credentials + auto-refresh):

```typescript
import { apiFetch } from "@/features/auth/lib/api"

export async function getUsers(): Promise<User[]> {
  return apiFetch<User[]>("/api/users")
}

export async function createUser(data: CreateUserRequest): Promise<User> {
  return apiFetch<User>("/api/users", {
    method: "POST",
    body: JSON.stringify(data),
  })
}
```

**Error handling:**
```typescript
try {
  await createUser(data)
  notify.success("Usuario creado")
} catch (err) {
  const message = err instanceof Error ? err.message : "Error inesperado"
  notify.error("Error al crear", { description: message })
}
```

---

## 9. Components

- Base components: shadcn/ui from `@/shared/ui/`
- Never modify `shared/ui/` directly — compose wrappers instead
- Icons: Lucide React; decorative icons use `aria-hidden="true"`
- Toasts: Use `notify` from `@/shared/lib/toast`, never call `sileo` directly
- `"use client"`: Only when needing browser APIs, `useState`, `useEffect`, or event handlers

---

## 10. Forms & Tables

**Forms:** Use `Field` from `@/shared/ui/field.tsx` (label + input + error). Validate with Zod v4.

**Tables:** `@tanstack/react-table` + `DataTable` from `@/shared/ui/data-table.tsx`:
```typescript
// lib/columns.tsx
"use client"
import { type ColumnDef } from "@tanstack/react-table"
import type { User } from "../lib/types"

export const columns: ColumnDef<User>[] = [
  { accessorKey: "username", header: "Usuario" },
]
```

---

## 11. RBAC

Protected routes defined in `features/auth/lib/rbac.ts` (ROUTE_ROLES). Navigation filtered in `shared/components/layout/app-sidebar.tsx`. Backend is source of truth.

---

## 12. Security

1. Never store tokens in localStorage/sessionStorage — HttpOnly cookies only
2. Never log credentials or personal data
3. Validate with Zod before sending to backend
4. Only `NEXT_PUBLIC_` env vars on client
5. Use soft delete, never physical deletion
6. Use `apiFetch` for all API calls

---

## 13. Adding a Feature

1. Create `features/<name>/` with: `index.ts`, `components/`, `lib/`
2. Define types in `lib/types.ts`
3. Implement API in `lib/api.ts` using `apiFetch`
4. Build components in `components/`
5. Export from `index.ts`
6. Add route in `app/dashboard/<name>/page.tsx`
7. Register route in `rbac.ts` if needed
8. Add nav item in `app-sidebar.tsx`

---

## 14. Pre-Commit Checklist

- [ ] `npm run build` passes
- [ ] No `any` types
- [ ] All imports use `@/` path alias
- [ ] UI text in Spanish
- [ ] API calls use `apiFetch`
- [ ] Forms validate with Zod v4
- [ ] Notifications use `notify`
- [ ] Barrel export updated
- [ ] Route in RBAC if access-controlled
- [ ] `"use client"` only where necessary
