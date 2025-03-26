package org.chnu.confplatform.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final PasswordEncoder passwordEncoder;

    public boolean validatePasswords(String password1, String password2) {
        return passwordEncoder.matches(password1, password2);
    }
}
