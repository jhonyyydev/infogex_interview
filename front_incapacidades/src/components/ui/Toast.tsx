// components/ui/Toast.tsx
"use client"

import { useState, useEffect } from "react"

type ToastType = "success" | "error" | "info" | "warning"

interface ToastState {
  message: string
  type: ToastType
  visible: boolean
}

export function useToast() {
  const [toast, setToast] = useState<ToastState>({
    message: "",
    type: "info",
    visible: false,
  })

  const showToast = (message: string, type: ToastType = "info") => {
    setToast({ message, type, visible: true })
  }

  const hideToast = () => {
    setToast(prev => ({ ...prev, visible: false }))
  }

  useEffect(() => {
    if (toast.visible) {
      const timer = setTimeout(() => {
        hideToast()
      }, 5000) // Auto-hide after 5 seconds

      return () => clearTimeout(timer)
    }
  }, [toast.visible])

  const ToastComponent = () => {
    if (!toast.visible) return null

    const getToastStyles = () => {
      const baseStyles = "fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 max-w-md"
      
      switch (toast.type) {
        case "success":
          return `${baseStyles} bg-green-100 border border-green-400 text-green-700`
        case "error":
          return `${baseStyles} bg-red-100 border border-red-400 text-red-700`
        case "warning":
          return `${baseStyles} bg-yellow-100 border border-yellow-400 text-yellow-700`
        default:
          return `${baseStyles} bg-blue-100 border border-blue-400 text-blue-700`
      }
    }

    const getIcon = () => {
      switch (toast.type) {
        case "success":
          return "✓"
        case "error":
          return "✗"
        case "warning":
          return "⚠"
        default:
          return "ℹ"
      }
    }

    return (
      <div className={getToastStyles()}>
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <span className="text-lg mr-2">{getIcon()}</span>
            <span>{toast.message}</span>
          </div>
          <button
            onClick={hideToast}
            className="ml-4 text-lg hover:opacity-70"
          >
            ×
          </button>
        </div>
      </div>
    )
  }

  return { showToast, hideToast, ToastComponent }
}