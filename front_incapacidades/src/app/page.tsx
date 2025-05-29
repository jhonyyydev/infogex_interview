"use client"

import { useState, useEffect } from "react"
import { useAuth } from "@/contexts/AuthContext"
import { LoginForm } from "@/components/disabilities/LoginForm"
import { RegisterForm } from "@/components/disabilities/RegisterForm"
import { useRouter } from "next/navigation"

export default function HomePage() {
  const [isLogin, setIsLogin] = useState(true)
  const [mounted, setMounted] = useState(false)
  const router = useRouter()

  // Asegurar que el componente esté montado antes de usar useAuth
  useEffect(() => {
    setMounted(true)
  }, [])

  // Solo usar useAuth después de que el componente esté montado
  const auth = useAuth()
  const { isAuthenticated, isLoading } = auth

  useEffect(() => {
    if (mounted && isAuthenticated) {
      router.push("/dashboard")
    }
  }, [isAuthenticated, router, mounted])

  // Mostrar loading mientras se monta el componente o se carga la auth
  if (!mounted || isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-2 text-gray-600">Cargando...</p>
        </div>
      </div>
    )
  }

  if (isAuthenticated) {
    return null 
  }

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 px-4">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Sistema de Gestión de Incapacidades</h1>
        <p className="text-gray-600 mt-2">Gestiona las incapacidades de manera eficiente</p>
      </div>

      {isLogin ? (
        <LoginForm onSwitchToRegister={() => setIsLogin(false)} />
      ) : (
        <RegisterForm onSwitchToLogin={() => setIsLogin(true)} />
      )}
    </div>
  )
}
