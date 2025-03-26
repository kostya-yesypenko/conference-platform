package org.chnu.confplatform.back.filtering.specification.user;


import org.chnu.confplatform.back.filtering.FilterableProperty;
import org.chnu.confplatform.back.filtering.predicate.EntityFilterSpecificationsBuilder;
import org.chnu.confplatform.back.filtering.predicate.EqualingSpecificationBuilder;
import org.chnu.confplatform.back.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSpecificationBuilder implements EntityFilterSpecificationsBuilder<User> {

    private static final List<FilterableProperty<User>> FILTERABLE_PROPERTIES = List.of(
            new FilterableProperty<>("email", String.class,
                    EqualingSpecificationBuilder.SUPPORTED_OPERATORS, new EqualingSpecificationBuilder<>())

    );

    @Override
    public List<FilterableProperty<User>> getFilterableProperties() {
        return FILTERABLE_PROPERTIES;
    }
}

