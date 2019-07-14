package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.dao.api.ExchangeDAO;
import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.model.entity.Exchange;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import com.google.inject.Inject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Random;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@RunWith(MockitoJUnitRunner.class)
public class TransactionHandlerImplTest extends Assert {

    @InjectMocks
    private TransactionHandlerImpl underTest;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private ExchangeDAO exchangeDAO;

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private AccountDAO accountDAO;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup(){

    }

    private Exchange newExchange(double value) {
        var ex = new Exchange();
        ex.setValue(valueOf(value));
        return ex;
    }

    @Test
    public void testTransactonHandling() {
        var t = new Transaction();

        when(exchangeService.exchange(any(), any(), any(), anyBoolean())).thenReturn(newExchange(1234));
        when(transactionDAO.save(any(Transaction.class))).thenReturn(t);

        t.setSender(newAccount());
        t.setReceiver(newAccount());
        t.setAmount(BigDecimal.TEN);

        var result = underTest.handleTransaction(t);

        assertTrue(result.getCompleted());
        verify(exchangeService).exchange(t.getAmount(), t.getSender().getCurrency(), t.getReceiver().getCurrency(), false);
        verify(accountDAO, times(2)).save(any(Account.class));
        verify(exchangeDAO).save(any(Exchange.class));
        verify(transactionDAO).save(any(Transaction.class));
    }

    private Account newAccount() {
        Random r = new Random();
        var a = new Account();
        a.setBalance(valueOf(r.nextDouble()*1000));
        Currency[] currencies = Currency.values();
        a.setCurrency(currencies[r.nextInt(currencies.length)]);
        a.setOverdraft(valueOf(-5_000));
        return a;
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(exchangeService, exchangeDAO, accountDAO, transactionDAO);
    }

}