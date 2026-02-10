package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.StockRequest;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        log.info("Checking stock for SKU codes: {}", skuCodes);
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .quantity(inventory.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deductStock(List<StockRequest> stockRequests) {
        log.info("Deducting stock for requests: {}", stockRequests);
        for (StockRequest request : stockRequests) {
            Inventory inventory = inventoryRepository.findBySkuCode(request.getSkuCode())
                    .orElseThrow(() -> new RuntimeException("Product not found in inventory: " + request.getSkuCode()));

            if (inventory.getQuantity() < request.getQuantity()) {
                throw new RuntimeException("Insufficient stock for SKU: " + request.getSkuCode());
            }

            inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

    public void createInventory(Inventory inventory) {
        log.info("Adding inventory for SKU: {}", inventory.getSkuCode());
        inventoryRepository.save(inventory);
    }
}
