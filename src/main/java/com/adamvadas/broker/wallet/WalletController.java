package com.adamvadas.broker.wallet;

import com.adamvadas.broker.api.RestApiResponse;
import com.adamvadas.broker.data.InMemoryAccountStore;
import com.adamvadas.broker.wallet.error.CustomError;
import com.adamvadas.broker.wallet.error.FiatCurrencyNotSupportedException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.Collection;
import java.util.List;

import static com.adamvadas.broker.watchlist.WatchListController.ACCOUNT_ID;
import static io.micronaut.http.server.netty.types.stream.NettyStreamedCustomizableResponseType.LOG;

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
    public HttpResponse<RestApiResponse> depositFiatMoney(@Body DepositFiatMoney deposit) {
        // option 1: custom HttpResponse
        if (!SUPPORTED_FIAT_CURRENCIES.contains(deposit.symbol().value())) {
            return HttpResponse.badRequest()
                    .body(new CustomError(
                            HttpStatus.BAD_REQUEST.getCode(),
                            "UNSUPPORTED_FIAT_CURRENCY",
                            String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES)
                    ));
        }

        var wallet = store.depositToWallet(deposit);
        LOG.debug("Deposit to wallet: {}", wallet);
        return HttpResponse.ok().body(wallet);
    }

    @Post(
            value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Wallet withdrawFiatMoney(@Body WithDrawFiatMoney withdraw) {
        // option 2: custom error processing
        if (!SUPPORTED_FIAT_CURRENCIES.contains(withdraw.symbol().value())) {
            throw new FiatCurrencyNotSupportedException(String.format("Only %s are supported", SUPPORTED_FIAT_CURRENCIES));
        }

        var wallet = store.withdrawFromWallet(withdraw);
        LOG.debug("Deposit to wallet: {}", wallet);
        return wallet;
    }
}
