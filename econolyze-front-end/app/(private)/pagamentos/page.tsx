import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { ArrowLeft, AlertCircle } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"

export default function PagamentosPage() {
  const transacoesAbertas = [
    {
      id: 1,
      descricao: "Conta de Luz",
      valorTotal: 150.0,
      valorPago: 50.0,
      categoria: "Contas",
      data: "2025-01-15",
    },
    {
      id: 2,
      descricao: "Aluguel",
      valorTotal: 1200.0,
      valorPago: 600.0,
      categoria: "Moradia",
      data: "2025-01-01",
    },
    {
      id: 3,
      descricao: "Cartão de Crédito",
      valorTotal: 850.0,
      valorPago: 200.0,
      categoria: "Cartão",
      data: "2025-01-10",
    },
  ]

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/carteira">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Transações em Aberto</h1>
        </div>

        <Card className="bg-destructive/10 border-destructive/30">
          <CardContent className="pt-6 flex items-start gap-3">
            <AlertCircle className="h-5 w-5 text-destructive mt-0.5 shrink-0" />
            <div>
              <p className="font-semibold text-destructive">Atenção</p>
              <p className="text-sm text-muted-foreground">
                Você possui {transacoesAbertas.length} transações com pagamentos pendentes
              </p>
            </div>
          </CardContent>
        </Card>

        <div className="space-y-3">
          {transacoesAbertas.map((transacao) => {
            const valorRestante = transacao.valorTotal - transacao.valorPago
            const percentualPago = (transacao.valorPago / transacao.valorTotal) * 100

            return (
              <Link href={`/econolyze-front-end/app/private/pagamentos/${transacao.id}`} key={transacao.id}>
                <Card className="hover:bg-accent/50 transition-colors cursor-pointer">
                  <CardContent className="pt-6 space-y-3">
                    <div className="flex items-start justify-between">
                      <div>
                        <p className="font-semibold">{transacao.descricao}</p>
                        <p className="text-sm text-muted-foreground">{transacao.categoria}</p>
                      </div>
                      <Badge variant="outline" className="border-destructive/30 text-destructive">
                        Em aberto
                      </Badge>
                    </div>

                    <div className="space-y-2">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Progresso</span>
                        <span className="font-medium">{percentualPago.toFixed(0)}%</span>
                      </div>
                      <div className="w-full bg-secondary rounded-full h-2 overflow-hidden">
                        <div className="bg-primary h-full transition-all" style={{ width: `${percentualPago}%` }} />
                      </div>
                      <div className="flex justify-between text-xs text-muted-foreground">
                        <span>
                          Pago: R$ {transacao.valorPago.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                        </span>
                        <span>Restante: R$ {valorRestante.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}</span>
                      </div>
                    </div>

                    <div className="pt-2">
                      <p className="text-lg font-bold text-destructive">
                        Total: R$ {transacao.valorTotal.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                      </p>
                    </div>
                  </CardContent>
                </Card>
              </Link>
            )
          })}
        </div>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
