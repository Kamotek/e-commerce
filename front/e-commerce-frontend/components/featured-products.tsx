// app/components/FeaturedProducts.tsx
"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { ShoppingCart, Eye } from "lucide-react"
import Link from "next/link"
import { fetchProductsFeatured, Product } from "@/lib/fetch-products"
import { useCart } from "@/contexts/CartContext"

export default function FeaturedProducts() {
  const [featuredProducts, setFeaturedProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState<boolean>(true)
  const [error, setError] = useState<string | null>(null)
  const [addedToCart, setAddedToCart] = useState<Record<string, boolean>>({})
  const { addToCart } = useCart()

  useEffect(() => {
    async function loadFeatured() {
      try {
        const products = await fetchProductsFeatured()
        setFeaturedProducts(products)
      } catch (err: any) {
        setError(err.message || "Unknown error")
      } finally {
        setLoading(false)
      }
    }
    loadFeatured()
  }, [])

  const handleAddToCart = (product: Product) => {
    addToCart(product, 1)
    setAddedToCart((prev) => ({
      ...prev,
      [product.id]: true,
    }))
    setTimeout(() => {
      setAddedToCart((prev) => ({
        ...prev,
        [product.id]: false,
      }))
    }, 2000)
  }

  if (loading) {
    return <div className="text-center py-8">Loading products...</div>
  }
  if (error) {
    return <div className="text-center py-8 text-red-500">error: {error}</div>
  }

  return (
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
        {featuredProducts.map((product) => (
            <Card key={product.id} className="overflow-hidden">
              <div className="relative">
                <img
                    src={
                      product.imageUrls && product.imageUrls.length > 0
                          ? product.imageUrls[0]
                          : "/placeholder.svg"
                    }
                    alt={product.name}
                    className="w-full h-48 object-cover"
                />
                {product.badge && (
                    <Badge className="absolute top-2 right-2">{product.badge}</Badge>
                )}
              </div>
              <CardContent className="p-4">
                <h3 className="font-semibold text-lg mb-1">{product.name}</h3>
                <p className="text-muted-foreground text-sm mb-2">{product.description}</p>
                <p className="font-bold text-lg">${product.price.toFixed(2)}</p>
              </CardContent>
              <CardFooter className="p-4 pt-0 flex gap-2">
                <Button
                    variant="default"
                    size="sm"
                    className={
                      addedToCart[product.id] ? "flex-1 animate-pulse" : "flex-1"
                    }
                    onClick={() => handleAddToCart(product)}
                    disabled={addedToCart[product.id]}
                >
                  {addedToCart[product.id] ? (
                      "Added!"
                  ) : (
                      <>
                        <ShoppingCart className="mr-2 h-4 w-4" />
                        Buy now!
                      </>
                  )}
                </Button>
                <Button variant="outline" size="sm" asChild>
                  <Link href={`/catalog/product/${product.id}`}>
                    <Eye className="mr-2 h-4 w-4" />
                    Details
                  </Link>
                </Button>
              </CardFooter>
            </Card>
        ))}
      </div>
  )
}
