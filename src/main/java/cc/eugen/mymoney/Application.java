package cc.eugen.mymoney;

import cc.eugen.mymoney.configuration.api.Initializer;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;

import java.util.Set;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/

@Singleton
class Application {

    private static final int PORT = 8080;

    @Inject
    private Javalin server;

    @Inject
    private Set<Initializer> initializers;


    /**
     * initializes application and starts server
     */
    void start() {
        initializers.forEach(Initializer::init);
        server.start(PORT);
    }
}
