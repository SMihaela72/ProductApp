package service;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import repository.ProductRepository;
import repository.ProductRepositoryJDBC;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    //@Autowired
    private final ProductRepository productRepository;

    /*
    @Autowired
    private JdbcTemplate jdbcRepository;
    */

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        /*  =====
        String sql = "SELECT * FROM product";

        List<Map<String, Object>> rows = jdbcRepository.queryForList(sql);
        List<Product> products = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            Long id = (Long) row.get("id");
            String name = (String) row.get("name");
            double price = (double) row.get("price");

            Product product = new Product(id, name, price);
            products.add(product);
        }

        return products;

         */
        return productRepository.findAll();
    }

    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    /*
    public List<Product> getProductsByName(@PathVariable String name) {
        return productRepository.getProductsByName(name).orElseThrow(() -> new RuntimeException("Product not found"));
    }
    */

    public Product addProduct(@RequestBody Product product) {
        try {
            if (product.getName() == null || product.getName().isEmpty()) {
                throw new InvalidInputException("Product name cannot be null or empty.");
            }

            if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidInputException("Product price must be greater than zero.");
            }

            productRepository.save(product);
            logger.info("Product added successfully: {}", product);
        } catch (InvalidInputException e) {
            logger.error("Error adding product: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while adding product: {}", e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public Product changePrice(@PathVariable Long id, @RequestBody Double newPrice) {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id: " + String.valueOf(id) + " not found "));

            if (newPrice == null || newPrice <= 0) {
                throw new InvalidInputException("Price must be greater than zero.");
            }

            product.setPrice(newPrice);
            productRepository.save(product);

            logger.info("Price was updated successfully for for the product with id: {}", id);
        } catch (ResourceNotFoundException e) {
            logger.error("Error while updating price for the product with id: {}. {}", id, e.getMessage());
            throw e;
        } catch (InvalidInputException e) {
            logger.error("Error while updating price for the product with id: {}. {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while updating price for the product with id: {}. {}", id, e.getMessage());
            throw new RuntimeException("Unexpected error occurred", e);
        }
    }

    public int getProductNumberStartingWith(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}