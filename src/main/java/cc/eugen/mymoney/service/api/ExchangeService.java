package cc.eugen.mymoney.service.api;

import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.model.entity.Exchange;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public interface ExchangeService {

     /**
      * @param amount amount has to be exchanged
      * @param from the original currency
      * @param to the foreign currency
      * @param withFee flag indicates if fees apply
      * @return a created exchanged object
      */
     Exchange exchange(BigDecimal amount, Currency from, Currency to, boolean withFee);

     /**
      * @return a list of all exchanges
      */
     List<Exchange> retrieveAll();
}
