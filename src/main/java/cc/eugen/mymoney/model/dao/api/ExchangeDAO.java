package cc.eugen.mymoney.model.dao.api;

import cc.eugen.mymoney.model.entity.Exchange;

import java.util.List;
import java.util.Optional;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface ExchangeDAO {

    Exchange save(Exchange e);

    Optional<List<Exchange>> findAll();
}
