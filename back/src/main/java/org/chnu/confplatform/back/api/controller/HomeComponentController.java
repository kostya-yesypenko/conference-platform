package org.chnu.confplatform.back.api.controller;

import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.api.dto.homeComponent.HomeComponentRequest;
import org.chnu.confplatform.back.api.dto.homeComponent.HomeComponentResponse;
import org.chnu.confplatform.back.api.mapper.HomeComponentMapper;
import org.chnu.confplatform.back.common.ApplicationConstants;
import org.chnu.confplatform.back.model.HomeComponent;
import org.chnu.confplatform.back.service.HomeComponentService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeComponentController {
    public static final String API_URL = ApplicationConstants.ApiConstants.API_PREFIX
            + ApplicationConstants.ApiConstants.BACKOFFICE + ApplicationConstants.ApiConstants.HOME_COMPONENT_ENDPOINT;

    private final HomeComponentService homeComponentService;
    private final HomeComponentMapper homeComponentMapper;

    @GetMapping
    public ResponseEntity<HomeComponentResponse> getHomeComponent() throws ChangeSetPersister.NotFoundException {
        HomeComponent entity = homeComponentService.getById(1).get();
        if(entity == null){
            throw new ChangeSetPersister.NotFoundException();
        }
        return ResponseEntity.status(200).body(homeComponentMapper.entityToResponse(entity));
    }

    @PostMapping
    public ResponseEntity<HomeComponentResponse> createHomeComponent(@RequestBody HomeComponentRequest request){
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        HomeComponent homeComponent = homeComponentMapper.requestToEntity(request,Optional.empty());
        homeComponent = homeComponentService.save(homeComponent);
        return ResponseEntity.status(201).body(homeComponentMapper.entityToResponse(homeComponent));
    }

    @PostMapping("/{id}")
    public ResponseEntity<HomeComponentResponse> updateHomeComponent(
            @PathVariable Integer id, @RequestBody HomeComponentRequest request) throws MessagingException, IOException {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        HomeComponent homeComponent = homeComponentMapper.requestToEntity(request, Optional.ofNullable(id));
        homeComponent = homeComponentService.update(homeComponent);
        return ResponseEntity.status(201).body(homeComponentMapper.entityToResponse(homeComponent));
    }
}
