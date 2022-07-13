package com.adamvadas.broker.data;

import com.adamvadas.broker.wallet.DepositFiatMoney;
import com.adamvadas.broker.wallet.Wallet;
import com.adamvadas.broker.wallet.WithDrawFiatMoney;
import com.adamvadas.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.*;

@Singleton
public class InMemoryAccountStore {

    public static final UUID ACCOUNT_ID = UUID.fromString("f4245629-83df-4ed8-90d9=7401045b5921");
    private final Map<UUID, WatchList> watchListsPerAccount = new HashMap<>();
    private final Map<UUID, Map<UUID, Wallet>> walletsPerAccount = new HashMap<>();

    public WatchList getWatchList(final UUID accountId) {
        return watchListsPerAccount.getOrDefault(accountId, new WatchList());
    }

    public WatchList updateWatchList(final UUID accountId, final WatchList watchList) {
        watchListsPerAccount.put(accountId, watchList);
        return getWatchList(accountId);
    }

    public void deleteWatchList(final UUID accountId) {
        watchListsPerAccount.remove(accountId);
    }

    public Collection<Wallet> getWallets(UUID accountId) {
        return Optional.ofNullable(walletsPerAccount.get(accountId))
                .orElse(new HashMap<>())
                .values();
    }

    public Wallet depositToWallet(DepositFiatMoney deposit)
    {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(deposit.accountId())
        ).orElse(
                new HashMap<>()
        );

        var oldWallet = Optional.ofNullable(
                wallets.get(deposit.walletId()))
                .orElse(
                        new Wallet(ACCOUNT_ID, deposit.walletId(), deposit.symbol(), BigDecimal.ZERO, BigDecimal.ZERO)
                );

        var newWallet = oldWallet.addAvailable(deposit.amount());

        // Update wallet in store
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);

        return newWallet;
    }

    public Wallet withdrawFromWallet(WithDrawFiatMoney withdraw)
    {
        final var wallets = Optional.ofNullable(
                walletsPerAccount.get(withdraw.accountId())
        ).orElse(
                new HashMap<>()
        );

        var oldWallet = wallets.get(withdraw.accountId());
        var newWallet = oldWallet.withdraw(withdraw.amount());

        // Update wallet in store
        wallets.put(newWallet.walletId(), newWallet);
        walletsPerAccount.put(newWallet.accountId(), wallets);

        return newWallet;
    }
}
