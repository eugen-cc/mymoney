package cc.eugen.mymoney.model.dao.api;

import cc.eugen.mymoney.model.entity.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface TransactionDAO {

    Optional<Transaction> findById(Long transactionId);

    Transaction save(Transaction transaction);

    Optional<List<Transaction>> findAll();

}
