package webapp.pickme.petshop.service.order;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.OrderPartView;
import webapp.pickme.petshop.api.view.OrderView;
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

    public OrderView add(OrderView orderView){
        if(orderView.getOrderPartViews() != null) {
            var order = new Order();
            order.setDate(LocalDate.now());
            order.setStatus(Status.Pending);
            order.setUserName(getAuthenticatedUser());
            this.orderRepository.save(order);
            order.setOrderParts(mapOrderPartView(orderView.getOrderPartViews(), order));
            return new OrderView(this.orderRepository.save(order));
        }
        throw new IllegalArgumentException("An order can not be empty!");
    }

    private String getAuthenticatedUser(){
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
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

    public List<OrderView> getAll(){
        return this.orderRepository.findAll()
                                   .stream()
                                   .map(OrderView::new)
                                   .collect(Collectors.toList());
    }

    public List<OrderView> getAllByStatus(Status status){
        return this.orderRepository.findAllByStatus(status)
                                   .stream()
                                   .map(OrderView::new)
                                   .collect(Collectors.toList());
    }

    public void delete(Long id){
        this.orderRepository.deleteById(id);
    }

    public OrderView acceptOrder(Long id){
        var order = orderRepository.findById(id)
                                          .orElseThrow(() ->
                                                  new IllegalStateException("Order with id " + id + " does not exists!"));
        order.setStatus(Status.Accepted);
        orderRepository.save(order);
        changeProductsStock(order.getOrderParts());
        return new OrderView(order);
    }

    public OrderView changeStatus(Long id, Status status){
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with id " + id + " does not exists!"));
        if(order.getStatus().equals(Status.Pending)){
            acceptOrder(order.getId());
        }
        if(status.equals(Status.Pending))
            throw new IllegalStateException("You can't change an order status to pending!");
        order.setStatus(status);
        return new OrderView(this.orderRepository.save(order));
    }

    private void changeProductsStock(List<OrderPart> orderParts){
        orderParts.forEach(orderPart -> {
            var product = orderPart.getProduct();
            product.setStock(product.getStock() - orderPart.getQuantity());
            this.productService.edit(product);
        });
    }
}
