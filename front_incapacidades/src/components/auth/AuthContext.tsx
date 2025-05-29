// contexts/AuthContext.tsx
"use client"

import { createContext, useContext, useState, useEffect, type ReactNode } from "react"
import type { User } from "@/lib/types"
import { apiService } from "@/lib/api"

interface AuthContextType {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (username: string, password: string) => Promise<{ success: boolean; message: string }>
  logout: () => void
  updateUser: (user: User) => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // Solo ejecutar en el cliente
    if (typeof window !== "undefined") {
      try {
        const token = localStorage.getItem("token")
        const userData = localStorage.getItem("user")

        if (token && userData) {
          const parsedUser = JSON.parse(userData)
          setUser(parsedUser)
        }
      } catch (error) {
        console.error("Error loading saved user data:", error)
        // Limpiar datos corruptos
        localStorage.removeItem("token")
        localStorage.removeItem("user")
      }
    }
    setIsLoading(false)
  }, [])

  const login = async (username: string, password: string): Promise<{ success: boolean; message: string }> => {
    try {
      const response = await apiService.login({ username, password })

      if (response.success && response.data) {
        const { token, user: userData } = response.data

        // Solo usar localStorage en el cliente
        if (typeof window !== "undefined") {
          localStorage.setItem("token", token)
          localStorage.setItem("user", JSON.stringify(userData))
        }

        setUser(userData)
        return { success: true, message: response.message || "Login exitoso" }
      }

      return { 
        success: false, 
        message: response.message || "Error en el login" 
      }
    } catch (error) {
      console.error("Login error:", error)
      return { 
        success: false, 
        message: "Error de conexión con el servidor" 
      }
    }
  }

  const logout = () => {
    // Solo usar localStorage en el cliente
    if (typeof window !== "undefined") {
      localStorage.removeItem("token")
      localStorage.removeItem("user")
    }
    setUser(null)
  }

  const updateUser = (updatedUser: User) => {
    setUser(updatedUser)
    if (typeof window !== "undefined") {
      localStorage.setItem("user", JSON.stringify(updatedUser))
    }
  }

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
    updateUser,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}