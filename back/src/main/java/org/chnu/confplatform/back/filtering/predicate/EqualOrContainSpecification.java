package org.chnu.confplatform.back.filtering.predicate;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class EqualOrContainSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 2206438670684039538L;

    private final SearchCriteria searchCriteria;

    public EqualOrContainSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (searchCriteria.getValue() != null) {
            List<Predicate> predicates = new ArrayList<>();
            if (FilteringOperation.EQUAL == searchCriteria.getOperation()) {
                predicates.add(cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue()));
            }
            if (FilteringOperation.CONTAIN == searchCriteria.getOperation()) {
                String searchValueLowerCase = StringUtils.lowerCase((String) searchCriteria.getValue());
                predicates.add(cb.like(cb.lower(root.get(searchCriteria.getKey())), "%" + searchValueLowerCase + "%"));
            }
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EqualOrContainSpecification)) return false;

        final EqualOrContainSpecification that = (EqualOrContainSpecification) o;

        return new EqualsBuilder()
                .append(searchCriteria, that.searchCriteria)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(searchCriteria)
                .hashCode();
    }

    @Override
    public String toString() {
        return "EqualingOrNullSpecification{" + "searchCriteria=" + searchCriteria + '}';
    }
}

