package webapp.pickme.petshop.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.Status;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Status status);
}
