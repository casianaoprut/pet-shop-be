package webapp.pickme.petshop.service.product;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.Filter;
import webapp.pickme.petshop.api.view.ProductView;
import webapp.pickme.petshop.data.model.product.Product;
import webapp.pickme.petshop.data.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
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

    public ProductView add(ProductView productView) throws IOException {
        var product = mapProductViewToProduct(productView);
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

    public ProductView edit(ProductView productView) throws IOException {
        var product = mapProductViewToProduct(productView);
        return new ProductView(this.productRepository.save(product));
    }

    public void edit(Product product){
        this.productRepository.save(product);
    }

    public ProductView getById(Long id){
        return new ProductView(this.productRepository.findById(id).orElseThrow(() ->
                new ProductException("Incorrect id!")));
    }

    public List<ProductView> getProductListByIdList(List<Long> idList){
        return idList.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    @Transactional
    public byte[] getProductPhoto(Long id) throws SQLException, IOException {
        var product = this.productRepository.findById(id).orElseThrow(() -> new ProductException("Invalid id"));
        return product.getPhoto().getBinaryStream().readAllBytes();
    }

    private Product mapProductViewToProduct(ProductView productView) throws IOException {
        var product = new Product(productView);
        var file = productView.getPhoto();
        var iStream = file.getInputStream();
        long size = file.getSize();
        var session = (Session) entityManager.getDelegate();
        Blob photo = Hibernate.getLobCreator(session).createBlob(iStream, size);
        product.setPhoto(photo);
        return product;
    }
}
