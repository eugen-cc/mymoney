package cc.eugen.mymoney.service.impl;

import cc.eugen.mymoney.ApplicationTestBase;
import cc.eugen.mymoney.model.dto.AccountDTO;
import cc.eugen.mymoney.model.dto.TransactionDTO;
import cc.eugen.mymoney.model.dto.TransferDTO;
import cc.eugen.mymoney.model.entity.Currency;
import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author Eugen Gross
 * @since 07/23/2019
 **/
@RunWith(ConcurrentTestRunner.class)
public class ConcurrentTest extends ApplicationTestBase {

    private static final String BASE_URI = "http://localhost:8080/mymoney/service";
    private static final String ACCOUNT = "/account";
    private static final String TRANSFER = "/transfer";

    private static final int THREAD_COUNT = 10;
    private static final int INITIAL = 1000;

    private <T> T convert(String s, Class<T> tClass) {
        return new Gson().fromJson(s, tClass);
    }

    private AccountDTO acc1;
    private AccountDTO acc2;

    @Before
    public void setUp() throws IOException, InterruptedException {
        AccountDTO account1 = AccountDTO.builder().balance(1000).currency(Currency.EUR).build();
        AccountDTO account2 = AccountDTO.builder().balance(1000).currency(Currency.EUR).build();

        HttpResponse<String> create1 = createAccount(account1);
        HttpResponse<String> create2 = createAccount(account2);

        assertEquals(201, create1.statusCode());
        assertEquals(201, create2.statusCode());

        acc1 = convert(create1.body(), AccountDTO.class);
        acc2 = convert(create2.body(), AccountDTO.class);
    }

    @Test
    @ThreadCount(ConcurrentTest.THREAD_COUNT)
    public void transfer() throws IOException, InterruptedException {

        double oneToTwo = 100;
        var transfer1 = TransferDTO.builder().amount(oneToTwo).sender(acc1.getId()).receiver(acc2.getId()).build();
        HttpResponse<String> transaction1 = performTransfer(transfer1);
        var transactionDTO1 = convert(transaction1.body(), TransactionDTO.class);
        assertEquals(transfer1.getAmount(), transactionDTO1.getAmount(), 0.0001);
    }

    @Test
    @ThreadCount(ConcurrentTest.THREAD_COUNT)
    public void transferBack() throws IOException, InterruptedException {
        double twoToOne = 50;
        var transfer2 = TransferDTO.builder().amount(twoToOne).sender(acc2.getId()).receiver(acc1.getId()).build();
        HttpResponse<String> transaction2 = performTransfer(transfer2);
        var transactionDTO2 = convert(transaction2.body(), TransactionDTO.class);
        assertEquals(transfer2.getAmount(), transactionDTO2.getAmount(), 0.0001);
    }

    @After
    public void check() throws IOException, InterruptedException {
        var reload1 = findAccount(acc1.getId());
        var reload2 = findAccount(acc2.getId());

        double balance1 = convert(reload1.body(), AccountDTO.class).getBalance();
        double balance2 = convert(reload2.body(), AccountDTO.class).getBalance();

        assertEquals(INITIAL - (THREAD_COUNT * 100) + (THREAD_COUNT * 50), balance1, 0.0001);
        assertEquals(INITIAL - (THREAD_COUNT * 50) + (THREAD_COUNT * 100), balance2, 0.0001);

        System.out.println("balance1 = " + balance1);
        System.out.println("balance2 = " + balance2);
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

