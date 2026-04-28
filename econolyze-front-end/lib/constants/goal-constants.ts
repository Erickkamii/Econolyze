import type { GoalStatus, GoalType } from "@/lib/types/account.types"

export const GOAL_TYPES: readonly { value: GoalType; label: string }[] = [
  { value: "VACATION", label: "Férias" },
  { value: "TRAVEL", label: "Viagem" },
  { value: "INVESTMENT", label: "Investimento" },
  { value: "SAVING", label: "Reserva" },
  { value: "OTHER", label: "Outra" },
]

export const GOAL_STATUSES: readonly { value: GoalStatus; label: string }[] = [
  { value: "ACTIVE", label: "Ativa" },
  { value: "DRAFT", label: "Rascunho" },
  { value: "PAUSED", label: "Pausada" },
  { value: "COMPLETED", label: "Concluída" },
  { value: "OVERDUE", label: "Atrasada" },
  { value: "CANCELLED", label: "Cancelada" },
]
