package productapp.productapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import productapp.productapp.exception.InvalidInputException;
import productapp.productapp.exception.ProductNotFoundException;
import productapp.productapp.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productapp.productapp.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    //  Add product
    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully");
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A product with the same attributes already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    // Change product price
    @PutMapping("/{id}/changePrice")
    public ResponseEntity<String> changePrice(@PathVariable Long id, @RequestBody double newPrice) {
        try {
            productService.changePrice(id, newPrice);
            return ResponseEntity.ok("The price has been successfully updated");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    // Get product count
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getProductCount() {
        int count = productService.getProductCount();
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    //Get products with the price smaller than priceToCompare
    @GetMapping("/getProductsWithPriceLessThan")
    public List<Product> getProductsWithPriceLessThan(Double maxPrice) {
        return productService.getProductsWithPriceLessThan(maxPrice);
    }

    //Get products with name containing stringToFind
    @GetMapping("/getProductsWithNameContains")
    public List<Product> getProductsWithNameContains(String stringToFind) {
        return productService.getProductsWithNameContains(stringToFind);
    }

    // Delete all products from DB
    @DeleteMapping("/deleteAllProducts")
    public ResponseEntity<Void> deleteAllProducts() {
        productService.deleteAllProducts();
        return ResponseEntity.noContent().build();
    }

    //Delete product with id
    @DeleteMapping("/deleteProductById/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
