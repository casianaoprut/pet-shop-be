package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.pickme.petshop.data.model.product.Breed;
import webapp.pickme.petshop.data.model.product.Category;


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
