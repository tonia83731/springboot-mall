package com.tonia.springbootmall.dao.impl;

import com.tonia.springbootmall.dao.OrderDao;
import com.tonia.springbootmall.model.Order;
import com.tonia.springbootmall.model.OrderItem;
import com.tonia.springbootmall.rollmapper.OrderItemRowMapper;
import com.tonia.springbootmall.rollmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` " +
                "WHERE order_id = :orderId";
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if (orderList.size() > 0) {
            return orderList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<OrderItem>  getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT\n" +
                "   oi.order_item_id,\n" +
                "   oi.order_id,\n" +
                "   oi.product_id AS product_id,\n" +
                "   oi.quantity,\n" +
                "   oi.amount,\n" +
                "   p.product_name,\n" +
                "   p.image_url\n" +
                "FROM order_item as oi " +
                "LEFT JOIN product as p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
        return orderItemList;
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount) {
        String sql = "INSERT INTO `order`(user_id, total_amount, created_date, last_modified_date) " +
                "VALUES (:userId, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String,Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        Integer orderId = keyHolder.getKey().intValue();

        return orderId;
    }
    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
//        for (OrderItem orderItem : orderItemList) {
//            String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount)" +
//                    "VALUES (:order_id, :product_id, :quantity, :amount)";
//            Map<String,Object> map = new HashMap<>();
//            map.put("order_id", orderId);
//            map.put("product_id", orderItem.getProductId());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//            namedParameterJdbcTemplate.update(sql, map);
//        }

        String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount)" +
                "VALUES (:orderId, :productId, :quantity, :amount)";
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("orderId", orderId);
            mapSqlParameterSource.addValue("productId", orderItem.getProductId());
            mapSqlParameterSource.addValue("quantity", orderItem.getQuantity());
            mapSqlParameterSource.addValue("amount", orderItem.getAmount());

            parameterSources[i] = mapSqlParameterSource;
        }
        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }
}
