import type { Metadata } from "next"
import AdminDashboard from "@/components/admin-dashboard"

export const metadata: Metadata = {
  title: "Admin Dashboard - TechHub",
  description: "Manage products and view analytics",
}

export default function AdminPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Admin Dashboard</h1>
      <AdminDashboard />
    </div>
  )
}
