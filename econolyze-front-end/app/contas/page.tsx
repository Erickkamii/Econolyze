import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { ArrowLeft, Plus, Wallet, CreditCard, Banknote, PiggyBank } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"

export default function ContasPage() {
  const contas = [
    { id: 1, nome: "Nubank", tipo: "corrente", saldo: 3250.5, icon: CreditCard, cor: "text-purple-500" },
    { id: 2, nome: "Inter", tipo: "corrente", saldo: 1890.0, icon: Wallet, cor: "text-orange-500" },
    { id: 3, nome: "Carteira Física", tipo: "dinheiro", saldo: 250.0, icon: Banknote, cor: "text-green-500" },
    { id: 4, nome: "Poupança BB", tipo: "poupanca", saldo: 10456.82, icon: PiggyBank, cor: "text-blue-500" },
  ]

  const saldoTotal = contas.reduce((acc, conta) => acc + conta.saldo, 0)

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/carteira">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <h1 className="text-2xl font-bold">Minhas Contas</h1>
          </div>
          <Link href="/contas/nova">
            <Button size="icon" className="rounded-full">
              <Plus className="h-5 w-5" />
            </Button>
          </Link>
        </div>

        <Card className="bg-gradient-to-br from-primary/20 to-primary/5 border-primary/30">
          <CardContent className="pt-6">
            <p className="text-sm text-muted-foreground mb-2">Saldo Total</p>
            <p className="text-3xl font-bold">R$ {saldoTotal.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}</p>
          </CardContent>
        </Card>

        <div className="space-y-3">
          {contas.map((conta) => {
            const Icon = conta.icon
            return (
              <Link href={`/contas/${conta.id}`} key={conta.id}>
                <Card className="hover:bg-accent/50 transition-colors cursor-pointer">
                  <CardContent className="pt-6 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                      <div className={`p-3 rounded-full bg-secondary ${conta.cor}`}>
                        <Icon className="h-6 w-6" />
                      </div>
                      <div>
                        <p className="font-semibold">{conta.nome}</p>
                        <p className="text-sm text-muted-foreground capitalize">{conta.tipo}</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-lg font-bold">
                        R$ {conta.saldo.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
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
