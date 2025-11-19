import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { PagamentoForm } from "@/components/pagamento-form"
import { ArrowLeft } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"

export default function NovoPagamentoPage() {
  // Mock data - em produção viria do banco
  const transacao = {
    id: 1,
    descricao: "Conta de Luz",
    valorTotal: 150.0,
    valorPago: 50.0,
    categoria: "Contas",
  }

  const valorRestante = transacao.valorTotal - transacao.valorPago

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/pagamentos">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Registrar Pagamento</h1>
        </div>

        <Card className="bg-secondary/50">
          <CardContent className="pt-6 space-y-3">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-sm text-muted-foreground">Transação</p>
                <p className="text-lg font-semibold">{transacao.descricao}</p>
              </div>
              <div className="text-right">
                <p className="text-sm text-muted-foreground">Valor Total</p>
                <p className="text-lg font-bold">
                  R$ {transacao.valorTotal.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </p>
              </div>
            </div>

            <div className="grid grid-cols-2 gap-4 pt-2">
              <div>
                <p className="text-sm text-muted-foreground">Já Pago</p>
                <p className="text-base font-semibold text-primary">
                  R$ {transacao.valorPago.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </p>
              </div>
              <div>
                <p className="text-sm text-muted-foreground">Restante</p>
                <p className="text-base font-semibold text-destructive">
                  R$ {valorRestante.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <PagamentoForm transacaoId={transacao.id} valorRestante={valorRestante} />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
