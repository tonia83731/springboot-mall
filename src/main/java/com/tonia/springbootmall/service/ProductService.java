package com.tonia.springbootmall.service;

import com.tonia.springbootmall.dto.ProductRequest;
import com.tonia.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Product getProductById(Integer productId);
    List<Product> getProducts();
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
