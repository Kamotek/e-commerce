// app/catalog/[id]/ProductActions.tsx
"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { ShoppingCart } from "lucide-react";
import { useCart } from "@/contexts/CartContext";
import { Product } from "@/lib/fetch-products";

export default function ProductActions({ product }: { product: Product }) {
    const { addToCart } = useCart();
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [added, setAdded] = useState(false);

    const handleAddToCart = () => {
        addToCart(product, 1);
        setAdded(true);
        setTimeout(() => setAdded(false), 2000);
    };

    const handleBuyNow = () => {
        setLoading(true);
        addToCart(product, 1);
        setAdded(true);
        setTimeout(() => setAdded(false), 2000);
        router.push('/cart');
    };

    return (
        <div className="space-y-4">
            <div className="flex flex-col sm:flex-row gap-4">
                <Button
                    className={added ? 'flex-1 animate-pulse' : 'flex-1'}
                    size="lg"
                    onClick={handleAddToCart}
                    disabled={added}
                >
                    {added ? (
                        'Added!'
                    ) : (
                        <>
                            <ShoppingCart className="mr-2 h-5 w-5" />
                            Add to cart
                        </>
                    )}
                </Button>
                <Button
                    variant="outline"
                    size="lg"
                    onClick={handleBuyNow}
                    disabled={loading || added}
                >
                    {loading ? 'Redirecting...' : 'Buy Now'}
                </Button>
            </div>
        </div>
    );
}
