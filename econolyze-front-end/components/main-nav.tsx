"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { Home, BarChart3, TrendingUp, Repeat, History } from "lucide-react"
import { cn } from "@/lib/utils"

export function MainNav() {
  const pathname = usePathname()

  const navItems = [
    { href: "/carteira", label: "Home", icon: Home },
    { href: "/historico", label: "Histórico", icon: History },
    { href: "/recorrentes", label: "Recorrentes", icon: Repeat },
    { href: "/analises", label: "Análises", icon: BarChart3 },
    { href: "/investimentos", label: "Investimentos", icon: TrendingUp },
  ]

  return (
    <nav className="fixed bottom-0 left-0 right-0 bg-card border-t border-border">
      <div className="max-w-5xl mx-auto px-6">
        <div className="flex items-center justify-around h-16">
          {navItems.map((item) => {
            const Icon = item.icon
            const isActive = pathname === item.href

            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  "flex flex-col items-center gap-1 px-6 py-2 rounded-lg transition-colors",
                  isActive ? "text-primary" : "text-muted-foreground hover:text-foreground",
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
