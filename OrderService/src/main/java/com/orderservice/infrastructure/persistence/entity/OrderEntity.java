package com.orderservice.infrastructure.persistence.entity;

import com.orderservice.domain.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(nullable = false)
    private boolean finished;

    @Column(name = "ship_street", nullable = false)
    private String shippingStreet;

    @Column(name = "ship_city", nullable = false)
    private String shippingCity;

    @Column(name = "ship_postal_code", nullable = false)
    private String shippingPostalCode;

    @Column(name = "ship_country", nullable = false)
    private String shippingCountry;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Order.PaymentStatus paymentStatus;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @PrePersist
    void onCreate() {
        if (this.orderDate == null) {
            this.orderDate = Instant.now();
        }
        if (this.paymentStatus == null) {
            this.paymentStatus = Order.PaymentStatus.PENDING;
        }
    }
}
