package com.catalogservice.application.command.handler;

import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.domain.model.Product;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.infrastructure.messaging.producer.CreateProductPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        Product product = new Product(
                cmd.getName(),
                cmd.getSpecification(),
                cmd.getDescription(),
                cmd.getPrice(),
                cmd.getCategory()
        );
        productRepository.save(product);

        publisher.publish(cmd);
        return product.getId();
    }
}
