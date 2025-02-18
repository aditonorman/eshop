package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testCreate() {
        // Arrange
        Product product = new Product();
        given(productRepository.create(product)).willReturn(product);

        // Act
        Product created = productService.create(product);

        // Assert
        assertEquals(product, created);
        verify(productRepository).create(product);
    }

    @Test
    void testFindAll() {
        // Arrange
        Product product1 = new Product();
        Product product2 = new Product();
        Iterator<Product> productIterator = Arrays.asList(product1, product2).iterator();

        given(productRepository.findAll()).willReturn(productIterator);

        // Act
        var results = productService.findAll();

        // Assert
        assertEquals(2, results.size());
        verify(productRepository).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Product product = new Product();
        // If your Product has productId as the field, call product.setProductId(...)
        // e.g. product.setProductId("1");
        // For demonstration, let's assume Product has setProductId(...):
        product.setProductId("1");
        given(productRepository.findById("1")).willReturn(product);

        // Act
        Product found = productService.findById("1");

        // Assert
        assertEquals("1", found.getProductId()); // or found.getProductId()
        verify(productRepository).findById("1");
    }

    @Test
    void testUpdate() {
        // Arrange
        Product product = new Product();
        given(productRepository.update(product)).willReturn(product);

        // Act
        Product updated = productService.update(product);

        // Assert
        assertEquals(product, updated);
        verify(productRepository).update(product);
    }

    @Test
    void testDelete() {
        // Arrange
        given(productRepository.delete("1")).willReturn(true);

        // Act
        boolean deleted = productService.delete("1");

        // Assert
        assertTrue(deleted);
        verify(productRepository).delete("1");
    }
}
