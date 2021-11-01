package webapp.pickme.petshop.service.product;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.product.Product;
import webapp.pickme.petshop.data.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public Product add(Product product){
        return productRepository.save(product);
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }
}
