package com.vegetarianlefty.controller;

import com.vegetarianlefty.service.InventoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * description
 *
 * @date 2025/1/17 15:51
 */
@RestController
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/inventory/sale")
    public String sale() {
        return inventoryService.sale();
    }
}
