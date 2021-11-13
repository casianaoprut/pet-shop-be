package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.api.view.Filter;
import webapp.pickme.petshop.api.view.ProductView;
import webapp.pickme.petshop.service.product.ProductException;
import webapp.pickme.petshop.service.product.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductView>> getAll() {
        return ResponseEntity.ok(this.productService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<ProductView> add(@RequestBody ProductView product){
        return  ResponseEntity.ok(this.productService.add(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductView>> getFilteredProducts(@RequestBody Filter filter){
        return ResponseEntity.ok(this.productService.filter(filter));
    }

    @PutMapping("/edit")
    public ResponseEntity<ProductView> edit(@RequestBody ProductView productView){
        return ResponseEntity.ok(this.productService.edit(productView));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(this.productService.getById(id));
        } catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}