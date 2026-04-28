import Link from "next/link"
import { ArrowLeft } from "lucide-react"

import { Button } from "@/components/ui/button"
import { ChatbotButton } from "@/components/chatbot-button"
import { MainNav } from "@/components/main-nav"
import { MetaForm } from "@/components/meta-form"

export default function NovaMetaPage() {
  return (
    <div className="min-h-screen pb-20">
      <div className="max-w-2xl mx-auto p-6 space-y-6">
        <div className="flex items-center gap-4">
          <Link href="/investimentos">
            <Button variant="ghost" size="icon">
              <ArrowLeft className="h-5 w-5" />
            </Button>
          </Link>
          <h1 className="text-2xl font-bold">Nova Meta</h1>
        </div>

        <MetaForm />
      </div>

      <MainNav />
      <ChatbotButton />
    </div>
  )
}
