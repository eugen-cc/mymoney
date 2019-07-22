package cc.eugen.mymoney.service.api;

import cc.eugen.mymoney.model.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface TransactionService {

    /**
     * @param transactionId id of the transaction
     * @return expected transaction object
     */
    Transaction retrieveTransaction(Long transactionId);

    /**
     * @param sender id of the sender
     * @param receiver id of the receiver
     * @param amount amount in currency of the sender
     * @return a transaction object
     */
    Transaction transfer(Long sender, Long receiver, BigDecimal amount);

    /**
     * @return a list of all transactions or an empty list
     */
    List<Transaction> retrieveAll();

    /**
     * @param transaction Transaction object needs to be updated
     * @return updated object
     */
    Transaction update(Transaction transaction);
}
