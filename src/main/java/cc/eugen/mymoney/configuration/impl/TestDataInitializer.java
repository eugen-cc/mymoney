package cc.eugen.mymoney.configuration.impl;

import cc.eugen.mymoney.configuration.api.Initializer;
import cc.eugen.mymoney.service.api.AccountService;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import static cc.eugen.mymoney.model.entity.Currency.CHF;
import static cc.eugen.mymoney.model.entity.Currency.EUR;
import static cc.eugen.mymoney.model.entity.Currency.GBP;
import static cc.eugen.mymoney.model.entity.Currency.JPY;
import static cc.eugen.mymoney.model.entity.Currency.RUB;
import static cc.eugen.mymoney.model.entity.Currency.USD;
import static java.math.BigDecimal.valueOf;

/**
 * THIS CLASS CREATES SOME ACCOUNTS ON STARTUP, JUST FOR DEMONSTRATION
 *
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
public class TestDataInitializer implements Initializer {

    @Inject
    private AccountService accountService;

    public void init() {
        log.debug("create test data");
        accountService.createAccount(valueOf(1_285.66), GBP, valueOf(-1_200.00));
        accountService.createAccount(valueOf(0.01), GBP);
        accountService.createAccount(valueOf(1_250.36), EUR);
        accountService.createAccount(valueOf(3_500.07), USD);
        accountService.createAccount(valueOf(16.66), USD);
        accountService.createAccount(valueOf(6_001.01), USD);
        accountService.createAccount(valueOf(8_700.31), GBP);
        accountService.createAccount(valueOf(40_589.90), CHF);
        accountService.createAccount(valueOf(150_500.31), RUB);
        accountService.createAccount(valueOf(90_500.31), RUB);
        accountService.createAccount(valueOf(23_160.77), JPY);
        accountService.createAccount(valueOf(-650.90), EUR, valueOf(-750));
        accountService.createAccount(valueOf(1_099.99), EUR, valueOf(-1_000));
        accountService.createAccount(valueOf(-2_875.75), USD, valueOf(-3_000));
    }
}
