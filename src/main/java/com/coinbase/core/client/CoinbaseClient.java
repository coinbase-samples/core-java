package com.coinbase.core.client;

import com.coinbase.core.common.HttpMethod;
import com.coinbase.core.errors.CoinbaseClientException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Set;

public interface CoinbaseClient {
    <T> T sendRequest(
            HttpMethod httpMethod,
            String path,
            List<Integer> expectedStatusCodes,
            Object options,
            TypeReference<T> responseClass) throws CoinbaseClientException;
}
