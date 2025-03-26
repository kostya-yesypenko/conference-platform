package org.chnu.confplatform.back.dao;


import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.predicate.EqualingOrNullSpecification;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.chnu.confplatform.back.model.EmailConfirmationToken;
import org.chnu.confplatform.back.repository.EmailConfirmationTokenRepository;
import org.chnu.confplatform.back.repository.base.PrimaryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConfirmationTokenDao extends AbstractDao<EmailConfirmationToken> {

    private final EmailConfirmationTokenRepository repository;

    @Override
    protected PrimaryRepository getRepository() {
        return repository;
    }

    public EmailConfirmationToken findByToken(String token) {
        SearchCriteria searchCriteria = new SearchCriteria("token", FilteringOperation.EQUAL, token);
        EqualingOrNullSpecification<EmailConfirmationToken> tokenSpecification = new EqualingOrNullSpecification<>(searchCriteria);
        return getOne(tokenSpecification);
    }
}
