package org.chnu.confplatform.back.filtering.predicate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class EqualingSpecification<EntityType> implements Specification<EntityType> {

    private static final long serialVersionUID = 800528251296820234L;

    private final SearchCriteria searchCriteria;

    public EqualingSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        switch (searchCriteria.getOperation()) {
            case EQUAL:
                return cb.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NOT_EQUAL:
                return cb.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EqualingSpecification)) return false;

        EqualingSpecification<?> that = (EqualingSpecification<?>) o;

        return new EqualsBuilder()
                .append(searchCriteria, that.searchCriteria)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(searchCriteria)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "EqualingSpecification{" +
                "searchCriteria=" + searchCriteria +
                '}';
    }
}
