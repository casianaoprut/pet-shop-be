package webapp.pickme.petshop.data.model.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private String name;

    private Integer maxPrice;

    private Integer minPrice;

    private Category category;

    private Breed breed;

}
