import { MainNav } from "@/components/main-nav"
import { InvestimentoChart } from "@/components/investimento-chart"
import { InvestimentoMetrics } from "@/components/investimento-metrics"
import { ChatbotButton } from "@/components/chatbot-button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import Link from "next/link"

export default function InvestimentosPage() {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-5xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between gap-4">
          <h1 className="text-2xl font-bold">Investimentos - CDB vs CDI</h1>
          <Button asChild>
            <Link href="/metas/nova">
              <Plus className="mr-2 h-4 w-4" />
              Nova Meta
            </Link>
          </Button>
        </div>

        <InvestimentoMetrics />

        <InvestimentoChart />

        <Card>
          <CardHeader>
            <CardTitle>Alocação de Ativos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium">CDB</span>
                  <span className="text-sm text-muted-foreground">R$ 35.000,00 (45%)</span>
                </div>
                <div className="h-3 bg-secondary rounded-full overflow-hidden">
                  <div className="h-full bg-blue-600" style={{ width: "45%" }} />
                </div>
              </div>

              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium">Tesouro Direto</span>
                  <span className="text-sm text-muted-foreground">R$ 25.000,00 (32%)</span>
                </div>
                <div className="h-3 bg-secondary rounded-full overflow-hidden">
                  <div className="h-full bg-green-600" style={{ width: "32%" }} />
                </div>
              </div>

              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium">Ações</span>
                  <span className="text-sm text-muted-foreground">R$ 12.000,00 (15%)</span>
                </div>
                <div className="h-3 bg-secondary rounded-full overflow-hidden">
                  <div className="h-full bg-purple-600" style={{ width: "15%" }} />
                </div>
              </div>

              <div>
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm font-medium">Fundos Imobiliários</span>
                  <span className="text-sm text-muted-foreground">R$ 6.000,00 (8%)</span>
                </div>
                <div className="h-3 bg-secondary rounded-full overflow-hidden">
                  <div className="h-full bg-yellow-600" style={{ width: "8%" }} />
                </div>
              </div>
            </div>

            <div className="mt-6 pt-6 border-t border-border">
              <div className="flex items-center justify-between text-lg font-semibold">
                <span>Total Investido</span>
                <span className="text-primary">R$ 78.000,00</span>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Rentabilidade Mensal</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {[
                { mes: "Dezembro/24", valor: 1245.32, percentual: 1.65 },
                { mes: "Novembro/24", valor: 1180.21, percentual: 1.58 },
                { mes: "Outubro/24", valor: 1205.45, percentual: 1.62 },
                { mes: "Setembro/24", valor: 1165.89, percentual: 1.55 },
                { mes: "Agosto/24", valor: 1220.76, percentual: 1.68 },
                { mes: "Julho/24", valor: 1190.34, percentual: 1.6 },
              ].map((item) => (
                <div key={item.mes} className="flex items-center justify-between p-3 bg-secondary/50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium">{item.mes}</p>
                    <p className="text-xs text-muted-foreground">+{item.percentual}%</p>
                  </div>
                  <div className="text-right">
                    <p className="text-sm font-semibold text-green-600">
                      +R$ {item.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                    </p>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
