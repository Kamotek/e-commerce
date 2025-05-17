"use client";

import { useState, useEffect } from "react";
import { Card, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ShoppingCart, Eye } from "lucide-react";
import Link from "next/link";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { fetchProducts, Product } from "@/lib/fetch-products";

export default function ProductGrid() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [addedToCart, setAddedToCart] = useState<Record<string, boolean>>({});
  const [currentPage, setCurrentPage] = useState(1);

  const productsPerPage = 9;
  const totalPages = Math.ceil(products.length / productsPerPage);
  const currentProducts = products.slice(
      (currentPage - 1) * productsPerPage,
      currentPage * productsPerPage
  );

  useEffect(() => {
    async function load() {
      try {
        const data = await fetchProducts();
        setProducts(data);
      } catch (e: any) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  const handleAddToCart = (id: string) => {
    setAddedToCart((prev) => ({ ...prev, [id]: true }));
    setTimeout(() => {
      setAddedToCart((prev) => ({ ...prev, [id]: false }));
    }, 2000);
  };

  if (loading) return <p>Loading products...</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;

  return (
      <>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {currentProducts.map((product) => (
              <Card key={product.id} className="overflow-hidden">
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
                  <h3 className="font-semibold text-lg mb-1">{product.name}</h3>
                  <p className="text-muted-foreground text-sm mb-2">
                    {product.description}
                  </p>
                  <div className="flex items-baseline gap-2">
                    <p className="font-bold text-lg">${product.price.toFixed(2)}</p>
                    {product.originalPrice && (
                        <p className="text-sm line-through text-muted-foreground">
                          ${product.originalPrice.toFixed(2)}
                        </p>
                    )}
                  </div>
                </CardContent>
                <CardFooter className="p-4 pt-0 flex gap-2">
                  <Button
                      variant="default"
                      size="sm"
                      className="flex-1"
                      onClick={() => handleAddToCart(product.id)}
                  >
                    {addedToCart[product.id] ? (
                        "Added!"
                    ) : (
                        <>
                          <ShoppingCart className="mr-2 h-4 w-4" /> Buy Now
                        </>
                    )}
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

        <Pagination className="mt-8">
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    if (currentPage > 1) setCurrentPage(currentPage - 1);
                  }}
                  className={currentPage === 1 ? "pointer-events-none opacity-50" : ""}
              />
            </PaginationItem>

            {Array.from({ length: totalPages }).map((_, i) => {
              const page = i + 1;
              if (
                  page === 1 ||
                  page === totalPages ||
                  (page >= currentPage - 1 && page <= currentPage + 1)
              ) {
                return (
                    <PaginationItem key={page}>
                      <PaginationLink
                          href="#"
                          onClick={(e) => {
                            e.preventDefault();
                            setCurrentPage(page);
                          }}
                          isActive={page === currentPage}
                      >
                        {page}
                      </PaginationLink>
                    </PaginationItem>
                );
              }
              if (
                  (page === 2 && currentPage > 3) ||
                  (page === totalPages - 1 && currentPage < totalPages - 2)
              ) {
                return (
                    <PaginationItem key={page}>
                      <PaginationEllipsis />
                    </PaginationItem>
                );
              }
              return null;
            })}

            <PaginationItem>
              <PaginationNext
                  href="#"
                  onClick={(e) => {
                    e.preventDefault();
                    if (currentPage < totalPages) setCurrentPage(currentPage + 1);
                  }}
                  className={currentPage === totalPages ? "pointer-events-none opacity-50" : ""}
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      </>
  );
}
