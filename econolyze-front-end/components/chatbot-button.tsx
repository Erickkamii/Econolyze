"use client"

import type React from "react"
import { useEffect, useRef, useState } from "react"
import { MessageCircle, Send, X } from "lucide-react"
import { toast } from "sonner"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ChatService } from "@/lib/services/chat.service"
import { cn } from "@/lib/utils"

type ChatMessage = {
  id: string
  role: "user" | "assistant"
  content: string
}

const TEXTAREA_LINE_HEIGHT = 20
const TEXTAREA_VERTICAL_PADDING = 16
const TEXTAREA_MAX_HEIGHT = TEXTAREA_LINE_HEIGHT * 3 + TEXTAREA_VERTICAL_PADDING

const TOOL_CALL_PATTERN = /<function=[^>]*>[\s\S]*?<\/function>/g

function createId() {
  return `${Date.now()}-${Math.random().toString(16).slice(2)}`
}

function removeToolCalls(value: string) {
  return value.replace(TOOL_CALL_PATTERN, "")
}

export function ChatbotButton() {
  const [isOpen, setIsOpen] = useState(false)
  const [message, setMessage] = useState("")
  const [messages, setMessages] = useState<ChatMessage[]>([
    {
      id: "welcome",
      role: "assistant",
      content: "Olá! Como posso ajudar você hoje?",
    },
  ])
  const [isStreaming, setIsStreaming] = useState(false)
  const abortRef = useRef<AbortController | null>(null)
  const messagesEndRef = useRef<HTMLDivElement | null>(null)
  const textareaRef = useRef<HTMLTextAreaElement | null>(null)

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }, [messages, isOpen])

  useEffect(() => {
    const textarea = textareaRef.current
    if (!textarea) return

    textarea.style.height = "auto"
    textarea.style.height = `${Math.min(textarea.scrollHeight, TEXTAREA_MAX_HEIGHT)}px`
  }, [message])

  useEffect(() => {
    return () => {
      abortRef.current?.abort()
    }
  }, [])

  async function handleSubmit(event?: React.FormEvent) {
    event?.preventDefault()

    const content = message.trim()
    if (!content || isStreaming) return

    const assistantId = createId()
    const controller = new AbortController()
    abortRef.current = controller

    setMessage("")
    setIsStreaming(true)
    setMessages((current) => [
      ...current,
      { id: createId(), role: "user", content },
      { id: assistantId, role: "assistant", content: "" },
    ])

    try {
      const answer = await ChatService.chat({
        message: content,
        signal: controller.signal,
      })

      setMessages((current) =>
        current.map((item) =>
          item.id === assistantId
            ? { ...item, content: answer }
            : item,
        ),
      )
    } catch (error: any) {
      if (error?.name !== "AbortError") {
        toast.error(error.message ?? "Erro ao conversar com o assistente")
        setMessages((current) =>
          current.map((item) =>
            item.id === assistantId
              ? { ...item, content: "Não consegui responder agora. Tente novamente em instantes." }
              : item,
          ),
        )
      }
    } finally {
      setIsStreaming(false)
      abortRef.current = null
    }
  }

  function handleClose() {
    abortRef.current?.abort()
    setIsOpen(false)
  }

  function handleKeyDown(event: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault()
      void handleSubmit()
    }
  }

  return (
    <>
      {isOpen && (
        <Card className="fixed bottom-24 right-6 z-50 flex h-[min(32rem,calc(100vh-8rem))] w-[23rem] max-w-[calc(100vw-3rem)] flex-col overflow-hidden rounded-md border-border/70 bg-card/95 shadow-2xl backdrop-blur">
          <CardHeader className="flex flex-row items-center justify-between space-y-0 border-b border-border/70 px-3 py-2">
            <CardTitle className="text-sm font-semibold">Assistente Econolyze</CardTitle>
            <Button variant="ghost" size="icon" className="h-6 w-6 rounded-full" onClick={handleClose}>
              <X className="h-4 w-4" />
            </Button>
          </CardHeader>
          <CardContent className="flex min-h-0 flex-1 flex-col overflow-hidden bg-background/20 p-0">
            <div className="min-h-0 flex-1 space-y-3 overflow-y-auto overflow-x-hidden px-3 py-4">
              {messages.map((item) => (
                <div
                  key={item.id}
                  className={cn(
                    "max-w-[88%] overflow-hidden rounded-md border p-3 text-sm shadow-sm",
                    item.role === "user"
                      ? "ml-auto border-primary/40 bg-primary/15 text-foreground dark:bg-primary/[0.12]"
                      : "mr-auto border-border/70 bg-secondary/80 text-foreground",
                  )}
                >
                  {removeToolCalls(item.content) ? (
                    <p className="whitespace-pre-wrap break-words">{removeToolCalls(item.content)}</p>
                  ) : (
                    <p className="text-muted-foreground">Pensando...</p>
                  )}
                </div>
              ))}
              <div ref={messagesEndRef} />
            </div>
            <form onSubmit={handleSubmit} className="shrink-0 border-t border-border/70 bg-card/95 p-3 pb-2.5">
              <div className="flex items-end gap-2">
                <textarea
                  ref={textareaRef}
                  placeholder="Digite sua mensagem..."
                  value={message}
                  onChange={(event) => setMessage(event.target.value)}
                  onKeyDown={handleKeyDown}
                  rows={1}
                  className="form-control min-h-10 resize-none overflow-y-auto overflow-x-hidden rounded-md px-3 py-2 text-sm leading-5 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px]"
                  style={{ maxHeight: TEXTAREA_MAX_HEIGHT }}
                  disabled={isStreaming}
                />
                <Button size="icon" className="h-10 shrink-0 rounded-full" disabled={!message.trim() || isStreaming}>
                  <Send className="h-4 w-4" />
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      <Button
        size="icon"
        className="fixed bottom-6 right-6 z-40 h-14 w-14 rounded-full shadow-lg"
        onClick={() => setIsOpen((current) => !current)}
      >
        <MessageCircle className="h-6 w-6" />
      </Button>
    </>
  )
}
