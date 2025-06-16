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
