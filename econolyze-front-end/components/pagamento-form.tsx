"use client"

import type React from "react"
import { useState } from "react"
import { Button } from "@/components/ui/button"
import { CurrencyInput } from "@/components/currency-input"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"

interface PagamentoFormProps {
  transacaoId: number
  valorRestante: number
}

export function PagamentoForm({ transacaoId, valorRestante }: PagamentoFormProps) {
  const [conta, setConta] = useState("")
  const [tipoPagamento, setTipoPagamento] = useState<"total" | "parcial">("total")
  const [valorParcial, setValorParcial] = useState("")
  const [data, setData] = useState("")

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const valorPagamento = tipoPagamento === "total" ? valorRestante : (Number.parseInt(valorParcial) || 0) / 100
    console.log({ transacaoId, conta, tipoPagamento, valorPagamento, data })
  }

  return (
    <Card>
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="space-y-6">
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
            <div className="space-y-2">
              <Label htmlFor="valorParcial">Valor a Pagar</Label>
              <CurrencyInput
                id="valorParcial"
                value={valorParcial}
                onChange={setValorParcial}
                max={valorRestante}
                required
              />
              <p className="text-xs text-muted-foreground">
                Valor máximo: R$ {valorRestante.toLocaleString("pt-BR", { minimumFractionDigits: 2 })}
              </p>
            </div>
          )}

          <div className="space-y-2">
            <Label htmlFor="conta">Conta para Pagamento</Label>
            <Select value={conta} onValueChange={setConta}>
              <SelectTrigger className="bg-secondary">
                <SelectValue placeholder="Selecione a conta" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="nubank">Nubank - R$ 3.250,50</SelectItem>
                <SelectItem value="inter">Inter - R$ 1.890,00</SelectItem>
                <SelectItem value="carteira">Carteira Física - R$ 250,00</SelectItem>
                <SelectItem value="poupanca">Poupança BB - R$ 10.456,82</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="data">Data do Pagamento</Label>
            <Input
              id="data"
              type="date"
              value={data}
              onChange={(e) => setData(e.target.value)}
              className="bg-secondary"
              required
            />
          </div>

          <Button type="submit" className="w-full" size="lg">
            Confirmar Pagamento
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
