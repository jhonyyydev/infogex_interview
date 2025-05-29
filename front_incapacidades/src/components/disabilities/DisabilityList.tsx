"use client"

import { useState, useEffect } from "react"
import type { Disability } from "@/lib/types"
import { apiService } from "@/lib/api"
import { useToast } from "@/components/ui/Toast"
import { DisabilityForm } from "@/components/disabilities/DisabilityForm"

export function DisabilityList() {
  const [disabilities, setDisabilities] = useState<Disability[]>([])
  const [filteredDisabilities, setFilteredDisabilities] = useState<Disability[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [editingDisability, setEditingDisability] = useState<Disability | null>(null)
  const [searchTerm, setSearchTerm] = useState("")
  const [statusFilter, setStatusFilter] = useState("ALL")
  const { showToast, ToastComponent } = useToast()

  const fetchDisabilities = async () => {
    setIsLoading(true)
    try {
      const response = await apiService.getMyDisabilities()
      if (response.success && response.data) {
        setDisabilities(response.data)
        setFilteredDisabilities(response.data)
      }
    } catch (error: any) {
      showToast(error.message || "Error al cargar incapacidades", "error")
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    fetchDisabilities()
  }, [])

  useEffect(() => {
    let filtered = disabilities

    if (statusFilter !== "ALL") {
      filtered = filtered.filter((d) => d.status === statusFilter)
    }

    if (searchTerm) {
      filtered = filtered.filter(
        (d) =>
          d.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          d.firstLastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          d.documentNumber.includes(searchTerm) ||
          d.filingNumber.includes(searchTerm),
      )
    }

    setFilteredDisabilities(filtered)
  }, [disabilities, statusFilter, searchTerm])

  const handleEdit = (disability: Disability) => {
    setEditingDisability(disability)
    setShowForm(true)
  }

  const handleDelete = async (id: string) => {
    if (!confirm("¿Estás seguro de eliminar esta incapacidad?")) return

    try {
      const response = await apiService.deleteDisability(id)
      if (response.success) {
        showToast("Incapacidad eliminada", "success")
        fetchDisabilities()
      }
    } catch (error: any) {
      showToast(error.message || "Error al eliminar", "error")
    }
  }

  const handleFormSuccess = () => {
    setShowForm(false)
    setEditingDisability(null)
    fetchDisabilities()
  }

  const handleFormCancel = () => {
    setShowForm(false)
    setEditingDisability(null)
  }

  const getStatusBadge = (status: string) => {
    const colors = {
      PENDING: "bg-yellow-100 text-yellow-800",
      IN_PROGRESS: "bg-blue-100 text-blue-800",
      COMPLETED: "bg-green-100 text-green-800",
    }
    const labels = {
      PENDING: "Pendiente",
      IN_PROGRESS: "En Curso",
      COMPLETED: "Completado",
    }
    return (
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${colors[status as keyof typeof colors]}`}>
        {labels[status as keyof typeof labels]}
      </span>
    )
  }

  if (showForm) {
    return <DisabilityForm disability={editingDisability} onSuccess={handleFormSuccess} onCancel={handleFormCancel} />
  }

  return (
    <div className="space-y-6">
      {ToastComponent}

      {/* Header */}
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Mis Incapacidades</h2>
        <button
          onClick={() => setShowForm(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
        >
          Nueva Incapacidad
        </button>
      </div>

      {/* Filtros */}
      <div className="bg-white p-4 rounded-lg shadow-md">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Buscar</label>
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              placeholder="Nombre, documento, radicado..."
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Estado</label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="ALL">Todos</option>
              <option value="PENDING">Pendiente</option>
              <option value="IN_PROGRESS">En Curso</option>
              <option value="COMPLETED">Completado</option>
            </select>
          </div>
        </div>
      </div>

      {/* Lista */}
      {isLoading ? (
        <div className="text-center py-8">Cargando...</div>
      ) : filteredDisabilities.length === 0 ? (
        <div className="text-center py-8 text-gray-500">No se encontraron incapacidades</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredDisabilities.map((disability) => (
            <div key={disability.id} className="bg-white p-6 rounded-lg shadow-md">
              <div className="flex justify-between items-start mb-4">
                <h3 className="font-semibold">
                  {disability.firstName} {disability.firstLastName}
                </h3>
                {getStatusBadge(disability.status)}
              </div>

              <div className="space-y-2 text-sm text-gray-600">
                <p>
                  <strong>Radicado:</strong> {disability.filingNumber}
                </p>
                <p>
                  <strong>Documento:</strong> {disability.documentType} {disability.documentNumber}
                </p>
                <p>
                  <strong>Fechas:</strong> {disability.startDate} - {disability.endDate}
                </p>
                <p>
                  <strong>Salario:</strong> ${disability.salary.toLocaleString()}
                </p>
                <p>
                  <strong>EPS:</strong> {disability.eps}
                </p>
              </div>

              {disability.status === "PENDING" && (
                <div className="flex justify-end space-x-2 mt-4">
                  <button
                    onClick={() => handleEdit(disability)}
                    className="px-3 py-1 text-blue-600 border border-blue-600 rounded hover:bg-blue-50"
                  >
                    Editar
                  </button>
                  <button
                    onClick={() => handleDelete(disability.id)}
                    className="px-3 py-1 text-red-600 border border-red-600 rounded hover:bg-red-50"
                  >
                    Eliminar
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
