import { Card, CardContent } from "@/components/ui/card"
import { Wallet } from "lucide-react"

interface SaldoCardProps {
  saldo?: number
}

export function SaldoCard({ saldo = 15847.32 }: SaldoCardProps) {
  return (
    <Card className="bg-card/50 border-border backdrop-blur-sm">
      <CardContent className="pt-6">
        <div className="flex items-start justify-between">
          <div>
            <p className="text-sm text-muted-foreground mb-2">Saldo da Carteira</p>
            <p className="text-4xl font-bold text-foreground">
              R$ {saldo.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </p>
          </div>
          <div className="h-12 w-12 rounded-lg bg-primary/10 flex items-center justify-center">
            <Wallet className="h-6 w-6 text-primary" />
          </div>
        </div>
      </CardContent>
    </Card>
  )
}
