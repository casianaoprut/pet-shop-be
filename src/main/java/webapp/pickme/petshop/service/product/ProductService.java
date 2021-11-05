package webapp.pickme.petshop.service.product;

import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.Filter;
import webapp.pickme.petshop.api.view.ProductView;
import webapp.pickme.petshop.data.model.product.Product;
import webapp.pickme.petshop.data.repository.ProductRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProductView> getAll(){
        return productRepository.findAll()
                                .stream()
                                .map(ProductView::new)
                                .collect(Collectors.toList());
    }

    public ProductView add(ProductView productView){
        var product = new Product(productView);
        return new ProductView(productRepository.save(product));
    }

    public void delete(Long id){
        productRepository.deleteById(id);
    }

    public List<ProductView> filter(Filter filter){
        return entityManager.createQuery(
                productQueryFactory.createFilterForProduct( entityManager, filter)
        ).getResultList()
         .stream()
         .map(ProductView::new)
         .collect(Collectors.toList());
    }

    public ProductView edit(ProductView productView){
        var product = new Product(productView);
        return new ProductView(this.productRepository.save(product));
    }

    public void edit(Product product){
        this.productRepository.save(product);
    }
}
