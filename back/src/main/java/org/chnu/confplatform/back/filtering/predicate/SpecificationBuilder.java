package org.chnu.confplatform.back.filtering.predicate;

import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<EntityType> {

    Specification<EntityType> buildSpecification(SearchCriteria searchCriteria);

}

