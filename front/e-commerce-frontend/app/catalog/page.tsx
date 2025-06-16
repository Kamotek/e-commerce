// app/catalog/page.tsx
"use client"

import { useState, useEffect } from "react"
import { useSearchParams } from "next/navigation"
import ProductGrid from "@/components/product-grid"
import ProductCarousel from "@/components/product-carousel"
import ProductFilters, { FilterState } from "@/components/product-filters"

export interface ActiveFilters {
    priceRange: [number, number]
    categories: string[]
    brands: string[]
    inStock: boolean
    sortBy: string
}

export default function CatalogPage() {
    const searchParams = useSearchParams()
    const categoryParam = searchParams.get("category")

    const defaultFilters: ActiveFilters = {
        priceRange: [0, 2000],
        categories: categoryParam ? categoryParam.split(",") : [],
        brands: [],
        inStock: false,
        sortBy: 'featured',
    }

    const [activeFilters, setActiveFilters] = useState<ActiveFilters>(defaultFilters)
    const [currentPage, setCurrentPage] = useState(1)

    useEffect(() => {
        if (categoryParam) {
            setActiveFilters((prev) => ({
                ...prev,
                categories: categoryParam.split(","),
            }))
            setCurrentPage(1)
        } else {
            setActiveFilters((prev) => ({
                ...prev,
                categories: [],
            }))
        }
    }, [categoryParam])

    const handleApplyFilters = (newFilters: FilterState) => {
        setActiveFilters({
            priceRange: newFilters.priceRange,
            categories: newFilters.selectedCategories,
            brands: newFilters.selectedBrands,
            inStock: newFilters.inStock,
            sortBy: newFilters.sortBy,
        })
        setCurrentPage(1)
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-8">Product Catalog</h1>

            <section className="mb-12">
                <h2 className="text-2xl font-semibold mb-6">Featured Products</h2>
                <ProductCarousel />
            </section>

            <div className="flex flex-col lg:flex-row gap-8">
                <aside className="w-full lg:w-64 flex-shrink-0">
                    <ProductFilters
                        onApplyFilters={handleApplyFilters}
                        defaultValues={activeFilters}
                    />
                </aside>

                <div className="flex-1">
                    <ProductGrid
                        filters={activeFilters}
                        currentPage={currentPage}
                        onPageChange={setCurrentPage}
                    />
                </div>
            </div>
        </div>
    )
}
