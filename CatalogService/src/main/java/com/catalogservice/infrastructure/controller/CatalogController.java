package com.catalogservice.infrastructure.controller;

import com.catalogservice.application.command.handler.CreateProductCommandHandler;
import com.catalogservice.application.command.handler.DeleteProductCommandHandler;
import com.catalogservice.application.command.handler.UpdateProductCommandHandler;
import com.catalogservice.application.command.model.CreateProductCommand;
import com.catalogservice.application.command.model.DeleteProductCommand;
import com.catalogservice.application.command.model.UpdateProductCommand;
import com.catalogservice.application.query.handler.ReadProductQueryHandler;
import com.catalogservice.domain.model.PagedResult;
import com.catalogservice.domain.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/catalog/products")
public class CatalogController {

    private final CreateProductCommandHandler createHandler;
    private final UpdateProductCommandHandler updateHandler;
    private final ReadProductQueryHandler readHandler;
    private final DeleteProductCommandHandler deleteHandler;

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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductCommand cmd) {
        cmd.setId(id);
        updateHandler.handle(cmd);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/filter")
    public ResponseEntity<PagedResult<Product>> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean availableOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        Page<Product> resultPage = readHandler.handleFindByFilters(
                category,
                maxPrice,
                brand,
                availableOnly,
                page,
                size,
                sort
        );

        PagedResult<Product> pagedResult = new PagedResult<>(
                resultPage.getContent(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.isLast()
        );

        return ResponseEntity.ok(pagedResult);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        deleteHandler.handle(new DeleteProductCommand(id));
        return ResponseEntity.noContent().build();
    }


}

