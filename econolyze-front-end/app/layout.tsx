import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { AuthProvider } from "@/context/auth.context"
import { ThemeProvider } from "@/components/theme-provider"
import { Toaster } from "sonner";

const inter = Inter({ subsets: ["latin"], variable: "--font-sans" })

export const metadata: Metadata = {
  title: "Econolyze - Gestão Financeira Pessoal",
  description: "Sistema moderno de gestão financeira pessoal",
  icons: {
    icon: [
      {
        url: "/icon-light-32x32.png",
        media: "(prefers-color-scheme: light)",
      },
      {
        url: "/icon-dark-32x32.png",
        media: "(prefers-color-scheme: dark)",
      },
      {
        url: "/icon.svg",
        type: "image/svg+xml",
      },
    ],
    apple: "/apple-icon.png",
  },
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="pt-BR" suppressHydrationWarning>
      <body className={`${inter.variable} font-sans antialiased`}>
      <ThemeProvider>
        <AuthProvider>
            {children}
        </AuthProvider>
        {/* Analytics component was removed due to causing an error */}
        <Toaster position="top-right"/>
      </ThemeProvider>
      </body>
    </html>
  )
}
