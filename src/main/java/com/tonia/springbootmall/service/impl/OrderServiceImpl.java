package com.tonia.springbootmall.service.impl;

import com.tonia.springbootmall.dao.OrderDao;
import com.tonia.springbootmall.dao.ProductDao;
import com.tonia.springbootmall.dao.UserDao;
import com.tonia.springbootmall.dto.BuyItem;
import com.tonia.springbootmall.dto.CreateOrderRequest;
import com.tonia.springbootmall.model.Order;
import com.tonia.springbootmall.model.OrderItem;
import com.tonia.springbootmall.model.Product;
import com.tonia.springbootmall.model.User;
import com.tonia.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        List<OrderItem> orderItems = orderDao.getOrderItemsByOrderId(orderId);
        order.setOrderItemList(orderItems);
        return order;
    }

    @Override
    @Transactional
    public  Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest){
        User user = userDao.getUserById(userId);
        if (user == null){
            log.warn("User {} not exists!", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not exists!");
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for (BuyItem buyItem : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            // checked product exist status && product stock
            if (product == null) {
                log.warn("Product {} not exists!", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product " + buyItem.getProductId() + " not exists!");
            } else if (product.getStock() < buyItem.getQuantity()) {
                log.warn("Product {} out of stock!", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product "+  buyItem.getProductId() +" out of stock!");
            }
            // updated product stock
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            // calculated total
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
