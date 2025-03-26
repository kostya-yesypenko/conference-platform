package org.chnu.confplatform.back.filtering;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ArchivedSpecification<EntityType> implements Specification<EntityType> {

    private static final long serialVersionUID = -7315580228476708139L;

    @Override
    public Predicate toPredicate(Root<EntityType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get("archived"), false);
    }
}
