package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.exception.NotFoundException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject
    private AccountDAO accountDAO;

    @Override
    public Account retrieveAccount(Long accountId) {
        log.debug("retrieveAccount {} ", accountId);
        return accountDAO.findById(accountId).orElseThrow(()-> new NotFoundException("There is not account with id:"+accountId));
    }

    @Override
    @Transactional
    public Account createAccount(BigDecimal init, Currency currency, BigDecimal overdraft) {
        var account = new Account();
        account.setBalance(init);
        account.setCurrency(currency);
        account.setOverdraft(overdraft);
        return accountDAO.save(account);
    }

    @Override
    public Account createAccount(BigDecimal init, Currency currency) {
        return createAccount(init, currency, BigDecimal.ZERO);
    }

    @Override
    public List<Account> retrieveAll() {
        log.debug("get all accounts");
        return accountDAO.findAll().orElse(emptyList());
    }

}

