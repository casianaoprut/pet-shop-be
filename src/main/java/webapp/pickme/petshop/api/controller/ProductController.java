package webapp.pickme.petshop.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webapp.pickme.petshop.api.view.Filter;
import webapp.pickme.petshop.api.view.ProductView;
import webapp.pickme.petshop.service.product.ProductException;
import webapp.pickme.petshop.service.product.ProductService;

import java.io.IOException;
import java.sql.SQLException;
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

    @RequestMapping(value="/add",
            method=RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductView> add(@ModelAttribute ProductView productData) throws IOException {
        return  ResponseEntity.ok(this.productService.add(productData));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        this.productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ProductView>> getFilteredProducts(@RequestBody Filter filter){
        return ResponseEntity.ok(this.productService.filter(filter));
    }

    @RequestMapping(value="/edit",
            method=RequestMethod.PUT,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductView> edit(@ModelAttribute ProductView productData) throws IOException {
        return ResponseEntity.ok(this.productService.edit(productData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(this.productService.getById(id));
        } catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-by-id-list")
    public ResponseEntity<?> getProductListByIdList(@RequestParam("idList") List<Long> idList ){
        try {
            return ResponseEntity.ok(this.productService.getProductListByIdList(idList));
        }catch (ProductException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<?> getProductPhoto(@PathVariable("id") Long id){
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpg");
        try {
            var photo = this.productService.getProductPhoto(id);
            return ResponseEntity.ok().headers(headers).body(photo);
        } catch (SQLException | IOException  ex) {
            return ResponseEntity.internalServerError().body("There was an error fetchig the image!" + ex);
        }
    }
}