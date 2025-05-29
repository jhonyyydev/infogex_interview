// contexts/AuthContext.tsx
"use client"

import { createContext, useContext, useState, useEffect, ReactNode } from 'react'

interface User {
  id: string
  username: string
  role: string
  // Agrega otras propiedades del usuario según necesites
}

interface AuthResult {
  success: boolean
  message: string
}

interface AuthContextType {
  user: User | null
  login: (username: string, password: string) => Promise<AuthResult>
  logout: () => void
  isLoading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  // Verificar si hay una sesión guardada al cargar
  useEffect(() => {
    const savedUser = localStorage.getItem('user')
    if (savedUser) {
      try {
        setUser(JSON.parse(savedUser))
      } catch (error) {
        localStorage.removeItem('user')
      }
    }
    setIsLoading(false)
  }, [])

  const login = async (username: string, password: string): Promise<AuthResult> => {
    try {
      // Simulación de usuarios de prueba - reemplaza con tu lógica real
      const testUsers = [
        { id: '1', username: 'admin', password: 'admin123', role: 'admin' },
        // Agrega más usuarios de prueba si necesitas
      ]

      const foundUser = testUsers.find(
        u => u.username === username && u.password === password
      )

      if (foundUser) {
        const userData: User = {
          id: foundUser.id,
          username: foundUser.username,
          role: foundUser.role
        }
        
        setUser(userData)
        localStorage.setItem('user', JSON.stringify(userData))
        
        return {
          success: true,
          message: `¡Bienvenido de vuelta, ${userData.username}!`
        }
      } else {
        return {
          success: false,
          message: 'Usuario o contraseña incorrectos'
        }
      }
    } catch (error) {
      console.error('Error durante el login:', error)
      return {
        success: false,
        message: 'Error interno del servidor. Intenta nuevamente.'
      }
    }
  }

  const logout = () => {
    setUser(null)
    localStorage.removeItem('user')
  }

  const value: AuthContextType = {
    user,
    login,
    logout,
    isLoading
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}