package productapp.productapp;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import productapp.productapp.exception.ProductNotFoundException;
import productapp.productapp.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import productapp.productapp.repository.ProductRepository;
import productapp.productapp.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InMemoryUserDetailsManager userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void initialize() {
        if (!userDetailsService.userExists("admin")) {
            UserDetails adminUser = User.withUsername("admin")
                    .password("productAppAdmin")
                    .roles("ADMIN")
                    .build();
            userDetailsService.createUser(adminUser);
        }

        if (!userDetailsService.userExists("user")) {
            UserDetails commonUser = User.withUsername("user")
                    .password("productAppUser")
                    .roles("USER")
                    .build();
            userDetailsService.createUser(commonUser);
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddProductAsAdmin() throws Exception {
        String newProduct = "{\"name\":\"Product_test1\",\"price\":29.99}";
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProduct))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddProductAsUser() throws Exception {
        // Definirea cererii pentru adăugarea unui produs
        String newProduct = "{\"name\":\"Product_test2\",\"price\":129.99}";

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProduct))
                .andExpect(MockMvcResultMatchers.status().isForbidden()); // Verifică dacă cererea este respinsă
    }

    @Test
    public void testAddProduct() {
        productRepository.deleteAll();

        /*
        Authentication authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                "user",
                "productAppUser",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        */
        Product product = new Product();
        product.setName("HDD");
        product.setPrice(250.00);

        productService.addProduct(product);

        List<Product> products = productRepository.findAll();
        assertEquals(1, products.size(), "The new product was inserted");
        Product newProduct = products.get(0);
        assertNotNull(newProduct.getId(), "Id was generated for the new product");
        assertEquals("HDD", newProduct.getName(), "Product name not match");
        assertEquals(250.00, newProduct.getPrice(), "Product price not match");
    }

    @Test
    public void testChangePrice() {
        productRepository.deleteAll();

        Product product = new Product();
        product.setName("Laptop_model2");
        product.setPrice(2250.00);
        Product savedProduct = productRepository.save(product);

        Double newPrice = 2140.00;
        productService.changePrice(savedProduct.getId(), newPrice);

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(updatedProduct, "Product have to not be null");
        assertEquals(newPrice, updatedProduct.getPrice(), "Product price not updated");
    }

    @Test
    public void testCount() {
        int count = productService.getProductCount();
        assertEquals(count, 5, "Error getCount");
    }

    @Test
    public void testChangePriceProductNotFound() {
        Double newPrice = 542.00;
        assertThrows(ProductNotFoundException.class, () -> {
            productService.changePrice(-1000L, newPrice);
        }, "Expected to throw exception ProductNotFoundException");
    }

    @Test
    public void testDeleteProductById() {
        Product product = new Product("Laptop_model3", 1234.00);
        product = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertTrue(foundProduct.isPresent());

        productService.deleteProductById(product.getId());

        Optional<Product> deletedProduct = productRepository.findById(product.getId());
        assertFalse(deletedProduct.isPresent());
    }
}