"use client";

import { ViewTransition } from "react";
import { usePathname } from "next/navigation";

export function TransitionContent({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();

  return (
    <ViewTransition
      key={pathname}
      name="dashboard-content"
      enter="vt-content-blur"
      exit="vt-content-blur"
      share="vt-content-blur"
    >
      <div className="flex flex-1 flex-col min-w-0">{children}</div>
    </ViewTransition>
  );
}
