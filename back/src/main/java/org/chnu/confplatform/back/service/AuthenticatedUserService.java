package org.chnu.confplatform.back.service;


import org.chnu.confplatform.back.model.base.Role;
import org.chnu.confplatform.back.security.AuthenticatedUser;
import org.chnu.confplatform.back.service.exception.NoCurrentUserException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public boolean isAdmin() {
        return Role.ROLE_ADMIN.equals(currentUser().getRole());
    }

    public boolean isChair() {
        return Role.ROLE_CHAIR.equals(currentUser().getRole());
    }


    private AuthenticatedUser currentUser() throws NoCurrentUserException {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new NoCurrentUserException();
        }

        if (!(authentication.getPrincipal() instanceof AuthenticatedUser)) {
            throw new NoCurrentUserException();
        }

        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
        if (authenticatedUser == null) {
            throw new NoCurrentUserException();
        }

        return authenticatedUser;
    }

    public void authenticateUser(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getUsername());

        authenticateUser(authentication);
    }

    public void authenticateUser(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
