"use client"

import type React from "react"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { CurrencyInput } from "@/components/currency-input"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface RecorrenciaFormProps {
  isEdit?: boolean
}

export function RecorrenciaForm({ isEdit = false }: RecorrenciaFormProps) {
  const [valor, setValor] = useState("0")
  const [tipo, setTipo] = useState<"receita" | "gasto">("gasto")

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("[v0] Form submitted")
  }

  return (
    <Card>
      <CardContent className="p-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          {!isEdit && (
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
          )}

          {/* Descrição */}
          <div className="space-y-2">
            <Label htmlFor="descricao">Descrição</Label>
            <Input id="descricao" placeholder="Ex: Aluguel, Netflix, Salário..." required />
          </div>

          {/* Valor */}
          <div className="space-y-2">
            <Label htmlFor="valor">Valor</Label>
            <CurrencyInput id="valor" value={valor} onChange={setValor} required />
          </div>

          {/* Categoria */}
          <div className="space-y-2">
            <Label htmlFor="categoria">Categoria</Label>
            <Select required>
              <SelectTrigger id="categoria">
                <SelectValue placeholder="Selecione uma categoria" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="moradia">Moradia</SelectItem>
                <SelectItem value="transporte">Transporte</SelectItem>
                <SelectItem value="alimentacao">Alimentação</SelectItem>
                <SelectItem value="lazer">Lazer</SelectItem>
                <SelectItem value="saude">Saúde</SelectItem>
                <SelectItem value="educacao">Educação</SelectItem>
                <SelectItem value="outros">Outros</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {!isEdit && (
            <>
              {/* Frequência */}
              <div className="space-y-2">
                <Label htmlFor="frequencia">Frequência</Label>
                <Select required>
                  <SelectTrigger id="frequencia">
                    <SelectValue placeholder="Selecione a frequência" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="DAILY">Diário</SelectItem>
                    <SelectItem value="WEEKLY">Semanal</SelectItem>
                    <SelectItem value="MONTHLY">Mensal</SelectItem>
                    <SelectItem value="YEARLY">Anual</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              {/* Data de Início */}
              <div className="space-y-2">
                <Label htmlFor="dataInicio">Data de Início</Label>
                <Input id="dataInicio" type="date" required />
              </div>
            </>
          )}

          {/* Data de Fim (Opcional) */}
          <div className="space-y-2">
            <Label htmlFor="dataFim">Data de Fim (opcional)</Label>
            <Input id="dataFim" type="date" />
          </div>

          {/* Máximo de Ocorrências (Opcional) */}
          <div className="space-y-2">
            <Label htmlFor="maxOcorrencias">Máximo de Ocorrências (opcional)</Label>
            <Input id="maxOcorrencias" type="number" min="1" placeholder="Ex: 12" />
          </div>

          <Button type="submit" className="w-full">
            {isEdit ? "Salvar Alterações" : "Criar Recorrência"}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
