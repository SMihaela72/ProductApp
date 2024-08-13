package controller;

import exception.InvalidInputException;
import exception.ResourceNotFoundException;
import model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import repository.ProductRepository;
import service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    /*
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    */
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    /*
    //  Add product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Find product by ID
    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Change the product price
    @PutMapping("/{id}/price")
    public Product changePrice(@PathVariable Long id, @RequestBody double newPrice) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setPrice(newPrice);
        return productRepository.save(product);
    }

     */

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
        //return productRepository.findAll();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Get products by name
    @GetMapping
    public List<Product> getProductsByName(@PathVariable String name) {
        return productService.getAllProducts();
    }

    //  Add product
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    // Change the product price
    @PutMapping("/{id}/changePrice")
    public ResponseEntity<String> changePrice(@PathVariable Long id, @RequestBody double newPrice) {
        try {
            productService.changePrice(id, newPrice);
            return ResponseEntity.ok("Price was updated successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }
}
