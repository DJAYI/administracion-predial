import { AppSidebar } from "@/shared/components/layout/app-sidebar";
import { AuthGuard } from "@/features/auth/components/auth-guard";
import { TransitionContent } from "@/shared/components/layout/transition-content";
import {
  SidebarInset,
  SidebarProvider,
  SidebarSeparator,
  SidebarTrigger,
} from "@/shared/ui/sidebar";
import { TooltipProvider } from "@/shared/ui/tooltip";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <AuthGuard>
      <TooltipProvider>
        <SidebarProvider>
          <AppSidebar variant="sidebar" />
          <SidebarInset>
            <header className="flex h-16 shrink-0 items-center gap-2 border-b px-4 top-0 bg-background z-10 sticky mx-3">
              <SidebarTrigger className="-ml-1 border border-gray-200/50" />

              <span>
                <SidebarSeparator
                  orientation="vertical"
                  className="mr-2 data-[orientation=vertical]:h-4 bg-gray-300/50"
                />
              </span>
            </header>
            <div className="flex flex-1 flex-col min-w-0 overflow-x-hidden">
              <TransitionContent>{children}</TransitionContent>
            </div>
          </SidebarInset>
        </SidebarProvider>
      </TooltipProvider>
    </AuthGuard>
  );
}
