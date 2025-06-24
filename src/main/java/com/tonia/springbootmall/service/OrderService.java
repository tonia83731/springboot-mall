package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.CreateOrderRequest;
import com.tonia.springbootmall.dto.OrderQueryParams;
import com.tonia.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Order getOrderById(Integer orderId);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Integer countOrder(OrderQueryParams orderQueryParams);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);


}
