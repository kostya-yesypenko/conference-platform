package org.chnu.confplatform.back.api.mapper.base;


import org.chnu.confplatform.back.api.dto.AbstractRequest;
import org.chnu.confplatform.back.api.dto.AbstractResponse;

import java.util.List;
import java.util.Optional;

public interface MapperI<T, ResponseType extends AbstractResponse, RequestType extends AbstractRequest> {

    ResponseType entityToResponse(T entity);

    List<ResponseType> entitiesToListResponse(List<T> entities);

    T requestToEntity(RequestType request, Optional<Integer> id);

}
