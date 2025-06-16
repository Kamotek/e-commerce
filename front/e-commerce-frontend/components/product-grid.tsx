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

import { fetchProductsPage, Page, Product } from "@/lib/fetch-products";
import { ActiveFilters } from "@/app/catalog/page";
import { useCart } from "@/contexts/CartContext";

interface ProductGridProps {
    filters: ActiveFilters;
    currentPage: number;
    onPageChange: (page: number) => void;
}

export default function ProductGrid({
                                        filters,
                                        currentPage,
                                        onPageChange,
                                    }: ProductGridProps) {
    const [pageData, setPageData] = useState<Page<Product> | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [justAdded, setJustAdded] = useState<Record<string, boolean>>({});

    const { addToCart } = useCart();
    const productsPerPage = 9;

    useEffect(() => {
        setLoading(true);
        setError(null);

        fetchProductsPage(currentPage, productsPerPage, filters)
            .then((data) => setPageData(data))
            .catch((e: any) => setError(e.message))
            .finally(() => setLoading(false));
    }, [currentPage, filters]);

    const handleAddToCart = (product: Product) => {
        addToCart(product, 1);
        setJustAdded((prev) => ({ ...prev, [product.id]: true }));
        setTimeout(() => {
            setJustAdded((prev) => ({ ...prev, [product.id]: false }));
        }, 2000);
    };

    if (loading) return <p>Loading products...</p>;
    if (error) return <p className="text-red-500">Error: {error}</p>;
    if (!pageData) return null;

    const { content: products, totalPages, number: zeroBasedPage } = pageData;
    const page = zeroBasedPage + 1;

    return (
        <>
            <div className="flex justify-between items-center mb-4">
                <p>
                    Strona {page} z {totalPages}
                </p>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                {products.map((product) => (
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
                                <p className="font-bold text-lg">
                                    ${product.price.toFixed(2)}
                                </p>
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
                                onClick={() => handleAddToCart(product)}
                            >
                                {justAdded[product.id] ? (
                                    "Added!"
                                ) : (
                                    <>
                                        <ShoppingCart className="mr-2 h-4 w-4" /> Buy now!
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
                                if (page > 1) onPageChange(page - 1);
                            }}
                            className={page === 1 ? "pointer-events-none opacity-50" : ""}
                        />
                    </PaginationItem>

                    {Array.from({ length: totalPages }).map((_, i) => {
                        const p = i + 1;
                        if (
                            p === 1 ||
                            p === totalPages ||
                            (p >= page - 1 && p <= page + 1)
                        ) {
                            return (
                                <PaginationItem key={p}>
                                    <PaginationLink
                                        href="#"
                                        onClick={(e) => {
                                            e.preventDefault();
                                            onPageChange(p);
                                        }}
                                        isActive={p === page}
                                    >
                                        {p}
                                    </PaginationLink>
                                </PaginationItem>
                            );
                        }
                        if (
                            (p === 2 && page > 3) ||
                            (p === totalPages - 1 && page < totalPages - 2)
                        ) {
                            return (
                                <PaginationItem key={p}>
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
                                if (page < totalPages) onPageChange(page + 1);
                            }}
                            className={
                                page === totalPages ? "pointer-events-none opacity-50" : ""
                            }
                        />
                    </PaginationItem>
                </PaginationContent>
            </Pagination>
        </>
    );
}
