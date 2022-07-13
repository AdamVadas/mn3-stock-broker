package com.adamvadas.broker.wallet;

import com.adamvadas.broker.Symbol;
import com.adamvadas.broker.api.RestApiResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record Wallet(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal available,
        BigDecimal locked
) implements RestApiResponse {

    public Wallet addAvailable(BigDecimal amountToAdd) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToAdd),
                this.locked
        );
    }

    public Wallet withdraw(BigDecimal amountToWithDraw) {
        return new Wallet(
                this.accountId,
                this.walletId,
                this.symbol,
                this.available.add(amountToWithDraw),
                this.locked
        );
    }
}
