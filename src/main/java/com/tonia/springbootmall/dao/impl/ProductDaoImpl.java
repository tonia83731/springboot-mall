package com.tonia.springbootmall.dao.impl;

import com.tonia.springbootmall.constant.ProductCategory;
import com.tonia.springbootmall.dao.ProductDao;
import com.tonia.springbootmall.dto.ProductQueryParams;
import com.tonia.springbootmall.dto.ProductRequest;
import com.tonia.springbootmall.model.Product;
import com.tonia.springbootmall.rollmapper.ProductRollMapper;
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
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "select product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date " +
                "FROM product " +
                "WHERE product_id = :productId ";
        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRollMapper());
        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, description, price, stock, created_date, last_modified_date  " +
                "FROM product WHERE 1=1";
        Map<String,Object> map = new HashMap<>();
        sql = addFilteringSql(sql, map, productQueryParams);

        sql += " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
        sql += " LIMIT " + productQueryParams.getLimit() + " OFFSET " + productQueryParams.getOffset();
        List<Product> productList = namedParameterJdbcTemplate.query(sql,map, new ProductRollMapper());
        return productList;
    }

    @Override
    public Integer countProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";
        Map<String,Object> map = new HashMap<>();

        sql = addFilteringSql(sql, map, productQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, created_date, last_modified_date)" +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";
        Map<String,Object> map = new HashMap<>();
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product " +
                "SET product_name = :productName, " +
                "category = :category, " +
                "image_url = :imageUrl, " +
                "price = :price, " +
                "stock = :stock, " +
                "description = :description, " +
                "last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);
        map.put("productName",productRequest.getProductName());
        map.put("category",productRequest.getCategory().toString());
        map.put("imageUrl",productRequest.getImageUrl());
        map.put("price",productRequest.getPrice());
        map.put("stock",productRequest.getStock());
        map.put("description",productRequest.getDescription());
        Date now = new Date();
        map.put("lastModifiedDate",now);
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map));
    }

    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";
        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);
        namedParameterJdbcTemplate.update(sql, map);
    }
    // ----------------------------------------------------------------------------------
    private String addFilteringSql(String sql, Map<String,Object> map, ProductQueryParams productQueryParams) {
        if (productQueryParams.getCategory() != null) {
            sql += " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }
        if (productQueryParams.getSearch() != null) {
            sql += " AND product_name LIKE :search ";
            map.put("search", "%"+productQueryParams.getSearch()+"%");
        }

        return sql;
    }
}
