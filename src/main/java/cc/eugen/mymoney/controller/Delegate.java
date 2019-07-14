package cc.eugen.mymoney.controller;

import cc.eugen.mymoney.configuration.api.Initializer;
import cc.eugen.mymoney.model.GenericResponse;
import cc.eugen.mymoney.model.dto.Transfer;
import cc.eugen.mymoney.model.entity.Exchange;
import cc.eugen.mymoney.service.api.AccountService;
import cc.eugen.mymoney.service.api.ExchangeService;
import cc.eugen.mymoney.service.api.TransactionService;
import cc.eugen.mymoney.service.exception.TransferFailedException;
import cc.eugen.mymoney.service.impl.converter.Converter;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.javalin.Javalin;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static io.javalin.plugin.openapi.annotations.ContentType.JSON;
import static java.math.BigDecimal.valueOf;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
@Singleton
@Slf4j
public class Delegate implements Initializer {

    private static final String BASE_URI = "/mymoney/service";
    private static final String ACCOUNTS = "/accounts";
    private static final String ACCOUNT_BY_ID = "/account/:id";
    private static final String TRANSACTIONS = "/transactions";
    private static final String EXCHANGES = "/exchanges";
    private static final String TRANSFER = "/transfer";

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

        server.get(BASE_URI + ACCOUNTS, ctx -> ctx.contentType(JSON).result(allAcounts()).status());
        server.get(BASE_URI + ACCOUNT_BY_ID, ctx -> ctx.contentType(JSON).result(account(ctx)));
        server.get(BASE_URI + TRANSACTIONS, ctx -> ctx.contentType(JSON).result(allTransactions()).status(200));
        server.get(BASE_URI + EXCHANGES, ctx -> ctx.contentType(JSON).result(allExchanges()));
        server.post(BASE_URI + TRANSFER, ctx -> ctx.contentType(JSON).result(transfer(ctx)).status(201));

        server.exception(TransferFailedException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(e.getMessage());
        });

        server.config.showJavalinBanner = false;
    }

    private String allExchanges() {
        var response = GenericResponse.<List<Exchange>>builder().object(exchangeService.retrieveAll()).build();
        return converter.convert(response);
    }

    private String transfer(Context ctx) {
        var transfer = converter.convert(new String(ctx.bodyAsBytes()), Transfer.class);
        var transaction = transactionService.transfer(transfer.getSender(), transfer.getReceiver(), valueOf(transfer.getAmount()));
        return converter.convert(transaction);
    }

    private String account(Context ctx) {
        var id = ctx.pathParam("id");
        var account = accountService.retrieveAccount(Long.parseLong(id));
        return converter.convert(account);
    }

    private String allAcounts() {
        return converter.convert(accountService.retrieveAll());
    }

    private String allTransactions() {
        return converter.convert(transactionService.retrieveAll());
    }

}
