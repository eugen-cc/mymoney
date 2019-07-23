package cc.eugen.mymoney.model.dao.impl;

import cc.eugen.mymoney.model.dao.api.ExchangeDAO;
import cc.eugen.mymoney.model.entity.Exchange;
import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class ExchangeDAOImpl implements ExchangeDAO {

    @Inject
    private Provider<EntityManager> em;

    @Override
    public Exchange save(Exchange e) {
        e = em.get().merge(e);
        em.get().flush();
        return e;
    }

    @Override
    public Optional<List<Exchange>> findAll() {
        var query = em.get().createNamedQuery(Exchange.FIND_ALL, Exchange.class);
        return Optional.ofNullable(query.getResultList());
    }
}
