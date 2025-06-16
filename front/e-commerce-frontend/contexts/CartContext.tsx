// File: /contexts/CartContext.tsx
"use client";

import React, {
    createContext,
    useContext,
    useState,
    useEffect,
    ReactNode,
} from "react";
import { Product } from "@/lib/fetch-products";

export interface CartItem {
    product: Product;
    quantity: number;
}

interface CartContextType {
    items: CartItem[];
    totalItems: number;
    totalPrice: number;
    addToCart: (product: Product, qty?: number) => void;
    removeFromCart: (productId: string) => void;
    updateQuantity: (productId: string, qty: number) => void;
    clearCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

const LOCAL_STORAGE_KEY = "myshop_cart";

export function CartProvider({ children }: { children: ReactNode }) {
    const [items, setItems] = useState<CartItem[]>([]);

    useEffect(() => {
        const data = localStorage.getItem(LOCAL_STORAGE_KEY);
        if (data) {
            try {
                setItems(JSON.parse(data));
            } catch {
                setItems([]);
            }
        }
    }, []);

    useEffect(() => {
        localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(items));
    }, [items]);

    const addToCart = (product: Product, qty: number = 1) => {
        setItems((prev) => {
            const idx = prev.findIndex((i) => i.product.id === product.id);
            if (idx !== -1) {
                const next = [...prev];
                next[idx].quantity += qty;
                return next;
            }
            return [...prev, { product, quantity: qty }];
        });
    };

    const removeFromCart = (productId: string) => {
        setItems((prev) => prev.filter((i) => i.product.id !== productId));
    };

    const updateQuantity = (productId: string, qty: number) => {
        if (qty <= 0) {
            return removeFromCart(productId);
        }
        setItems((prev) =>
            prev.map((i) =>
                i.product.id === productId ? { ...i, quantity: qty } : i
            )
        );
    };

    const clearCart = () => {
        setItems([]);
    };

    const totalItems = items.reduce((sum, i) => sum + i.quantity, 0);
    const totalPrice = items.reduce(
        (sum, i) => sum + i.quantity * i.product.price,
        0
    );

    return (
        <CartContext.Provider
            value={{
                items,
                totalItems,
                totalPrice,
                addToCart,
                removeFromCart,
                updateQuantity,
                clearCart,
            }}
        >
            {children}
        </CartContext.Provider>
    );
}

export function useCart(): CartContextType {
    const ctx = useContext(CartContext);
    if (!ctx) {
        throw new Error("useCart must be used within a CartProvider");
    }
    return ctx;
}
