import type { Metadata } from "next"
import ProductGrid from "@/components/product-grid"
import ProductCarousel from "@/components/product-carousel"
import ProductFilters from "@/components/product-filters"

export const metadata: Metadata = {
  title: "Product Catalog - TechHub",
  description: "Browse our selection of computers, phones, accessories, and PC parts",
}

export default function CatalogPage() {
  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Product Catalog</h1>

      <section className="mb-12">
        <h2 className="text-2xl font-semibold mb-6">Featured Products</h2>
        <ProductCarousel />
      </section>

      <div className="flex flex-col lg:flex-row gap-8">
        <aside className="w-full lg:w-64 flex-shrink-0">
          <ProductFilters />
        </aside>

        <div className="flex-1">
          <ProductGrid />
        </div>
      </div>
    </div>
  )
}
