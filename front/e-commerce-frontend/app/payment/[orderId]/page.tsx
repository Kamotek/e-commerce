"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { getOrder, Order } from "@/lib/order-api";
import { processPayment } from "@/lib/payment-api";
import { useCart } from "@/contexts/CartContext";
import { Button } from "@/components/ui/button";
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Loader2, AlertCircle, CheckCircle, XCircle } from "lucide-react";

export default function PaymentPage() {
    const params = useParams();
    const router = useRouter();
    const { clearCart } = useCart();
    const orderId = params.orderId as string;

    const [order, setOrder] = useState<Order | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isProcessing, setIsProcessing] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [paymentStatus, setPaymentStatus] = useState<"PENDING" | "COMPLETED" | "FAILED">("PENDING");
    const [cardNumber, setCardNumber] = useState("1111-2222-3333-4444");

    useEffect(() => {
        if (!orderId) {
            setError("Order ID is missing.");
            setIsLoading(false);
            return;
        }

        getOrder(orderId)
            .then(data => {
                setOrder(data);
                if (data.paymentStatus !== 'PENDING') {
                    setPaymentStatus(data.paymentStatus === 'CLOSED' ? 'COMPLETED' : 'FAILED');
                }
            })
            .catch(() => setError("Could not find your order. Please try again."))
            .finally(() => setIsLoading(false));
    }, [orderId]);

    const handlePayment = async () => {
        if (!order) return;

        setIsProcessing(true);
        setError(null);

        try {
            const response = await processPayment(order.id, cardNumber);
            setPaymentStatus(response.paymentStatus);
            if (response.paymentStatus === 'COMPLETED') {
                clearCart();
            }
        } catch (err: any) {
            setError(err.message || "An unknown payment error occurred.");
            setPaymentStatus("FAILED");
        } finally {
            setIsProcessing(false);
        }
    };

    if (isLoading) {
        return <div className="flex justify-center items-center h-screen"><Loader2 className="h-12 w-12 animate-spin text-primary" /></div>;
    }

    if (!order) {
        return (
            <div className="text-center mt-20 space-y-4">
                <h1 className="text-2xl font-bold">Order Not Found</h1>
                <p className="text-muted-foreground">{error}</p>
                <Button onClick={() => router.push('/')}>Go to Homepage</Button>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8 max-w-2xl">
            <Card>
                <CardHeader>
                    <CardTitle className="text-2xl text-center">
                        {paymentStatus === 'PENDING' && "Complete Your Payment"}
                        {paymentStatus === 'COMPLETED' && "Payment Successful"}
                        {paymentStatus === 'FAILED' && "Payment Failed"}
                    </CardTitle>
                    <p className="text-muted-foreground text-center">Order ID: {order.id}</p>
                </CardHeader>

                {paymentStatus === 'PENDING' && (
                    <>
                        <CardContent className="space-y-6">
                            <div className="text-4xl font-bold text-center">
                                Total: ${order.totalAmount?.toFixed(2) ?? '0.00'}
                            </div>
                            <div>
                                <Label htmlFor="card-number">Card Number (use '1111...' for guaranteed success)</Label>
                                <Input id="card-number" value={cardNumber} onChange={e => setCardNumber(e.target.value)} placeholder="xxxx-xxxx-xxxx-xxxx" />
                            </div>
                            {error && <p className="text-red-500 flex items-center gap-2"><AlertCircle size={16}/>{error}</p>}
                        </CardContent>
                        <CardFooter>
                            <Button className="w-full text-lg py-6" onClick={handlePayment} disabled={isProcessing}>
                                {isProcessing && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                                Pay ${order.totalAmount?.toFixed(2) ?? '0.00'}
                            </Button>
                        </CardFooter>
                    </>
                )}

                {paymentStatus === 'COMPLETED' && (
                    <CardContent className="text-center space-y-4 py-8">
                        <CheckCircle className="h-20 w-20 text-green-500 mx-auto" />
                        <h2 className="text-2xl font-bold">Thank you!</h2>
                        <p>Your order has been confirmed and paid successfully.</p>
                        <Button onClick={() => router.push('/')}>Continue Shopping</Button>
                    </CardContent>
                )}

                {paymentStatus === 'FAILED' && (
                    <CardContent className="text-center space-y-4 py-8">
                        <XCircle className="h-20 w-20 text-red-500 mx-auto" />
                        <h2 className="text-2xl font-bold">Something went wrong</h2>
                        <p className="text-muted-foreground">{error || "Please try again or use a different payment method."}</p>
                        <div className="flex gap-4 justify-center">
                            <Button variant="outline" onClick={() => { setPaymentStatus('PENDING'); setError(null); }}>Try Again</Button>
                            <Button onClick={() => router.push('/contact')}>Contact Support</Button>
                        </div>
                    </CardContent>
                )}
            </Card>
        </div>
    );
}