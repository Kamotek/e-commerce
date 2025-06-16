import { ActiveFilters } from "@/app/catalog/page"; // Upewnij się, że ścieżka jest poprawna

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

export interface Page<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
}

const BASE_URL = "http://localhost:8085/api/catalog/products";


export async function fetchProduct(id: string): Promise<Product> {
    const res = await fetch(`${BASE_URL}/${id}`);
    if (!res.ok) {
        throw new Error(`Failed to load product ${id}: ${res.status}`);
    }
    return res.json();
}

export async function fetchProductsFeatured(): Promise<Product[]> {
    const res = await fetch(`${BASE_URL}/featured`);
    if (!res.ok) {
        throw new Error(`Failed to load featured products: ${res.status}`);
    }
    return res.json();
}

export async function fetchProductsByCategory(
    category: string,
    size: number
): Promise<Product[]> {
    const url = new URL(`${BASE_URL}/filter`, window.location.origin);
    url.searchParams.set("category", category);
    url.searchParams.set("page", "0");
    url.searchParams.set("size", size.toString());

    const res = await fetch(url.toString());
    if (!res.ok) {
        throw new Error(`Failed to load products by category: ${res.status}`);
    }
    const pageData: Page<Product> = await res.json();
    return pageData.content;
}

export async function fetchProductsPage(
    page: number,
    size: number,
    filters: ActiveFilters
): Promise<Page<Product>> {
    const zeroBased = page - 1;
    const url = new URL(`${BASE_URL}/filter`);
    url.searchParams.set("page", zeroBased.toString());
    url.searchParams.set("size", size.toString());

    if (filters.categories.length > 0) {
        url.searchParams.set("category", filters.categories[0]);
    }
    if (filters.brands.length > 0) {
        url.searchParams.set("brand", filters.brands[0]);
    }
    if (filters.priceRange && filters.priceRange[1] < 2000) {
        url.searchParams.set("maxPrice", filters.priceRange[1].toString());
    }
    if (filters.inStock) {
        url.searchParams.set("availableOnly", "true");
    }

    if (filters.sortBy && filters.sortBy !== 'featured') {
        let sortParam = '';
        switch (filters.sortBy) {
            case 'price-low':
                sortParam = 'price,asc';
                break;
            case 'price-high':
                sortParam = 'price,desc';
                break;
            case 'rating':
                sortParam = 'rating,desc';
                break;
        }
        if (sortParam) {
            url.searchParams.set('sort', sortParam);
        }
    }

    console.log("Fetching products with URL:", url.toString());

    const res = await fetch(url.toString());
    if (!res.ok) {
        throw new Error(`Failed to load products page: ${res.status} - ${await res.text()}`);
    }
    return res.json();
}

export async function createProduct(productData: Omit<Product, 'id'>): Promise<Product> {
    const res = await fetch(BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData),
    });
    if (!res.ok) {
        const errorBody = await res.text();
        throw new Error(`Failed to create product: ${res.status} ${errorBody}`);
    }
    const location = res.headers.get('Location');
    if (!location) {
        throw new Error('Location header not found in response after creating product');
    }
    const getNewProductResponse = await fetch(location);
    if (!getNewProductResponse.ok) {
        throw new Error('Failed to fetch newly created product from location header.');
    }
    return getNewProductResponse.json();
}


export async function updateProduct(id: string, productData: Partial<Product>): Promise<void> {
    const res = await fetch(`${BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData),
    });
    if (!res.ok) {
        const errorBody = await res.text();
        throw new Error(`Failed to update product: ${res.status} ${errorBody}`);
    }
}


export async function deleteProduct(id: string): Promise<void> {
    const res = await fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
    });
    if (!res.ok) {
        const errorBody = await res.text();
        throw new Error(`Failed to delete product: ${res.status} ${errorBody}`);
    }
}

export async function fetchAllProductsAdmin(page: number, size: number): Promise<Page<Product>> {
    const url = new URL(`${BASE_URL}/filter`);
    url.searchParams.set("page", (page - 1).toString());
    url.searchParams.set("size", size.toString());

    const res = await fetch(url.toString());
    if (!res.ok) {
        throw new Error(`Failed to load products for admin: ${res.status}`);
    }
    return res.json();
}
