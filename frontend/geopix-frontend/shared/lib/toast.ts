import { sileo } from "sileo"

/**
 * Utilidad centralizada de notificaciones para GEOPIX.
 *
 * Detecta automaticamente el tema (claro/oscuro) y aplica el estilo
 * de toast invertido para maximo contraste.
 *
 * Uso:
 *   import { notify } from "@/lib/toast"
 *   notify.success("Predio creado")
 *   notify.error("Error al guardar", { description: "Revise los campos" })
 *   notify.promise(fetchData(), { loading: "Cargando...", success: "Listo", error: "Error" })
 */

type ToastOptions = {
  description?: string
  duration?: number
}

const variants = {
  dark: {
    fill: "#1e2433",
    styles: {
      title: "text-white!",
      description: "text-white/55!",
      badge: "bg-white/10!",
      button: "bg-white/10! hover:bg-white/15!",
    },
  },
  light: {
    fill: "#FAFBFC",
    styles: {
      title: "text-slate-900!",
      description: "text-slate-500!",
      badge: "bg-slate-900/8!",
      button: "bg-slate-900/8! hover:bg-slate-900/12!",
    },
  },
} as const

function getVariantProps() {
  if (typeof document === "undefined") return variants.dark
  const isDark = document.documentElement.classList.contains("dark")
  return isDark ? variants.light : variants.dark
}

export const notify = {
  /** Notificacion exitosa (verde) */
  success(message: string, opts?: ToastOptions) {
    sileo.success({
      title: message,
      description: opts?.description,
      duration: opts?.duration ?? 4000,
      ...getVariantProps(),
    })
  },

  /** Notificacion de error (roja) */
  error(message: string, opts?: ToastOptions) {
    sileo.error({
      title: message,
      description: opts?.description,
      duration: opts?.duration ?? 5000,
      ...getVariantProps(),
    })
  },

  /** Notificacion informativa (azul) */
  info(message: string, opts?: ToastOptions) {
    sileo.info({
      title: message,
      description: opts?.description,
      duration: opts?.duration ?? 4000,
      ...getVariantProps(),
    })
  },

  /** Notificacion de advertencia (ambar) */
  warning(message: string, opts?: ToastOptions) {
    sileo.warning({
      title: message,
      description: opts?.description,
      duration: opts?.duration ?? 4500,
      ...getVariantProps(),
    })
  },

  /** Toast asincronico que muestra loading -> success/error */
  promise<T>(
    promise: Promise<T>,
    messages: { loading: string; success: string; error: string },
  ) {
    const v = getVariantProps()
    return sileo.promise(promise, {
      loading: { title: messages.loading, ...v },
      success: { title: messages.success, ...v },
      error: { title: messages.error, ...v },
    })
  },
}
