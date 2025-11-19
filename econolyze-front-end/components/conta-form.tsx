"use client"

import type React from "react"
import { useState } from "react"
import { Button } from "@/components/ui/button"
import { CurrencyInput } from "@/components/currency-input"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

export function ContaForm() {
  const [nome, setNome] = useState("")
  const [tipo, setTipo] = useState("")
  const [saldoInicial, setSaldoInicial] = useState("")

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const saldoReais = (Number.parseInt(saldoInicial) || 0) / 100
    console.log({ nome, tipo, saldoInicial: saldoReais })
  }

  return (
    <Card>
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="nome">Nome da Conta</Label>
            <Input
              id="nome"
              type="text"
              placeholder="Ex: Nubank, Inter, Carteira Física"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className="bg-secondary"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="tipo">Tipo de Conta</Label>
            <Select value={tipo} onValueChange={setTipo}>
              <SelectTrigger className="bg-secondary">
                <SelectValue placeholder="Selecione o tipo" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="corrente">Conta Corrente</SelectItem>
                <SelectItem value="poupanca">Poupança</SelectItem>
                <SelectItem value="cartao">Cartão de Crédito</SelectItem>
                <SelectItem value="dinheiro">Dinheiro</SelectItem>
                <SelectItem value="investimento">Investimento</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="saldo">Saldo Inicial</Label>
            <CurrencyInput id="saldo" value={saldoInicial} onChange={setSaldoInicial} required />
            <p className="text-xs text-muted-foreground">Informe o saldo atual desta conta</p>
          </div>

          <Button type="submit" className="w-full" size="lg">
            Criar Conta
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
