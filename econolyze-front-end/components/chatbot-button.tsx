"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { MessageCircle, X, Send } from "lucide-react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"

export function ChatbotButton() {
  const [isOpen, setIsOpen] = useState(false)
  const [message, setMessage] = useState("")

  return (
    <>
      {isOpen && (
        <Card className="fixed bottom-24 right-6 w-80 max-w-[calc(100vw-3rem)] shadow-lg z-50">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3 border-b border-border">
            <CardTitle className="text-base">Assistente Econolyze</CardTitle>
            <div className="flex items-center gap-2">
              <span className="text-xs text-muted-foreground bg-muted px-2 py-1 rounded">WIP</span>
              <Button variant="ghost" size="icon" className="h-6 w-6" onClick={() => setIsOpen(false)}>
                <X className="h-4 w-4" />
              </Button>
            </div>
          </CardHeader>
          <CardContent className="p-0">
            <div className="h-64 overflow-y-auto p-4 space-y-3">
              <div className="bg-secondary p-3 rounded-lg text-sm">
                <p className="text-foreground">Olá! 👋 Como posso ajudar você hoje?</p>
              </div>
              <div className="text-xs text-muted-foreground pl-3">
                Exemplos:
                <ul className="list-disc list-inside mt-1 space-y-1">
                  <li>Qual foi meu gasto total este mês?</li>
                  <li>Mostrar investimentos</li>
                  <li>Criar transação recorrente</li>
                </ul>
              </div>
            </div>
            <div className="p-4 border-t border-border">
              <div className="flex gap-2">
                <Input
                  placeholder="Digite sua mensagem..."
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  className="bg-secondary"
                />
                <Button size="icon" className="shrink-0">
                  <Send className="h-4 w-4" />
                </Button>
              </div>
            </div>
          </CardContent>
        </Card>
      )}

      <Button
        size="icon"
        className="fixed bottom-6 right-6 h-14 w-14 rounded-full shadow-lg z-40"
        onClick={() => setIsOpen(!isOpen)}
      >
        <MessageCircle className="h-6 w-6" />
      </Button>
    </>
  )
}
