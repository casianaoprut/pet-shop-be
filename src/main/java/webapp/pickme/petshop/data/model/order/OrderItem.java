package webapp.pickme.petshop.data.model.order;

import webapp.pickme.petshop.data.model.product.Product;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class OrderItem {

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

}
