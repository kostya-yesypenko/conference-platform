package org.chnu.confplatform.back.api.mapper;

import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.api.dto.homeComponent.HomeComponentRequest;
import org.chnu.confplatform.back.api.dto.homeComponent.HomeComponentResponse;
import org.chnu.confplatform.back.api.mapper.base.MapperI;
import org.chnu.confplatform.back.model.HomeComponent;
import org.chnu.confplatform.back.service.HomeComponentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeComponentMapper implements MapperI<HomeComponent, HomeComponentResponse, HomeComponentRequest> {

    private final HomeComponentService homeComponentService;

    @Override
    public HomeComponentResponse entityToResponse(HomeComponent entity) {
        HomeComponentResponse response = new HomeComponentResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        return response;
    }

    @Override
    public List<HomeComponentResponse> entitiesToListResponse(List<HomeComponent> entities) {
        return null;
    }

    @Override
    public HomeComponent requestToEntity(HomeComponentRequest request, Optional<Integer> id) {
        HomeComponent homeComponent = new HomeComponent();
        id.ifPresent(homeComponent::setId);
        homeComponent.setPhone(request.getPhone());
        homeComponent.setTitle(request.getTitle());
        homeComponent.setEmail(request.getEmail());
        return homeComponent;
    }
}
