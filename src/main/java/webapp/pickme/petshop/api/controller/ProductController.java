package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.data.model.product.Filter;
import webapp.pickme.petshop.data.model.product.Product;
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
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(this.productService.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<Product> add(@RequestBody Product product){
        return  ResponseEntity.ok(this.productService.add(product));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> getFilteredProducts(@RequestBody Filter filter){
        return new ResponseEntity<>(this.productService.filter(filter), HttpStatus.FOUND );
    }

    @PutMapping("/edit")
    public ResponseEntity<Product> edit(@RequestBody Product product){
        return ResponseEntity.ok(this.productService.edit(product));
    }
}