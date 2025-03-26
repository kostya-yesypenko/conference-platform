package org.chnu.confplatform.back.api.controller.base;


import org.chnu.confplatform.back.filtering.predicate.EntityFilterSpecificationsBuilder;

public interface FilterableController<T> {

    default EntityFilterSpecificationsBuilder<T> getFilterSpecificationsBuilder() {
        return null;
    }
}

