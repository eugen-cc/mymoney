package cc.eugen.mymoney.model.dao.impl;

import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.entity.Transaction;
import com.google.inject.Inject;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class TransactionDAOImpl implements TransactionDAO {

    @Inject
    private Provider<EntityManager> em;

    public Transaction findById(Long transactionId) {
        return em.get().find(Transaction.class, transactionId);
    }

    public Transaction save(Transaction transaction) {
        return em.get().merge(transaction);
    }

    @Override
    public Optional<List<Transaction>> findAll() {
        var query = em.get().createNamedQuery(Transaction.FIND_ALL, Transaction.class);
        return Optional.ofNullable(query.getResultList());
    }
}
