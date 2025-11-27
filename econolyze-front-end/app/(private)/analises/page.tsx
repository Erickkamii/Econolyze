import { MainNav } from "@/components/main-nav"
import { ChatbotButton } from "@/components/chatbot-button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { TrendingUp, TrendingDown, DollarSign, PieChart } from "lucide-react"

export default function AnalisesPage() {
  // Sample data for preview
  const monthlyData = [
    { month: "Jan", receitas: 8500, despesas: 6200 },
    { month: "Fev", receitas: 9200, despesas: 6800 },
    { month: "Mar", receitas: 8800, despesas: 7100 },
    { month: "Abr", receitas: 9500, despesas: 6500 },
    { month: "Mai", receitas: 10200, despesas: 7300 },
    { month: "Jun", receitas: 9800, despesas: 6900 },
  ]

  const categoryData = [
    { name: "Alimentação", value: 2800, color: "bg-blue-500", percent: 32 },
    { name: "Transporte", value: 1500, color: "bg-blue-400", percent: 17 },
    { name: "Moradia", value: 2200, color: "bg-blue-600", percent: 25 },
    { name: "Lazer", value: 1100, color: "bg-blue-300", percent: 13 },
    { name: "Outros", value: 1300, color: "bg-blue-200", percent: 13 },
  ]

  const totalReceitas = monthlyData.reduce((acc, m) => acc + m.receitas, 0)
  const totalDespesas = monthlyData.reduce((acc, m) => acc + m.despesas, 0)
  const saldo = totalReceitas - totalDespesas
  const maxValue = Math.max(...monthlyData.map((m) => Math.max(m.receitas, m.despesas)))

  return (
    <div className="min-h-screen pb-20 bg-background">
      <div className="max-w-5xl mx-auto p-6 space-y-6">
        <div>
          <h1 className="text-2xl font-bold">Análises Financeiras</h1>
          <p className="text-sm text-muted-foreground mt-1">Últimos 6 meses - Preview do sistema</p>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card className="border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <TrendingUp className="w-4 h-4" />
                Total Receitas
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-500">R$ {totalReceitas.toLocaleString("pt-BR")}</div>
              <p className="text-xs text-muted-foreground mt-1">Média: R$ {(totalReceitas / 6).toFixed(0)}/mês</p>
            </CardContent>
          </Card>

          <Card className="border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <TrendingDown className="w-4 h-4" />
                Total Despesas
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-500">R$ {totalDespesas.toLocaleString("pt-BR")}</div>
              <p className="text-xs text-muted-foreground mt-1">Média: R$ {(totalDespesas / 6).toFixed(0)}/mês</p>
            </CardContent>
          </Card>

          <Card className="border-border/50">
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium text-muted-foreground flex items-center gap-2">
                <DollarSign className="w-4 h-4" />
                Saldo Período
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className={`text-2xl font-bold ${saldo >= 0 ? "text-blue-500" : "text-red-500"}`}>
                R$ {saldo.toLocaleString("pt-BR")}
              </div>
              <p className="text-xs text-muted-foreground mt-1">{saldo >= 0 ? "Superávit" : "Déficit"} acumulado</p>
            </CardContent>
          </Card>
        </div>

        {/* Monthly Comparison Chart */}
        <Card className="border-border/50">
          <CardHeader>
            <CardTitle className="text-lg flex items-center gap-2">
              <TrendingUp className="w-5 h-5" />
              Evolução Mensal
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {monthlyData.map((item) => (
                <div key={item.month} className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="font-medium">{item.month}</span>
                    <div className="flex gap-4 text-xs">
                      <span className="text-green-500">↑ R$ {item.receitas.toLocaleString("pt-BR")}</span>
                      <span className="text-red-500">↓ R$ {item.despesas.toLocaleString("pt-BR")}</span>
                    </div>
                  </div>
                  <div className="flex gap-1 h-8">
                    <div
                      className="bg-green-500/20 border-l-2 border-green-500 rounded flex items-center justify-end px-2"
                      style={{ width: `${(item.receitas / maxValue) * 100}%` }}
                    >
                      <span className="text-xs text-green-500 font-medium">
                        {((item.receitas / maxValue) * 100).toFixed(0)}%
                      </span>
                    </div>
                  </div>
                  <div className="flex gap-1 h-8">
                    <div
                      className="bg-red-500/20 border-l-2 border-red-500 rounded flex items-center justify-end px-2"
                      style={{ width: `${(item.despesas / maxValue) * 100}%` }}
                    >
                      <span className="text-xs text-red-500 font-medium">
                        {((item.despesas / maxValue) * 100).toFixed(0)}%
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Category Breakdown */}
        <Card className="border-border/50">
          <CardHeader>
            <CardTitle className="text-lg flex items-center gap-2">
              <PieChart className="w-5 h-5" />
              Despesas por Categoria
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {/* Visual representation */}
              <div className="flex h-8 rounded-lg overflow-hidden">
                {categoryData.map((cat) => (
                  <div
                    key={cat.name}
                    className={cat.color}
                    style={{ width: `${cat.percent}%` }}
                    title={`${cat.name}: ${cat.percent}%`}
                  />
                ))}
              </div>

              {/* Category list */}
              <div className="space-y-3">
                {categoryData.map((cat) => (
                  <div key={cat.name} className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div className={`w-3 h-3 rounded-full ${cat.color}`} />
                      <span className="text-sm font-medium">{cat.name}</span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="text-sm text-muted-foreground">{cat.percent}%</span>
                      <span className="text-sm font-semibold">R$ {cat.value.toLocaleString("pt-BR")}</span>
                    </div>
                  </div>
                ))}
              </div>

              <div className="pt-4 border-t border-border">
                <div className="flex items-center justify-between">
                  <span className="text-sm font-semibold">Total</span>
                  <span className="text-lg font-bold">
                    R$ {categoryData.reduce((acc, cat) => acc + cat.value, 0).toLocaleString("pt-BR")}
                  </span>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Preview Notice */}
        <Card className="border-blue-500/50 bg-blue-500/5">
          <CardContent className="pt-6">
            <p className="text-sm text-center text-muted-foreground">
              📊 Preview do sistema com dados de exemplo. Na versão final, os dados serão calculados automaticamente a
              partir das suas transações reais.
            </p>
          </CardContent>
        </Card>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
