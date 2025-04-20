package com.catalogservice.infrastructure.controller;

import com.catalogservice.application.command.handler.CreateProductCommandHandler;
import com.catalogservice.application.command.handler.UpdateProductCommandHandler;
import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.application.query.handler.ReadProductQueryHandler;
import com.catalogservice.domain.repository.ProductRepository;
import com.catalogservice.domain.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog/products")
public class CatalogController {

    private final CreateProductCommandHandler createHandler;
    private final UpdateProductCommandHandler updateHandler;
    private final ProductRepository productRepository;
    private final ReadProductQueryHandler readHandler;

    public CatalogController(CreateProductCommandHandler createHandler,
                             UpdateProductCommandHandler updateHandler,
                             ProductRepository productRepository, ReadProductQueryHandler readHandler) {
        this.createHandler       = createHandler;
        this.updateHandler       = updateHandler;
        this.productRepository   = productRepository;
        this.readHandler = readHandler;
    }

    @PostMapping
    public ResponseEntity<UUID> createProduct(@RequestBody CreateProductCommand cmd) {
        UUID newId = createHandler.handle(cmd);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newId)
                .toUri();
        return ResponseEntity.created(location).body(newId);
    }

    @GetMapping
    public List<Product> getAll() {
        return readHandler.handleFindAll().stream()
                .map(p -> new Product(
                        p.getId(), p.getName(), p.getDescription(),
                        p.getPrice(), p.getCategory()))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        return readHandler.handleFindById(id)
                .map(p -> ResponseEntity.ok(new Product(
                        p.getId(), p.getName(), p.getDescription(),
                        p.getPrice(), p.getCategory())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id,
                                              @RequestBody UpdateProductCommand cmd) {
        cmd.setId(id);
        updateHandler.handle(cmd);
        return ResponseEntity.noContent().build();
    }
}
