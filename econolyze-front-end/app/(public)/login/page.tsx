import Link from "next/link"
import { LoginForm } from "@/components/login-form"

export default function LoginPage() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-4">Econolyze</h1>
          <p className="text-muted-foreground">Bem-vindo de volta</p>
        </div>

        <LoginForm />

        <div className="mt-6 text-center space-y-2">
          <Link href="/registro" className="text-sm text-primary hover:underline block">
            Criar uma conta
          </Link>
          <Link href="/recuperar-senha" className="text-sm text-muted-foreground hover:text-foreground block">
            Esqueceu a senha?
          </Link>
        </div>
      </div>
    </div>
  )
}
