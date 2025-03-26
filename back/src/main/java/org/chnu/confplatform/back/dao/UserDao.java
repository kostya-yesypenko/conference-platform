package org.chnu.confplatform.back.dao;

import lombok.RequiredArgsConstructor;
import org.chnu.confplatform.back.filtering.FilteringOperation;
import org.chnu.confplatform.back.filtering.predicate.EqualOrContainSpecification;
import org.chnu.confplatform.back.filtering.searchcriteria.SearchCriteria;
import org.chnu.confplatform.back.model.User;
import org.chnu.confplatform.back.repository.base.PrimaryRepository;
import org.chnu.confplatform.back.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDao extends AbstractDao<User> {

    private final UserRepository repository;

    @Override
    protected PrimaryRepository<Integer, User> getRepository() {
        return repository;
    }

    public User findByEmail(String email) {
        SearchCriteria searchCriteria = new SearchCriteria("email", FilteringOperation.EQUAL, email);
        EqualOrContainSpecification<User> emailSpecification = new EqualOrContainSpecification<>(searchCriteria);
        return getOne(emailSpecification);
    }
}
