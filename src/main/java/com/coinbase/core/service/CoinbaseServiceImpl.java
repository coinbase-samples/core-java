package com.coinbase.core.service;

import com.coinbase.core.client.CoinbaseClient;
import com.coinbase.core.common.HttpMethod;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Set;

public abstract class CoinbaseServiceImpl implements CoinbaseService {
    private final CoinbaseClient client;

    public CoinbaseServiceImpl(CoinbaseClient client) {
        this.client = client;
    }

    @Override
    public <T> T request(
            HttpMethod httpMethod,
            String path,
            Object options,
            List<Integer> expectedStatusCodes,
            TypeReference<T> responseClass) {
        return this.client.sendRequest(httpMethod, path, expectedStatusCodes, options, responseClass);
    }
}
