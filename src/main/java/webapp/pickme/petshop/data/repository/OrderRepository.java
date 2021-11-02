package webapp.pickme.petshop.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.pickme.petshop.data.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
