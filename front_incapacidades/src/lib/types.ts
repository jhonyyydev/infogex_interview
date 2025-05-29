// lib/types.ts
export interface User {
  id: string
  username: string
  email: string
  firstName: string
  lastName: string
  documentType?: string
  documentNumber?: string
  roles?: string[]
}

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  message?: string
}

export interface RegisterRequest {
  username: string
  password: string
  email: string
  firstName: string
  lastName: string
  documentType: string
  documentNumber: string
  roles: string[]
}

export interface DisabilityRequest {
  documentNumber: string
  documentType: string
  firstName: string
  secondName?: string
  firstLastName: string
  secondLastName?: string
  salary: number
  eps: string
  hireDate: string
  startDate: string
  endDate: string
  email: string
  phone?: string
}

export interface Disability {
  id: string
  filingNumber: string
  documentNumber: string
  documentType: string
  firstName: string
  secondName?: string
  firstLastName: string
  secondLastName?: string
  salary: number
  eps: string
  hireDate: string
  startDate: string
  endDate: string
  email: string
  phone?: string
  creator: {
    id: string
    username: string
    firstName: string
    lastName: string
  }
  status: "PENDING" | "IN_PROGRESS" | "COMPLETED"
  createdAt: string
  updatedAt: string
}

export interface ApiResponse<T> {
  success: boolean
  message: string
  data?: T
  errors?: string[]
}

export interface Role {
  id: string
  name: string
}

export interface StatusChangeRequest {
  newStatus: "PENDING" | "IN_PROGRESS" | "COMPLETED"
}