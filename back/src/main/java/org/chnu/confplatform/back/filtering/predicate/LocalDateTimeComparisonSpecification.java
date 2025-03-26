package org.chnu.confplatform.back.filtering.predicate;


import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class LocalDateTimeComparisonSpecification<EntityType> implements Specification<EntityType> {

    private final SearchCriteria searchCriteria;

    public LocalDateTimeComparisonSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        LocalDateTime value = (LocalDateTime) searchCriteria.getValue();
        if (FilteringOperation.GREATER_THEN == searchCriteria.getOperation()) {
            return cb.greaterThan(root.get(searchCriteria.getKey()), value);
        } else if (FilteringOperation.LESS_THEN == searchCriteria.getOperation()) {
            return cb.lessThan(root.get(searchCriteria.getKey()), value);
        } else if (FilteringOperation.GREATER_OR_EQUAL == searchCriteria.getOperation()) {
            return cb.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), value);
        } else if (FilteringOperation.LESS_OR_EQUAL == searchCriteria.getOperation()) {
            return cb.lessThanOrEqualTo(root.get(searchCriteria.getKey()), value);
        } else if (FilteringOperation.EQUAL == searchCriteria.getOperation()) {
            return cb.equal(root.get(searchCriteria.getKey()), value);
        } else if (FilteringOperation.NOT_EQUAL == searchCriteria.getOperation()) {
            return cb.notEqual(root.get(searchCriteria.getKey()), value);
        } else {
            return null;
        }
    }

}
