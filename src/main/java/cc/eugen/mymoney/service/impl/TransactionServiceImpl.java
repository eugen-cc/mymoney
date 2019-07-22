package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.model.dao.api.TransactionDAO;
import cc.eugen.mymoney.model.entity.Account;
import cc.eugen.mymoney.model.entity.Transaction;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.TransactionHandler;
import cc.eugen.mymoney.service.api.TransactionService;
import cc.eugen.mymoney.service.exception.NotFoundException;
import cc.eugen.mymoney.service.exception.TransferFailedException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Singleton
public class TransactionServiceImpl implements TransactionService {

    @Inject
    private TransactionDAO transactionDAO;

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionHandler handler;


    @Override
    public Transaction retrieveTransaction(Long transactionId) {
        log.debug("retrieve transaction " + transactionId);
        return transactionDAO.findById(transactionId).orElseThrow(() -> new NotFoundException("There is not transaction with id:" + transactionId));
    }

    @Override
    @Transactional
    public Transaction transfer(Long senderId, Long receiverId, BigDecimal amount) {
        log.debug("Sender: {} , Receiver: {} , Amount: {}", senderId, receiverId, amount.doubleValue());
        var sender = accountService.retrieveAccount(senderId);
        var receiver = accountService.retrieveAccount(receiverId);
        validate(sender,receiver, amount);
        var transaction = newTransaction(sender, receiver, amount);
        return handler.handleTransaction(transaction);
    }

    private Transaction newTransaction(Account sender, Account receiver, BigDecimal amount) {
        var t = new Transaction();
        t.setSender(sender);
        t.setReceiver(receiver);
        t.setAmount(amount);
        t.setCurrency(sender.getCurrency());
        return t;
    }

    private void validate(Account sender, Account receiver, BigDecimal amount) {
        if(sender.getId().equals(receiver.getId())){
            throw new TransferFailedException("Sender must not be the receiver!");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 1) {
            throw new TransferFailedException("Illegal amount " + amount.doubleValue());
        }
        if (sender.getBalance().subtract(amount).compareTo(sender.getOverdraft()) < 0) {
            throw new TransferFailedException("Not enough credit!");
        }
    }

    @Override
    public List<Transaction> retrieveAll() {
        log.debug("get all transactions");
        return transactionDAO.findAll().orElse(Collections.emptyList());
    }

    @Override
    @Transactional
    public Transaction update(Transaction transaction) {
        return transactionDAO.save(transaction);
    }
}
