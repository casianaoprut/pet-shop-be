package webapp.pickme.petshop.service.order;

public class OrderException extends RuntimeException{
    OrderException(String message){
        super(message);
    }
}
