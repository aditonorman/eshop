package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void testCreateProductPage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createProduct"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testCreateProductPost() throws Exception {
        // Simulate creation (service.create returns a Product)
        doReturn(new Product()).when(service).create(any(Product.class));

        mockMvc.perform(post("/product/create")
                        // Simulate form submission with an empty productId (or any other fields)
                        .param("productId", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).create(any(Product.class));
    }

    @Test
    void testProductListPage() throws Exception {
        Product product1 = new Product();
        product1.setProductId("1");
        Product product2 = new Product();
        product2.setProductId("2");
        List<Product> products = Arrays.asList(product1, product2);
        doReturn(products).when(service).findAll();

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("productList"))
                .andExpect(model().attribute("products", products));

        verify(service, times(1)).findAll();
    }

    @Test
    void testEditProductPageFound() throws Exception {
        Product product = new Product();
        product.setProductId("1");
        doReturn(product).when(service).findById("1");

        mockMvc.perform(get("/product/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProduct"))
                .andExpect(model().attribute("product", product));

        verify(service, times(1)).findById("1");
    }

    @Test
    void testEditProductPageNotFound() throws Exception {
        doReturn(null).when(service).findById("non-existent");

        mockMvc.perform(get("/product/edit/non-existent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).findById("non-existent");
    }

    @Test
    void testEditProductPost() throws Exception {
        // Simulate an update: binding a product with productId "1"
        doReturn(new Product()).when(service).update(any(Product.class));

        mockMvc.perform(post("/product/edit")
                        .param("productId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).update(any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Use doReturn(true) because delete returns a boolean value.
        doReturn(true).when(service).delete("1");

        mockMvc.perform(get("/product/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).delete("1");
    }
}
