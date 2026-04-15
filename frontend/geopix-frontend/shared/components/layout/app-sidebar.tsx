"use client";

import * as React from "react";
import { ViewTransition } from "react";
import Link from "next/link";
import { usePathname } from "next/navigation";

import { NavUser } from "@/shared/components/layout/nav-user";
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
} from "@/shared/ui/sidebar";
import {
  HomeIcon,
  LandPlotIcon,
  BarChart3Icon,
  GlobeIcon,
  SettingsIcon,
  HexagonIcon,
  SunIcon,
  MoonIcon,
} from "lucide-react";
import { useTheme } from "next-themes";
import { useAuth } from "@/features/auth/context";
import { filterNavByRoles } from "@/features/auth/lib/rbac";
import type { NavItem } from "@/features/auth/lib/rbac";
import type { Role } from "@/features/auth/lib/types";

const navItems: NavItem[] = [
  {
    title: "Inicio",
    url: "/dashboard",
    icon: HomeIcon,
  },
  {
    title: "Creación de Predios",
    url: "/dashboard/creacion-de-predios",
    icon: LandPlotIcon,
    roles: ["EJECUTOR_INTEGRAL", "ADMIN"] as Role[],
  },
  {
    title: "Reporte Económico",
    url: "/dashboard/reporte-economico",
    icon: BarChart3Icon,
    roles: ["CONTADOR", "ADMIN"] as Role[],
  },
  {
    title: "Georeferenciación",
    url: "/dashboard/georeferenciacion",
    icon: GlobeIcon,
    roles: ["EJECUTOR_INTEGRAL", "ADMIN"] as Role[],
  },
];

const configItem: NavItem = {
  title: "Configuración",
  url: "/dashboard/configuracion",
  icon: SettingsIcon,
  roles: ["ADMIN"] as Role[],
};

function ActiveIndicator() {
  return (
    <ViewTransition
      name="sidebar-active-indicator"
      share="vt-sidebar-indicator"
    >
      <span
        className="absolute inset-0 z-30 rounded-r-md border-l-[3px] border-[#C9A24D]"
        aria-hidden="true"
      />
    </ViewTransition>
  );
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const pathname = usePathname();
  const { resolvedTheme, setTheme } = useTheme();
  const { user } = useAuth();
  const [mounted, setMounted] = React.useState(false);

  React.useEffect(() => setMounted(true), []);

  const userRoles = user?.roles ?? [];

  const visibleNavItems = filterNavByRoles(navItems, userRoles);
  const showConfig = filterNavByRoles([configItem], userRoles).length > 0;

  const activeTextClass =
    "text-sidebar-primary hover:bg-[#C9A24D]/10 hover:text-[#C9A24D] rounded-l-none transition-colors font-semibold";
  const inactiveClass =
    "text-sidebar-foreground/65 hover:text-sidebar-foreground hover:bg-sidebar-accent hover:bg-[#C9A24D]/20 transition-colors";

  const userData = {
    name: user?.username ?? "Usuario",
    email: user?.email ?? "",
    avatar: "",
  };

  return (
    <Sidebar collapsible="offcanvas" {...props}>
      <SidebarHeader
        className="px-4 pt-5 pb-2"
        aria-label="GEOPIX - Administración de Predios"
      >
        <div className="flex items-center gap-2.5">
          <div className="flex items-center justify-center size-8 rounded-lg bg-sidebar-primary">
            <HexagonIcon
              className="size-4 text-sidebar-primary-foreground"
              aria-hidden="true"
            />
          </div>
          <div className="flex flex-col">
            <span className="text-sm font-bold text-white inline tracking-widest">
              <span className="text-[#C9A24D]">GEO</span>
              <span className="text-white">PIX</span>
            </span>
            <span className="text-[10px] font-medium text-sidebar-foreground/35 tracking-wide uppercase">
              Administración de Predios
            </span>
          </div>
        </div>
      </SidebarHeader>

      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel className="text-sidebar-foreground/35 text-[10px] mb-2 uppercase tracking-[0.15em] font-semibold">
            Administración
          </SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu className="gap-4">
              {visibleNavItems.map((item) => {
                const isActive =
                  item.url === "/dashboard"
                    ? pathname === "/dashboard"
                    : pathname.startsWith(item.url);
                return (
                  <SidebarMenuItem key={item.title} className="relative">
                    {isActive && <ActiveIndicator />}
                    <SidebarMenuButton
                      asChild
                      tooltip={item.title}
                      isActive={isActive}
                      className={`relative ${isActive ? activeTextClass : inactiveClass}`}
                    >
                      <Link
                        href={item.url}
                        aria-current={isActive ? "page" : undefined}
                      >
                        <item.icon
                          className={
                            isActive
                              ? "text-sidebar-primary"
                              : "text-sidebar-foreground/40"
                          }
                          aria-hidden="true"
                        />
                        <span>{item.title}</span>
                      </Link>
                    </SidebarMenuButton>
                  </SidebarMenuItem>
                );
              })}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>

      <SidebarFooter>
        <SidebarMenu className="gap-0.5">
          <SidebarMenuItem>
            <SidebarMenuButton
              tooltip={
                mounted && resolvedTheme === "dark"
                  ? "Modo claro"
                  : "Modo oscuro"
              }
              onClick={() =>
                setTheme(resolvedTheme === "dark" ? "light" : "dark")
              }
              className={inactiveClass}
              aria-label={
                mounted && resolvedTheme === "dark"
                  ? "Cambiar a modo claro"
                  : "Cambiar a modo oscuro"
              }
            >
              {mounted && resolvedTheme === "dark" ? (
                <SunIcon
                  className="text-sidebar-foreground/40"
                  aria-hidden="true"
                />
              ) : (
                <MoonIcon
                  className="text-sidebar-foreground/40 fill-white"
                  aria-hidden="true"
                />
              )}
              <span>
                {mounted && resolvedTheme === "dark"
                  ? "Modo claro"
                  : "Modo oscuro"}
              </span>
            </SidebarMenuButton>
          </SidebarMenuItem>

          {showConfig && (
            <SidebarMenuItem className="relative">
              {pathname.startsWith("/dashboard/configuracion") && (
                <ActiveIndicator />
              )}
              <SidebarMenuButton
                asChild
                tooltip="Configuración"
                isActive={pathname.startsWith("/dashboard/configuracion")}
                className={`relative z-[1] ${
                  pathname.startsWith("/dashboard/configuracion")
                    ? activeTextClass
                    : inactiveClass
                }`}
              >
                <Link
                  href="/dashboard/configuracion"
                  aria-current={
                    pathname.startsWith("/dashboard/configuracion")
                      ? "page"
                      : undefined
                  }
                >
                  <SettingsIcon
                    className={
                      pathname.startsWith("/dashboard/configuracion")
                        ? "text-sidebar-primary"
                        : "text-sidebar-foreground/40"
                    }
                    aria-hidden="true"
                  />
                  <span>Configuración</span>
                </Link>
              </SidebarMenuButton>
            </SidebarMenuItem>
          )}
        </SidebarMenu>

        <div className="border-t border-gray-300/30 mt-2 pt-2">
          <NavUser user={userData} />
        </div>
      </SidebarFooter>
    </Sidebar>
  );
}
