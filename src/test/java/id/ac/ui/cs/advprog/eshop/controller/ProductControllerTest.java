package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = new ConcurrentModel();
    }

    @Test
    void testCreateProductPage() {
        String viewName = productController.createProductPage(model);
        assertEquals("createProduct", viewName);
        assertTrue(model.containsAttribute("product"));
    }

    @Test
    void testCreateProductPost() {
        Product product = new Product();
        String redirect = productController.createProductPost(product, model);
        assertEquals("redirect:/product/list", redirect);
    }

    @Test
    void testProductListPage() {
        given(productService.findAll()).willReturn(Collections.emptyList());

        String viewName = productController.productListPage(model);
        assertEquals("productList", viewName);
        assertTrue(model.containsAttribute("products"));
    }

    @Test
    void testEditProductPageFound() {
        Product product = new Product();
        given(productService.findById("1")).willReturn(product);

        String viewName = productController.editProductPage("1", model);
        assertEquals("editProduct", viewName);
        assertTrue(model.containsAttribute("product"));
    }

    @Test
    void testEditProductPageNotFound() {
        // This test covers the branch where the product is not found.
        given(productService.findById("nonexistent")).willReturn(null);

        String viewName = productController.editProductPage("nonexistent", model);
        assertEquals("redirect:/product/list", viewName);
    }

    @Test
    void testEditProductPost() {
        Product product = new Product();
        product.setProductId("1");

        String redirect = productController.editProductPost(product);
        assertEquals("redirect:/product/list", redirect);
    }

    @Test
    void testDeleteProduct() {
        String redirect = productController.deleteProduct("1");
        assertEquals("redirect:/product/list", redirect);
    }
}
