package com.catalogservice.application.command.handler;

import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.domain.model.Product;
import com.catalogservice.infrastructure.messaging.producer.UpdateProductPublisher;
import com.catalogservice.util.JsonUtils;
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

        // Update basic fields
        existing.setName(cmd.getName());
        existing.setDescription(cmd.getDescription());
        existing.setPrice(cmd.getPrice());
        existing.setOriginalPrice(cmd.getOriginalPrice());
        existing.setCategory(cmd.getCategory());
        existing.setInventory(cmd.getInventory());
        existing.setStatus(cmd.getStatus());
        existing.setBrand(cmd.getBrand());
        existing.setBadge(cmd.getBadge());
        existing.setRating(cmd.getRating());
        existing.setReviewCount(cmd.getReviewCount());

        // Update structured fields from JSON
        existing.setSpecifications(JsonUtils.jsonToMap(cmd.getSpecificationsJson()));
        existing.setImageUrls(JsonUtils.jsonToList(cmd.getImageUrlsJson()));

        // Publish and persist
        publisher.publish(cmd);
        productRepository.save(existing);
    }
}
