package org.chnu.confplatform.back.api.controller.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chnu.confplatform.back.api.dto.PageResponse;
import org.chnu.confplatform.back.api.dto.AbstractRequest;
import org.chnu.confplatform.back.api.dto.AbstractResponse;
import org.chnu.confplatform.back.filtering.FilterableProperty;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.IllegalFilteringOperationException;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.chnu.confplatform.back.api.mapper.base.MapperI;
import org.chnu.confplatform.back.model.base.Archivable;
import org.chnu.confplatform.back.model.base.Identifiable;
import org.chnu.confplatform.back.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public abstract class AbstractController<T extends Identifiable & Archivable, RequestType extends AbstractRequest,
        ResponseType extends AbstractResponse> implements FilterableController<T> {

    public static final Integer DEFAULT_PAGE_SIZE = 25;
    public static final Integer DEFAULT_PAGE_INDEX = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    private static final Pattern PATTERN = Pattern.compile("(\\w+?)(:|!_=|[!<>_]=?|=)(.*)");

    public abstract MapperI<T, ResponseType, RequestType> getMapper();

    public abstract AbstractService<T> getService();

    protected ConversionService conversionService;

    public abstract Class<T> getEntityClass();

    @GetMapping(path = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PageResponse<ResponseType>> getAll(
            @PathParam(value = "sort") Sort sort,

            @RequestParam(required = false) Optional<@PositiveOrZero Integer> index,

            @RequestParam(required = false) Optional<@Min(value = 0) @Max(value = 250) Integer> size,

            @RequestParam(value = "expand_fields", required = false) Optional<String> expand,

            @RequestParam(required = false) Optional<String> search) {
        if (size.isPresent() && (size.get() <= 0 || size.get() > 250)) {
            return ResponseEntity.badRequest().build();
        }
        if (index.isPresent() && index.get() < 0) {
            return ResponseEntity.badRequest().build();
        }
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        Specification<T> filteringSpecification = buildDefaultGetAllFilteringSpec(search);
        sort = mapSortPropertities(sort);

        int pageIndex = index.orElse(DEFAULT_PAGE_INDEX);

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<T> entitiesPaged = getService().get(filteringSpecification, pageable);
        if (entitiesPaged != null) {
            List<ResponseType> responses = entitiesPaged.getContent().stream()
                    .map(item -> getMapper().entityToResponse(item)).toList();
            PageResponse<ResponseType> apiResponseTypePageResponse =
                    new PageResponse<>(responses, entitiesPaged.getTotalElements(), entitiesPaged.getNumber(), entitiesPaged.getSize());
            return ResponseEntity.ok(apiResponseTypePageResponse);
        }
        return ResponseEntity.badRequest().build();
    }

    protected Sort mapSortPropertities(Sort sort) {
        if (sort == null) {
            return Sort.unsorted();
        }
        Iterator<Sort.Order> iterator = sort.iterator();
        List<Sort.Order> mappedOrders = new LinkedList<>();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            Sort.Order mappedOrder = new Sort.Order(
                    order.getDirection(),
                    order.getProperty(),
                    order.getNullHandling());
            mappedOrders.add(mappedOrder);
        }
        return Sort.by(mappedOrders);
    }


    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> getRecord(@PathVariable(value = "id") String id) {
        Optional<T> entity = getService().getById(Integer.valueOf(id));
        return entity.<ResponseEntity<Object>>map(t -> new ResponseEntity<>(getMapper().entityToResponse(t),
                HttpStatus.OK)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ResponseType> createRecord(@Valid @RequestBody(required = false) RequestType request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        T entity = getMapper().requestToEntity(request, Optional.empty());
        entity = executeEntityCreate(entity);
        ResponseType response = getMapper().entityToResponse(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<ResponseType> updateRecord(@PathVariable(value = "id") String id, @Valid @RequestBody RequestType request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            T entity = getMapper().requestToEntity(request, Integer.valueOf(id).describeConstable());
            entity = executeEntityUpdate(entity);
            ResponseType response = getMapper().entityToResponse(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PatchMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseType patchRecord(
            @PathVariable(value = "id") String id, @RequestBody Map<String, Object> request,
            HttpServletResponse response) throws MessagingException, IOException {
        T entity = getById(id);
        entity = executeEntityPatch(entity, request);

        return getMapper().entityToResponse(entity);
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteRecord(@PathVariable(value = "id") String id) {
        if (id != null) {
            getService().deleteById(Integer.valueOf(id));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    protected T executeEntityUpdate(T entity) throws MessagingException, IOException {
        return getService().update(entity);
    }

    protected T executeEntityCreate(T entity) {
        return getService().save(entity);
    }

    private T getById(String id) {
        return getService().getById(Integer.valueOf(id)).orElseThrow();
    }

    protected T executeEntityPatch(T entity, Map<String, Object> request) throws MessagingException, IOException {
        return getService().update(entity);
    }


    protected Specification<T> buildDefaultGetAllFilteringSpec(Optional<String> search) {
        if (search.isPresent() && this.getFilterSpecificationsBuilder() != null) {
            List<FilterableProperty<T>> filterableProperties = this.getFilterSpecificationsBuilder().getFilterableProperties();
            List<SearchCriteria> searchCriteria = parseSearchCriteria(search.get(), filterableProperties);
            return this.getFilterSpecificationsBuilder().buildSpecification(searchCriteria);
        }

        return null;
    }

    protected List<SearchCriteria> parseSearchCriteria(String searchQuery, List<FilterableProperty<T>> filterableProperties) {
        String[] searchParams = searchQuery.split(",");
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        for (String searchParameter : searchParams) {
            Matcher matcher = PATTERN.matcher(searchParameter);
            while (matcher.find()) {
                String key = matcher.group(1);
                String operationStr = matcher.group(2);
                FilteringOperation operation = FilteringOperation.fromString(operationStr);
                String value = matcher.group(3);
                Optional<FilterableProperty<T>> filterableProperty = filterableProperties.stream()
                        .filter(property -> property.getPropertyName().equals(key)).findFirst();
                if (filterableProperty.isPresent()) {
                    Object convertedValue;
                    if ("null".equals(value) || StringUtils.isEmpty(value)) {
                        convertedValue = null;
                    } else {
                        convertedValue = convertValueForCriteria(key, operation, value, filterableProperty.get());
                    }
                    // check if a FilterableOperation is supported
                    if (!filterableProperty.get().getOperators().contains(operation)) {
                        throw new IllegalFilteringOperationException("Operation '" + operation + "' is not supported for property " + key);
                    }
                    searchCriteria.add(new SearchCriteria(key, operation, convertedValue));
                } else {
                    LOGGER.warn("Filtering on property '{}' has been skipped because it's absent in filterableProperties", key);
                }
            }
        }
        return searchCriteria;
    }

    protected Object convertValueForCriteria(String key, FilteringOperation operation, String value, FilterableProperty<T> filterableProperty) {
        return conversionService.convert(value, filterableProperty.getExpectedType());
    }
}
