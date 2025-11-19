"use client"

import { useEffect, useRef, useState } from "react"

export function ParallaxHero() {
  const canvasRef = useRef<HTMLCanvasElement>(null)
  const [scrollY, setScrollY] = useState(0)

  useEffect(() => {
    const handleScroll = () => {
      setScrollY(window.scrollY)
    }

    window.addEventListener("scroll", handleScroll)
    return () => {
      window.removeEventListener("scroll", handleScroll)
    }
  }, [])

  useEffect(() => {
    const canvas = canvasRef.current
    if (!canvas) return

    const ctx = canvas.getContext("2d")
    if (!ctx) return

    const resizeCanvas = () => {
      canvas.width = window.innerWidth
      canvas.height = 600
    }

    resizeCanvas()
    window.addEventListener("resize", resizeCanvas)

    let animationFrame: number
    let time = 0

    const pointCount = 60

    const animate = () => {
      if (!ctx || !canvas) return

      ctx.fillStyle = "#000000"
      ctx.fillRect(0, 0, canvas.width, canvas.height)

      const parallaxOffset = scrollY * 0.5

      time += 0.01

      const incomePoints: { x: number; y: number }[] = []
      const expensePoints: { x: number; y: number }[] = []

      // Generate income line (always trending up)
      for (let i = 0; i < pointCount; i++) {
        const x = (i * canvas.width) / (pointCount - 1)
        const baseY = canvas.height * 0.75 - (i * canvas.height * 0.5) / pointCount

        const waveY = Math.sin(time * 2 + i * 0.2) * 20

        const y = baseY + waveY + parallaxOffset
        incomePoints.push({ x, y })
      }

      // Generate expense line (always trending down)
      for (let i = 0; i < pointCount; i++) {
        const x = (i * canvas.width) / (pointCount - 1)
        const baseY = canvas.height * 0.25 + (i * canvas.height * 0.5) / pointCount

        const waveY = Math.sin(time * 1.8 + i * 0.25) * 18

        const y = baseY + waveY + parallaxOffset
        expensePoints.push({ x, y })
      }

      ctx.globalAlpha = 0.7

      // Draw income line (blue - thicker and more visible)
      ctx.beginPath()
      ctx.moveTo(incomePoints[0].x, incomePoints[0].y)
      for (let i = 1; i < incomePoints.length; i++) {
        ctx.lineTo(incomePoints[i].x, incomePoints[i].y)
      }
      ctx.strokeStyle = "#3b82f6"
      ctx.lineWidth = 4
      ctx.shadowBlur = 15
      ctx.shadowColor = "rgba(59, 130, 246, 0.5)"
      ctx.stroke()

      // Fill area under income line
      ctx.lineTo(canvas.width, canvas.height)
      ctx.lineTo(0, canvas.height)
      ctx.closePath()
      const incomeGradient = ctx.createLinearGradient(0, 0, 0, canvas.height)
      incomeGradient.addColorStop(0, "rgba(59, 130, 246, 0.2)")
      incomeGradient.addColorStop(1, "rgba(59, 130, 246, 0.02)")
      ctx.fillStyle = incomeGradient
      ctx.shadowBlur = 0
      ctx.fill()

      // Draw expense line (red - thicker and more visible)
      ctx.beginPath()
      ctx.moveTo(expensePoints[0].x, expensePoints[0].y)
      for (let i = 1; i < expensePoints.length; i++) {
        ctx.lineTo(expensePoints[i].x, expensePoints[i].y)
      }
      ctx.strokeStyle = "#ef4444"
      ctx.lineWidth = 4
      ctx.shadowBlur = 15
      ctx.shadowColor = "rgba(239, 68, 68, 0.5)"
      ctx.stroke()

      // Fill area under expense line
      ctx.lineTo(canvas.width, canvas.height)
      ctx.lineTo(0, canvas.height)
      ctx.closePath()
      const expenseGradient = ctx.createLinearGradient(0, 0, 0, canvas.height)
      expenseGradient.addColorStop(0, "rgba(239, 68, 68, 0.2)")
      expenseGradient.addColorStop(1, "rgba(239, 68, 68, 0.02)")
      ctx.fillStyle = expenseGradient
      ctx.shadowBlur = 0
      ctx.fill()

      ctx.globalAlpha = 1

      animationFrame = requestAnimationFrame(animate)
    }

    animate()

    return () => {
      cancelAnimationFrame(animationFrame)
      window.removeEventListener("resize", resizeCanvas)
    }
  }, [scrollY])

  return (
    <div className="relative h-[600px] overflow-hidden">
      <canvas ref={canvasRef} className="absolute inset-0 w-full h-full" />

      <div className="absolute inset-0 bg-gradient-to-b from-transparent via-background/40 to-background pointer-events-none" />
    </div>
  )
}
