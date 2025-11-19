import Link from "next/link"
import { RegistroForm } from "@/components/registro-form"

export default function RegistroPage() {
  return (
    <div className="min-h-screen flex items-center justify-center p-6">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-foreground mb-4">Econolyze</h1>
          <p className="text-muted-foreground">Crie sua conta</p>
        </div>

        <RegistroForm />

        <div className="mt-6 text-center">
          <Link href="/login" className="text-sm text-primary hover:underline">
            Já tem uma conta? Entrar
          </Link>
        </div>
      </div>
    </div>
  )
}
