"use client"

import type React from "react"

import { useState, useEffect } from "react"
import type { Disability, DisabilityRequest } from "@/lib/types"
import { apiService } from "@/lib/api"
import { useToast } from "@/components/ui/Toast"

interface DisabilityFormProps {
  disability?: Disability | null
  onSuccess: () => void
  onCancel: () => void
}

export function DisabilityForm({ disability, onSuccess, onCancel }: DisabilityFormProps) {
  const [formData, setFormData] = useState<DisabilityRequest>({
    documentNumber: "",
    documentType: "CC",
    firstName: "",
    secondName: "",
    firstLastName: "",
    secondLastName: "",
    salary: 0,
    eps: "",
    hireDate: "",
    startDate: "",
    endDate: "",
    email: "",
    phone: "",
    position: "",
  })
  const [isLoading, setIsLoading] = useState(false)
  const { showToast, ToastComponent } = useToast()

  useEffect(() => {
    if (disability) {
      setFormData({
        documentNumber: disability.documentNumber,
        documentType: disability.documentType,
        firstName: disability.firstName,
        secondName: disability.secondName || "",
        firstLastName: disability.firstLastName,
        secondLastName: disability.secondLastName || "",
        salary: disability.salary,
        eps: disability.eps,
        hireDate: disability.hireDate,
        startDate: disability.startDate,
        endDate: disability.endDate,
        email: disability.email,
        phone: disability.phone || "",
        position: "",
      })
    }
  }, [disability])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: name === "salary" ? Number(value) : value })
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!formData.firstName || !formData.firstLastName || !formData.email || !formData.salary) {
      showToast("Por favor completa todos los campos obligatorios", "error")
      return
    }

    setIsLoading(true)
    try {
      let response
      if (disability) {
        response = await apiService.updateDisability(disability.id, formData)
      } else {
        response = await apiService.createDisability(formData)
      }

      if (response.success) {
        showToast(disability ? "Incapacidad actualizada" : "Incapacidad creada", "success")
        onSuccess()
      } else {
        showToast(response.message || "Error al guardar", "error")
      }
    } catch (error: any) {
      showToast(error.message || "Error al guardar", "error")
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      {ToastComponent}
      <h3 className="text-xl font-bold mb-4">{disability ? "Editar" : "Nueva"} Incapacidad</h3>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de Documento *</label>
            <select
              name="documentType"
              value={formData.documentType}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="CC">Cédula de Ciudadanía</option>
              <option value="CE">Cédula de Extranjería</option>
              <option value="TI">Tarjeta de Identidad</option>
              <option value="PP">Pasaporte</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Número de Documento *</label>
            <input
              type="text"
              name="documentNumber"
              value={formData.documentNumber}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Primer Nombre *</label>
            <input
              type="text"
              name="firstName"
              value={formData.firstName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Segundo Nombre</label>
            <input
              type="text"
              name="secondName"
              value={formData.secondName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Primer Apellido *</label>
            <input
              type="text"
              name="firstLastName"
              value={formData.firstLastName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Segundo Apellido</label>
            <input
              type="text"
              name="secondLastName"
              value={formData.secondLastName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Salario *</label>
            <input
              type="number"
              name="salary"
              value={formData.salary}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">EPS *</label>
            <input
              type="text"
              name="eps"
              value={formData.eps}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Fecha de Contratación *</label>
            <input
              type="date"
              name="hireDate"
              value={formData.hireDate}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Fecha de Inicio *</label>
            <input
              type="date"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Fecha de Fin *</label>
            <input
              type="date"
              name="endDate"
              value={formData.endDate}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email *</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Teléfono</label>
            <input
              type="tel"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
        </div>

        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={onCancel}
            className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            disabled={isLoading}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:opacity-50"
          >
            {isLoading ? "Guardando..." : disability ? "Actualizar" : "Crear"}
          </button>
        </div>
      </form>
    </div>
  )
}
