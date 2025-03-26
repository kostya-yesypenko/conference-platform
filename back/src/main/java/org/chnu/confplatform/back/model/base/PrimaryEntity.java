package org.chnu.confplatform.back.model.base;

public interface PrimaryEntity<T> {

    T getId();

    void setId(T id);
}

