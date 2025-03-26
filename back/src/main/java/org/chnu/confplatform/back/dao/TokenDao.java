package org.chnu.confplatform.back.dao;


import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.predicate.EqualOrContainSpecification;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.chnu.confplatform.back.model.Token;
import org.chnu.confplatform.back.model.User;
import org.chnu.confplatform.back.repository.base.PrimaryRepository;

import org.chnu.confplatform.back.repository.TokenRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenDao extends AbstractDao<Token> {

    private final TokenRepository tokenRepository;

    @Override
    protected PrimaryRepository<Integer, Token> getRepository() {
        return tokenRepository;
    }

    public Token findByRefreshToken(String refreshToken) {
        SearchCriteria criteria = new SearchCriteria("refreshToken", FilteringOperation.EQUAL, refreshToken);
        EqualOrContainSpecification<Token> searchTokenByRefreshToken = new EqualOrContainSpecification<>(criteria);
        return getOne(searchTokenByRefreshToken);
    }

    public Optional<Token> findByUser(User user) {
        SearchCriteria criteria = new SearchCriteria("user", FilteringOperation.EQUAL, user);
        EqualOrContainSpecification<Token> findByUserSpec = new EqualOrContainSpecification<>(criteria);
        return findOne(findByUserSpec);
    }

    public Optional<Token> findOne(Specification<Token> spec) {
        return tokenRepository.findOne(spec);
    }
}
