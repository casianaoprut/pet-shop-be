package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.Status;
import webapp.pickme.petshop.service.order.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAll(){
        return ResponseEntity.ok(this.orderService.getAll());
    }

    @GetMapping("/{status}")
    public  ResponseEntity<List<Order>> getAllByStatus(@PathVariable("status") Status status){
        return ResponseEntity.ok(this.orderService.getAllByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<Order> add(@RequestBody Order order){
        return new ResponseEntity<>(this.orderService.add(order), HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    private ResponseEntity<Order> editOrder(@RequestBody Order order){
        return ResponseEntity.ok(this.orderService.editOrder(order));
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.orderService.delete(id);
        return ResponseEntity.ok().build();
    }
}
