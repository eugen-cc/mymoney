package cc.eugen.mymoney;

import cc.eugen.mymoney.configuration.impl.ServiceConfiguration;
import cc.eugen.mymoney.controller.Delegate;
import cc.eugen.mymoney.model.dao.PersistenceInitializer;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionService;
import com.google.inject.Injector;
import io.javalin.Javalin;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.google.inject.Guice.createInjector;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Getter
public abstract class ApplicationTestBase extends Assert {

    private static Javalin server;
    private static Injector guice;
    private static Application app;

    protected AccountService accountService;
    protected TransactionService transactionService;
    protected ExchangeService exchangeService;
    protected Delegate delegate;

    @BeforeClass
    public static void init() throws InterruptedException {
        guice = createInjector(new ServiceConfiguration());
        server = guice.getInstance(Javalin.class);
        var initializer = guice.getInstance(PersistenceInitializer.class);
        app = guice.getInstance(Application.class);
        app.start();
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        accountService = guice.getInstance(AccountService.class);
        transactionService = guice.getInstance(TransactionService.class);
        exchangeService = guice.getInstance(ExchangeService.class);
        delegate = guice.getInstance(Delegate.class);
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }
}