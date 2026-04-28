"use client"

import type React from "react"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { toast } from "sonner"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { CurrencyFormField, SelectFormField, TextFormField } from "@/components/form-fields"
import { useAuth } from "@/context/auth.context"
import { AccountService } from "@/lib/services/account.service"
import { PaymentService } from "@/lib/services/payment.service"
import type { Account } from "@/lib/types/account.types"

interface PagamentoFormProps {
  transacaoId: number
  valorRestante: number
}

export function PagamentoForm({ transacaoId, valorRestante }: PagamentoFormProps) {
  const router = useRouter()
  const { accessToken } = useAuth()
  const [conta, setConta] = useState("")
  const [tipoPagamento, setTipoPagamento] = useState<"total" | "parcial">("total")
  const [valorParcial, setValorParcial] = useState("")
  const [data, setData] = useState("")
  const [accounts, setAccounts] = useState<Account[]>([])
  const [loadingAccounts, setLoadingAccounts] = useState(true)
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    if (!accessToken) return

    async function loadAccounts() {
      setLoadingAccounts(true)

      try {
        const data = await AccountService.getAll(accessToken)
        setAccounts(data)
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar contas")
      } finally {
        setLoadingAccounts(false)
      }
    }

    loadAccounts()
  }, [accessToken])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!accessToken) return toast.error("Você precisa estar autenticado.")
    if (!conta) return toast.error("Selecione a conta para pagamento.")
    if (!data) return toast.error("Informe a data do pagamento.")

    const valorPagamento = tipoPagamento === "total" ? valorRestante : (Number.parseInt(valorParcial) || 0) / 100
    if (valorPagamento <= 0) return toast.error("Informe um valor de pagamento válido.")

    const selectedAccount = accounts.find((account) => String(account.id) === conta)

    try {
      setSubmitting(true)
      await PaymentService.create({
        transactionId: transacaoId,
        amount: valorPagamento,
        method: selectedAccount?.type === "MONEY" ? "CASH" : "PIX",
        paidAt: data,
        status: "COMPLETED",
        accountId: Number(conta),
      }, accessToken)

      toast.success("Pagamento registrado!")
      router.refresh()
      router.replace("/pagamentos")
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao registrar pagamento")
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Card>
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="form-shell">
          <div className="space-y-3">
            <Label>Tipo de Pagamento</Label>
            <RadioGroup value={tipoPagamento} onValueChange={(value) => setTipoPagamento(value as "total" | "parcial")}>
              <div className="flex items-center space-x-2 p-4 border rounded-lg hover:bg-accent/50 cursor-pointer">
                <RadioGroupItem value="total" id="total" />
                <Label htmlFor="total" className="flex-1 cursor-pointer">
                  <span className="font-semibold">Pagamento Total</span>
                  <p className="text-sm text-muted-foreground">
                    R$ {valorRestante.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
                  </p>
                </Label>
              </div>

              <div className="flex items-center space-x-2 p-4 border rounded-lg hover:bg-accent/50 cursor-pointer">
                <RadioGroupItem value="parcial" id="parcial" />
                <Label htmlFor="parcial" className="flex-1 cursor-pointer">
                  <span className="font-semibold">Pagamento Parcial</span>
                  <p className="text-sm text-muted-foreground">Informar valor específico</p>
                </Label>
              </div>
            </RadioGroup>
          </div>

          {tipoPagamento === "parcial" && (
            <CurrencyFormField
                id="valorParcial"
                label="Valor a Pagar"
                value={valorParcial}
                onChange={setValorParcial}
                max={valorRestante}
                required
                hint={`Valor máximo: R$ ${valorRestante.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`}
            />
          )}

          <SelectFormField
            label="Conta para Pagamento"
            value={conta}
            onValueChange={setConta}
            placeholder={loadingAccounts ? "Carregando contas..." : "Selecione a conta"}
            disabled={loadingAccounts}
            options={
              accounts.length
                ? accounts.map((account) => ({
                  value: String(account.id),
                  label: `${account.name} - R$ ${account.actualBalance.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}`,
                }))
                : [{ value: "no-accounts", label: "Nenhuma conta disponível", disabled: true }]
            }
          />

          <TextFormField
              id="data"
              label="Data do Pagamento"
              type="date"
              value={data}
              onChange={(e) => setData(e.target.value)}
              required
          />

          <Button type="submit" className="w-full" size="lg" disabled={loadingAccounts || submitting}>
            {submitting ? "Registrando..." : "Confirmar Pagamento"}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
