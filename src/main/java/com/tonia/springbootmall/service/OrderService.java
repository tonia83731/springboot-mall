package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.CreateOrderRequest;
import com.tonia.springbootmall.model.Order;

public interface OrderService {
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
