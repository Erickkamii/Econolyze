"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { ArrowLeft, Plus, Target } from "lucide-react"
import { toast } from "sonner"

import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Skeleton } from "@/components/ui/skeleton"
import { ChatbotButton } from "@/components/chatbot-button"
import { MainNav } from "@/components/main-nav"
import { useAuth } from "@/context/auth.context"
import { GOAL_STATUSES, GOAL_TYPES } from "@/lib/constants/goal-constants"
import { GoalService } from "@/lib/services/goal.service"
import type { Goal, GoalProgress } from "@/lib/types/account.types"
import { formatCurrency } from "@/lib/utils/transaction-mappers"

function getGoalTypeLabel(value: Goal["type"]) {
  return GOAL_TYPES.find((type) => type.value === value)?.label ?? value
}

function getGoalStatusLabel(value: Goal["status"]) {
  return GOAL_STATUSES.find((status) => status.value === value)?.label ?? value
}

export default function MetasPage() {
  const { accessToken } = useAuth()
  const [goals, setGoals] = useState<Goal[]>([])
  const [progressByGoal, setProgressByGoal] = useState<Record<number, GoalProgress>>({})
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (!accessToken) return

    async function loadGoals() {
      setLoading(true)

      try {
        const data = await GoalService.getAll(accessToken)
        setGoals(data)

        const progressEntries = await Promise.allSettled(
          data.map((goal) => GoalService.getProgress(goal.id, accessToken)),
        )

        const nextProgress = progressEntries.reduce<Record<number, GoalProgress>>((acc, result) => {
          if (result.status === "fulfilled") {
            acc[result.value.id] = result.value
          }
          return acc
        }, {})

        setProgressByGoal(nextProgress)
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar metas")
      } finally {
        setLoading(false)
      }
    }

    loadGoals()
  }, [accessToken])

  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/carteira">
              <Button variant="ghost" size="icon">
                <ArrowLeft className="h-5 w-5" />
              </Button>
            </Link>
            <h1 className="text-2xl font-bold">Metas</h1>
          </div>
          <Link href="/metas/nova">
            <Button size="icon" className="rounded-full">
              <Plus className="h-5 w-5" />
            </Button>
          </Link>
        </div>

        <div className="space-y-3">
          {loading ? (
            [...Array(3)].map((_, index) => (
              <Skeleton key={index} className="h-32 w-full rounded-lg" />
            ))
          ) : goals.length === 0 ? (
            <Card>
              <CardContent className="pt-6 text-center text-muted-foreground">
                Nenhuma meta cadastrada
              </CardContent>
            </Card>
          ) : (
            goals.map((goal) => {
              const progress = Math.min(100, Math.max(0, progressByGoal[goal.id]?.progress ?? 0))

              return (
                <Card key={goal.id} className="hover:bg-accent/50 transition-colors">
                  <CardContent className="space-y-4 pt-6">
                    <div className="flex items-start gap-4">
                      <div className="rounded-full bg-primary/10 p-3 text-primary">
                        <Target className="h-5 w-5" />
                      </div>
                      <div className="min-w-0 flex-1">
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <p className="font-semibold">{goal.name}</p>
                            <p className="text-sm text-muted-foreground">
                              {getGoalTypeLabel(goal.type)} · {getGoalStatusLabel(goal.status)}
                            </p>
                          </div>
                          <p className="text-right font-semibold">R$ {formatCurrency(goal.amount)}</p>
                        </div>
                        {goal.description && (
                          <p className="mt-2 text-sm text-muted-foreground">{goal.description}</p>
                        )}
                      </div>
                    </div>

                    <div className="space-y-2">
                      <div className="flex justify-between text-sm">
                        <span className="text-muted-foreground">Progresso</span>
                        <span className="font-medium">{progress.toFixed(0)}%</span>
                      </div>
                      <div className="h-2 overflow-hidden rounded-full bg-secondary">
                        <div className="h-full rounded-full bg-primary transition-all" style={{ width: `${progress}%` }} />
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )
            })
          )}
        </div>
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
