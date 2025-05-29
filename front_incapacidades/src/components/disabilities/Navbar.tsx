"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { useAuth } from "@/contexts/AuthContext"

export function Navbar() {
  const [mounted, setMounted] = useState(false)
  const auth = useAuth()
  const { user, logout, isAuthenticated } = auth

  useEffect(() => {
    setMounted(true)
  }, [])

  if (!mounted || !isAuthenticated) return null

  return (
    <nav className="bg-blue-600 text-white p-4">
      <div className="max-w-6xl mx-auto flex justify-between items-center">
        <Link href="/dashboard" className="text-xl font-bold">
          Gestión de Incapacidades
        </Link>

        <div className="flex items-center space-x-4">
          <Link href="/dashboard" className="hover:text-blue-200">
            Dashboard
          </Link>
          <Link href="/disabilities" className="hover:text-blue-200">
            Incapacidades
          </Link>
          <span className="text-blue-200">
            {user?.firstName} {user?.lastName}
          </span>
          <button onClick={logout} className="bg-blue-700 px-3 py-1 rounded hover:bg-blue-800">
            Cerrar Sesión
          </button>
        </div>
      </div>
    </nav>
  )
}
