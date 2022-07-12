package com.adamvadas.broker.watchlist;

import com.adamvadas.broker.data.InMemoryAccountStore;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.util.UUID;

@Controller("/account/watchlist")
public record WatchListController(InMemoryAccountStore store) {

    public static final UUID ACCOUNT_ID = UUID.randomUUID();

    @Get(produces = MediaType.APPLICATION_JSON)
    public WatchList get() {
        return store.getWatchList(ACCOUNT_ID);
    }

    @Put(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public WatchList update(@Body WatchList watchList) {
        return store.updateWatchList(ACCOUNT_ID, watchList);
    }

    @Delete(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Void> deleteWatchList() {
        store.deleteWatchList(ACCOUNT_ID);
        return HttpResponse.noContent();
    }
}
