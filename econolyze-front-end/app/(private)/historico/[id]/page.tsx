import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ArrowLeft, ShoppingBag } from "lucide-react"
import Link from "next/link"

export default function TransacaoDetalhesPage({ params }: { params: { id: string } }) {
  // Mock data - em produção viria do backend
  const transacao = {
    id: params.id,
    categoria: "Compras",
    descricao: "Supermercado Extra",
    valor: 247.5,
    tipo: "gasto",
    icon: ShoppingBag,
    data: "10/12/2025",
    hora: "14:30",
    conta: "Nubank",
    observacoes: "Compras do mês - mercado",
    status: "pago",
  }

  const isReceita = transacao.tipo === "receita"
  const Icon = transacao.icon

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/historico">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Detalhes da Transação</h1>
        </div>

        <Card>
          <CardHeader>
            <div className="flex items-center gap-4">
              <div
                className={`h-16 w-16 rounded-full flex items-center justify-center ${isReceita ? "bg-success/20" : "bg-muted"}`}
              >
                <Icon className={`h-8 w-8 ${isReceita ? "text-success" : "text-muted-foreground"}`} />
              </div>
              <div className="flex-1">
                <CardTitle className="text-xl">{transacao.descricao}</CardTitle>
                <p className="text-sm text-muted-foreground">{transacao.categoria}</p>
              </div>
            </div>
          </CardHeader>

          <CardContent className="space-y-4">
            <div className="flex items-center justify-between py-3 border-b">
              <span className="text-muted-foreground">Valor</span>
              <span className={`text-2xl font-bold ${isReceita ? "text-success" : "text-foreground"}`}>
                {isReceita ? "+" : "-"} R$ {transacao.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
              </span>
            </div>

            <div className="flex items-center justify-between py-3 border-b">
              <span className="text-muted-foreground">Data</span>
              <span className="font-medium">{transacao.data}</span>
            </div>

            <div className="flex items-center justify-between py-3 border-b">
              <span className="text-muted-foreground">Hora</span>
              <span className="font-medium">{transacao.hora}</span>
            </div>

            <div className="flex items-center justify-between py-3 border-b">
              <span className="text-muted-foreground">Conta</span>
              <span className="font-medium">{transacao.conta}</span>
            </div>

            <div className="flex items-center justify-between py-3 border-b">
              <span className="text-muted-foreground">Status</span>
              <span className="font-medium capitalize">{transacao.status}</span>
            </div>

            {transacao.observacoes && (
              <div className="py-3">
                <p className="text-muted-foreground mb-2">Observações</p>
                <p className="text-sm">{transacao.observacoes}</p>
              </div>
            )}

            <div className="pt-4 space-y-2">
              <Link href={`/historico/${transacao.id}/editar`}>
                <Button variant="outline" className="w-full bg-transparent">
                  Editar Transação
                </Button>
              </Link>
              <Button variant="destructive" className="w-full">
                Excluir Transação
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
