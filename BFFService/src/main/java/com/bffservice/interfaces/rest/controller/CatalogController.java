package com.bffservice.interfaces.rest.controller;


import com.bffservice.application.command.model.CreateProductCommand;
import com.bffservice.application.command.model.UpdateProductCommand;
import com.bffservice.domain.model.Product;
import com.bffservice.interfaces.rest.CatalogServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog/products")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogServiceClient catalogClient;

    @PostMapping
    public ResponseEntity<UUID> createProduct(
            @Valid @RequestBody CreateProductCommand cmd
    ) {
        ResponseEntity<UUID> resp = catalogClient.createProduct(cmd);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resp.getBody())
                .toUri();
        return ResponseEntity.created(location).body(resp.getBody());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        ResponseEntity<List<Product>> resp = catalogClient.getAllProducts();
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @GetMapping("/featured")
    public ResponseEntity<List<Product>> getFeaturedProducts() {
        ResponseEntity<List<Product>> resp = catalogClient.getAllProductsFeatured();
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(
            @PathVariable UUID id
    ) {
        ResponseEntity<Product> resp = catalogClient.getProductById(id);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductCommand cmd
    ) {
        cmd.setId(id);
        ResponseEntity<Void> resp = catalogClient.updateProduct(id, cmd);
        return ResponseEntity.status(resp.getStatusCode()).build();
    }
}
