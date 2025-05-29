// lib/api.ts
import type { 
  LoginRequest, 
  LoginResponse, 
  RegisterRequest, 
  DisabilityRequest, 
  ApiResponse, 
  User, 
  Disability,
  Role,
  StatusChangeRequest
} from "./types"

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8085/api"

class ApiService {
  private getAuthHeaders(): HeadersInit {
    if (typeof window === 'undefined') {
      return { "Content-Type": "application/json" }
    }
    
    const token = localStorage.getItem("token")
    return {
      "Content-Type": "application/json",
      ...(token && { Authorization: `Bearer ${token}` }),
    }
  }

  private async handleResponse<T>(response: Response): Promise<ApiResponse<T>> {
    try {
      const data = await response.json()
      
      if (!response.ok) {
        return {
          success: false,
          message: data.message || `Error ${response.status}: ${response.statusText}`,
          errors: data.errors || [data.message || response.statusText]
        }
      }
      
      // Si la respuesta del backend ya tiene el formato ApiResponse
      if (typeof data.success !== 'undefined') {
        return data
      }
      
      // Si no, envolvemos la respuesta
      return {
        success: true,
        message: "Operación exitosa",
        data: data
      }
    } catch (error) {
      return {
        success: false,
        message: "Error de comunicación con el servidor",
        errors: [error instanceof Error ? error.message : "Error desconocido"]
      }
    }
  }

  async login(credentials: LoginRequest): Promise<ApiResponse<{ token: string; user: User }>> {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      })

      const result = await response.json()

      if (!response.ok) {
        return {
          success: false,
          message: result.message || "Credenciales inválidas",
          errors: [result.message || "Error de autenticación"]
        }
      }

      if (result.token) {
        // Obtener información del usuario usando el token
        const userInfo = await this.getCurrentUser(result.token)
        
        return {
          success: true,
          message: "Login exitoso",
          data: {
            token: result.token,
            user: userInfo || {
              id: "temp",
              username: credentials.username,
              email: credentials.username + "@temp.com",
              firstName: "Usuario",
              lastName: "Sistema"
            }
          }
        }
      }

      return {
        success: false,
        message: "Token no recibido",
        errors: ["Error en la respuesta del servidor"]
      }
    } catch (error) {
      return {
        success: false,
        message: "Error de conexión",
        errors: [error instanceof Error ? error.message : "Error desconocido"]
      }
    }
  }

  private async getCurrentUser(token: string): Promise<User | null> {
    try {
      const response = await fetch(`${API_BASE_URL}/user/profile`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        }
      })

      if (response.ok) {
        const result = await response.json()
        return result.data || result
      }
    } catch (error) {
      console.warn("No se pudo obtener información del usuario:", error)
    }
    return null
  }

  async register(userData: RegisterRequest): Promise<ApiResponse<User>> {
    const response = await fetch(`${API_BASE_URL}/user/create`, {
      method: "POST",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(userData),
    })
    return this.handleResponse(response)
  }

  async getRoles(): Promise<ApiResponse<Role[]>> {
    const response = await fetch(`${API_BASE_URL}/role/list`, {
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }

  async createDisability(disability: DisabilityRequest): Promise<ApiResponse<Disability>> {
    const response = await fetch(`${API_BASE_URL}/disability/create`, {
      method: "POST",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(disability),
    })
    return this.handleResponse(response)
  }

  async getMyDisabilities(): Promise<ApiResponse<Disability[]>> {
    const response = await fetch(`${API_BASE_URL}/disability/my-disabilities`, {
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }

  async getAllDisabilities(): Promise<ApiResponse<Disability[]>> {
    const response = await fetch(`${API_BASE_URL}/disability/list`, {
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }

  async getDisabilityById(id: string): Promise<ApiResponse<Disability>> {
    const response = await fetch(`${API_BASE_URL}/disability/${id}`, {
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }

  async updateDisability(id: string, disability: DisabilityRequest): Promise<ApiResponse<Disability>> {
    const response = await fetch(`${API_BASE_URL}/disability/${id}`, {
      method: "PUT",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(disability),
    })
    return this.handleResponse(response)
  }

  async deleteDisability(id: string): Promise<ApiResponse<void>> {
    const response = await fetch(`${API_BASE_URL}/disability/${id}`, {
      method: "DELETE",
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }

  async updateDisabilityStatus(id: string, statusRequest: StatusChangeRequest): Promise<ApiResponse<Disability>> {
    const response = await fetch(`${API_BASE_URL}/disability/${id}/status`, {
      method: "PUT",
      headers: this.getAuthHeaders(),
      body: JSON.stringify(statusRequest),
    })
    return this.handleResponse(response)
  }

  async searchDisabilities(params: {
    status?: string
    startDate?: string
    endDate?: string
    search?: string
  }): Promise<ApiResponse<Disability[]>> {
    const searchParams = new URLSearchParams()
    if (params.status) searchParams.append('status', params.status)
    if (params.startDate) searchParams.append('startDate', params.startDate)
    if (params.endDate) searchParams.append('endDate', params.endDate)
    if (params.search) searchParams.append('search', params.search)

    const response = await fetch(`${API_BASE_URL}/disability/search?${searchParams}`, {
      headers: this.getAuthHeaders(),
    })
    return this.handleResponse(response)
  }
}

export const apiService = new ApiService()