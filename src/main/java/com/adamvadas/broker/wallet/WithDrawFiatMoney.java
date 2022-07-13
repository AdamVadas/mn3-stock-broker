package com.adamvadas.broker.wallet;

import com.adamvadas.broker.Symbol;

import java.math.BigDecimal;
import java.util.UUID;

public record WithDrawFiatMoney(
        UUID accountId,
        UUID walletId,
        Symbol symbol,
        BigDecimal amount
) {
}
