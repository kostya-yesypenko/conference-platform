package org.chnu.confplatform.back.filtering.specification;


import org.chnu.confplatform.back.model.base.Identifiable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public class InCollectionSpecification<T, R extends Identifiable> implements Specification<T> {

    private static final long serialVersionUID = 3657277566518014718L;

    private final Class<T> entityClass;

    private final String fieldPath;

    private final R value;

    public InCollectionSpecification(Class<T> entityClass, String fieldPath, R value) {
        this.entityClass = entityClass;
        this.fieldPath = fieldPath;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        Subquery<T> subQuery = query.subquery(entityClass);
        Root<T> sqRoot = subQuery.from(entityClass);
        Join<R, T> sqJoin = SpecificationUtil.buildJoin(sqRoot, fieldPath);
        subQuery.select(sqRoot);
        subQuery.where(cb.equal(sqJoin, value));

        return cb.in(root).value(subQuery);
    }

}
