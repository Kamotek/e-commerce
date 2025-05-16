package com.catalogservice.application.command.handler;

import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.messaging.producer.CreateProductPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.catalogservice.util.JsonUtils.jsonToList;
import static com.catalogservice.util.JsonUtils.jsonToMap;

@Slf4j
@Service
public class CreateProductCommandHandler {
    private final ProductRepository productRepository;
    private final CreateProductPublisher publisher;

    public CreateProductCommandHandler(ProductRepository productRepository, CreateProductPublisher publisher) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    public UUID handle(CreateProductCommand cmd) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(cmd.getName())
                .description(cmd.getDescription())
                .price(cmd.getPrice())
                .originalPrice(cmd.getOriginalPrice())
                .category(cmd.getCategory())
                .inventory(cmd.getInventory())
                .status(cmd.getStatus())
                .brand(cmd.getBrand())
                .badge(cmd.getBadge())
                .rating(cmd.getRating())
                .reviewCount(cmd.getReviewCount())
                .specifications(jsonToMap(cmd.getSpecification()))
                .imageUrls(jsonToList(cmd.getImageUrls()))
                .build();

        productRepository.save(product);
        publisher.publish(cmd);
        return product.getId();
    }
}
