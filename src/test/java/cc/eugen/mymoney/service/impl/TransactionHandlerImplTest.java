package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.model.entity.Exchange;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private ExchangeService exchangeServiceMock;

    @Mock
    private TransactionService transactionServiceMock;

    @Mock
    private AccountService accountServiceMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    private Exchange newExchange(double value) {
        var ex = new Exchange();
        ex.setValue(valueOf(value));
        return ex;
    }

    @Before
    public void init(){
        when(accountServiceMock.retrieveAccount(anyLong())).thenAnswer(in -> newAccount(in.getArgument(0)));
    }

    @Test
    public void testTransactonHandling() {
        var t = new Transaction();

        when(exchangeServiceMock.exchange(any(), any(), any(), anyBoolean())).thenReturn(newExchange(1234));
        when(transactionServiceMock.update(any(Transaction.class))).thenReturn(t);

        t.setSender(newAccount(1L));
        t.setReceiver(newAccount(2L));
        t.setAmount(BigDecimal.TEN);

        var result = underTest.handleTransaction(t);

        assertTrue(result.getCompleted());
        verify(exchangeServiceMock).exchange(t.getAmount(), t.getSender().getCurrency(), t.getReceiver().getCurrency(), false);
        verify(accountServiceMock, times(2)).retrieveAccount(anyLong());
        verify(accountServiceMock, times(2)).updateAccount(any(Account.class));
        verify(exchangeServiceMock).updateExchange(any(Exchange.class));
        verify(transactionServiceMock).update(any(Transaction.class));
    }

    private Account newAccount(Long id) {
        Random r = new Random();
        var a = new Account();
        a.setId(id);
        a.setBalance(valueOf(r.nextDouble()*1000));
        Currency[] currencies = Currency.values();
        a.setCurrency(currencies[r.nextInt(currencies.length)]);
        a.setOverdraft(valueOf(-5_000));
        return a;
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(exchangeServiceMock, accountServiceMock, transactionServiceMock);
    }

}