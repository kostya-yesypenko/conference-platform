package org.chnu.confplatform.back.repository.base;

import org.chnu.confplatform.back.model.base.Identifiable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseCRUDRepository<EntityType extends Identifiable> extends PrimaryRepository<Integer, EntityType> {
}
