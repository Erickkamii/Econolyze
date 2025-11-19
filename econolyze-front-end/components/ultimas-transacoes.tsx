import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ShoppingBag, Coffee, Zap, Briefcase } from "lucide-react"

const transacoes = [
  { id: 1, categoria: "Compras", descricao: "Supermercado", valor: -247.5, tipo: "gasto", icon: ShoppingBag },
  { id: 2, categoria: "Alimentação", descricao: "Café da manhã", valor: -32.0, tipo: "gasto", icon: Coffee },
  { id: 3, categoria: "Contas", descricao: "Conta de luz", valor: -185.9, tipo: "gasto", icon: Zap },
  { id: 4, categoria: "Salário", descricao: "Pagamento mensal", valor: 5500.0, tipo: "receita", icon: Briefcase },
]

export function UltimasTransacoes() {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-lg">Últimas Transações</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-3">
          {transacoes.map((transacao) => {
            const Icon = transacao.icon
            const isReceita = transacao.tipo === "receita"

            return (
              <div
                key={transacao.id}
                className="flex items-center gap-4 p-3 rounded-lg hover:bg-secondary/50 transition-colors"
              >
                <div
                  className={`h-10 w-10 rounded-full flex items-center justify-center ${
                    isReceita ? "bg-success/20" : "bg-muted"
                  }`}
                >
                  <Icon className={`h-5 w-5 ${isReceita ? "text-success" : "text-muted-foreground"}`} />
                </div>

                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium truncate">{transacao.descricao}</p>
                  <p className="text-xs text-muted-foreground">{transacao.categoria}</p>
                </div>

                <div className={`text-sm font-semibold ${isReceita ? "text-success" : "text-foreground"}`}>
                  {isReceita ? "+" : ""} R${" "}
                  {Math.abs(transacao.valor).toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </div>
              </div>
            )
          })}
        </div>
      </CardContent>
    </Card>
  )
}
