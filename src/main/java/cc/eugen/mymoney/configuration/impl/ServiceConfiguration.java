package cc.eugen.mymoney.configuration.impl;

import cc.eugen.mymoney.configuration.api.Initializer;
import cc.eugen.mymoney.controller.Delegate;
import cc.eugen.mymoney.model.dao.PersistenceInitializer;
import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.dao.api.ExchangeDAO;
import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.dao.impl.AccountDAOImpl;
import cc.eugen.mymoney.model.dao.impl.ExchangeDAOImpl;
import cc.eugen.mymoney.model.dao.impl.TransactionDAOImpl;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import cc.eugen.mymoney.service.api.TransactionService;
import cc.eugen.mymoney.service.impl.AccountServiceImpl;
import cc.eugen.mymoney.service.impl.ExchangeServiceImpl;
import cc.eugen.mymoney.service.impl.TransactionHandlerImpl;
import cc.eugen.mymoney.service.impl.TransactionServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.jpa.JpaPersistModule;
import io.javalin.Javalin;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class ServiceConfiguration extends AbstractModule {

    private final Javalin javalin = Javalin.create();

    @Override
    protected void configure() {

        install(new JpaPersistModule("mymoney")); //from persistence.xml
        bind(PersistenceInitializer.class);

        bind(AccountDAO.class).to(AccountDAOImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class).in(Singleton.class);
        bind(TransactionDAO.class).to(TransactionDAOImpl.class).in(Singleton.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class).in(Singleton.class);
        bind(TransactionHandler.class).to(TransactionHandlerImpl.class).in(Singleton.class);
        bind(ExchangeService.class).to(ExchangeServiceImpl.class).in(Singleton.class);
        bind(ExchangeDAO.class).to(ExchangeDAOImpl.class).in(Singleton.class);

        Multibinder<Initializer> actionBinder = Multibinder.newSetBinder(binder(), Initializer.class);
        actionBinder.addBinding().to(Delegate.class);

        // test data for demonstration purpose
        // actionBinder.addBinding().to(TestDataInitializer.class);

        bind(Javalin.class).toInstance(javalin);
    }
}
