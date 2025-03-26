package org.chnu.confplatform.back.api.controller;


import org.chnu.confplatform.back.api.controller.base.AbstractController;
import org.chnu.confplatform.back.common.ApplicationConstants;
import org.chnu.confplatform.back.filtering.specification.user.UserSpecificationBuilder;
import org.chnu.confplatform.back.api.dto.user.UserRequest;
import org.chnu.confplatform.back.api.dto.user.UserResponse;
import org.chnu.confplatform.back.filtering.predicate.EntityFilterSpecificationsBuilder;
import org.chnu.confplatform.back.api.mapper.base.MapperI;
import org.chnu.confplatform.back.api.mapper.UserMapper;
import org.chnu.confplatform.back.model.User;
import org.chnu.confplatform.back.service.AbstractService;
import org.chnu.confplatform.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserController.API_URL)
public class UserController extends AbstractController<User, UserRequest, UserResponse> {

    public static final String API_URL = ApplicationConstants.ApiConstants.API_PREFIX
            + ApplicationConstants.ApiConstants.BACKOFFICE + ApplicationConstants.ApiConstants.USERS_ENDPOINT;
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    private UserSpecificationBuilder specificationBuilder;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public MapperI<User, UserResponse, UserRequest> getMapper() {
        return userMapper;
    }

    @Override
    public AbstractService<User> getService() {
        return userService;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public EntityFilterSpecificationsBuilder<User> getFilterSpecificationsBuilder() {
        return specificationBuilder;
    }

}
