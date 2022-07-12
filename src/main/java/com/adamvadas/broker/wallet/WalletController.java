package com.adamvadas.broker.wallet;

import com.adamvadas.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.Collection;
import java.util.List;

import static com.adamvadas.broker.watchlist.WatchListController.ACCOUNT_ID;

@Controller("/account/wallets")
public record WalletController(InMemoryAccountStore store) {

    public static final List<String> SUPPORTED_FIAT_CURRENCIES = List.of("EUR", "USD", "CHF", "GBP");

    @Get(produces = MediaType.APPLICATION_JSON)
    public Collection<Wallet> get() {
        return store.getWallets(ACCOUNT_ID);
    }

    @Post(
            value = "/deposit",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<Void> depositFiatMoney(@Body DepositFiatMoney deposit) {
        // option 1: custom HttpResponse
        return HttpResponse.ok();
    }

    @Post(
            value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public void withdrawFiatMoney(@Body WithDrawFiatMoney withdraw) {
        // option 2: custom error processing

    }
}