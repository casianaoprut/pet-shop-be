package webapp.pickme.petshop.service.order;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.OrderPartView;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.OrderPart;
import webapp.pickme.petshop.data.model.order.Status;
import webapp.pickme.petshop.data.repository.OrderRepository;
import webapp.pickme.petshop.service.product.ProductService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final OrderPartService orderPartService;

    public OrderService(OrderRepository orderRepository, ProductService productService, OrderPartService orderPartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderPartService = orderPartService;
    }

    public Order add(List<OrderPartView> orderPartViews){
        var order = new Order();
        order.setDate(LocalDate.now());
        order.setStatus(Status.Pending);
        this.orderRepository.save(order);
        order.setOrderParts(mapOrderPartView(orderPartViews, order));
        return this.orderRepository.save(order);
    }

    private List<OrderPart> mapOrderPartView(List<OrderPartView> orderPartViews, Order order){
        return orderPartViews.stream()
                .map(orderPartView -> {
                    var orderPart = new OrderPart();
                    var product = this.productService.find(orderPartView.getProductId());
                    orderPart.setProduct(product);
                    orderPart.setQuantity(orderPartView.getQuantity());
                    orderPart.setOrderId(order.getId());
                    return orderPartService.add(orderPart);
                }).collect(Collectors.toList());
    }

    public List<Order> getAll(){
        return this.orderRepository.findAll();
    }

    public List<Order> getAllByStatus(Status status){
        return this.orderRepository.findAllByStatus(status);
    }

    public void delete(Long id){
        this.orderRepository.deleteById(id);
    }

    public Order acceptOrder(Long id){
        var order = orderRepository.findById(id)
                                          .orElseThrow(() ->
                                                  new IllegalStateException("Order with id " + id + " does not exists!"));
        order.setStatus(Status.Accepted);
        orderRepository.save(order);
        changeProductsStock(order.getOrderParts());
        return order;
    }

    public Order changeStatus(Long id, Status status){
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with id " + id + " does not exists!"));
        if (order.getStatus() == Status.Pending){
            acceptOrder(order.getId());
        }
        order.setStatus(status);
        return orderRepository.save(order);
    }

    private void changeProductsStock(List<OrderPart> orderParts){
        orderParts.forEach(orderPart -> {
            var product = orderPart.getProduct();
            product.setStock(product.getStock() - orderPart.getQuantity());
            this.productService.edit(product);
        });
    }
}