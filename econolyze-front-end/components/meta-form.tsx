"use client"

import type React from "react"
import { useState } from "react"
import { useRouter } from "next/navigation"
import { toast } from "sonner"

import { CurrencyFormField, FormGrid, SelectFormField, TextFormField } from "@/components/form-fields"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { useAuth } from "@/context/auth.context"
import { GOAL_STATUSES, GOAL_TYPES } from "@/lib/constants/goal-constants"
import { GoalService } from "@/lib/services/goal.service"
import type { GoalStatus, GoalType } from "@/lib/types/account.types"

export function MetaForm() {
  const router = useRouter()
  const { accessToken } = useAuth()

  const [name, setName] = useState("")
  const [amount, setAmount] = useState("")
  const [description, setDescription] = useState("")
  const [type, setType] = useState<GoalType | "">("")
  const [status, setStatus] = useState<GoalStatus>("ACTIVE")
  const [submitting, setSubmitting] = useState(false)

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault()

    if (!accessToken) return toast.error("Você precisa estar autenticado.")
    if (!type) return toast.error("Selecione o tipo da meta.")

    try {
      setSubmitting(true)
      await GoalService.create({
        name,
        amount: (Number.parseInt(amount) || 0) / 100,
        description,
        type,
        status,
      }, accessToken)

      toast.success("Meta criada com sucesso!")
      router.replace("/metas")
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao criar meta")
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <Card className="w-full">
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="form-shell">
          <TextFormField
            label="Nome da Meta"
            placeholder="Ex: Reserva de emergência"
            value={name}
            onChange={(event) => setName(event.target.value)}
            required
          />

          <CurrencyFormField
            label="Valor Alvo"
            value={amount}
            onChange={setAmount}
            required
          />

          <TextFormField
            label="Descrição"
            placeholder="Ex: Guardar dinheiro para seis meses de despesas"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            required
          />

          <FormGrid>
            <SelectFormField
              label="Tipo"
              value={type}
              onValueChange={(value) => setType(value as GoalType)}
              placeholder="Selecione o tipo"
              options={GOAL_TYPES}
              required
            />

            <SelectFormField
              label="Status"
              value={status}
              onValueChange={(value) => setStatus(value as GoalStatus)}
              placeholder="Selecione o status"
              options={GOAL_STATUSES}
              required
            />
          </FormGrid>

          <Button type="submit" className="w-full" size="lg" disabled={submitting}>
            {submitting ? "Criando..." : "Criar Meta"}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
