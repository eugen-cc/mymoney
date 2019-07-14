package cc.eugen.mymoney;

import cc.eugen.mymoney.configuration.impl.ServiceConfiguration;
import cc.eugen.mymoney.model.dao.PersistenceInitializer;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionService;
import com.google.inject.Injector;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import static com.google.inject.Guice.createInjector;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Getter
public abstract class ApplicationTest extends Assert {

    private static Javalin server;
    private static Injector guice;

    protected AccountService accountService;
    protected TransactionService transactionService;
    protected ExchangeService exchangeService;

    @BeforeClass
    public static void init() {
        guice = createInjector(new ServiceConfiguration());
        var initializer = guice.getInstance(PersistenceInitializer.class);
        var app = guice.getInstance(Application.class);
        server = guice.getInstance(Javalin.class);
        app.start();
    }

    @Before
    public void setUp() {
        accountService = guice.getInstance(AccountService.class);
        transactionService = guice.getInstance(TransactionService.class);
        exchangeService = guice.getInstance(ExchangeService.class);
    }

    @After
    public void tearDown() {
        server.stop();
    }
}