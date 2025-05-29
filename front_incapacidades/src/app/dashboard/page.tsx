"use client"

import { useState, useEffect } from "react"
import { useAuth } from "@/contexts/AuthContext"
import { apiService } from "@/lib/api"
import type { Disability } from "@/lib/types"

export default function DashboardPage() {
  const [mounted, setMounted] = useState(false)
  const [disabilities, setDisabilities] = useState<Disability[]>([])
  const [stats, setStats] = useState({
    total: 0,
    pending: 0,
    inProgress: 0,
    completed: 0,
  })

  const auth = useAuth()
  const { user } = auth

  useEffect(() => {
    setMounted(true)
  }, [])

  useEffect(() => {
    if (!mounted) return

    const fetchData = async () => {
      try {
        const response = await apiService.getMyDisabilities()
        if (response.success && response.data) {
          setDisabilities(response.data)
          setStats({
            total: response.data.length,
            pending: response.data.filter((d) => d.status === "PENDING").length,
            inProgress: response.data.filter((d) => d.status === "IN_PROGRESS").length,
            completed: response.data.filter((d) => d.status === "COMPLETED").length,
          })
        }
      } catch (error) {
        console.error("Error fetching data:", error)
      }
    }

    fetchData()
  }, [mounted])

  if (!mounted) {
    return (
      <div className="flex items-center justify-center py-12">
        <div className="text-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-2 text-gray-600">Cargando...</p>
        </div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h1 className="text-2xl font-bold text-gray-900">
          Bienvenido, {user?.firstName} {user?.lastName}
        </h1>
        <p className="text-gray-600">Aquí tienes un resumen de tus incapacidades</p>
      </div>

      {/* Estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <div className="text-3xl font-bold text-blue-600">{stats.total}</div>
          <div className="text-gray-600">Total</div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <div className="text-3xl font-bold text-yellow-600">{stats.pending}</div>
          <div className="text-gray-600">Pendientes</div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <div className="text-3xl font-bold text-blue-600">{stats.inProgress}</div>
          <div className="text-gray-600">En Curso</div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow-md text-center">
          <div className="text-3xl font-bold text-green-600">{stats.completed}</div>
          <div className="text-gray-600">Completadas</div>
        </div>
      </div>

      {/* Incapacidades recientes */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-bold mb-4">Incapacidades Recientes</h2>
        {disabilities.length === 0 ? (
          <p className="text-gray-500 text-center py-8">No tienes incapacidades registradas</p>
        ) : (
          <div className="space-y-3">
            {disabilities.slice(0, 5).map((disability) => (
              <div key={disability.id} className="flex justify-between items-center p-3 border rounded">
                <div>
                  <div className="font-medium">
                    {disability.firstName} {disability.firstLastName}
                  </div>
                  <div className="text-sm text-gray-600">Radicado: {disability.filingNumber}</div>
                </div>
                <div className="text-right">
                  <div className="text-sm text-gray-600">
                    {disability.startDate} - {disability.endDate}
                  </div>
                  <div className="text-sm">
                    <span
                      className={`px-2 py-1 rounded text-xs ${
                        disability.status === "PENDING"
                          ? "bg-yellow-100 text-yellow-800"
                          : disability.status === "IN_PROGRESS"
                            ? "bg-blue-100 text-blue-800"
                            : "bg-green-100 text-green-800"
                      }`}
                    >
                      {disability.status === "PENDING"
                        ? "Pendiente"
                        : disability.status === "IN_PROGRESS"
                          ? "En Curso"
                          : "Completado"}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Acciones rápidas */}
      <div className="bg-white p-6 rounded-lg shadow-md">
        <h2 className="text-xl font-bold mb-4">Acciones Rápidas</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <a
            href="/disabilities"
            className="flex items-center p-4 border rounded-lg hover:bg-gray-50 transition-colors"
          >
            <div>
              <h3 className="font-medium">Ver Todas las Incapacidades</h3>
              <p className="text-sm text-gray-600">Gestiona todas tus incapacidades</p>
            </div>
          </a>

          <a
            href="/disabilities"
            className="flex items-center p-4 border rounded-lg hover:bg-gray-50 transition-colors"
          >
            <div>
              <h3 className="font-medium">Nueva Incapacidad</h3>
              <p className="text-sm text-gray-600">Registra una nueva incapacidad</p>
            </div>
          </a>
        </div>
      </div>
    </div>
  )
}
