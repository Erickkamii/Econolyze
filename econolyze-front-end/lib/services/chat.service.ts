import { apiFetch } from "@/lib/services/api-client"

type StreamChatOptions = {
  message: string
  signal?: AbortSignal
  onChunk: (chunk: string) => void
}

type ChatResponse = {
  aiResponse?: string
  content?: string
}

function extractChunk(json: string): string {
  try {
    const parsed = JSON.parse(json) as ChatResponse | string

    if (typeof parsed === "string") {
      return parsed
    }

    return parsed.content ?? parsed.aiResponse ?? ""
  } catch {
    return ""
  }
}

function emitJsonObjects(buffer: string, onChunk: (chunk: string) => void): string {
  const regex = /\{"content":"((?:\\.|[^"\\])*)"\}/g

  let match: RegExpExecArray | null
  let lastIndex = 0

  while ((match = regex.exec(buffer)) !== null) {
    const rawContent = match[1]

    const content = JSON.parse(`"${rawContent}"`)
    onChunk(content)

    lastIndex = regex.lastIndex
  }

  return buffer.slice(lastIndex)
}

function emitSseEvents(buffer: string, onChunk: (chunk: string) => void): string {
  const events = buffer.split(/\n\n/)
  const rest = events.pop() ?? ""

  for (const event of events) {
    const data = event
      .split(/\n/)
      .filter((line) => line.startsWith("data:"))
      .map((line) => line.startsWith("data: ") ? line.slice(6) : line.slice(5))
      .join("\n")

    if (data && data !== "[DONE]") {
      emitJsonObjects(data, onChunk)
    }
  }

  return rest
}

export class ChatService {
  static async stream({ message, signal, onChunk }: StreamChatOptions) {
    const response = await apiFetch("/ai/chat", {
      method: "POST",
      signal,
      headers: {
        "Content-Type": "application/json",
        Accept: "text/event-stream",
      },
      body: JSON.stringify({ message }),
    })

    if (!response.ok) {
      throw new Error("Erro ao conversar com o assistente")
    }

    if (!response.body) return

    const contentType = response.headers.get("content-type") ?? ""
    const isSse = contentType.includes("text/event-stream")

    const reader = response.body.getReader()
    const decoder = new TextDecoder()

    let buffer = ""

    while (true) {
      const { value, done } = await reader.read()
      if (done) break

      const text = decoder.decode(value, { stream: true })
      buffer = emitJsonObjects(buffer + text, onChunk)
    }

    const tail = decoder.decode()
    if (tail) {
      buffer = isSse
        ? emitSseEvents(buffer + tail, onChunk)
        : emitJsonObjects(buffer + tail, onChunk)
    }

    // Não manda buffer cru pra tela.
    // Se sobrar algo aqui, é JSON incompleto ou lixo de stream.
  }
}