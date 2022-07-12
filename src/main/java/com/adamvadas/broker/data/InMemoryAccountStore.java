package com.adamvadas.broker.data;

import com.adamvadas.broker.wallet.Wallet;
import com.adamvadas.broker.watchlist.WatchList;
import jakarta.inject.Singleton;

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
}
