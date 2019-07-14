package cc.eugen.mymoney.service.api;

import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface AccountService {

    /**
     * @param accountId id of the account
     * @return expected account
     */
    Account retrieveAccount(Long accountId);

    /**
     * @param init initial amount
     * @param currency currency of the account
     * @return the created account object
     */
    Account createAccount(BigDecimal init, Currency currency);

    /**
     * @param init initial amount
     * @param currency currency of the account
     * @param overdraft account limit, must be negative
     * @return the created account object
     */
    Account createAccount(BigDecimal init, Currency currency, BigDecimal overdraft);

    /**
     * @return a list of all accounts, or an empty list
     */
    List<Account> retrieveAll();
}
