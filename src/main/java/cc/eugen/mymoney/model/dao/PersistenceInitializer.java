package cc.eugen.mymoney.model.dao;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/

@Singleton
@Slf4j
public class PersistenceInitializer {

    @Inject
    PersistenceInitializer(PersistService service) {
        service.start();
        log.info("JPA is started and ready.");
    }

}
