package org.chnu.confplatform.back.service;


import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.dao.AbstractDao;
import org.chnu.confplatform.back.dao.UserDao;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.predicate.LocalDateTimeComparisonSpecification;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.chnu.confplatform.back.model.EmailConfirmationToken;
import org.chnu.confplatform.back.model.base.Role;
import org.chnu.confplatform.back.model.User;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService extends AbstractService<User> {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    protected AbstractDao<User> getDao() {
        return userDao;
    }

    public User findByEmail(String email) {
        return this.userDao.findByEmail(email);
    }

    public User createOrUpdate(User user) throws MessagingException, IOException {
        if (user.getId() == null) {
            return save(user);
        }
        return update(user);
    }

    public boolean comparePasswords(String newPassword, String oldHashedPassword) {
        return passwordEncoder.matches(newPassword, oldHashedPassword);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void confirmRegistration(String token) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findByToken(token);
        if (token.equals(emailConfirmationToken.getToken()) || LocalDateTime.now().isBefore(emailConfirmationToken.getExpiredAt())) {
            User user = emailConfirmationToken.getUser();
            user.setRole(Role.ROLE_USER);
            emailConfirmationTokenService.delete(emailConfirmationToken);
            save(user);
        }
    }

    public void deleteUserAndEmailConfirmationToken(User user, EmailConfirmationToken token) {
        emailConfirmationTokenService.delete(token);
        delete(user);
    }

    @Scheduled(cron = "${cron.batch.deleteNotActiveUsers}")
    public void deleteNotConfirmedUsers() {
        SearchCriteria searchCriteria = new SearchCriteria("expiredAt", FilteringOperation.LESS_THEN, LocalDateTime.now());
        Specification<EmailConfirmationToken> specification = new LocalDateTimeComparisonSpecification<>(searchCriteria);
        List<EmailConfirmationToken> tokens = emailConfirmationTokenService.get(specification);
        tokens.forEach(token -> {
            deleteUserAndEmailConfirmationToken(token.getUser(), token);
        });
    }
}
