package org.chnu.confplatform.back.service;

import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.dao.AbstractDao;
import org.chnu.confplatform.back.dao.HomeComponentDao;
import org.chnu.confplatform.back.model.HomeComponent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeComponentService extends AbstractService<HomeComponent>{
    private final HomeComponentDao homeComponentDao;

    public HomeComponent save(HomeComponent entity){
        if(homeComponentDao.getById(1).isEmpty()){
            return super.save(entity);
        }
        return null;
    }

    @Override
    protected AbstractDao<HomeComponent> getDao() {
        return homeComponentDao;
    }
}
