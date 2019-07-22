package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import cc.eugen.mymoney.service.api.TransactionService;
import cc.eugen.mymoney.service.exception.TransferFailedException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Singleton
public class TransactionHandlerImpl implements TransactionHandler {

    /**
     * contains the lock objects per account
     */
    private static final Map<Long, Object> accountLockMap = new ConcurrentHashMap<>();

    @Inject
    private ExchangeService exchangeService;

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionService transactionService;

    private Object getLock(Long id) {
        return accountLockMap.computeIfAbsent(id, k -> new Object());
    }

    @Override
    public Transaction handleTransaction(Transaction transaction) {

        var sender = transaction.getSender();
        var receiver = transaction.getReceiver();
        var amount = transaction.getAmount();

        verifySenderIsNotReceiver(sender, receiver);

        // Ignoring exchange fees here, as it makes the scenario too complex for this demo !!
        var exchange = exchangeService.exchange(amount, sender.getCurrency(), receiver.getCurrency(), false);

        Long lowerAccountId = Math.min(sender.getId(), receiver.getId());
        Long higherAccountId = Math.max(sender.getId(), receiver.getId());

        // using lowerid first prevents deadlock
        synchronized (getLock(lowerAccountId)) {
            synchronized (getLock(higherAccountId)) {
                sender = accountService.retrieveAccount(sender.getId());
                receiver = accountService.retrieveAccount(receiver.getId());

                sender.setBalance(sender.getBalance().subtract(amount));
                receiver.setBalance(receiver.getBalance().add(exchange.getValue()));
                transaction.setCompleted(true);

                accountService.updateAccount(sender);
                accountService.updateAccount(receiver);
                exchangeService.updateExchange(exchange);

                return transactionService.update(transaction);
            }
        }
    }

    private void verifySenderIsNotReceiver(Account sender, Account receiver) {
        if (sender.getId() == null || receiver.getId() == null || sender.getId().equals(receiver.getId())) {
            throw new TransferFailedException("Sender and reciever must not be equal!");
        }
    }
}
