package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.ApplicationTest;
import cc.eugen.mymoney.model.entity.Currency;
import org.junit.Test;

import static java.math.BigDecimal.valueOf;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class IntAccountServiceImplTest extends ApplicationTest {

    @Test
    public void testAccountService() {
        var wrote = accountService.createAccount(valueOf(100.00), Currency.EUR, valueOf(-300));
        var read = accountService.retrieveAccount(wrote.getId());
        assertEquals(wrote, read);
    }

    @Test
    public void testRetrieveAll() {
        var accounts = accountService.retrieveAll();
        assertTrue(accounts.size() > 0);
    }


}