package webapp.pickme.petshop.service.order;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.Status;
import webapp.pickme.petshop.data.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order add(Order order){
        return this.orderRepository.save(order);
    }

    public List<Order> getAll(){
        return this.orderRepository.findAll();
    }

    public List<Order> getAllByStatus(Status status){
        return this.orderRepository.findAllByStatus(status);
    }

    public Order editOrder(Order order){
        this.orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalStateException("Order with id " + order.getId() + " does not exists!"));
        return this.orderRepository.save(order);
    }

    public void delete(Long id){
        this.orderRepository.deleteById(id);
    }
}
