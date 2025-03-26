package org.chnu.confplatform.back.security;


import org.chnu.confplatform.back.model.AuthData;
import org.chnu.confplatform.back.model.base.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class AuthenticatedUser extends User {

    private static final long serialVersionUID = 6700782328491796936L;

    private final Role role;

    public AuthenticatedUser(AuthData authData, Role role,
                             boolean enabled, boolean accountNonExpired,
                             boolean credentialsNonExpired, boolean accountNonLocked,
                             List<GrantedAuthority> authorityList) {
        super(authData.getClientSecretKey(), "client_secret_key", authorityList);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

}
