package productapp.productapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import productapp.productapp.exception.InvalidInputException;
import productapp.productapp.exception.ProductNotFoundException;
import productapp.productapp.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import productapp.productapp.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            log.info("Successfully executed - getAllProducts");
            return products;
        } catch (Exception e) {
            log.error("Error at call getAllProducts: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public Product getProductById(@PathVariable Long id) {
            return productRepository.findById(id).orElseThrow(
                    () -> {
                        log.error("Error at getProductById for id: : {}", id);
                        return new ProductNotFoundException("Product not found");
                    });
    }

    @Transactional
    public void addProduct(@RequestBody Product product) {
        try {
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new InvalidInputException("Product name cannot be null or empty.");
            }

            if (product.getPrice() == null || product.getPrice() <= 0) {
                throw new InvalidInputException("Product price must be greater than zero.");
            }

            productRepository.save(product);
            log.info("Product added successfully: {}", product);
        } catch (InvalidInputException e) {
            log.error("Error adding product: {}", e.getMessage());
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("A product with the same attributes already exists: {}", e.getMessage());
            throw new RuntimeException("A product with the same attributes already exists: ", e);
        } catch (Exception e) {
            log.error("Error while adding product: {}", e.getMessage());
            throw new RuntimeException("Unexpected error: ", e);
        }
    }

    @Transactional
    public void changePrice(@PathVariable Long id, @RequestBody Double newPrice) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " not found "));

            if (newPrice == null || newPrice <= 0) {
                throw new InvalidInputException("Price must be greater than zero.");
            }

            product.setPrice(newPrice);
            product.setUpdDate(LocalDateTime.now());
            productRepository.save(product);

            log.info("Price was successfully updated for the product with id: {}", id);
        } catch (ProductNotFoundException e) {
            log.error("Product not found for id: {}. {}", id, e.getMessage());
            throw e;
        }
        catch (InvalidInputException e) {
            log.error("Invalid input to update the product with id: {}. {}", id, e.getMessage());
            throw e;
        }catch (Exception e) {
            log.error("Error while updating price for the product with id: {}. {}", id, e.getMessage());
            throw new RuntimeException("Unexpected error: ", e);
        }
    }

    public int getProductCount() {
        int count = productRepository.getProductCount();
        log.info("The count of products in database is {}", count);
        return count;
    }

    public List<Product> getProductsWithPriceLessThan(Double maxPrice) {
        log.info("Call getProductsWithPriceLessThan");
        return productRepository.getProductsWithPriceLessThan(maxPrice);
    }

    public List<Product> getProductsWithNameContains(String stringToFind) {
        log.info("Call getProductsWithNameContains");
        return productRepository.getProductsWithNameContains(stringToFind);
    }

    @Transactional
    public void deleteAllProducts() {
        try {
            productRepository.deleteAll();
        } catch (Exception e) {
            log.error("Error at call deleteAllProducts: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred while trying to delete all products", e);
        }
    }

    @Transactional
    public void deleteProductById(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error at call deleteProductById: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred while trying to delete the product by ID", e);
        }
    }

}