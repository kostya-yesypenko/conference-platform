package org.chnu.confplatform.back.filtering.predicate;

import org.chnu.confplatform.back.filtering.FilterableProperty;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface EntityFilterSpecificationsBuilder<EntityType> {

    Logger LOG = LoggerFactory.getLogger(EntityFilterSpecificationsBuilder.class);


    List<FilterableProperty<EntityType>> getFilterableProperties();

    default Specification<EntityType> buildSpecification(List<SearchCriteria> searchCriterias) {

        Specification<EntityType> specification = null;
        for (SearchCriteria searchCriteria : searchCriterias) {
            Optional<FilterableProperty<EntityType>> filterableProperty =
                    getFilterableProperties().stream()
                            .filter(property -> property.getPropertyName().equals(searchCriteria.getKey())).findFirst();

            if (filterableProperty.isPresent()) {

                FilterableProperty<EntityType> filterablePropertyObject = filterableProperty.get();
                Specification<EntityType> buildSpecification = filterablePropertyObject.getSpecificationBuilder().buildSpecification(searchCriteria);

                if (specification == null) {
                    specification = Specification.where(buildSpecification);
                } else {
                    specification = specification.and(buildSpecification);
                }

            } else {
                LOG.warn("Filtering on property '{}' has been skipped because it's absent in filterableProperties",
                        searchCriteria.getKey());
            }
        }
        return specification;
    }


    default Specification<EntityType> buildSpecification(SearchCriteria... searchCriteria) {
        return buildSpecification(Arrays.asList(searchCriteria));
    }

}
