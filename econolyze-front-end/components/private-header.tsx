"use client"

import { LogOut } from "lucide-react"
import { usePathname } from "next/navigation"
import { toast } from "sonner"

import { ThemeToggle } from "@/components/theme-toggle"
import { Button } from "@/components/ui/button"
import { useAuth } from "@/context/auth.context"

const FORM_ROUTES = [
  /^\/transacao$/,
  /^\/transacao\/nova$/,
  /^\/historico\/[^/]+\/editar$/,
  /^\/recorrentes\/nova$/,
  /^\/recorrentes\/[^/]+\/editar$/,
  /^\/contas\/nova$/,
  /^\/metas\/nova$/,
  /^\/pagamentos\/[^/]+$/,
]

export function PrivateHeader() {
  const pathname = usePathname()
  const { logout, isLoading } = useAuth()
  const isFormRoute = FORM_ROUTES.some((route) => route.test(pathname))

  if (isFormRoute) return null

  const handleLogout = () => {
    if (isLoading) return

    toast.promise(logout(), {
      loading: "Saindo...",
      success: "Logout bem-sucedido!",
      error: "Erro ao sair. Tente novamente.",
    })
  }

  return (
    <header className="mx-auto flex w-full max-w-5xl items-center justify-between px-6 pt-6">
      <h1 className="text-2xl font-bold text-primary">Econolyze</h1>

      <div className="flex items-center gap-2">
        <ThemeToggle />
        <Button
          variant="ghost"
          size="sm"
          onClick={handleLogout}
          disabled={isLoading}
          className="text-primary hover:bg-primary/10 transition-colors duration-200"
        >
          <LogOut className="mr-2 h-4 w-4" />
          Sair
        </Button>
      </div>
    </header>
  )
}
