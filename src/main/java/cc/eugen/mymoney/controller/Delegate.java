package cc.eugen.mymoney.controller;

import cc.eugen.mymoney.configuration.api.Initializer;
import cc.eugen.mymoney.model.dto.AccountDTO;
import cc.eugen.mymoney.model.dto.TransactionDTO;
import cc.eugen.mymoney.model.dto.TransferDTO;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionService;
import cc.eugen.mymoney.service.exception.NotFoundException;
import cc.eugen.mymoney.service.exception.TransferFailedException;
import cc.eugen.mymoney.service.impl.converter.Converter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import static io.javalin.plugin.openapi.annotations.ContentType.JSON;
import static java.math.BigDecimal.valueOf;
import static java.util.stream.Collectors.toList;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Slf4j
@Singleton
public class Delegate implements Initializer {

    private static final String BASE_URI = "/mymoney/service";
    private static final String ACCOUNT = "/account";
    private static final String ACCOUNTS = "/accounts";
    private static final String ACCOUNT_BY_ID = "/account/:id";
    private static final String TRANSACTIONS = "/transactions";
    private static final String EXCHANGES = "/exchanges";
    private static final String TRANSFER = "/transfer";
    private static final String ID = "id";

    @Inject
    private Javalin server;

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private ExchangeService exchangeService;

    @Inject
    private Converter converter;

    @Override
    public void init() {
        server.get(BASE_URI + ACCOUNTS, ctx -> ctx.contentType(JSON).result(allAccounts()).status(200));
        server.get(BASE_URI + ACCOUNT_BY_ID, ctx -> ctx.contentType(JSON).result(getAccount(ctx)).status(200));
        server.post(BASE_URI + ACCOUNT, ctx -> ctx.contentType(JSON).result(createAccount(ctx)).status(201));
        server.get(BASE_URI + TRANSACTIONS, ctx -> ctx.contentType(JSON).result(allTransactions()).status(200));
        server.post(BASE_URI + TRANSFER, ctx -> ctx.contentType(JSON).result(transfer(ctx)).status(201));

        server.exception(TransferFailedException.class,(e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        } );

        server.exception(NotFoundException.class,(e, ctx) -> {
            ctx.status(404);
            ctx.result(e.getMessage());
        } );
    }

    private String createAccount(Context ctx) {
        var account = converter.convert(new String(ctx.bodyAsBytes()), AccountDTO.class);
        var created = accountService.createAccount(valueOf(account.getBalance()), account.getCurrency());

        var dto = AccountDTO.builder()
                .id(created.getId())
                .balance(created.getBalance().doubleValue())
                .currency(created.getCurrency())
                .build();
        return converter.convert(dto);
    }

    private String transfer(Context ctx) {
        var transfer = converter.convert(new String(ctx.bodyAsBytes()), TransferDTO.class);
        var transaction = transactionService.transfer(transfer.getSender(), transfer.getReceiver(), valueOf(transfer.getAmount()));
        var dto = TransactionDTO.builder()
                .id(transaction.getId())
                .sender(transaction.getSender().getId())
                .receiver(transaction.getReceiver().getId())
                .amount(transaction.getAmount().doubleValue())
                .currency(transaction.getCurrency())
                .completed(transaction.getCompleted())
                .build();
        return converter.convert(dto);
    }

    private String getAccount(Context ctx) {
        var id = ctx.pathParam(ID);
        var account = accountService.retrieveAccount(Long.parseLong(id));
        var dto = AccountDTO.builder()
                .id(account.getId())
                .balance(account.getBalance().doubleValue())
                .currency(account.getCurrency())
                .build();
        return converter.convert(dto);
    }

    private String allAccounts() {
        var accountList = accountService.retrieveAll()
                .stream()
                .map(acc -> AccountDTO.builder()
                        .id(acc.getId())
                        .balance(acc.getBalance().doubleValue())
                        .currency(acc.getCurrency())
                        .build())
                .collect(toList());
        return converter.convert(accountList);
    }

    private String allTransactions() {
        var transactionList = transactionService.retrieveAll()
                .stream()
                .map(t -> TransactionDTO.builder()
                        .sender(t.getSender().getId())
                        .receiver(t.getReceiver().getId())
                        .amount(t.getAmount().doubleValue())
                        .currency(t.getCurrency())
                        .completed(t.getCompleted())
                        .build())
                .collect(toList());
        return converter.convert(transactionList);
    }
}
