package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.pickme.petshop.data.model.order.Order;
import webapp.pickme.petshop.data.model.order.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderView {

    private Long id;

    private LocalDate date;

    private Status status;

    private String username;

    private List<OrderPartView> orderPartViews;

    public OrderView(Order order){
        this.id = order.getId();
        this.date = order.getDate();
        this.status = order.getStatus();
        this.username = order.getUserName();
        this.orderPartViews = order.getOrderParts()
                                   .stream()
                                   .map(OrderPartView::new)
                                   .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "OrderView{" +
                "id=" + id +
                ", date=" + date +
                ", status=" + status +
                ", username='" + username + '\'' +
                ", orderPartViews=" + orderPartViews +
                '}';
    }
}
