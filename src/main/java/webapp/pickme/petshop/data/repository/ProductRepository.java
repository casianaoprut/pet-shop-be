package webapp.pickme.petshop.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.pickme.petshop.data.model.product.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
}
