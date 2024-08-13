package productapp.productapp.repository;

import org.springframework.data.jpa.repository.Query;
import productapp.productapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query("SELECT count(p.id) FROM Product p ")
    int getProductCount();

    @Query("SELECT p FROM Product p WHERE p.price < ?1 ")
    List<Product> getProductsWithPriceLessThan(Double maxPrice);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:stringToFind% ")
    List<Product> getProductsWithNameContains(String stringToFind);
}
