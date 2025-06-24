package com.tonia.springbootmall.controller;

import com.tonia.springbootmall.dto.CreateOrderRequest;
import com.tonia.springbootmall.model.Order;
import com.tonia.springbootmall.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<Order> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        Integer orderId = orderService.createOrder(userId, createOrderRequest);
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
