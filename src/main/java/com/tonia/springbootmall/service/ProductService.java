package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.ProductRequest;
import com.tonia.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
}
