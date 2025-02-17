package com.vegetarianlefty.controller;

import com.vegetarianlefty.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * description
 *
 */

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping( "/order/add")
    public void addOrder()
    {
        orderService.addOrder();
    }


    @PostMapping(  "/order/{id}")
    public String findUserById(@PathVariable Integer id)
    {
        return orderService.getOrderById(id);
    }
}
