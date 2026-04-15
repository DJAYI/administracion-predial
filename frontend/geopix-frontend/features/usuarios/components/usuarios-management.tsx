"use client"

import { useState, useEffect, useMemo, useCallback } from "react"
import { Input } from "@/shared/ui/input"
import { Button } from "@/shared/ui/button"
import { Switch } from "@/shared/ui/switch"
import { DataTable } from "@/shared/ui/data-table"
import { Skeleton } from "@/shared/ui/skeleton"
import {
  SearchIcon,
  PlusIcon,
  UsersIcon,
  RefreshCwIcon,
} from "lucide-react"
import { notify } from "@/shared/lib/toast"
import { getUsers, getAllUsers, restoreUser } from "@/features/usuarios/lib/api"
import { getUserColumns } from "@/features/usuarios/lib/columns"
import { UserFormDialog } from "@/features/usuarios/components/user-form-dialog"
import { UserDetailDialog } from "@/features/usuarios/components/user-detail-dialog"
import { UserDeleteDialog } from "@/features/usuarios/components/user-delete-dialog"
import { UserHistoryDialog } from "@/features/usuarios/components/user-history-dialog"
import type { UserResponse } from "@/features/usuarios/lib/types"

export function UsuariosManagement() {
  // Data state
  const [users, setUsers] = useState<UserResponse[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
  const [includeDeleted, setIncludeDeleted] = useState(false)

  // Dialog state
  const [formOpen, setFormOpen] = useState(false)
  const [editingUser, setEditingUser] = useState<UserResponse | null>(null)
  const [detailOpen, setDetailOpen] = useState(false)
  const [detailUser, setDetailUser] = useState<UserResponse | null>(null)
  const [deleteOpen, setDeleteOpen] = useState(false)
  const [deletingUser, setDeletingUser] = useState<UserResponse | null>(null)
  const [historyOpen, setHistoryOpen] = useState(false)
  const [historyUser, setHistoryUser] = useState<UserResponse | null>(null)

  // Fetch users
  const fetchUsers = useCallback(async () => {
    setIsLoading(true)
    try {
      const data = includeDeleted ? await getAllUsers() : await getUsers()
      setUsers(data)
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Error al cargar usuarios"
      notify.error("Error", { description: message })
    } finally {
      setIsLoading(false)
    }
  }, [includeDeleted])

  useEffect(() => {
    fetchUsers()
  }, [fetchUsers])

  // Filter users by search query
  const filteredUsers = useMemo(() => {
    if (!searchQuery.trim()) return users
    const q = searchQuery.toLowerCase()
    return users.filter(
      (u) =>
        u.username.toLowerCase().includes(q) ||
        u.email.toLowerCase().includes(q) ||
        u.roles.some((r) => r.toLowerCase().includes(q)),
    )
  }, [users, searchQuery])

  // Action handlers
  function handleView(user: UserResponse) {
    setDetailUser(user)
    setDetailOpen(true)
  }

  function handleEdit(user: UserResponse) {
    setEditingUser(user)
    setFormOpen(true)
  }

  function handleCreate() {
    setEditingUser(null)
    setFormOpen(true)
  }

  function handleDelete(user: UserResponse) {
    setDeletingUser(user)
    setDeleteOpen(true)
  }

  async function handleRestore(user: UserResponse) {
    try {
      await restoreUser(user.id)
      notify.success("Usuario restaurado", {
        description: `El usuario "${user.username}" ha sido reactivado`,
      })
      fetchUsers()
    } catch (err) {
      const message =
        err instanceof Error ? err.message : "Error al restaurar"
      notify.error("Error al restaurar", { description: message })
    }
  }

  function handleHistory(user: UserResponse) {
    setHistoryUser(user)
    setHistoryOpen(true)
  }

  // Column definitions with actions
  const columns = useMemo(
    () =>
      getUserColumns({
        onView: handleView,
        onEdit: handleEdit,
        onDelete: handleDelete,
        onRestore: handleRestore,
        onHistory: handleHistory,
      }),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [],
  )

  return (
    <div className="flex flex-1 flex-col py-8 px-4 sm:px-6 lg:px-8 bg-background">
      <div className="w-full max-w-6xl mx-auto flex flex-col gap-6">
        {/* Header */}
        <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex items-center gap-3">
            <div className="flex items-center justify-center size-10 rounded-lg bg-primary/8 border border-primary/10">
              <UsersIcon className="size-5 text-primary" aria-hidden="true" />
            </div>
            <div>
              <h1 className="text-xl font-bold text-foreground tracking-tight">
                Gestion de usuarios
              </h1>
              <p className="text-sm text-muted-foreground">
                Administre las cuentas de usuario del sistema
              </p>
            </div>
          </div>

          <Button onClick={handleCreate}>
            <PlusIcon data-icon="inline-start" />
            Nuevo usuario
          </Button>
        </div>

        {/* Toolbar */}
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div className="relative w-full sm:max-w-sm">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <SearchIcon
                className="size-4 text-muted-foreground/60"
                aria-hidden="true"
              />
            </div>
            <Input
              type="search"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Buscar por nombre, email o rol..."
              className="pl-9 h-9 text-sm"
              aria-label="Buscar usuarios"
            />
          </div>

          <div className="flex items-center gap-4">
            <label className="flex items-center gap-2 text-sm text-muted-foreground cursor-pointer">
              <Switch
                checked={includeDeleted}
                onCheckedChange={setIncludeDeleted}
                size="sm"
              />
              Incluir eliminados
            </label>

            <Button
              variant="ghost"
              size="icon"
              className="size-8 text-muted-foreground hover:text-foreground"
              onClick={fetchUsers}
              aria-label="Recargar lista"
            >
              <RefreshCwIcon className="size-4" />
            </Button>
          </div>
        </div>

        {/* Table */}
        {isLoading ? (
          <div className="flex flex-col gap-3">
            <Skeleton className="h-10 w-full rounded-lg" />
            {[1, 2, 3, 4, 5].map((i) => (
              <Skeleton key={i} className="h-12 w-full rounded-lg" />
            ))}
          </div>
        ) : (
          <DataTable columns={columns} data={filteredUsers} />
        )}
      </div>

      {/* Dialogs */}
      <UserFormDialog
        open={formOpen}
        onOpenChange={setFormOpen}
        user={editingUser}
        onSuccess={fetchUsers}
      />

      <UserDetailDialog
        open={detailOpen}
        onOpenChange={setDetailOpen}
        user={detailUser}
      />

      <UserDeleteDialog
        open={deleteOpen}
        onOpenChange={setDeleteOpen}
        user={deletingUser}
        onSuccess={fetchUsers}
      />

      <UserHistoryDialog
        open={historyOpen}
        onOpenChange={setHistoryOpen}
        user={historyUser}
      />
    </div>
  )
}
