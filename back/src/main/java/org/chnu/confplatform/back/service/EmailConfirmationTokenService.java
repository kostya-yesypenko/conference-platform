package org.chnu.confplatform.back.service;


import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.dao.AbstractDao;
import org.chnu.confplatform.back.dao.EmailConfirmationTokenDao;
import org.chnu.confplatform.back.model.EmailConfirmationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConfirmationTokenService extends AbstractService<EmailConfirmationToken> {

    private final EmailConfirmationTokenDao dao;

    @Override
    protected AbstractDao<EmailConfirmationToken> getDao() {
        return dao;
    }

    public EmailConfirmationToken findByToken(String token) {
        return dao.findByToken(token);
    }
}
