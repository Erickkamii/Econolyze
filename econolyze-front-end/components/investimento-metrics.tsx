import { Card, CardContent } from "@/components/ui/card"
import { TrendingUp, Percent, DollarSign } from "lucide-react"

const metricas = [
  {
    label: "Rendimento Total",
    valor: "R$ 2.847,32",
    icon: DollarSign,
    cor: "text-success",
  },
  {
    label: "Rentabilidade Anualizada",
    valor: "13.2%",
    icon: Percent,
    cor: "text-primary",
  },
  {
    label: "Diferença vs CDI",
    valor: "+1.8%",
    icon: TrendingUp,
    cor: "text-accent",
  },
]

export function InvestimentoMetrics() {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
      {metricas.map((metrica, index) => {
        const Icon = metrica.icon

        return (
          <Card key={index}>
            <CardContent className="pt-6">
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-sm text-muted-foreground mb-2">{metrica.label}</p>
                  <p className={`text-2xl font-bold ${metrica.cor}`}>{metrica.valor}</p>
                </div>
                <div className={`h-10 w-10 rounded-full bg-muted flex items-center justify-center`}>
                  <Icon className={`h-5 w-5 ${metrica.cor}`} />
                </div>
              </div>
            </CardContent>
          </Card>
        )
      })}
    </div>
  )
}
