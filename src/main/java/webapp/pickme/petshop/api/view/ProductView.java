package webapp.pickme.petshop.api.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import webapp.pickme.petshop.data.model.product.Breed;
import webapp.pickme.petshop.data.model.product.Category;
import webapp.pickme.petshop.data.model.product.Product;

import java.sql.Blob;

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

    private boolean onSale;

    private MultipartFile photo;


    public ProductView(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.forBreed = product.getForBreed();
        this.category = product.getCategory();
        this.stock = product.getStock();
        this.onSale = product.isOnSale();
    }

}
