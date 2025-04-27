package com.orderservice.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @PrePersist
    void onCreate() {
        this.orderDate = Instant.now();
    }
}
