package webapp.pickme.petshop.service.product;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.data.model.product.Filter;
import webapp.pickme.petshop.data.model.product.Product;
import webapp.pickme.petshop.data.repository.ProductRepository;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductQueryFactory productQueryFactory;

    private final EntityManager entityManager;

    public ProductService(ProductRepository productRepository, ProductQueryFactory productQueryFactory, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.productQueryFactory = productQueryFactory;
        this.entityManager = entityManager;
    }

    public Product find(Long id){
        return this.productRepository.findById(id)
                                     .orElseThrow(() -> new IllegalStateException("product with id " + id + " does not exists!"));
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

    public List<Product> filter(Filter filter){
        return entityManager.createQuery(
                productQueryFactory.createFilterForProduct( entityManager, filter)
        ).getResultList();
    }

    public Product edit(Product product){
        return this.productRepository.save(product);
    }
}
