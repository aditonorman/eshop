package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();
    }

    @Test
    void testCreateGeneratesProductIdWhenNull() {
        // Create a product with null productId
        Product product = new Product();
        product.setProductId(null);

        Product created = repository.create(product);

        // The repository should generate a new non-null and non-empty productId
        assertNotNull(created.getProductId());
        assertFalse(created.getProductId().isEmpty());
    }

    @Test
    void testCreateGeneratesProductIdWhenEmpty() {
        // Create a product with empty productId
        Product product = new Product();
        product.setProductId("");

        Product created = repository.create(product);

        // The repository should generate a new non-null and non-empty productId
        assertNotNull(created.getProductId());
        assertFalse(created.getProductId().isEmpty());
    }

    @Test
    void testCreateUsesProvidedProductId() {
        // Create a product with a provided productId
        Product product = new Product();
        String providedId = "fixed-id";
        product.setProductId(providedId);

        Product created = repository.create(product);

        // The productId should remain unchanged
        assertEquals(providedId, created.getProductId());
    }

    @Test
    void testFindAll() {
        // Add two products to the repository
        Product product1 = new Product();
        product1.setProductId("id1");
        repository.create(product1);

        Product product2 = new Product();
        product2.setProductId("id2");
        repository.create(product2);

        // Retrieve all products using the iterator
        Iterator<Product> iterator = repository.findAll();
        List<Product> products = new ArrayList<>();
        iterator.forEachRemaining(products::add);

        assertEquals(2, products.size());
    }

    @Test
    void testFindAllEmpty() {
        // When the repository is empty, the iterator should have no elements.
        Iterator<Product> iterator = repository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFindByIdFound() {
        // Create and add a product
        Product product = new Product();
        product.setProductId("search-id");
        repository.create(product);

        // Find the product by its id
        Product found = repository.findById("search-id");

        assertNotNull(found);
        assertEquals("search-id", found.getProductId());
    }

    @Test
    void testFindByIdNotFound() {
        // Attempt to find a product with an id that was not added
        Product found = repository.findById("non-existent");
        assertNull(found);
    }

    @Test
    void testFindByIdWithNull() {
        // With an empty repository, finding by null returns null
        assertNull(repository.findById(null));

        // Even with products in the repository, searching for null returns null
        Product product = new Product();
        product.setProductId("id1");
        repository.create(product);
        assertNull(repository.findById(null));
    }

    @Test
    void testUpdateExistingProduct() {
        // Create and add a product
        Product product = new Product();
        product.setProductId("update-id");
        repository.create(product);

        // Create a new Product instance with the same id to simulate an update
        Product updatedProduct = new Product();
        updatedProduct.setProductId("update-id");
        // (Optionally set other fields if available to simulate an update)

        Product result = repository.update(updatedProduct);

        assertNotNull(result);
        assertEquals("update-id", result.getProductId());

        // Verify that the update has been applied in the repository
        Product found = repository.findById("update-id");
        assertEquals(updatedProduct, found);
    }

    @Test
    void testUpdateNonExistingProduct() {
        // Attempt to update a product that doesn't exist in the repository
        Product updatedProduct = new Product();
        updatedProduct.setProductId("non-existent-update");

        Product result = repository.update(updatedProduct);
        assertNull(result);
    }

    @Test
    void testUpdateProductWithNullProductId() {
        // Create and add a product normally
        Product product = new Product();
        product.setProductId("update-null-test");
        repository.create(product);

        // Create an update product with a null productId, which should not match any product
        Product updateProduct = new Product();
        updateProduct.setProductId(null);

        Product updated = repository.update(updateProduct);
        assertNull(updated);
    }

    @Test
    void testDeleteExistingProduct() {
        // Create and add a product
        Product product = new Product();
        product.setProductId("delete-id");
        repository.create(product);

        // Delete the product by its id
        boolean result = repository.delete("delete-id");
        assertTrue(result);

        // Verify that the product has been removed
        Product found = repository.findById("delete-id");
        assertNull(found);
    }

    @Test
    void testDeleteNonExistingProduct() {
        // Attempt to delete a product with a non-existent id
        boolean result = repository.delete("non-existent-delete");
        assertFalse(result);
    }

    @Test
    void testDeleteWithNullProductId() {
        // Create and add a product normally
        Product product = new Product();
        product.setProductId("delete-null-test");
        repository.create(product);

        // Attempt to delete using a null productId; should return false.
        boolean result = repository.delete(null);
        assertFalse(result);
    }
}
