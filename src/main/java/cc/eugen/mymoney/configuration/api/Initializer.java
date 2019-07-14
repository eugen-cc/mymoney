package cc.eugen.mymoney.configuration.api;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface Initializer {

    /**
     *  everything that needs to be done on startup
     */
    void init();
}
