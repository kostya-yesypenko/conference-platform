package org.chnu.confplatform.back.dao;


import org.chnu.confplatform.back.filtering.specification.EqualSpecification;
import org.chnu.confplatform.back.filtering.specification.IdSpecification;
import org.chnu.confplatform.back.filtering.specification.InCollectionSpecification;
import org.chnu.confplatform.back.filtering.specification.SpecificationUtil;
import org.chnu.confplatform.back.filtering.ArchivedSpecification;
import org.chnu.confplatform.back.model.base.Archivable;
import org.chnu.confplatform.back.model.base.Identifiable;
import org.chnu.confplatform.back.model.base.PrimaryEntity;
import org.chnu.confplatform.back.repository.base.PrimaryRepository;
import org.chnu.confplatform.back.service.AuthenticatedUserService;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends PrimaryEntity<Integer>> {

    protected AuthenticatedUserService userAuthorizationService;

    protected abstract PrimaryRepository<Integer, T> getRepository();

    protected Specification<T> addAdditionalSpecificationsForGet() {
        return null;
    }

    public boolean isEditAllowed(T entity) {
        return userAuthorizationService.isAdmin();
    }

    public boolean exists(Specification<T> filter) {
        return this.existsIncludingArchived(filter, false);
    }

    public boolean existsIncludingArchived(Specification<T> filter, boolean includeArchivedEntities) {
        final Specification<T> specification = addSpecifications(includeArchivedEntities).and(filter);
        return getRepository().count(specification) > 0;
    }

    public Page<T> get(Pageable pageable) {
        return get(pageable, false);
    }


    public Page<T> get(Pageable pageable, boolean includeArchived) {

        Specification<T> specification = addSpecifications(includeArchived).and(addAdditionalSpecificationsForGet());

        Sort sort = pageable.getSort();
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<T> page = getRepository().findAll(specification, pageRequest);

        hasReadPermission(page.getContent());

        return page;
    }

    public List<T> get() {

        Specification<T> specification = addSpecifications().and(addAdditionalSpecificationsForGet());
        List<T> list = getRepository().findAll(specification);

        hasReadPermission(list);

        return list;
    }

    public List<T> get(Sort sort) {
        return get(sort, false);
    }

    public List<T> get(Sort sort, boolean includeArchived) {
        Specification<T> specification = addSpecifications(includeArchived).and(addAdditionalSpecificationsForGet());

        List<T> list = getRepository().findAll(specification, sort);

        hasReadPermission(list);

        return list;
    }

    public Page<T> get(Specification<T> filter, Pageable pageable) {
        return get(filter, pageable, false);
    }

    public Page<T> get(Specification<T> filter, Pageable pageable, boolean includeArchived) {
        Specification<T> specification = addSpecifications(includeArchived).and(filter).and(addAdditionalSpecificationsForGet());

        Page<T> page = getRepository().findAll(specification, pageable);

        hasReadPermission(page.getContent());

        return page;
    }

    public List<T> getIncludeArchived(Specification<T> filter) {
        Specification<T> specification = addSpecifications(true).and(filter);
        return getRepository().findAll(specification);
    }

    public List<T> get(Specification<T> filter) {
        return this.get(filter, false);
    }

    public List<T> get(Specification<T> filter, boolean ignorePermissions) {
        Specification<T> specification = addSpecifications().and(filter).and(addAdditionalSpecificationsForGet());

        List<T> list = getRepository().findAll(specification);

        if (!ignorePermissions) {
            hasReadPermission(list);
        }

        return list;
    }

    public List<T> get(Specification<T> filter, Sort sort) {
        return get(filter, sort, false);
    }

    public List<T> get(Specification<T> filter, Sort sort, boolean includeArchived) {
        Specification<T> specification = addSpecifications(includeArchived).and(filter).and(addAdditionalSpecificationsForGet());
        List<T> list = getRepository().findAll(specification, sort);

        hasReadPermission(list);

        return list;

    }


    protected List<T> getByField(String field, Object value) {
        return this.getByField(field, value, false);
    }

    protected List<T> getByField(String field, Object value, boolean ignorePermissions) {
        EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
        return get(equalSpecification, ignorePermissions);
    }

    protected List<T> getByField(String field, Object value, Sort sort) {
        EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
        return get(equalSpecification, sort);
    }

    protected List<T> getByFieldIncludeArchived(String field, Object value) {
        EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
        return getIncludeArchived(equalSpecification);
    }

    protected boolean existsByField(String field, Object value) {
        final EqualSpecification<T> equalSpecification = new EqualSpecification<>(field, value);
        return exists(equalSpecification);
    }

    protected <R extends Identifiable> List<T> getByFieldInCollection(String field, R value) {
        return this.getByFieldInCollection(field, value, false);
    }

    protected <R extends Identifiable> List<T> getByFieldInCollection(String field, R value, boolean ignorePermissions) {
        InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
        return get(inCollectionSpecification, ignorePermissions);
    }

    protected <R extends Identifiable> List<T> getByFieldInCollectionIncludeArchived(String field, R value) {
        InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
        return getIncludeArchived(inCollectionSpecification);
    }

    protected <R extends Identifiable> T getOneByFieldInCollection(String field, R value) {
        InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(getEntityTypeClasses()[0], field, value);
        return getOne(inCollectionSpecification, false);
    }

    protected <R extends Identifiable> boolean existsByFieldInCollection(String field, R value) {
        final InCollectionSpecification<T, R> inCollectionSpecification = new InCollectionSpecification<>(
                getEntityTypeClasses()[0], field, value);
        return exists(inCollectionSpecification);
    }

    public T getOne(Specification<T> filter, boolean ignorePermissions) {
        Specification<T> specification = addSpecifications().and(filter);
        T entity = getRepository().findOne(specification).orElse(null);

        if (!ignorePermissions) {
            hasReadPermission(Collections.singletonList(entity));
        }

        return entity;
    }

    public T getOne(Integer id) {
        return getOneArchived(id, false);
    }

    public T getOne(Specification<T> specification) {
        return this.getOneArchived(specification, false);
    }

    public T getOneArchived(Integer id, boolean includeArchivedEntities) {
        return getOneArchived(id, includeArchivedEntities, null);
    }

    public T getOneArchived(Integer id, boolean includeArchivedEntities, Specification<T> additionalSpecification) {
        if (id == null) return null;
        Specification<T> mainSpecification = addSpecifications(includeArchivedEntities).and(new IdSpecification<>(id));

        if (additionalSpecification != null) {
            mainSpecification = additionalSpecification.and(mainSpecification);
        }

        return getRepository().findOne(mainSpecification).orElse(null);
    }

    public T getFirst(Specification<T> specification, boolean includeArchivedEntities) {
        Specification<T> specifications = addSpecifications(includeArchivedEntities).and(specification);

        List<T> result = getRepository().findAll(specifications);
        return result.isEmpty() ? null : result.get(0);
    }


    public T getOneArchived(Specification<T> specification, boolean includeArchivedEntities) {
        Specification<T> specifications = addSpecifications(includeArchivedEntities).and(specification);

        return getRepository().findOne(specifications).orElse(null);
    }

    public long getCount(Specification<T> filter) {
        PageRequest pageable = PageRequest.of(0, 1, Sort.unsorted());
        Page<T> records = get(filter, pageable);
        return records.getTotalElements();
    }

    public void delete(T entity) {
        delete(entity, false);
    }


    public void delete(T entity, boolean ignorePermissions) {
        getRepository().deleteById(entity.getId());
    }

    public void deleteById(Integer id) {
        T entity = getById(id).orElseThrow();
        if (entity instanceof Archivable) {
            ((Archivable) entity).setArchived(true);
            getRepository().save(entity);
        } else {
            delete(entity, false);
        }

    }

    protected Specification<T> addSpecifications() {
        return addSpecifications(false);
    }

    protected Specification<T> addSpecifications(boolean includeArchivedEntities) {
        Specification<T> specification = SpecificationUtil.initEmptySpec();

        if (!includeArchivedEntities && entityIsArchived()) {
            ArchivedSpecification<T> archivedSpecification = new ArchivedSpecification<>();
            specification = specification.and(archivedSpecification);
        }

        return specification;
    }

    public void hasReadPermission(List<T> entities) {
    }

    @SuppressWarnings("unchecked")
    protected Class<T>[] getEntityTypeClasses() {
        return (Class<T>[]) GenericTypeResolver.resolveTypeArguments(getClass(), AbstractDao.class);
    }

    private boolean entityIsArchived() {
        Class<T>[] entityTypeClasses = getEntityTypeClasses();
        return Archivable.class.isAssignableFrom(entityTypeClasses[0]);
    }

    public Optional<T> getById(Integer id) {
        return getRepository().findById(id);
    }

    public T create(T entity) {
        return getRepository().save(entity);
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public T update(T entity, boolean ignorePermissions) {
        return getRepository().save(entity);
    }


}
