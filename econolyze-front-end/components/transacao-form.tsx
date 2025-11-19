"use client"

import type React from "react"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { CurrencyInput } from "@/components/currency-input"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Switch } from "@/components/ui/switch"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

export function TransacaoForm({ tipoInicial }: { tipoInicial?: "receita" | "gasto" }) {
  const [tipo, setTipo] = useState<"receita" | "gasto">(tipoInicial || "gasto")
  const [valor, setValor] = useState("")
  const [descricao, setDescricao] = useState("")
  const [categoria, setCategoria] = useState("")
  const [conta, setConta] = useState("")
  const [recorrente, setRecorrente] = useState(false)
  const [pagamentoParcial, setPagamentoParcial] = useState(false)
  const [valorPago, setValorPago] = useState("")

  useEffect(() => {
    if (tipoInicial) {
      setTipo(tipoInicial)
    }
  }, [tipoInicial])

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const valorReais = (Number.parseInt(valor) || 0) / 100
    const valorPagoReais = (Number.parseInt(valorPago) || 0) / 100
    console.log({
      tipo,
      valor: valorReais,
      descricao,
      categoria,
      conta,
      recorrente,
      pagamentoParcial,
      valorPago: valorPagoReais,
    })
  }

  return (
    <Card>
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="flex gap-2">
            <Button
              type="button"
              variant={tipo === "receita" ? "default" : "outline"}
              className="flex-1"
              onClick={() => setTipo("receita")}
            >
              Receita
            </Button>
            <Button
              type="button"
              variant={tipo === "gasto" ? "default" : "outline"}
              className="flex-1"
              onClick={() => setTipo("gasto")}
            >
              Gasto
            </Button>
          </div>

          <div className="space-y-2">
            <Label htmlFor="valor">Valor Total</Label>
            <CurrencyInput id="valor" value={valor} onChange={setValor} required />
          </div>

          <div className="space-y-2">
            <Label htmlFor="descricao">Descrição</Label>
            <Input
              id="descricao"
              type="text"
              placeholder="Ex: Almoço no restaurante"
              value={descricao}
              onChange={(e) => setDescricao(e.target.value)}
              className="bg-secondary"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="categoria">Categoria</Label>
            <Select value={categoria} onValueChange={setCategoria}>
              <SelectTrigger className="bg-secondary">
                <SelectValue placeholder="Selecione uma categoria" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="alimentacao">🍽️ Alimentação</SelectItem>
                <SelectItem value="transporte">🚗 Transporte</SelectItem>
                <SelectItem value="lazer">🎮 Lazer</SelectItem>
                <SelectItem value="compras">🛍️ Compras</SelectItem>
                <SelectItem value="contas">💡 Contas</SelectItem>
                <SelectItem value="salario">💼 Salário</SelectItem>
                <SelectItem value="freelance">💻 Freelance</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="conta">Conta/Origem</Label>
            <Select value={conta} onValueChange={setConta}>
              <SelectTrigger className="bg-secondary">
                <SelectValue placeholder="Selecione uma conta" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="nubank">Nubank - R$ 3.250,50</SelectItem>
                <SelectItem value="inter">Inter - R$ 1.890,00</SelectItem>
                <SelectItem value="carteira">Carteira Física - R$ 250,00</SelectItem>
                <SelectItem value="poupanca">Poupança BB - R$ 10.456,82</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-4 pt-2">
            <div className="flex items-center justify-between">
              <Label htmlFor="recorrente" className="cursor-pointer">
                Tornar Recorrente
              </Label>
              <Switch id="recorrente" checked={recorrente} onCheckedChange={setRecorrente} />
            </div>

            <div className="flex items-center justify-between">
              <div>
                <Label htmlFor="pagamentoParcial" className="cursor-pointer">
                  {tipo === "receita" ? "Recebimento Parcial" : "Pagamento Parcial"}
                </Label>
                <p className="text-xs text-muted-foreground">
                  {tipo === "receita"
                    ? "Registrar apenas parte do recebimento agora"
                    : "Registrar apenas parte do pagamento agora"}
                </p>
              </div>
              <Switch id="pagamentoParcial" checked={pagamentoParcial} onCheckedChange={setPagamentoParcial} />
            </div>

            {pagamentoParcial && (
              <div className="space-y-2 pl-4 border-l-2 border-primary/30">
                <Label htmlFor="valorPago">{tipo === "receita" ? "Valor Recebido Agora" : "Valor Pago Agora"}</Label>
                <CurrencyInput
                  id="valorPago"
                  value={valorPago}
                  onChange={setValorPago}
                  max={(Number.parseInt(valor) || 0) / 100}
                  required
                />
                <p className="text-xs text-muted-foreground">
                  O restante ficará em aberto para futuros {tipo === "receita" ? "recebimentos" : "pagamentos"}
                </p>
              </div>
            )}
          </div>

          <Button type="submit" className="w-full" size="lg">
            Salvar Transação
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
