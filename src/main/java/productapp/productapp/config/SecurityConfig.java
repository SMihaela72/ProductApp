package productapp.productapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/products/addProduct").hasRole("ADMIN")  // For adding products
                        .requestMatchers(HttpMethod.PUT, "/products//{id}/changePrice").hasRole("ADMIN")  // Change price
                        .requestMatchers(HttpMethod.DELETE, "/products/deleteProductById/{id}").hasRole("ADMIN")  // For deleting products
                        .requestMatchers(HttpMethod.DELETE, "/products/deleteAllProducts").hasRole("ADMIN")  // Delete all products
                        .requestMatchers("/api/products/**").authenticated()
                )
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}