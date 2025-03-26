package org.chnu.confplatform.back.api.mapper;


import org.chnu.confplatform.back.api.dto.user.UserRequest;
import org.chnu.confplatform.back.api.dto.user.UserResponse;
import org.chnu.confplatform.back.api.mapper.base.MapperI;
import org.chnu.confplatform.back.model.base.Role;
import org.chnu.confplatform.back.model.User;
import org.chnu.confplatform.back.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserMapper implements MapperI<User, UserResponse, UserRequest> {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserMapper(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse entityToResponse(User entity) {
        UserResponse response = new UserResponse();
        response.setId(entity.getId());
        response.setEmail(entity.getEmail());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setRole(entity.getRole().toString());
        return response;
    }

    @Override
    public List<UserResponse> entitiesToListResponse(List<User> entities) {
        List<UserResponse> responses = new ArrayList<>();
        for (User u : entities) {
            responses.add(entityToResponse(u));
        }
        return responses;
    }

    @Override
    public User requestToEntity(UserRequest request, Optional<Integer> id) {
        User user = new User();
        if (id.isPresent()) {
            user = userService.getById(id.get()).get();
        }
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() == null) {
            user.setRole(Role.valueOf("ROLE_ADMIN"));
        } else {
            user.setRole(Role.valueOf(request.getRole()));
        }
        if (request.getRole() == null) {
            user.setArchived(false);
        } else {
            user.setArchived(request.isArchived());
        }
        return user;
    }

}
