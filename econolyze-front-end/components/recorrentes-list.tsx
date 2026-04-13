"use client"

import { useEffect, useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Switch } from "@/components/ui/switch"
import { MoreVertical, Repeat } from "lucide-react"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Button } from "@/components/ui/button"
import Link from "next/link"
import { toast } from "sonner"

import { useAuth } from "@/context/auth.context"
import { RecurringService } from "@/lib/services/recurring.service"
import type { RecurringTemplate } from "@/lib/types/recurring.types"

const frequencyLabels: Record<string, string> = {
  DAILY: "Diário",
  WEEKLY: "Semanal",
  MONTHLY: "Mensal",
  YEARLY: "Anual",
}

export function RecorrentesList() {
  const { accessToken, isLoading: authLoading } = useAuth()
  const [items, setItems] = useState<RecurringTemplate[]>([])
  const [loading, setLoading] = useState(true)
  const [togglingId, setTogglingId] = useState<number | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)

  useEffect(() => {
    if (authLoading || !accessToken) return

    async function load() {
      try {
        const data = await RecurringService.getAll(accessToken)
        setItems(data)
      } catch (error: any) {
        toast.error(error.message ?? "Erro ao carregar recorrências.")
      } finally {
        setLoading(false)
      }
    }

    load()
  }, [accessToken, authLoading])

  async function handleToggle(id: number) {
    setTogglingId(id)
    try {
      const updated = await RecurringService.toggle(id, accessToken)
      setItems((prev) =>
          prev.map((item) => (item.id === id ? { ...item, isActive: updated.isActive } : item))
      )
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao alternar recorrência.")
    } finally {
      setTogglingId(null)
    }
  }

  async function handleDelete(id: number) {
    setDeletingId(id)
    try {
      await RecurringService.delete(id, accessToken)
      setItems((prev) => prev.filter((item) => item.id !== id))
      toast.success("Recorrência excluída.")
    } catch (error: any) {
      toast.error(error.message ?? "Erro ao excluir recorrência.")
    } finally {
      setDeletingId(null)
    }
  }

  if (loading) return null

  if (items.length === 0) {
    return (
        <p className="text-center text-muted-foreground py-12">
          Nenhuma recorrência cadastrada.
        </p>
    )
  }

  return (
      <div className="space-y-3">
        {items.map((item) => (
            <Card key={item.id} className={item.isActive ? "" : "opacity-60"}>
              <CardContent className="p-4">
                <div className="flex items-center gap-4">
                  <Link
                      href={`/recorrentes/${item.id}`}
                      className="flex-1 flex items-center gap-4 cursor-pointer hover:opacity-80 transition-opacity"
                  >
                    <div className="h-12 w-12 rounded-full bg-primary/20 flex items-center justify-center shrink-0">
                      <Repeat className="h-6 w-6 text-primary" />
                    </div>

                    <div className="flex-1 min-w-0">
                      <p className="font-medium truncate">{item.description}</p>
                      <div className="flex flex-wrap items-center gap-2 text-sm text-muted-foreground mt-1">
                        <span>{frequencyLabels[item.frequency] ?? item.frequency}</span>
                        <span>•</span>
                        <span>
                                            R${" "}
                          {item.amount.toLocaleString("pt-BR", {
                            minimumFractionDigits: 2,
                          })}
                                        </span>
                        {item.nextOccurrence && (
                            <>
                              <span>•</span>
                              <span>
                                                    Próximo:{" "}
                                {new Date(item.nextOccurrence).toLocaleDateString("pt-BR")}
                                                </span>
                            </>
                        )}
                        {item.endDate && (
                            <>
                              <span>•</span>
                              <span>
                                                    Até:{" "}
                                {new Date(item.endDate).toLocaleDateString("pt-BR")}
                                                </span>
                            </>
                        )}
                      </div>
                    </div>
                  </Link>

                  <div className="flex items-center gap-2 shrink-0">
                                <span className="text-xs text-muted-foreground hidden sm:block">
                                    {item.isActive ? "Ativa" : "Inativa"}
                                </span>
                    <Switch
                        checked={item.isActive}
                        disabled={togglingId === item.id}
                        onCheckedChange={() => handleToggle(item.id)}
                    />
                  </div>

                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon" className="h-8 w-8 shrink-0">
                        <MoreVertical className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem asChild>
                        <Link href={`/recorrentes/${item.id}/editar`}>Editar</Link>
                      </DropdownMenuItem>
                      <DropdownMenuItem
                          className="text-destructive"
                          disabled={deletingId === item.id}
                          onClick={() => handleDelete(item.id)}
                      >
                        {deletingId === item.id ? "Excluindo..." : "Excluir"}
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </div>
              </CardContent>
            </Card>
        ))}
      </div>
  )
}