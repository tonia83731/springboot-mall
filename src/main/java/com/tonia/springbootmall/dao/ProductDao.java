package com.tonia.springbootmall.dao;

import com.tonia.springbootmall.model.Product;

public interface ProductDao {
    Product getProductById(Integer productId);
}
