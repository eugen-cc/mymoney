package cc.eugen.mymoney.configuration.impl;

import cc.eugen.mymoney.model.dao.PersistenceInitializer;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class ServletConfiguration extends ServletModule {

    @Override
    protected void configureServlets() {
        install(new JpaPersistModule("mymoney")); //from persistence.xml
        bind(PersistenceInitializer.class);
        filter("/*").through(PersistFilter.class);
    }
}
