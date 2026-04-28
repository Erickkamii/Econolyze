"use client"

import type React from "react"

import { CurrencyInput } from "@/components/currency-input"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { cn } from "@/lib/utils"

type FormFieldProps = {
  label: React.ReactNode
  htmlFor?: string
  hint?: React.ReactNode
  className?: string
  children: React.ReactNode
}

export function FormField({ label, htmlFor, hint, className, children }: FormFieldProps) {
  return (
    <div className={cn("form-field", className)}>
      <Label htmlFor={htmlFor}>{label}</Label>
      {children}
      {hint && <p className="text-xs text-muted-foreground">{hint}</p>}
    </div>
  )
}

type FormGridProps = {
  children: React.ReactNode
  className?: string
  columns?: 2 | 3 | 4
}

export function FormGrid({ children, className, columns = 2 }: FormGridProps) {
  return (
    <div
      className={cn(
        "form-grid",
        columns === 4 && "md:grid-cols-[repeat(2,minmax(0,1fr))] xl:grid-cols-[repeat(4,minmax(0,1fr))]",
        columns === 3 && "md:grid-cols-[repeat(3,minmax(0,1fr))]",
        columns === 2 && "md:grid-cols-[repeat(2,minmax(0,1fr))]",
        className,
      )}
    >
      {children}
    </div>
  )
}

type TextFormFieldProps = React.ComponentProps<typeof Input> & {
  label: React.ReactNode
  hint?: React.ReactNode
}

export function TextFormField({ label, hint, className, id, ...props }: TextFormFieldProps) {
  return (
    <FormField label={label} htmlFor={id} hint={hint}>
      <Input id={id} className={cn("form-control", className)} {...props} />
    </FormField>
  )
}

type CurrencyFormFieldProps = React.ComponentProps<typeof CurrencyInput> & {
  label: React.ReactNode
  hint?: React.ReactNode
}

export function CurrencyFormField({ label, hint, className, id, ...props }: CurrencyFormFieldProps) {
  return (
    <FormField label={label} htmlFor={id} hint={hint}>
      <CurrencyInput id={id} className={cn("form-control", className)} {...props} />
    </FormField>
  )
}

type SelectOption = {
  value: string
  label: React.ReactNode
  disabled?: boolean
}

type SelectFormFieldProps = {
  label: React.ReactNode
  value: string
  onValueChange: (value: string) => void
  placeholder?: string
  options?: readonly SelectOption[]
  disabled?: boolean
  required?: boolean
  hint?: React.ReactNode
  className?: string
  triggerClassName?: string
  children?: React.ReactNode
}

export function SelectFormField({
  label,
  value,
  onValueChange,
  placeholder = "Selecione",
  options,
  disabled,
  required,
  hint,
  className,
  triggerClassName,
  children,
}: SelectFormFieldProps) {
  return (
    <FormField label={label} hint={hint} className={className}>
      <Select value={value} onValueChange={onValueChange} required={required}>
        <SelectTrigger className={cn("min-w-0 w-full border-border bg-secondary", triggerClassName)} disabled={disabled}>
          <SelectValue placeholder={placeholder} />
        </SelectTrigger>
        <SelectContent>
          {children ??
            options?.map((option) => (
              <SelectItem key={option.value} value={option.value} disabled={option.disabled}>
                {option.label}
              </SelectItem>
            ))}
        </SelectContent>
      </Select>
    </FormField>
  )
}
