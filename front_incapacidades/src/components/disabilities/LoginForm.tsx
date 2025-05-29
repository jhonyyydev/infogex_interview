"use client"

import type React from "react"

import { useState } from "react"
import { useAuth } from "@/contexts/AuthContext"
import { useToast } from "@/components/ui/Toast"

interface LoginFormProps {
  onSwitchToRegister: () => void
}

export function LoginForm({ onSwitchToRegister }: LoginFormProps) {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [isLoading, setIsLoading] = useState(false)
  const { login } = useAuth()
  const { showToast, ToastComponent } = useToast()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!username || !password) {
      showToast("Por favor completa todos los campos", "error")
      return
    }

    setIsLoading(true)
    try {
      const success = await login(username, password)
      if (success) {
        showToast("Inicio de sesión exitoso", "success")
      } else {
        showToast("Credenciales inválidas", "error")
      }
    } catch (error) {
      showToast("Error al iniciar sesión", "error")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="max-w-md mx-auto bg-white p-6 rounded-lg shadow-md">
      {ToastComponent}
      <h2 className="text-2xl font-bold mb-6 text-center">Iniciar Sesión</h2>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Usuario</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Nombre de usuario"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Contraseña"
          />
        </div>

        <button
          type="submit"
          disabled={isLoading}
          className="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 disabled:opacity-50"
        >
          {isLoading ? "Iniciando..." : "Iniciar Sesión"}
        </button>
      </form>

      <p className="mt-4 text-center text-sm text-gray-600">
        ¿No tienes cuenta?{" "}
        <button onClick={onSwitchToRegister} className="text-blue-600 hover:text-blue-800">
          Regístrate
        </button>
      </p>
    </div>
  )
}
