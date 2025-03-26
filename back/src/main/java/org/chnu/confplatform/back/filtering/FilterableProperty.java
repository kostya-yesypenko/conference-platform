package org.chnu.confplatform.back.filtering;


import org.chnu.confplatform.back.filtering.predicate.SpecificationBuilder;

import java.util.List;


public class FilterableProperty<EntityType> {

    private final String propertyName;

    private final Class<?> expectedType;

    private final List<FilteringOperation> operators;

    private final SpecificationBuilder<EntityType> specificationBuilder;

    public FilterableProperty(String propertyName,
                              Class<?> expectedType, List<FilteringOperation> operators,
                              SpecificationBuilder<EntityType> specificationBuilder) {
        this.propertyName = propertyName;
        this.expectedType = expectedType;
        this.operators = operators;
        this.specificationBuilder = specificationBuilder;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public List<FilteringOperation> getOperators() {
        return operators;
    }

    public SpecificationBuilder<EntityType> getSpecificationBuilder() {
        return specificationBuilder;
    }
}

