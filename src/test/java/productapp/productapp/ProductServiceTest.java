package com.example.projectspring_01;

import com.example.projectspring_01.controller.ProductController;
import com.example.projectspring_01.model.Product;
import com.example.projectspring_01.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProductServiceTest.class);

    @Test
    public void testLogging() {
        logger.info("This is a test log message.");
    }

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @Test
    public void testGetAllProducts() {
        System.out.println("Start testGetAllProducts");
        logger.info("Start testGetAllProducts");
        List<Product> productList = productController.getAllProducts();
        logger.info("Size productList = " + productList.size());
        System.out.println("Size productList = " + productList.size());

        // 1. Setup: Pregătește datele de test
        Product product1 = new Product("Product1", 100.0);
        Product product2 = new Product("Product2", 200.0);
        Product saved1 = productController.addProduct(product1);
        Product saved2 = productController.addProduct(product2);
        //Product saved = productRepository.save(product1);
        //Product saved2 = productRepository.save(product2);
        List<Product> mockProducts = Arrays.asList(product1, product2);

        // 2. Definește comportamentul mock-ului
        /*when(productRepository.findAll()).thenReturn(mockProducts);

        // 3. Execute: Apelează metoda pe care vrei să o testezi
        List<Product> products = productController.getAllProducts();

        // 4. Verify: Verifică rezultatul
        assertEquals(2, products.size());
        assertEquals("Product1", products.get(0).getName());
        assertEquals("Product2", products.get(1).getName());

        // 5. Verifică interacțiunile cu mock-ul (opțional)
        verify(productRepository, times(1)).findAll();*/
    }
}
