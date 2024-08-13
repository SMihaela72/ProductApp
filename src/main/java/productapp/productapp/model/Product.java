package productapp.productapp.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "INSDATE", updatable = false)
    private LocalDateTime insDate;

    @Column(name = "UPDDATE")
    private LocalDateTime updDate;

    public Product() {
        this.insDate = LocalDateTime.now();
    }

    public Product(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.insDate = LocalDateTime.now();
    }

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
        this.insDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getInsDate() {
        return insDate;
    }

    public LocalDateTime getUpdDate() {
        return updDate;
    }

    public void setUpdDate(LocalDateTime updDate) {
        this.updDate = updDate;
    }
}