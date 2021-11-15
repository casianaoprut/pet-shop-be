package webapp.pickme.petshop.service.product;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import webapp.pickme.petshop.api.view.Filter;
import webapp.pickme.petshop.data.model.product.Product;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Service
public class ProductQueryFactory {

     CriteriaQuery<Product> createFilterForProduct(EntityManager entityManager, Filter filter){
        var cb = entityManager.getCriteriaBuilder();
        var cr = cb.createQuery(Product.class);
        var root = cr.from(Product.class);
        cr.select(root);
        return cr.where(getPredicatesForProduct(filter, cb, root));
    }

    private Predicate[] getPredicatesForProduct(Filter filter, CriteriaBuilder cb, Root<Product> root){
        return Stream.of(
            filterForContainsString(filter.getName(), "name", cb, root),
            filterForPrice(filter.getMaxPrice(), filter.getMinPrice(), cb, root),
            propertyEqualsValue("category", filter.getCategory(), cb, root),
            propertyEqualsValue("forBreed",filter.getBreed(), cb, root)
        ).toArray(Predicate[]::new);
    }

    private Predicate filterForContainsString(String stringContent, String propertyKey, CriteriaBuilder cb, Root<Product> root){
        if (StringUtils.isEmpty(stringContent)) {
            return cb.and();
        }
        return cb.like(cb.lower(root.get(propertyKey)), "%" + stringContent + "%".toLowerCase());
    }

    private Predicate filterForPrice(Integer maxPrice, Integer minPrice, CriteriaBuilder cb, Root<Product> root){
        if (maxPrice == null && minPrice == null){
            return cb.and();
        }
        if(maxPrice == null){
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        }
        if(minPrice == null){
            minPrice = 0;
        }
        return cb.between(root.get("price"), minPrice, maxPrice);
    }

    private Predicate propertyEqualsValue(String propertyKey, Object value, CriteriaBuilder cb, Root<Product> root) {
         if (value == null) {
             return cb.and();
         }
         return cb.equal(root.get(propertyKey), value);
    }
}
