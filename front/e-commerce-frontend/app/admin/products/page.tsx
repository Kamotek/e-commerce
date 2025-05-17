import type { Metadata } from "next"
import AdminProductList from "@/components/admin-product-list"

export const metadata: Metadata = {
  title: "Manage Products - TechHub Admin",
  description: "Add, edit, and delete products",
}

export default function AdminProductsPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Manage Products</h1>
      <AdminProductList />
    </div>
  )
}
