package webapp.pickme.petshop.service.order;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.OrderPart;
import webapp.pickme.petshop.data.model.order.Status;
import webapp.pickme.petshop.data.repository.OrderRepository;
import webapp.pickme.petshop.service.product.ProductService;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
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

    public Order acceptOrder(Long id){
        var order = orderRepository.findById(id)
                                          .orElseThrow(() ->
                                                  new IllegalStateException("Order with id " + id + " does not exists!"));
        order.setStatus(Status.Accepted);
        editOrder(order);
        changeProductsStock(order.getOrderParts());
        return order;
    }

    private void changeProductsStock(List<OrderPart> orderParts){
        orderParts.forEach(orderPart -> {
            var product = orderPart.getProduct();
            product.setStock(product.getStock() - orderPart.getQuantity());
            this.productService.edit(product);
        });
    }
}
