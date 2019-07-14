package cc.eugen.mymoney.service.api;

import cc.eugen.mymoney.model.entity.Transaction;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface TransactionHandler {

    /**
     * @param transaction transaction that needs to be executed
     * @return an updated transaction object
     */
    Transaction handleTransaction(Transaction transaction);
}
