import { apiRequest } from "@/lib/services/api-client"
import type { CreateGoalPayload, Goal, GoalProgress } from "@/lib/types/account.types"

export class GoalService {
  private static readonly GOAL_PATH = "/goal"

  private static ensureToken(accessToken: string | null) {
    if (!accessToken) {
      throw new Error("Access token is required")
    }
  }

  static create(payload: CreateGoalPayload, accessToken: string | null) {
    this.ensureToken(accessToken)

    return apiRequest<Goal>(
      `${this.GOAL_PATH}/create`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      },
      "Erro ao criar meta",
    )
  }

  static getAll(accessToken: string | null) {
    this.ensureToken(accessToken)

    return apiRequest<Goal[]>(
      `${this.GOAL_PATH}`,
      {},
      "Erro ao buscar metas",
    )
  }

  static getById(id: number, accessToken: string | null) {
    this.ensureToken(accessToken)

    return apiRequest<Goal>(
      `${this.GOAL_PATH}/${id}`,
      {},
      "Erro ao buscar meta",
    )
  }

  static getProgress(id: number, accessToken: string | null) {
    this.ensureToken(accessToken)

    return apiRequest<GoalProgress>(
      `${this.GOAL_PATH}/progress/${id}`,
      {},
      "Erro ao buscar progresso da meta",
    )
  }
}
