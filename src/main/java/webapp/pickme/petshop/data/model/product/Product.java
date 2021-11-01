package webapp.pickme.petshop.data.model.product;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @SequenceGenerator(
        name = "product-sequence",
        sequenceName = "product_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long Id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Breed forBreed;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer stock;

}
