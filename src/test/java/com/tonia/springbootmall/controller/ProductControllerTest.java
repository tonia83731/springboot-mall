package com.tonia.springbootmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonia.springbootmall.constant.ProductCategory;
import com.tonia.springbootmall.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{productId}", 1);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", notNullValue()))
                .andExpect(jsonPath("$.stock", notNullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void getProduct_notFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/{productId}", 1000);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    public void createProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Test food");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://www.test.com");
        productRequest.setPrice(100);
        productRequest.setStock(10);

        String json = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.productName", equalTo("Test food")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.stock", equalTo(10)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void createProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Test food");

        String json = objectMapper.writeValueAsString(productRequest);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder).andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void updateProduct_success() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Test food");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://www.test.com");
        productRequest.setPrice(100);
        productRequest.setStock(10);
        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.productName", equalTo("Test food")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", equalTo(100)))
                .andExpect(jsonPath("$.stock", equalTo(10)))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void updateProduct_illegalArgument() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Test food");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder).andExpect(status().is(400));
    }

    @Transactional
    @Test
    public void updateProduct_productNotFound() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductName("Test food");
        productRequest.setCategory(ProductCategory.FOOD);
        productRequest.setImageUrl("https://test.com");
        productRequest.setPrice(100);
        productRequest.setStock(10);
        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1000)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Transactional
    @Test
    public void deleteProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/products/{productId}", 1);
        mockMvc.perform(requestBuilder).andExpect(status().is(204));
    }

    @Transactional
    @Test
    public void deleteProduct_deleteNonExistingProduct() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/products/{productId}", 1000);
        mockMvc.perform(requestBuilder).andExpect(status().is(204));
    }

    @Test
    public void getProducts() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(5)));
    }

    @Test
    public void getProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .param("search", "B")
                .param("category", "CAR");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)));
    }

    @Test
    public void getProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .param("orderBy", "price")
                .param("sort", "desc");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(6)))
                .andExpect(jsonPath("$.results[1].productId", equalTo(5)))
                .andExpect(jsonPath("$.results[2].productId", equalTo(7)))
                .andExpect(jsonPath("$.results[3].productId", equalTo(4)))
                .andExpect(jsonPath("$.results[4].productId", equalTo(2)));
    }

    @Test
    public void getProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products")
                .param("limit", "2")
                .param("offset", "2");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit", notNullValue()))
                .andExpect(jsonPath("$.offset", notNullValue()))
                .andExpect(jsonPath("$.total", notNullValue()))
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[0].productId", equalTo(5)))
                .andExpect(jsonPath("$.results[1].productId", equalTo(4)));
    }
}