package com.tonia.springbootmall.service.impl;

import com.tonia.springbootmall.dao.OrderDao;
import com.tonia.springbootmall.dao.ProductDao;
import com.tonia.springbootmall.dto.BuyItem;
import com.tonia.springbootmall.dto.CreateOrderRequest;
import com.tonia.springbootmall.model.OrderItem;
import com.tonia.springbootmall.model.Product;
import com.tonia.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional
    public  Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest){
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());
            Integer amount = product.getPrice() * buyItem.getQuantity();
            totalAmount += amount;

            // transform buyitem to orderitem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        Integer orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createOrderItems(orderId, orderItemList);
        return orderId;
    }
}
