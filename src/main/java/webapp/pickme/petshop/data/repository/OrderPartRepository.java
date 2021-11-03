package webapp.pickme.petshop.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.pickme.petshop.data.model.order.OrderPart;

public interface OrderPartRepository extends JpaRepository<OrderPart, Long> {
}
