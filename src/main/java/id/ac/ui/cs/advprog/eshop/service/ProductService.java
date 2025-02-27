package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    public Product create(Product product);
    public List<Product> findAll();

    // Methods for editing
    public Product findById(String productId);
    public Product update(Product product);

    // New method for deletion
    public boolean delete(String productId);
}
