"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { Home, BarChart3, TrendingUp, Repeat, History, Target } from "lucide-react"
import { cn } from "@/lib/utils"

export function MainNav() {
  const pathname = usePathname()

  const navItems = [
    { href: "/carteira", label: "Home", icon: Home },
    { href: "/historico", label: "Histórico", icon: History },
    { href: "/recorrentes", label: "Recorrentes", icon: Repeat },
    { href: "/metas", label: "Metas", icon: Target },
    { href: "/analises", label: "Análises", icon: BarChart3 },
    { href: "/investimentos", label: "Investimentos", icon: TrendingUp },
  ]

  return (
    <nav
      className="fixed bottom-0 left-0 right-0 border-t border-border bg-card/95 backdrop-blur supports-[backdrop-filter]:bg-card/80"
      aria-label="Navegacao principal"
    >
      <div className="max-w-5xl mx-auto px-6">
        <div className="flex h-16 items-center justify-around pb-[max(env(safe-area-inset-bottom),0px)]">
          {navItems.map((item) => {
            const Icon = item.icon
            const isActive = pathname === item.href

            return (
              <Link
                key={item.href}
                href={item.href}
                aria-current={isActive ? "page" : undefined}
                className={cn(
                  "flex flex-col items-center gap-1 rounded-lg px-3 py-2 transition-all duration-200",
                  isActive || pathname.startsWith(`${item.href}/`)
                    ? "bg-primary/10 text-primary"
                    : "text-muted-foreground hover:bg-secondary/70 hover:text-foreground",
                )}
              >
                <Icon className="h-5 w-5" />
                <span className="text-xs font-medium">{item.label}</span>
              </Link>
            )
          })}
        </div>
      </div>
    </nav>
  )
}
