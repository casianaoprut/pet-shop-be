package webapp.pickme.petshop.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> delete(@PathVariable Long id){
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }
}