"use client"

import React, { useState, useEffect } from "react"
import { useRouter } from "next/navigation"
import { toast } from "sonner"

import { Button } from "@/components/ui/button"
import { CurrencyInput } from "@/components/currency-input"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"

import { useAuth } from "@/context/auth.context"
import { RecurringService } from "@/lib/services/recurring.service"
import type { RecurringTemplate } from "@/lib/types/recurring.types"

const CATEGORIES = [
  { value: "HOUSING", label: "Moradia" },
  { value: "TRANSPORT", label: "Transporte" },
  { value: "FOOD", label: "Alimentação" },
  { value: "LEISURE", label: "Lazer" },
  { value: "HEALTH", label: "Saúde" },
  { value: "EDUCATION", label: "Educação" },
  { value: "OTHER", label: "Outros" },
]

const PAYMENT_METHODS = [
  { value: "PIX", label: "Pix" },
  { value: "CREDIT_CARD", label: "Cartão de Crédito" },
  { value: "DEBIT_CARD", label: "Cartão de Débito" },
  { value: "BANK_TRANSFER", label: "Transferência" },
  { value: "CASH", label: "Dinheiro" },
]

interface Props {
  isEdit?: boolean
  templateId?: number
}

export function RecorrenciaForm({ isEdit = false, templateId }: Props) {
  const router = useRouter()
  const { accessToken, isLoading: authLoading } = useAuth()

  const [loading, setLoading] = useState(isEdit)
  const [submitting, setSubmitting] = useState(false)

  const [tipo, setTipo] = useState<"INCOME" | "EXPENSE">("EXPENSE")
  const [valor, setValor] = useState("0")
  const [descricao, setDescricao] = useState("")
  const [categoria, setCategoria] = useState("")
  const [metodo, setMetodo] = useState("")
  const [frequencia, setFrequencia] = useState("")
  const [dataInicio, setDataInicio] = useState("")
  const [dataFim, setDataFim] = useState("")
  const [maxOcorrencias, setMaxOcorrencias] = useState("")

  // Carrega dados existentes no modo edição
  useEffect(() => {
    if (authLoading || !isEdit || !templateId || !accessToken) return

    async function load() {
      try {
        const data: RecurringTemplate = await RecurringService.getById(templateId!, accessToken)
        setValor(String(Math.round(Number(data.amount) * 100)))
        setDescricao(data.description)
        setCategoria(data.category)
        setMetodo(data.method)
        setDataFim(data.endDate ?? "")
        setMaxOcorrencias(data.maxOccurrences ? String(data.maxOccurrences) : "")
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar recorrência.")
      } finally {
        setLoading(false)
      }
    }

    load()
  }, [isEdit, templateId, accessToken, authLoading])

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!accessToken) return toast.error("Você precisa estar autenticado.")

    const amount = (Number.parseInt(valor) || 0) / 100
    setSubmitting(true)

    try {
      if (isEdit && templateId) {
        await RecurringService.update(templateId, {
          id: templateId,
          amount,
          category: categoria,
          method: metodo,
          description: descricao,
          endDate: dataFim || null,
          maxOccurrences: maxOcorrencias ? Number(maxOcorrencias) : null,
        }, accessToken)

        toast.success("Recorrência atualizada!")
        router.push(`/recorrentes/${templateId}`)
      } else {
        await RecurringService.create({
          amount,
          type: tipo,
          category: categoria,
          paymentMethod: metodo,
          description: descricao,
          frequency: frequencia as any,
          startDate: dataInicio,
          endDate: dataFim || null,
          maxOccurrences: maxOcorrencias ? Number(maxOcorrencias) : null,
        }, accessToken)

        toast.success("Recorrência criada!")
        router.push("/recorrentes")
      }
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao salvar recorrência.")
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return null

  return (
      <Card>
        <CardContent className="p-6">
          <form onSubmit={handleSubmit} className="space-y-6">

            {/* Tipo — só na criação */}
            {!isEdit && (
                <div className="flex gap-2">
                  <Button
                      type="button"
                      variant={tipo === "INCOME" ? "default" : "outline"}
                      className="flex-1"
                      onClick={() => setTipo("INCOME")}
                  >
                    Receita
                  </Button>
                  <Button
                      type="button"
                      variant={tipo === "EXPENSE" ? "default" : "outline"}
                      className="flex-1"
                      onClick={() => setTipo("EXPENSE")}
                  >
                    Gasto
                  </Button>
                </div>
            )}

            {/* Descrição */}
            <div className="space-y-2">
              <Label>Descrição</Label>
              <Input
                  placeholder="Ex: Aluguel, Netflix, Salário..."
                  value={descricao}
                  onChange={(e) => setDescricao(e.target.value)}
                  className="bg-secondary"
                  required
              />
            </div>

            {/* Valor */}
            <div className="space-y-2">
              <Label>Valor</Label>
              <CurrencyInput value={valor} onChange={setValor} required className="w-full" />
            </div>

            {/* Categoria */}
            <div className="space-y-2">
              <Label>Categoria</Label>
              <Select value={categoria} onValueChange={setCategoria} required>
                <SelectTrigger className="bg-secondary">
                  <SelectValue placeholder="Selecione uma categoria" />
                </SelectTrigger>
                <SelectContent>
                  {CATEGORIES.map((c) => (
                      <SelectItem key={c.value} value={c.value}>{c.label}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Método */}
            <div className="space-y-2">
              <Label>Método de Pagamento</Label>
              <Select value={metodo} onValueChange={setMetodo} required>
                <SelectTrigger className="bg-secondary">
                  <SelectValue placeholder="Selecione o método" />
                </SelectTrigger>
                <SelectContent>
                  {PAYMENT_METHODS.map((m) => (
                      <SelectItem key={m.value} value={m.value}>{m.label}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {/* Frequência e Data Início — só na criação */}
            {!isEdit && (
                <>
                  <div className="space-y-2">
                    <Label>Frequência</Label>
                    <Select value={frequencia} onValueChange={setFrequencia} required>
                      <SelectTrigger className="bg-secondary">
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

                  <div className="space-y-2">
                    <Label>Data de Início</Label>
                    <Input
                        type="date"
                        value={dataInicio}
                        onChange={(e) => setDataInicio(e.target.value)}
                        className="bg-secondary"
                        required
                    />
                  </div>
                </>
            )}

            {/* Data de Fim */}
            <div className="space-y-2">
              <Label>Data de Fim <span className="text-muted-foreground text-xs">(opcional)</span></Label>
              <Input
                  type="date"
                  value={dataFim}
                  onChange={(e) => setDataFim(e.target.value)}
                  className="bg-secondary"
              />
            </div>

            {/* Máximo de Ocorrências */}
            <div className="space-y-2">
              <Label>Máximo de Ocorrências <span className="text-muted-foreground text-xs">(opcional)</span></Label>
              <Input
                  type="number"
                  min="1"
                  placeholder="Ex: 12"
                  value={maxOcorrencias}
                  onChange={(e) => setMaxOcorrencias(e.target.value)}
                  className="bg-secondary"
              />
            </div>

            <Button type="submit" className="w-full" size="lg" disabled={submitting}>
              {submitting
                  ? "Salvando..."
                  : isEdit
                      ? "Salvar Alterações"
                      : "Criar Recorrência"}
            </Button>
          </form>
        </CardContent>
      </Card>
  )
}