import { apiFetch } from "@/lib/services/api-client"

type ChatOptions = {
  message: string
  signal?: AbortSignal
}

type ChatResponse = {
  content?: string
  aiResponse?: string
}

export class ChatService {
  static async chat({ message, signal }: ChatOptions): Promise<string> {
    const response = await apiFetch("/ai/chat", {
      method: "POST",
      signal,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify({ message }),
    })

    if (!response.ok) {
      throw new Error("Erro ao conversar com o assistente")
    }

    const data = (await response.json()) as ChatResponse | string

    if (typeof data === "string") {
      return data
    }

    return data.content ?? data.aiResponse ?? ""
  }
}
