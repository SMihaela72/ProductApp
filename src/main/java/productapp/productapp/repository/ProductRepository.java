package repository;

import model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
/*
    public Product addProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

 */
    // Find products wuth the price greater than priceToCompare
    /*public List<Product> findByPriceGreaterThan(BigDecimal priceToCompare) {
        return null;
    }*/
}
