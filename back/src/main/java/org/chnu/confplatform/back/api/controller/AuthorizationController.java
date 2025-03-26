package org.chnu.confplatform.back.api.controller;


import org.chnu.confplatform.back.api.dto.authorization.AuthorizationRequest;
import org.chnu.confplatform.back.api.dto.authorization.AuthorizationResponse;
import org.chnu.confplatform.back.api.dto.user.UserRequest;
import org.chnu.confplatform.back.common.ApplicationConstants;
import org.chnu.confplatform.back.api.mapper.UserMapper;
import org.chnu.confplatform.back.model.EmailConfirmationToken;
import org.chnu.confplatform.back.model.base.Role;
import org.chnu.confplatform.back.model.Token;
import org.chnu.confplatform.back.model.User;
import org.chnu.confplatform.back.security.jwt.JwtService;
import org.chnu.confplatform.back.service.AuthorizationService;
import org.chnu.confplatform.back.service.EmailConfirmationTokenService;
import org.chnu.confplatform.back.service.EmailService;
import org.chnu.confplatform.back.service.TokenService;
import org.chnu.confplatform.back.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(AuthorizationController.API_URL)
public class AuthorizationController {
    public static final String API_URL = ApplicationConstants.ApiConstants.API_PREFIX
            + ApplicationConstants.ApiConstants.BACKOFFICE + ApplicationConstants.ApiConstants.AUTHENTICATION_CONTROLLER;

    @Value("${org.chnu.confplatform.back.backoffice.url}")
    private String frontUrl;

    private final UserService userService;

    private final AuthorizationService authorizationService;

    private final TokenService tokenService;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final EmailConfirmationTokenService emailConfirmationTokenService;

    private final EmailService emailService;

    public AuthorizationController(
            UserService userService,
            AuthorizationService authorizationService,
            TokenService tokenService,
            JwtService jwtService,
            UserMapper userMapper, EmailConfirmationTokenService emailConfirmationTokenService, EmailService emailService) {
        this.userService = userService;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthorizationResponse> login(@RequestBody AuthorizationRequest request) {
        User user = userService.findByEmail(request.getEmail());
        if (user != null && authorizationService.validatePasswords(request.getPassword(), user.getPassword()) && user.getRole() != Role.ROLE_UNCONFIRMED) {
            AuthorizationResponse response = new AuthorizationResponse();
            response.setToken(jwtService.generateToken(request.getEmail()));
            response.setRefreshToken(tokenService.createRefreshToken());
            response.setAccessTokenLifetimeMinutes(jwtService.jwtExpirationMinutes);
            response.setRefreshTokenLifetimeMinutes(jwtService.refreshTokenExpirationMinutes);
            response.setRole(user.getRole().toString());
            response.setUser(userMapper.entityToResponse(user));

            Token token = tokenService.findByUser(user).orElse(new Token());
            token.setUser(user);
            token.setRefreshToken(response.getRefreshToken());
            token.setExpired(LocalDateTime.now().plusMinutes(jwtService.refreshTokenExpirationMinutes));
            tokenService.save(token);

            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestParam @NotEmpty String refreshToken) {
        Token token = tokenService.findByRefreshToken(refreshToken);
        if (tokenService.validateRefreshToken(token)) {
            AuthorizationResponse response = new AuthorizationResponse();
            response.setToken(jwtService.generateToken(token.getUser().getEmail()));
            response.setRefreshToken(tokenService.createRefreshToken());
            response.setAccessTokenLifetimeMinutes(jwtService.jwtExpirationMinutes);
            response.setRefreshTokenLifetimeMinutes(jwtService.refreshTokenExpirationMinutes);
            response.setRole(token.getUser().getRole().toString());
            response.setUser(userMapper.entityToResponse(token.getUser()));

            token.setRefreshToken(response.getRefreshToken());
            token.setExpired(LocalDateTime.now().plusMinutes(jwtService.refreshTokenExpirationMinutes));
            tokenService.save(token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        tokenService.delete(tokenService.findByRefreshToken(refreshToken));
        return new ResponseEntity<>("token timed out", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<Object> getUserFromToken(HttpServletRequest servletRequest) {
        String token = jwtService.getTokenFromRequest(servletRequest);
        String email = jwtService.getEmailFromToken(token);
        User user = userService.findByEmail(email);
        return new ResponseEntity<>(userMapper.entityToResponse(user), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public void logout(@RequestParam @NotEmpty String refreshToken) {
        tokenService.delete(tokenService.findByRefreshToken(refreshToken));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody UserRequest request) throws IOException, MessagingException {
        User user = userMapper.requestToEntity(request, Optional.empty());
        user.setRole(Role.ROLE_UNCONFIRMED);
        user = userService.save(user);
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken();
        emailConfirmationToken.setExpiredAt(LocalDateTime.now().plusMinutes(jwtService.refreshTokenExpirationMinutes));
        emailConfirmationToken.setUser(user);
        emailConfirmationToken.setToken(UUID.randomUUID().toString());
        emailConfirmationTokenService.save(emailConfirmationToken);
        emailService.sendHtmlMessage(user.getEmail(), "Email Confirmation", emailService.getHtmlConfirmationContent(user, frontUrl + "/confirm" + "?token=" + emailConfirmationToken.getToken(), frontUrl + "/login"));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/confirm")
    public void confirmEmail(@RequestParam String token) {
        userService.confirmRegistration(token);
    }
}
