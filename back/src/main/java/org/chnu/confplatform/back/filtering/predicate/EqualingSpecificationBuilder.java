package org.chnu.confplatform.back.filtering.predicate;


import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

public class EqualingSpecificationBuilder<EntityType> implements SpecificationBuilder<EntityType> {

    public static final List<FilteringOperation> SUPPORTED_OPERATORS = Arrays.asList(
            FilteringOperation.EQUAL,
            FilteringOperation.NOT_EQUAL);

    @Override
    public Specification<EntityType> buildSpecification(SearchCriteria searchCriteria) {
        return new EqualingSpecification<>(searchCriteria);
    }

}
