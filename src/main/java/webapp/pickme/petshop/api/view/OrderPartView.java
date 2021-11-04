package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.pickme.petshop.data.model.order.OrderPart;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPartView {

    private Long productId;

    private Integer quantity;

    public OrderPartView(OrderPart orderPart){
        this.productId = orderPart.getProduct().getId();
        this.quantity = orderPart.getQuantity();
    }

}
