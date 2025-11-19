"use client"

import type React from "react"
import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent } from "@/components/ui/card"
import { Mail } from "lucide-react"

export function RecuperarSenhaForm() {
  const router = useRouter()
  const [email, setEmail] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)

    // Simula chamada de API para enviar e-mail de recuperação
    await new Promise((resolve) => setTimeout(resolve, 1500))

    console.log("Recuperação de senha solicitada para:", email)

    // Redireciona para página de confirmação
    router.push("/recuperar-senha/enviado")
  }

  return (
    <Card className="border-border/50">
      <CardContent className="pt-6">
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="space-y-2">
            <Label htmlFor="email">E-mail</Label>
            <div className="relative">
              <Mail className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                id="email"
                type="email"
                placeholder="seu@email.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="bg-secondary border-border pl-10"
              />
            </div>
            <p className="text-xs text-muted-foreground">
              Enviaremos um link para redefinição de senha para este e-mail
            </p>
          </div>

          <Button type="submit" className="w-full" size="lg" disabled={isLoading}>
            {isLoading ? "Enviando..." : "Enviar Link de Recuperação"}
          </Button>
        </form>
      </CardContent>
    </Card>
  )
}
