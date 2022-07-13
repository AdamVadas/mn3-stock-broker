package com.adamvadas.broker.wallet.error;

import com.adamvadas.broker.api.RestApiResponse;

public record CustomError(
        int status,
        String error,
        String message
) implements RestApiResponse {
}
