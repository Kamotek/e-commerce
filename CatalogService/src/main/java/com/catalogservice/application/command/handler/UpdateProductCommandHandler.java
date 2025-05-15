package com.catalogservice.application.command.handler;

import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.domain.model.Product;
import com.catalogservice.infrastructure.messaging.producer.UpdateProductPublisher;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UpdateProductCommandHandler {
    private final ProductRepository productRepository;
    private final UpdateProductPublisher publisher;

    public UpdateProductCommandHandler(ProductRepository productRepository, UpdateProductPublisher publisher) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    public void handle(UpdateProductCommand cmd) {
        Product existing = productRepository.findById(cmd.getId())
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + cmd.getId()));
        existing.setName(cmd.getName());
        existing.setSpecification(cmd.getSpecification());
        existing.setDescription(cmd.getDescription());
        existing.setPrice(cmd.getPrice());
        existing.setCategory(cmd.getCategory());
        publisher.publish(cmd);
        productRepository.save(existing);
    }
}
