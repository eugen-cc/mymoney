package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import cc.eugen.mymoney.service.exception.TransferFailedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl underTest;

    @Mock
    private TransactionDAO dao;

    @Mock
    private TransactionHandler handler;

    @Mock
    private AccountService accountService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        when(dao.findById(anyLong())).thenReturn(newTransaction());
        when(accountService.retrieveAccount(anyLong())).thenAnswer(in -> newAccount(in.getArgument(0)));
    }

    @Test
    public void testTransfer() {
        underTest.transfer(1L, 2L, valueOf(50.50));
        verify(accountService).retrieveAccount(1L);
        verify(accountService).retrieveAccount(2L);
        verify(handler).handleTransaction(any(Transaction.class));
    }

    @Test
    public void testNegativeAmountFails() {
        try {
            thrown.expect(TransferFailedException.class);
            thrown.expectMessage(contains("Illegal amount"));
            underTest.transfer(2L, 3L, valueOf(-50));
        } finally {
            verify(accountService, times(2)).retrieveAccount(anyLong());
        }
    }

    @Test
    public void testNotEnoughCreditFails() {
        when(accountService.retrieveAccount(10L)).thenReturn(newAccount(10, 100, -100));
        try {
            thrown.expect(TransferFailedException.class);
            thrown.expectMessage("Not enough credit!");
            underTest.transfer(10L, 3L, valueOf(200.01));
        } finally {
            verify(accountService, times(2)).retrieveAccount(anyLong());
        }
    }

    @Test
    public void testRetrieveTransaction() {
        var id = 5L;
        underTest.retrieveTransaction(id);
        verify(dao).findById(id);
    }

    @Test
    public void testRetrieveAllTransactions() {
        when(dao.findAll()).thenReturn(Optional.of(List.of(newTransaction(), newTransaction())));
        var transactions = underTest.retrieveAll();
        assertFalse(transactions.isEmpty());
        verify(dao).findAll();
    }

    @Test
    public void testRetrieveEmptyTransactions() {
        when(dao.findAll()).thenReturn(Optional.empty());
        List<Transaction> list = underTest.retrieveAll();
        assertTrue(list.isEmpty());
        verify(dao).findAll();
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(dao, accountService, handler);
    }

    private Transaction newTransaction() {
        var transaction = new Transaction();
        transaction.setSender(newAccount(1));
        transaction.setReceiver(newAccount(2));
        transaction.setCurrency(Currency.EUR);
        transaction.setAmount(valueOf(100));
        return transaction;
    }

    private Account newAccount(long id) {
        return newAccount(id, 100.0, 0);
    }

    private Account newAccount(long id, double balance, double limit) {
        var acc = new Account();
        acc.setId(id);
        acc.setBalance(valueOf(balance));
        acc.setCurrency(Currency.EUR);
        acc.setOverdraft(valueOf(limit));
        return acc;
    }

}