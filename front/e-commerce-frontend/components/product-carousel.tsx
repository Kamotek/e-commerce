"use client"

import { useState, useEffect, useRef } from "react"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { ShoppingCart, Eye, ChevronLeft, ChevronRight } from "lucide-react"
import Link from "next/link"
import { fetchProductsFeatured, Product } from "@/lib/fetch-products"

export default function ProductCarousel() {
  const [products, setProducts] = useState<Product[]>([])
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const [currentSlide, setCurrentSlide] = useState(0)
  const containerRef = useRef<HTMLDivElement>(null)

  // liczba elementów na slajdzie
  const itemsPerSlide = 3
  // liczbę slajdów
  const slidesCount = Math.ceil(products.length / itemsPerSlide)

  // slice products per slide
  const slides = Array.from({ length: slidesCount }).map((_, idx) =>
      products.slice(idx * itemsPerSlide, idx * itemsPerSlide + itemsPerSlide)
  )

  // fetch featured
  useEffect(() => {
    async function load() {
      try {
        const data = await fetchProductsFeatured()
        setProducts(data)
      } catch (e: any) {
        setError(e.message)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  // auto slide
  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slidesCount)
    }, 10000)
    return () => clearInterval(interval)
  }, [slidesCount])

  if (loading) return <p>Loading...</p>
  if (error) return <p className="text-red-500">Error: {error}</p>
  if (products.length === 0) return <p>No featured products</p>

  const next = () => setCurrentSlide((prev) => (prev + 1) % slidesCount)
  const prev = () => setCurrentSlide((prev) => (prev - 1 + slidesCount) % slidesCount)

  return (
      <div className="relative overflow-hidden">
        <div
            className="flex transition-transform duration-500 ease-in-out"
            style={{ transform: `translateX(-${currentSlide * 100}%)` }}
            ref={containerRef}
        >
          {slides.map((group, idx) => (
              <div key={idx} className="min-w-full flex gap-4">
                {group.map((product) => (
                    <Card key={product.id} className="flex-1 min-w-0 overflow-hidden">
                      <div className="relative">
                        <img
                            src={product.imageUrls?.[0] || "/placeholder.svg"}
                            alt={product.name}
                            className="w-full h-48 object-cover"
                        />
                        {product.badge && (
                            <Badge className="absolute top-2 right-2">{product.badge}</Badge>
                        )}
                      </div>
                      <CardContent className="p-4">
                        <h3 className="font-semibold text-lg mb-1 truncate">{product.name}</h3>
                        <p className="text-muted-foreground text-sm mb-2 line-clamp-2">
                          {product.description}
                        </p>
                        <p className="font-bold text-lg">${product.price.toFixed(2)}</p>
                      </CardContent>
                      <CardFooter className="p-4 pt-0 flex gap-2">
                        <Button
                            variant="default"
                            size="sm"
                            className="flex-1"
                            onClick={() => {
                              // handle add to cart
                            }}
                        >
                          <ShoppingCart className="mr-2 h-4 w-4" /> Buy Now
                        </Button>
                        <Button variant="outline" size="sm" asChild>
                          <Link href={`/catalog/product/${product.id}`}>
                            <Eye className="mr-2 h-4 w-4" /> Details
                          </Link>
                        </Button>
                      </CardFooter>
                    </Card>
                ))}
              </div>
          ))}
        </div>

        {/* Navigation dots */}
        <div className="flex justify-center mt-4 gap-1">
          {slides.map((_, idx) => (
              <button
                  key={idx}
                  className={`w-3 h-3 rounded-full p-0 ${
                      currentSlide === idx ? "bg-primary" : "bg-muted"
                  }`}
                  onClick={() => setCurrentSlide(idx)}
                  aria-label={`Go to slide ${idx + 1}`}
              />
          ))}
        </div>

        {/* Arrows */}
        <button
            className="absolute top-1/2 left-2 transform -translate-y-1/2 bg-background p-2 rounded-full shadow-md"
            onClick={prev}
            aria-label="Previous slide"
        >
          <ChevronLeft className="h-4 w-4" />
        </button>
        <button
            className="absolute top-1/2 right-2 transform -translate-y-1/2 bg-background p-2 rounded-full shadow-md"
            onClick={next}
            aria-label="Next slide"
        >
          <ChevronRight className="h-4 w-4" />
        </button>
      </div>
  )
}
