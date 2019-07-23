package cc.eugen.mymoney.model.dao.impl;

import cc.eugen.mymoney.model.dao.PersistenceInitializer;
import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.entity.Account;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
public class AccountDAOImpl implements AccountDAO {

    @Inject
    private PersistenceInitializer initializer;

    @Inject
    private Provider<EntityManager> em;

    @Transactional
    public Optional<Account> findById(Long accountId) {
        em.get().flush();
        return Optional.ofNullable(em.get().find(Account.class, accountId));
    }

    @Transactional
    public Account save(Account account) {
        account = em.get().merge(account);
        em.get().flush();
        log.info("saved account : {}", account);
        return account;
    }

    @Override
    public Optional<List<Account>> findAll() {
        var query = em.get().createNamedQuery(Account.FIND_ALL, Account.class);
        return Optional.ofNullable(query.getResultList());
    }
}
