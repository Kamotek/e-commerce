// /types/orders.ts

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
    paymentStatus: "PENDING" | "CLOSED" | "FAILED";
}

export interface OrderLink {
    rel: string;
    href: string;
    method: string;
}

export interface OrderResource {
    id: string;
    userId: string;
    orderDate: string;
    shippingStreet: string;
    shippingCity: string;
    shippingPostalCode: string;
    shippingCountry: string;
    items: { productId: string; quantity: number }[];
    paymentStatus: string;
    finished: boolean;
    totalAmount: number;
    links: OrderLink[];
}