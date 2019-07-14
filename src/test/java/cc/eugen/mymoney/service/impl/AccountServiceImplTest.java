package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Currency;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl underTest;

    @Mock
    private AccountDAO accountDAO;

    @Before
    public void setup() {
        when(accountDAO.findById(anyLong())).thenReturn(Optional.of(new Account()));
    }

    @Test
    public void testRetrieveAccount() {
        var id = 5L;
        underTest.retrieveAccount(id);
        verify(accountDAO).findById(id);
    }

    @Test
    public void testCreateAccount() {
        underTest.createAccount(BigDecimal.TEN, Currency.RUB);
        verify(accountDAO).save(any(Account.class));
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(accountDAO);
    }
}