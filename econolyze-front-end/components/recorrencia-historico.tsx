"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Repeat } from "lucide-react"
import { toast } from "sonner"

import { useAuth } from "@/context/auth.context"
import { RecurringService } from "@/lib/services/recurring.service"
import type { RecurringTemplate } from "@/lib/types/recurring.types"

const frequencyLabels: Record<string, string> = {
  DAILY: "Diário",
  WEEKLY: "Semanal",
  MONTHLY: "Mensal",
  YEARLY: "Anual",
}

interface Props {
  recorrenciaId: number
}

export function RecorrenciaHistorico({ recorrenciaId }: Props) {
  const { accessToken, isLoading: authLoading } = useAuth()
  const [template, setTemplate] = useState<RecurringTemplate | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (authLoading || !accessToken) return

    async function load() {
      try {
        const data = await RecurringService.getById(recorrenciaId, accessToken)
        setTemplate(data)
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar recorrência.")
      } finally {
        setLoading(false)
      }
    }

    load()
  }, [recorrenciaId, accessToken, authLoading])

  if (loading) return null
  if (!template) return null

  return (
      <div className="space-y-6">
        <Card>
          <CardHeader>
            <div className="flex items-center gap-4">
              <div className="h-14 w-14 rounded-full bg-primary/20 flex items-center justify-center shrink-0">
                <Repeat className="h-7 w-7 text-primary" />
              </div>
              <div>
                <CardTitle>{template.description}</CardTitle>
                <p className="text-sm text-muted-foreground mt-1">
                  {frequencyLabels[template.frequency] ?? template.frequency}
                  {" • "}
                  R${" "}
                  {Number(template.amount).toLocaleString("pt-BR", {
                    minimumFractionDigits: 2,
                  })}
                </p>
                <div className="flex gap-4 mt-2 text-xs text-muted-foreground">
                  <span>Processado {template.timesProcessed}x</span>
                  {template.nextOccurrence && (
                      <span>
                                        Próximo:{" "}
                        {new Date(template.nextOccurrence).toLocaleDateString("pt-BR")}
                                    </span>
                  )}
                  <span className={template.isActive ? "text-green-500" : "text-red-500"}>
                                    {template.isActive ? "Ativa" : "Inativa"}
                                </span>
                </div>
              </div>
            </div>
          </CardHeader>
        </Card>

        <div className="space-y-3">
          <h2 className="text-lg font-semibold">Histórico de Transações</h2>

          {template.transactions.length === 0 ? (
              <p className="text-muted-foreground text-sm py-4">
                Nenhuma transação gerada ainda.
              </p>
          ) : (
              template.transactions.map((t) => (
                  <Card key={t.id}>
                    <CardContent className="p-4">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="font-medium">
                            {new Date(t.date).toLocaleDateString("pt-BR")}
                          </p>
                          <p className={`text-sm ${t.status === "PAID" ? "text-green-500" : "text-yellow-500"}`}>
                            {t.status === "PAID" ? "Pago" : "Pendente"}
                          </p>
                        </div>
                        <p className="font-semibold text-lg">
                          R${" "}
                          {Number(t.amount).toLocaleString("pt-BR", {
                            minimumFractionDigits: 2,
                          })}
                        </p>
                      </div>
                    </CardContent>
                  </Card>
              ))
          )}
        </div>
      </div>
  )
}