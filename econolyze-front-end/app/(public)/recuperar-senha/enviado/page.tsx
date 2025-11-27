import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { CheckCircle2, Mail } from "lucide-react"

export default function RecuperacaoEnviadaPage() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <Card className="border-border/50">
          <CardContent className="pt-6 text-center space-y-6">
            <div className="flex justify-center">
              <div className="rounded-full bg-primary/10 p-4">
                <CheckCircle2 className="h-12 w-12 text-primary" />
              </div>
            </div>

            <div className="space-y-2">
              <h1 className="text-2xl font-bold text-foreground">E-mail Enviado!</h1>
              <p className="text-muted-foreground">
                Se o e-mail informado estiver cadastrado, você receberá instruções para redefinir sua senha.
              </p>
            </div>

            <div className="bg-secondary/50 p-4 rounded-lg space-y-2">
              <div className="flex items-center justify-center gap-2 text-sm text-muted-foreground">
                <Mail className="h-4 w-4" />
                <span>Verifique sua caixa de entrada e spam</span>
              </div>
            </div>

            <div className="space-y-3 pt-4">
              <Button asChild className="w-full" size="lg">
                <Link href="/login">Voltar para Login</Link>
              </Button>

              <Button asChild variant="outline" className="w-full bg-transparent">
                <Link href="/recuperar-senha">Enviar Novamente</Link>
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
