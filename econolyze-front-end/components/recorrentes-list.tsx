"use client"

import { useState } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { Switch } from "@/components/ui/switch"
import { Home, Wifi, Phone, Zap, Music, MoreVertical } from "lucide-react"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Button } from "@/components/ui/button"
import Link from "next/link"

const recorrentes = [
  {
    id: 1,
    nome: "Aluguel",
    frequencia: "Mensal",
    proximoVencimento: "05/12/2025",
    endDate: "05/12/2026",
    valor: 1200.0,
    ativa: true,
    icon: Home,
  },
  {
    id: 2,
    nome: "Internet",
    frequencia: "Mensal",
    proximoVencimento: "10/12/2025",
    endDate: null,
    valor: 99.9,
    ativa: true,
    icon: Wifi,
  },
  {
    id: 3,
    nome: "Celular",
    frequencia: "Mensal",
    proximoVencimento: "15/12/2025",
    endDate: "15/06/2026",
    valor: 79.9,
    ativa: true,
    icon: Phone,
  },
  {
    id: 4,
    nome: "Energia",
    frequencia: "Mensal",
    proximoVencimento: "20/12/2025",
    endDate: null,
    valor: 185.5,
    ativa: true,
    icon: Zap,
  },
  {
    id: 5,
    nome: "Spotify",
    frequencia: "Mensal",
    proximoVencimento: "12/12/2025",
    endDate: null,
    valor: 21.9,
    ativa: false,
    icon: Music,
  },
]

export function RecorrentesList() {
  const [items, setItems] = useState(recorrentes)

  const toggleAtiva = (id: number) => {
    setItems(items.map((item) => (item.id === id ? { ...item, ativa: !item.ativa } : item)))
  }

  const handleDelete = (id: number) => {
    setItems(items.filter((item) => item.id !== id))
  }

  return (
    <div className="space-y-3">
      {items.map((item) => {
        const Icon = item.icon

        return (
          <Card key={item.id} className={item.ativa ? "" : "opacity-60"}>
            <CardContent className="p-4">
              <div className="flex items-center gap-4">
                <Link
                  href={`/recorrentes/${item.id}`}
                  className="flex-1 flex items-center gap-4 cursor-pointer hover:opacity-80 transition-opacity"
                >
                  <div className="h-12 w-12 rounded-full bg-primary/20 flex items-center justify-center">
                    <Icon className="h-6 w-6 text-primary" />
                  </div>

                  <div className="flex-1 min-w-0">
                    <p className="font-medium">{item.nome}</p>
                    <div className="flex items-center gap-3 text-sm text-muted-foreground mt-1">
                      <span>{item.frequencia}</span>
                      <span>•</span>
                      <span>Próximo: {item.proximoVencimento}</span>
                      {item.endDate && (
                        <>
                          <span>•</span>
                          <span>Última: {item.endDate}</span>
                        </>
                      )}
                    </div>
                  </div>
                </Link>

                <div className="flex items-center gap-2">
                  <span className="text-xs text-muted-foreground">{item.ativa ? "Ativa" : "Inativa"}</span>
                  <Switch checked={item.ativa} onCheckedChange={() => toggleAtiva(item.id)} />
                </div>

                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon" className="h-8 w-8">
                      <MoreVertical className="h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end">
                    <DropdownMenuItem asChild>
                      <Link href={`/recorrentes/${item.id}/editar`}>Editar</Link>
                    </DropdownMenuItem>
                    <DropdownMenuItem className="text-destructive" onClick={() => handleDelete(item.id)}>
                      Excluir
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </div>
            </CardContent>
          </Card>
        )
      })}
    </div>
  )
}
