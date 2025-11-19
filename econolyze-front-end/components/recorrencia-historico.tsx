"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Home } from "lucide-react"

interface RecorrenciaHistoricoProps {
  recorrenciaId: string
}

const transacoesHistorico = [
  {
    id: 1,
    data: "01/11/2025",
    valor: 1200.0,
    status: "Pago",
  },
  {
    id: 2,
    data: "01/10/2025",
    valor: 1200.0,
    status: "Pago",
  },
  {
    id: 3,
    data: "01/09/2025",
    valor: 1200.0,
    status: "Pago",
  },
  {
    id: 4,
    data: "01/08/2025",
    valor: 1200.0,
    status: "Pago",
  },
  {
    id: 5,
    data: "01/12/2025",
    valor: 1200.0,
    status: "Pendente",
  },
]

export function RecorrenciaHistorico({ recorrenciaId }: RecorrenciaHistoricoProps) {
  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center gap-4">
            <div className="h-14 w-14 rounded-full bg-primary/20 flex items-center justify-center">
              <Home className="h-7 w-7 text-primary" />
            </div>
            <div>
              <CardTitle>Aluguel</CardTitle>
              <p className="text-sm text-muted-foreground mt-1">Recorrência Mensal • R$ 1.200,00</p>
            </div>
          </div>
        </CardHeader>
      </Card>

      <div className="space-y-3">
        <h2 className="text-lg font-semibold">Histórico de Transações</h2>
        {transacoesHistorico.map((transacao) => (
          <Card key={transacao.id}>
            <CardContent className="p-4">
              <div className="flex items-center justify-between">
                <div>
                  <p className="font-medium">{transacao.data}</p>
                  <p className={`text-sm ${transacao.status === "Pago" ? "text-green-500" : "text-yellow-500"}`}>
                    {transacao.status}
                  </p>
                </div>
                <p className="font-semibold text-lg">
                  R$ {transacao.valor.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                </p>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}
