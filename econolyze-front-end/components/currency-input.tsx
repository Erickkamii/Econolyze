"use client"

import type React from "react"
import { forwardRef, useRef, useEffect } from "react"
import { cn } from "@/lib/utils"

interface CurrencyInputProps {
  id?: string
  value: string
  onChange: (value: string) => void
  placeholder?: string
  className?: string
  required?: boolean
  max?: number
}

export const CurrencyInput = forwardRef<HTMLInputElement, CurrencyInputProps>(
  ({ id, value, onChange, placeholder = "0,00", className = "", required = false, max }, ref) => {
    const inputRef = useRef<HTMLInputElement>(null)

    const formatCurrency = (cents: number) => {
      const reais = cents / 100
      return reais.toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      })
    }

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
      // Allow: backspace, delete, tab, escape, enter
      if (e.key === "Backspace" || e.key === "Delete" || e.key === "Tab" || e.key === "Escape" || e.key === "Enter") {
        if (e.key === "Backspace") {
          e.preventDefault()
          const currentCents = Number.parseInt(value) || 0
          const newCents = Math.floor(currentCents / 10)
          onChange(newCents.toString())
        }
        return
      }

      // Only allow numbers
      if (!/^\d$/.test(e.key)) {
        e.preventDefault()
        return
      }

      e.preventDefault()
      const currentCents = Number.parseInt(value) || 0
      const newCents = currentCents * 10 + Number.parseInt(e.key)

      // Check max value if provided
      if (max !== undefined && newCents / 100 > max) {
        return
      }

      onChange(newCents.toString())
    }

    const displayValue = value ? formatCurrency(Number.parseInt(value)) : "0,00"

    useEffect(() => {
      if (inputRef.current) {
        inputRef.current.style.caretColor = "transparent"
      }
    }, [])

    return (
      <div className="relative">
        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-muted-foreground pointer-events-none z-10">
          R$
        </span>
        <input
          ref={inputRef}
          id={id}
          type="text"
          inputMode="numeric"
          onKeyDown={handleKeyDown}
          value={displayValue}
          onChange={() => {}}
          className={cn(
            "flex h-10 w-full rounded-md border border-input bg-secondary pl-10 pr-3 py-2 text-sm ring-offset-background file:border-0 file:bg-transparent file:text-sm file:font-medium placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50",
            className,
          )}
          required={required}
          autoComplete="off"
        />
      </div>
    )
  },
)

CurrencyInput.displayName = "CurrencyInput"
