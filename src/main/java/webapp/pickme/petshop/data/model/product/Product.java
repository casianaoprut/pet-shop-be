package webapp.pickme.petshop.data.model.product;

import lombok.*;
import webapp.pickme.petshop.api.view.ProductView;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @SequenceGenerator(
        name = "product_sequence",
        sequenceName = "product_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Breed forBreed;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer stock;

    public Product(ProductView productView){
        this.id = productView.getId();
        this.name = productView.getName();
        this.price = productView.getPrice();
        this.description = productView.getDescription();
        this.forBreed = productView.getForBreed();
        this.category = productView.getCategory();
        this.stock = productView.getStock();
    }
}
