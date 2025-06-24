package com.tonia.springbootmall.dao;

import com.tonia.springbootmall.constant.ProductCategory;
import com.tonia.springbootmall.dto.ProductQueryParams;
import com.tonia.springbootmall.dto.ProductRequest;
import com.tonia.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProductById(Integer productId);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Integer countProducts(ProductQueryParams productQueryParams);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void updateStock(Integer productId, Integer remainStock);
    void deleteProductById(Integer productId);
}
