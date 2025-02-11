package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());

        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }

    // ========= New Tests for Update (Edit) Functionality =========

    @Test
    void testUpdateProductPositive() {
        // Arrange: Create and add a product to the repository.
        Product product = new Product();
        product.setProductId("prod-001");
        product.setProductName("Original Product");
        product.setProductQuantity(100);
        productRepository.create(product);

        // Act: Create an updated product with the same productId.
        Product updatedProduct = new Product();
        updatedProduct.setProductId("prod-001");  // same ID to locate the product
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(150);
        Product result = productRepository.update(updatedProduct);

        // Assert: Verify that the update was successful.
        assertNotNull(result, "Update should return the updated product for an existing product.");
        assertEquals("Updated Product", result.getProductName(), "The product name should be updated.");
        assertEquals(150, result.getProductQuantity(), "The product quantity should be updated.");

        // Verify using findById.
        Product fetchedProduct = productRepository.findById("prod-001");
        assertNotNull(fetchedProduct, "Product should exist after update.");
        assertEquals("Updated Product", fetchedProduct.getProductName(), "The repository should reflect the updated product name.");
        assertEquals(150, fetchedProduct.getProductQuantity(), "The repository should reflect the updated product quantity.");
    }

    @Test
    void testUpdateProductNegative() {
        // Arrange: Create an updated product with an ID that doesn't exist in the repository.
        Product updatedProduct = new Product();
        updatedProduct.setProductId("non-existent");
        updatedProduct.setProductName("Non-existent Product");
        updatedProduct.setProductQuantity(200);

        // Act: Attempt to update the non-existent product.
        Product result = productRepository.update(updatedProduct);

        // Assert: Verify that update returns null for a non-existing product.
        assertNull(result, "Update should return null for a non-existing product.");
    }

    // ========= New Tests for Delete Functionality =========

    @Test
    void testDeleteProductPositive() {
        // Arrange: Create and add a product to the repository.
        Product product = new Product();
        product.setProductId("prod-002");
        product.setProductName("Product To Delete");
        product.setProductQuantity(75);
        productRepository.create(product);

        // Act: Delete the product using its productId.
        boolean deleteResult = productRepository.delete("prod-002");

        // Assert: Verify that the deletion was successful.
        assertTrue(deleteResult, "Deletion should return true for an existing product.");

        // Confirm that the product is no longer in the repository.
        Product fetchedProduct = productRepository.findById("prod-002");
        assertNull(fetchedProduct, "Product should not exist in the repository after deletion.");
    }

    @Test
    void testDeleteProductNegative() {
        // Act: Attempt to delete a product with an ID that does not exist.
        boolean deleteResult = productRepository.delete("non-existent");

        // Assert: Verify that the deletion fails.
        assertFalse(deleteResult, "Deletion should return false for a non-existing product.");
    }
}
