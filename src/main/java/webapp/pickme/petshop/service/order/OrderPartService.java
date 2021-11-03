package webapp.pickme.petshop.service.order;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.order.OrderPart;
import webapp.pickme.petshop.data.repository.OrderPartRepository;

@Service
public class OrderPartService {

    private final OrderPartRepository orderPartRepository;

    public OrderPartService(OrderPartRepository orderPartRepository) {
        this.orderPartRepository = orderPartRepository;
    }

    public OrderPart add(OrderPart orderPart){
        return this.orderPartRepository.save(orderPart);
    }
}
