package com.coinbase.core.service;

import com.coinbase.core.common.HttpMethod;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Set;

public interface CoinbaseService {
    <T> T request(
            HttpMethod httpMethod,
            String path,
            Object options,
            List<Integer> expectedStatusCodes,
            TypeReference<T> responseClass);
}
