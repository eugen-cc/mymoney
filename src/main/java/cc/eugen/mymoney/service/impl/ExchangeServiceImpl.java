package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.ExchangeDAO;
import cc.eugen.mymoney.model.entity.Currency;
import cc.eugen.mymoney.model.entity.Exchange;
import cc.eugen.mymoney.service.api.ExchangeService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cc.eugen.mymoney.model.entity.Currency.CHF;
import static cc.eugen.mymoney.model.entity.Currency.EUR;
import static cc.eugen.mymoney.model.entity.Currency.GBP;
import static cc.eugen.mymoney.model.entity.Currency.JPY;
import static cc.eugen.mymoney.model.entity.Currency.RUB;
import static cc.eugen.mymoney.model.entity.Currency.USD;
import static java.math.BigDecimal.valueOf;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Singleton
public class ExchangeServiceImpl implements ExchangeService {

    @Inject
    private ExchangeDAO exchangeDAO;


    private static final Map<Currency, BigDecimal> baseMap =
            Map.of(
                    USD, valueOf(1.0000),
                    EUR, valueOf(1.1301),
                    JPY, valueOf(0.0093),
                    CHF, valueOf(1.0200),
                    GBP, valueOf(1.2600),
                    RUB, valueOf(0.0158)
            );

    private BigDecimal feePercent = BigDecimal.valueOf(0.05);

    @Override
    public Exchange exchange(BigDecimal amount, Currency from, Currency to, boolean withFee) {

        log.debug("exchange {} {} to {} {}", amount.doubleValue(), from, to, withFee ? "with fee" : "");

        var exchange = new Exchange();
        var rate = baseMap.get(from).divide(baseMap.get(to), RoundingMode.HALF_DOWN);
        var value = amount.multiply(rate, MathContext.DECIMAL32);
        var fee = getFee(from, to, withFee, value);

        exchange.setAmount(amount);
        exchange.setExternal(from);
        exchange.setInternal(to);
        exchange.setRate(rate);
        exchange.setFee(fee);
        exchange.setValue(value.subtract(fee));

        log.debug("{}", exchange);

        return exchange;
    }

    @Override
    public List<Exchange> retrieveAll() {
        return exchangeDAO.findAll().orElse(Collections.emptyList());
    }

    @Override
    public Exchange updateExchange(Exchange exchange) {
        return exchangeDAO.save(exchange);
    }

    private BigDecimal getFee(Currency from, Currency to, boolean withFee, BigDecimal value) {
        return !withFee || from == to ? BigDecimal.ZERO : value.multiply(feePercent, MathContext.DECIMAL32);
    }

}
