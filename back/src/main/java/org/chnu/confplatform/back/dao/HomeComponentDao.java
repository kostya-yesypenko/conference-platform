package org.chnu.confplatform.back.dao;

import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.model.HomeComponent;
import org.chnu.confplatform.back.repository.HomeComponentRepository;
import org.chnu.confplatform.back.repository.base.PrimaryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeComponentDao extends AbstractDao<HomeComponent>{
    private final HomeComponentRepository homeComponentRepository;

    @Override
    protected PrimaryRepository<Integer, HomeComponent> getRepository() {
        return homeComponentRepository;
    }
}
