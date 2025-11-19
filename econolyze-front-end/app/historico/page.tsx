"use client"

import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Plus, ShoppingBag, Coffee, Zap, Briefcase, Car, HomeIcon } from "lucide-react"
import Link from "next/link"
import { useState } from "react"
import { cn } from "@/lib/utils"

const transacoes = [
  {
    id: 1,
    categoria: "Compras",
    descricao: "Supermercado Extra",
    valor: -247.5,
    tipo: "gasto",
    icon: ShoppingBag,
    data: "10/12/2025",
  },
  {
    id: 2,
    categoria: "Alimentação",
    descricao: "Café da manhã",
    valor: -32.0,
    tipo: "gasto",
    icon: Coffee,
    data: "10/12/2025",
  },
  {
    id: 3,
    categoria: "Contas",
    descricao: "Conta de luz",
    valor: -185.9,
    tipo: "gasto",
    icon: Zap,
    data: "09/12/2025",
  },
  {
    id: 4,
    categoria: "Salário",
    descricao: "Pagamento mensal",
    valor: 5500.0,
    tipo: "receita",
    icon: Briefcase,
    data: "05/12/2025",
  },
  {
    id: 5,
    categoria: "Transporte",
    descricao: "Combustível",
    valor: -180.0,
    tipo: "gasto",
    icon: Car,
    data: "08/12/2025",
  },
  {
    id: 6,
    categoria: "Moradia",
    descricao: "Aluguel",
    valor: -1200.0,
    tipo: "gasto",
    icon: HomeIcon,
    data: "01/12/2025",
  },
  {
    id: 7,
    categoria: "Freelance",
    descricao: "Projeto web",
    valor: 1500.0,
    tipo: "receita",
    icon: Briefcase,
    data: "03/12/2025",
  },
]

type FilterType = "todos" | "receita" | "gasto"

export default function HistoricoPage() {
  const [filtro, setFiltro] = useState<FilterType>("todos")

  const transacoesFiltradas = transacoes.filter((t) => {
    if (filtro === "todos") return true
    return t.tipo === filtro
  })

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Histórico</h1>
          <Link href="/transacao/nova">
            <Button size="icon" className="rounded-full">
              <Plus className="h-5 w-5" />
            </Button>
          </Link>
        </div>

        <div className="flex gap-2">
          <Button
            variant={filtro === "todos" ? "default" : "outline"}
            size="sm"
            onClick={() => setFiltro("todos")}
            className="flex-1"
          >
            Todos
          </Button>
          <Button
            variant={filtro === "receita" ? "default" : "outline"}
            size="sm"
            onClick={() => setFiltro("receita")}
            className="flex-1"
          >
            Receitas
          </Button>
          <Button
            variant={filtro === "gasto" ? "default" : "outline"}
            size="sm"
            onClick={() => setFiltro("gasto")}
            className="flex-1"
          >
            Gastos
          </Button>
        </div>

        <Card>
          <CardContent className="pt-6">
            <div className="space-y-3">
              {transacoesFiltradas.map((transacao) => {
                const Icon = transacao.icon
                const isReceita = transacao.tipo === "receita"

                return (
                  <Link key={transacao.id} href={`/historico/${transacao.id}`}>
                    <div className="flex items-center gap-4 p-3 rounded-lg hover:bg-secondary/50 transition-colors cursor-pointer">
                      <div
                        className={cn(
                          "h-10 w-10 rounded-full flex items-center justify-center",
                          isReceita ? "bg-success/20" : "bg-muted",
                        )}
                      >
                        <Icon className={cn("h-5 w-5", isReceita ? "text-success" : "text-muted-foreground")} />
                      </div>

                      <div className="flex-1 min-w-0">
                        <p className="text-sm font-medium truncate">{transacao.descricao}</p>
                        <div className="flex items-center gap-2 text-xs text-muted-foreground">
                          <span>{transacao.categoria}</span>
                          <span>•</span>
                          <span>{transacao.data}</span>
                        </div>
                      </div>

                      <div className={cn("text-sm font-semibold", isReceita ? "text-success" : "text-foreground")}>
                        {isReceita ? "+" : "-"} R${" "}
                        {Math.abs(transacao.valor).toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                      </div>
                    </div>
                  </Link>
                )
              })}
            </div>
          </CardContent>
        </Card>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
