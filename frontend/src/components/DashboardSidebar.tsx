import type { LucideIcon } from "lucide-react";
import { ChevronRight, LogOut } from "lucide-react";
import { useLocation, useNavigate } from "@tanstack/react-router";

import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import type { Session } from "@/lib/auth";
import { clearSession } from "@/lib/auth";
import { toast } from "sonner";

export interface DashboardSidebarItem {
  label: string;
  description: string;
  href: string;
  icon: LucideIcon;
}

export function DashboardSidebar({
  session,
  title,
  description,
  items,
  activeHref,
  onSelect,
}: {
  session: Session;
  title: string;
  description: string;
  items: DashboardSidebarItem[];
  activeHref: string;
  onSelect: (href: string) => void;
}) {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (href: string) => {
    const current = location.pathname + location.hash;
    if (href.startsWith("#")) return activeHref === href || location.hash === href;
    return current === href || location.pathname === href;
  };

  return (
    <aside className="lg:fixed lg:left-0 lg:top-0 lg:z-40 lg:h-screen lg:w-[232px]">
      <div className="flex h-full flex-col overflow-hidden bg-sidebar text-sidebar-foreground shadow-[0_18px_50px_rgba(10,20,55,0.25)] lg:border-r lg:border-white/10">
        <div className="flex h-[72px] items-center gap-3 border-b border-white/10 px-4">
          <div className="grid h-11 w-11 place-items-center rounded-full bg-white p-1.5 shadow-sm">
            <img
              src="/favicon.jpeg"
              alt="Resolvr"
              className="h-full w-full rounded-full object-cover"
              onError={(event) => {
                event.currentTarget.style.display = "none";
              }}
            />
          </div>
          <div className="min-w-0">
            <div className="font-display text-lg font-semibold tracking-tight text-white">Resolvr</div>
            <div className="text-[10px] uppercase tracking-[0.28em] text-white/55">
              Complaint portal
            </div>
          </div>
        </div>

        <div className="flex-1 min-h-0 px-3 py-4">
          <div className="mb-4 px-1">
            <div className="text-sm font-semibold text-white">{session.userName}</div>
            <div className="text-xs text-white/60">{session.userRole}</div>
          </div>

          <nav className="space-y-2">
            {items.map(({ icon: Icon, label, href }) => {
              const active = isActive(href);
              return (
                <Button
                  key={href}
                  variant="ghost"
                  className={cn(
                    "sidebar-pill h-auto w-full justify-start gap-3 rounded-full px-3 py-3 text-left",
                    active ? "sidebar-pill-active text-white hover:bg-white/8" : "text-white/90 hover:bg-white/10",
                  )}
                  onClick={() => {
                    if (href.startsWith("#")) {
                      onSelect(href);
                      return;
                    }
                    navigate({ to: href });
                  }}
                  >
                  <span className={cn("grid h-9 w-9 shrink-0 place-items-center rounded-full", active ? "bg-white/12" : "bg-white/8")}>
                    <Icon className="h-4.5 w-4.5 text-white" />
                  </span>
                  <span className="min-w-0 flex-1 text-left text-sm font-medium">{label}</span>
                  <ChevronRight className={cn("h-4 w-4 shrink-0 transition-transform", active && "translate-x-0.5")} />
                </Button>
              );
            })}
          </nav>
        </div>

        <div className="border-t border-white/10 p-3">
          <Button
            variant="ghost"
            className="sidebar-pill h-auto w-full justify-start gap-3 rounded-full px-3 py-3 text-white/95 hover:bg-white/10"
            onClick={() => {
              clearSession();
              toast.success("Signed out");
              navigate({ to: "/" });
            }}
          >
            <span className="grid h-9 w-9 shrink-0 place-items-center rounded-full bg-white/8">
              <LogOut className="h-4.5 w-4.5" />
            </span>
            <span className="text-sm font-medium">Logout</span>
          </Button>
          <div className="px-3 pt-3 text-[11px] tracking-[0.22em] text-white/45">
            RESOLVR v1.0
          </div>
        </div>
      </div>
    </aside>
  );
}
