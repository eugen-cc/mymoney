package cc.eugen.mymoney;

import cc.eugen.mymoney.configuration.impl.ServiceConfiguration;

import static com.google.inject.Guice.createInjector;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class Main {

    public static void main(String[] args) {
        var guice = createInjector(new ServiceConfiguration());
        var app = guice.getInstance(Application.class);
        app.start();
    }
}
