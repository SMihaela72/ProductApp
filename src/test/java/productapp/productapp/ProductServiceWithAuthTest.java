package productapp.productapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import productapp.productapp.config.SecurityConfig;
import productapp.productapp.model.Product;
import productapp.productapp.repository.ProductRepository;
import productapp.productapp.service.ProductService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProductServiceWithAuthTest {

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
/*
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddProductAsAdmin() throws Exception {
        String newProduct = "{\"name\":\"Product_test1\",\"price\":45.50}";
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProduct))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAddProductAsUser() throws Exception {
        String newProduct = "{\"name\":\"Product_test2\",\"price\":145.20}";

        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newProduct))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
*/
    @Test
    public void testAddProduct() {
        productRepository.deleteAll();

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
}