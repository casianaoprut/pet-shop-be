package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import webapp.pickme.petshop.data.model.product.Breed;
import webapp.pickme.petshop.data.model.product.Category;
import webapp.pickme.petshop.data.model.product.Product;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductView {

    private Long id;

    private String name;

    private Float price;

    private String description;

    private Breed forBreed;

    private Category category;

    private Integer stock;

    public ProductView(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.forBreed = product.getForBreed();
        this.category = product.getCategory();
        this.stock = product.getStock();
    }

}
