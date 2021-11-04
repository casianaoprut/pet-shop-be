package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.api.view.OrderPartView;
import webapp.pickme.petshop.api.view.OrderView;
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
    public ResponseEntity<List<OrderView>> getAll(){
        return ResponseEntity.ok(this.orderService.getAll());
    }

    @GetMapping("/all/{status}")
    public  ResponseEntity<List<OrderView>> getAllByStatus(@PathVariable("status") Status status){
        return ResponseEntity.ok(this.orderService.getAllByStatus(status));
    }

    @PostMapping("/add")
    public ResponseEntity<OrderView> add(@RequestBody OrderView orderView){
        return new ResponseEntity<>(this.orderService.add(orderView), HttpStatus.CREATED);
    }

    @PutMapping("/change-status/{status}/{id}")
    public ResponseEntity<OrderView> changeStatus(@PathVariable("id") Long id, @PathVariable("status") Status status){
        return ResponseEntity.ok(this.orderService.changeStatus(id, status));
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.orderService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<OrderView> accept(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.orderService.acceptOrder(id));
    }
}
