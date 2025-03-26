package org.chnu.confplatform.back.service;


import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.dao.AbstractDao;
import org.chnu.confplatform.back.dao.TokenDao;
import org.chnu.confplatform.back.model.Token;
import org.chnu.confplatform.back.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService extends AbstractService<Token> {

    private final TokenDao tokenDao;

    @Override
    protected AbstractDao<Token> getDao() {
        return tokenDao;
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public Token findByRefreshToken(String refreshToken) {
        return this.tokenDao.findByRefreshToken(refreshToken);
    }

    public Optional<Token> findByUser(User user) {
        return this.tokenDao.findByUser(user);
    }

    public boolean validateRefreshToken(Token token) {
        return (token != null && LocalDateTime.now().isBefore(token.getExpired()));
    }
}
