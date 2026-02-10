package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.StockRequest;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping("/deduct")
    @ResponseStatus(HttpStatus.OK)
    public void deductStock(@RequestBody List<StockRequest> stockRequests) {
        inventoryService.deductStock(stockRequests);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody Inventory inventory) {
        inventoryService.createInventory(inventory);
        return "Inventory Added Successfully";
    }
}
