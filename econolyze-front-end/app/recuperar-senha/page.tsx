import Link from "next/link"
import { RecuperarSenhaForm } from "@/components/recuperar-senha-form"
import { ArrowLeft } from "lucide-react"

export default function RecuperarSenhaPage() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <Link
          href="/login"
          className="inline-flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground mb-6"
        >
          <ArrowLeft className="h-4 w-4" />
          Voltar para login
        </Link>

        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-4">Recuperar Senha</h1>
          <p className="text-muted-foreground">Digite seu e-mail e enviaremos instruções para redefinir sua senha</p>
        </div>

        <RecuperarSenhaForm />
      </div>
    </div>
  )
}
