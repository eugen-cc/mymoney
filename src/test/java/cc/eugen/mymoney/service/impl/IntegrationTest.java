package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.ApplicationTestBase;
import cc.eugen.mymoney.model.dto.AccountDTO;
import cc.eugen.mymoney.model.dto.TransactionDTO;
import cc.eugen.mymoney.model.dto.TransferDTO;
import cc.eugen.mymoney.model.entity.Currency;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Eugen Gross
 * @since 07/14/2019
 **/
public class IntegrationTest extends ApplicationTestBase {


    private static final String BASE_URI = "http://localhost:8080/mymoney/service";
    private static final String ACCOUNT = "/account";
    private static final String TRANSACTIONS = "/transactions";
    private static final String TRANSFER = "/transfer";

    private <T> T convert(String s, Class<T> tClass) {
        return new Gson().fromJson(s, tClass);
    }

    @Test
    public void createAccountsAndTransferMoney() throws IOException, InterruptedException {
        AccountDTO account1 = AccountDTO.builder().balance(100).currency(Currency.EUR).build();
        AccountDTO account2 = AccountDTO.builder().balance(100).currency(Currency.EUR).build();

        HttpResponse<String> create1 = createAccount(account1);
        HttpResponse<String> create2 = createAccount(account2);

        assertEquals(201, create1.statusCode());
        assertEquals(201, create2.statusCode());

        AccountDTO dto1 = convert(create1.body(), AccountDTO.class);
        AccountDTO dto2 = convert(create2.body(), AccountDTO.class);

        double amount = 50.50;
        var transfer = TransferDTO.builder().amount(amount).sender(dto1.getId()).receiver(dto2.getId()).build();

        HttpResponse<String> transaction = performTransfer(transfer);
        TransactionDTO transactionDTO = convert(transaction.body(), TransactionDTO.class);

        assertEquals(transfer.getAmount(), transactionDTO.getAmount(), 0.0001);

        var reload1 = findAccount(dto1.getId());
        var reload2 = findAccount(dto2.getId());

        double balance1 = convert(reload1.body(), AccountDTO.class).getBalance();
        double balance2 = convert(reload2.body(), AccountDTO.class).getBalance();

        assertEquals(balance1, account1.getBalance() - amount, 0.0001);
        assertEquals(balance2, account2.getBalance() + amount, 0.0001);
    }

    @Test
    public void failTransferMoneyToOneself() throws IOException, InterruptedException {
        AccountDTO account1 = AccountDTO.builder().balance(100).currency(Currency.EUR).build();

        HttpResponse<String> create1 = createAccount(account1);

        assertEquals(201, create1.statusCode());

        AccountDTO dto = convert(create1.body(), AccountDTO.class);

        double amount = 50.50;
        var transfer = TransferDTO.builder().amount(amount).sender(dto.getId()).receiver(dto.getId()).build();

        HttpResponse<String> transaction = performTransfer(transfer);
        assertEquals(400, transaction.statusCode());
        assertEquals("Sender must not be the receiver!", transaction.body());
    }


    @Test
    public void retrieveNonExistingAccountShouldFail() throws IOException, InterruptedException {
        int id = 42;
        HttpResponse<String> response = findAccount(id);
        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains(id+""));
    }

    @Test
    public void transferOfIllegalAmountShouldFail() throws IOException, InterruptedException {

        AccountDTO account1 = AccountDTO.builder().balance(1000).currency(Currency.EUR).build();
        AccountDTO account2 = AccountDTO.builder().balance(400).currency(Currency.USD).build();

        HttpResponse<String> create1 = createAccount(account1);
        HttpResponse<String> create2 = createAccount(account2);

        assertEquals(201, create1.statusCode());
        assertEquals(201, create2.statusCode());

        AccountDTO dto1 = convert(create1.body(), AccountDTO.class);
        AccountDTO dto2 = convert(create2.body(), AccountDTO.class);

        final double amount = -50.00;
        var transfer = TransferDTO.builder().amount(amount).sender(dto1.getId()).receiver(dto2.getId()).build();

        HttpResponse<String> transaction = performTransfer(transfer);

        assertEquals(400, transaction.statusCode());
        assertTrue(transaction.body().contains("Illegal amount"));

    }

    @Test
    public void transferTooMuchShouldFail() throws IOException, InterruptedException {

        final double balance = 20.00;
        AccountDTO account1 = AccountDTO.builder().balance(balance).currency(Currency.EUR).build();
        AccountDTO account2 = AccountDTO.builder().balance(10).currency(Currency.USD).build();

        HttpResponse<String> create1 = createAccount(account1);
        HttpResponse<String> create2 = createAccount(account2);

        assertEquals(201, create1.statusCode());
        assertEquals(201, create2.statusCode());

        AccountDTO dto1 = convert(create1.body(), AccountDTO.class);
        AccountDTO dto2 = convert(create2.body(), AccountDTO.class);

        final double amount = balance + 0.01;
        var transfer = TransferDTO.builder().amount(amount).sender(dto1.getId()).receiver(dto2.getId()).build();

        HttpResponse<String> transaction = performTransfer(transfer);

        assertEquals(400, transaction.statusCode());
        assertTrue(transaction.body().contains("Not enough credit"));

    }

    private HttpResponse<String> findAccount(long id) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder().build();
        var request = HttpRequest.newBuilder().GET().uri(URI.create(BASE_URI + ACCOUNT + "/" + id)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> performTransfer(TransferDTO transfer) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder().build();
        var request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(transfer))).uri(URI.create(BASE_URI + TRANSFER)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> createAccount(AccountDTO account) throws IOException, InterruptedException {
        var client = HttpClient.newBuilder().build();
        var request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(account))).uri(URI.create(BASE_URI + ACCOUNT)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}