export interface CreateOrderItemCommand {
    productId: string;
    quantity: number;
}

export interface CreateOrderCommand {
    userId: string;
    orderDate: string;
    shippingStreet: string;
    shippingCity: string;
    shippingPostalCode: string;
    shippingCountry: string;
    items: CreateOrderItemCommand[];
}

export interface OrderItem {
    productId: string;
    quantity: number;
}

export interface Order {
    id: string;
    userId: string;
    orderDate: string;
    shippingStreet: string;
    shippingCity: string;
    shippingPostalCode: string;
    shippingCountry: string;
    items: OrderItem[];
    finished: boolean;
    paymentStatus: "PENDING" | "CLOSED" | "FAILED" | "PAID";
    totalAmount: number; 

}

const BASE_URL = process.env.NEXT_PUBLIC_BFF_URL || "http://localhost:8085/api";

async function handleResponse<T>(res: Response): Promise<T> {
    if (!res.ok) {
        const text = await res.text();
        throw new Error(`API error ${res.status}: ${text}`);
    }
    return res.json();
}

export async function createOrder(command: CreateOrderCommand): Promise<Order> {
    const res = await fetch(`${BASE_URL}/orders`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(command),
        credentials: "include",
    });
    return handleResponse<Order>(res);
}

export async function getOrder(orderId: string): Promise<Order> {
    const res = await fetch(`${BASE_URL}/orders/${orderId}`, {
        credentials: "include",
    });
    return handleResponse<Order>(res);
}


