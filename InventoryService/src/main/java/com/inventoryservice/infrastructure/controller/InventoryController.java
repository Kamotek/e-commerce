package com.inventoryservice.infrastructure.controller;

import com.inventoryservice.application.dto.CreateItemRequest;
import com.inventoryservice.application.dto.UpdateItemRequest;
import com.inventoryservice.application.dto.StockItemDto;
import com.inventoryservice.application.mapper.StockItemMapper;
import com.inventoryservice.application.command.model.AddStockCommand;
import com.inventoryservice.application.command.model.RemoveStockCommand;
import com.inventoryservice.application.command.handler.AddStockCommandHandler;
import com.inventoryservice.application.command.handler.RemoveStockCommandHandler;
import com.inventoryservice.domain.repository.StockRepository;
import com.inventoryservice.domain.model.StockItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final StockRepository repo;
    private final StockItemMapper mapper;
    private final AddStockCommandHandler addHandler;
    private final RemoveStockCommandHandler removeHandler;

    @PostMapping("/items")
    public ResponseEntity<StockItemDto> createItem(@Valid @RequestBody CreateItemRequest request) {
        StockItem created = repo.createItem(request.toDomain());
        StockItemDto dto = mapper.toDto(created);
        URI location = URI.create("/api/inventory/items/" + dto.getProductId());
        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<StockItemDto> updateItem(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateItemRequest request) {
        StockItem updated = repo.updateItem(productId, request.toDomain(productId));
        StockItemDto dto = mapper.toDto(updated);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteItem(@PathVariable UUID productId) {
        repo.deleteItem(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items/{productId}")
    public ResponseEntity<StockItemDto> getItem(@PathVariable UUID productId) {
        return repo.findByProductId(productId)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/items")
    public ResponseEntity<List<StockItemDto>> listItems() {
        List<StockItemDto> list = repo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/stock/{productId}/add")
    public ResponseEntity<Void> addStock(
            @PathVariable UUID productId,
            @RequestParam int amount) {
        log.info("Dziala");
        addHandler.handle(new AddStockCommand(productId, amount));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> add(@Valid @RequestBody AddStockCommand command) {

    }

    @PostMapping("/stock/{productId}/remove")
    public ResponseEntity<Void> removeStock(
            @PathVariable UUID productId,
            @RequestParam int amount) {
        removeHandler.handle(new RemoveStockCommand(productId, amount));
        return ResponseEntity.ok().build();
    }
}
