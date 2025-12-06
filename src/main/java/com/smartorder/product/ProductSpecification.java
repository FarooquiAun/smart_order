package com.smartorder.product;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilters(
            String name,
            BigDecimal minprice,
            BigDecimal maxprice,
            Boolean active,
            Instant createdFrom,
            Instant createdTo
    ){
        return (root, query, cb) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (name!=null && !name.isBlank()){
                predicates.add(
                        cb.like(
                              cb.lower(root.get("name")),
                                "%"+name.toLowerCase() +"%"
                        )
                );
            }
            if (minprice!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"),minprice));
            }
            if (maxprice!=null){
                predicates.add(cb.lessThanOrEqualTo(root.get("price"),maxprice));
            }
            if (active!=null){
                predicates.add(cb.equal(root.get("active"),active));
            }
            if (createdFrom!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"),createdFrom));
            }
            if (createdTo!=null){
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"),createdTo));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
