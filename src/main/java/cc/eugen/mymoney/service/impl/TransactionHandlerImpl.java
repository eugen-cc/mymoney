package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.AccountDAO;
import cc.eugen.mymoney.model.dao.api.ExchangeDAO;
import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Singleton
public class TransactionHandlerImpl implements TransactionHandler {

    @Inject
    private ExchangeService exchangeService;

    @Inject
    private TransactionDAO transactionDAO;

    @Inject
    private AccountDAO accountDAO;

    @Inject
    private ExchangeDAO exchangeDAO;


    @Override
    public Transaction handleTransaction(Transaction t) {

        var sender = t.getSender();
        var receiver = t.getReceiver();
        var amount = t.getAmount();

        // Ignoring exchange fees here, as it makes the scenario too complex for this demo !!
        var exchange = exchangeService.exchange(amount, sender.getCurrency(), receiver.getCurrency(), false);

        synchronized (this) {
            sender.setBalance(sender.getBalance().subtract(amount));
            receiver.setBalance(receiver.getBalance().add(exchange.getValue()));
            t.setCompleted(true);

            accountDAO.save(sender);
            accountDAO.save(receiver);
            exchangeDAO.save(exchange);
            return transactionDAO.save(t);
        }
    }
}
