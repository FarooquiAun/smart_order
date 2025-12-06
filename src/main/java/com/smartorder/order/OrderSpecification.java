package com.smartorder.order;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;



public class OrderSpecification {
    public static Specification<Order> withFilters(
            OrderStatus status,
            Instant createdFrom,
            Instant createdTo,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Long customerId
    ){
       return ((root, query, cb) ->{
           List<Predicate> predicates = new ArrayList<>();

           if (status != null) {
               predicates.add(cb.equal(root.get("status"), status));
           }

           if (createdFrom != null) {
               predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
           }

           if (createdTo != null) {
               predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
           }

           if (minAmount != null) {
               predicates.add(cb.greaterThanOrEqualTo(root.get("totalAmount"), minAmount));
           }

           if (maxAmount != null) {
               predicates.add(cb.lessThanOrEqualTo(root.get("totalAmount"), maxAmount));
           }

           if (customerId != null) {
               predicates.add(cb.equal(root.get("customer").get("id"), customerId));
           }

           return cb.and(predicates.toArray(new Predicate[0]));
       });

    }

}
