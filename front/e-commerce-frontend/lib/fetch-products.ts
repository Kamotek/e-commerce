export interface Product {
    id: string;
    name: string;
    description: string;
    price: number;
    originalPrice?: number;
    category: string;
    inventory?: number;
    status?: string;
    imageUrls?: string[];
    rating?: number;
    reviewCount?: number;
    specifications?: Record<string, string>;
    brand?: string;
    badge?: string;
}

export async function fetchProducts(): Promise<Product[]> {
    const res = await fetch("http://localhost:8085/api/catalog/products");
    if (!res.ok) {
        throw new Error(`Failed to load products: ${res.status}`);
    }
    return res.json();
}

export async function fetchProduct(id: string): Promise<Product> {
    const res = await fetch(`http://localhost:8085/api/catalog/products/${id}`);
    if (!res.ok) {
        throw new Error(`Failed to load product ${id}: ${res.status}`);
    }
    return res.json();
}

export async function fetchProductsFeatured(): Promise<Product[]> {
    const res = await fetch(`http://localhost:8085/api/catalog/products/featured`);
    if (!res.ok) {
        throw new Error(`Failed to load product featured ${res.status}`);
    }
    return res.json();
}
