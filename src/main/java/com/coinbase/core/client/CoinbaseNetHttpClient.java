/*
 * Copyright 2024-present Coinbase Global, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coinbase.core.client;

import com.coinbase.core.common.HttpMethod;
import com.coinbase.core.credentials.CoinbaseCredentials;
import com.coinbase.core.errors.CoinbaseClientException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CoinbaseNetHttpClient implements CoinbaseClient {
    protected final ObjectMapper mapper;
    private final HttpClient client;
    private final CoinbaseCredentials credentials;
    private final String baseUrl;

    public CoinbaseNetHttpClient(CoinbaseCredentials credentials, String baseUrl) {
        this.credentials = credentials;
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
        this.mapper = configureObjectMapper();
    }

    public CoinbaseNetHttpClient(CoinbaseCredentials credentials, String baseUrl, HttpClient client) {
        this.credentials = credentials;
        this.baseUrl = baseUrl;
        this.client = client;
        this.mapper = configureObjectMapper();
    }

    private ObjectMapper configureObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public CoinbaseCredentials getCredentials() {
        return credentials;
    }

    @Override
    public <T> T sendRequest(
            HttpMethod httpMethod,
            String path,
            List<Integer> expectedStatusCodes,
            Object options,
            TypeReference<T> responseClass) throws CoinbaseClientException {
        String callUrl;
        HttpRequest.BodyPublisher bodyPublisher;
        Map<String, String> authHeaders;
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        if (httpMethod == HttpMethod.GET || httpMethod == HttpMethod.DELETE) {
            String queryParams = toQueryParams(options);
            bodyPublisher = HttpRequest.BodyPublishers.noBody();
            callUrl = String.format("%s%s%s", baseUrl, path, queryParams);
            authHeaders = credentials.generateAuthHeaders(httpMethod.name(), URI.create(callUrl), "");
        } else {
            String jsonPayload = toJsonPayload(options);
            bodyPublisher = HttpRequest.BodyPublishers.ofString(jsonPayload);
            callUrl = String.format("%s%s%s", baseUrl, path, "");
            authHeaders = credentials.generateAuthHeaders(httpMethod.name(), URI.create(callUrl), jsonPayload);
            requestBuilder.header("Content-Type", "application/json");
        }

        URI uri = URI.create(callUrl);

        requestBuilder = requestBuilder
                .uri(uri)
                .method(httpMethod.name(), bodyPublisher);

        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue());
        }

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

            return this.handleResponse(response, expectedStatusCodes, responseClass);
        } catch (Throwable e) {
            throw new CoinbaseClientException("Failed to send http request", e);
        }
    }

    protected abstract <T> T handleResponse(
            HttpResponse<String> response,
            List<Integer> expectedStatusCodes,
            TypeReference<T> responseClass);

    private String toQueryParams(Object object) throws CoinbaseClientException {
        if (object == null) return "";

        try {
            String jsonString = mapper.writeValueAsString(object);
            JsonNode jsonNode = mapper.readTree(jsonString);

            List<String> queryParameters = new ArrayList<>();
            jsonNode.fields().forEachRemaining(entry -> {
                String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
                JsonNode valueNode = entry.getValue();
                if (valueNode.isArray()) {
                    valueNode.forEach(arrayElement -> {
                        String value = URLEncoder.encode(arrayElement.asText(), StandardCharsets.UTF_8);
                        queryParameters.add(String.format("%s=%s", key, value));
                    });
                } else {
                    String value = URLEncoder.encode(valueNode.asText(), StandardCharsets.UTF_8);
                    queryParameters.add(String.format("%s=%s", key, value));
                }
            });

            return String.join("&", queryParameters);
        } catch (Throwable e) {
            throw new CoinbaseClientException("Failed to convert object to query parameters", e);
        }
    }

    private String toJsonPayload(Object object) throws CoinbaseClientException {
        try {
            return mapper.writeValueAsString(object);
        } catch (Throwable e) {
            throw new CoinbaseClientException("Failed to convert object to json payload", e);
        }
    }
}

