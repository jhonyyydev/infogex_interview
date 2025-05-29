import type React from "react"
import type { Metadata } from "next"
import { Inter } from "next/font/google"
import "./globals.css"
import { AuthProvider } from "@/contexts/AuthContext"
import { Navbar } from "@/components/disabilities/Navbar"

const inter = Inter({ subsets: ["latin"] })

export const metadata: Metadata = {
  title: "Sistema de Gestión de Incapacidades",
  description: "Sistema para gestionar las incapacidades de los empleados",
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="es">
      <body className={inter.className}>
        <AuthProvider>
          <div className="min-h-screen bg-gray-50">
            <Navbar />
            <main className="max-w-6xl mx-auto py-6 px-4">{children}</main>
          </div>
        </AuthProvider>
      </body>
    </html>
  )
}
