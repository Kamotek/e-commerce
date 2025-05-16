package com.catalogservice.infrastructure.controller;

import com.catalogservice.application.command.handler.CreateProductCommandHandler;
import com.catalogservice.application.command.handler.UpdateProductCommandHandler;
import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.application.query.handler.ReadProductQueryHandler;
import com.catalogservice.domain.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/catalog/products")
public class CatalogController {

    private final CreateProductCommandHandler createHandler;
    private final UpdateProductCommandHandler updateHandler;
    private final ReadProductQueryHandler readHandler;

    public CatalogController(
            CreateProductCommandHandler createHandler,
            UpdateProductCommandHandler updateHandler,
            ReadProductQueryHandler readHandler) {
        this.createHandler = createHandler;
        this.updateHandler = updateHandler;
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

    @PostMapping("/batch")
    public ResponseEntity<Void> createProductsBatch(
            @RequestBody List<CreateProductCommand> cmds
    ) {
        for (CreateProductCommand cmd : cmds) {
            createHandler.handle(cmd);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/featured")
    public List<Product> getFeaturedProducts() {
        return readHandler.handleFindAllFeatured();
    }

    @GetMapping
    public List<Product> getAll() {
        return readHandler.handleFindAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable UUID id) {
        return readHandler.handleFindById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductCommand cmd) {
        cmd.setId(id);
        updateHandler.handle(cmd);
        return ResponseEntity.noContent().build();
    }
}
