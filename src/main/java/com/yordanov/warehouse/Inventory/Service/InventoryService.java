package com.yordanov.warehouse.Inventory.Service;

import com.yordanov.warehouse.Inventory.Model.Inventory;
import com.yordanov.warehouse.Inventory.Repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;


    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


}
