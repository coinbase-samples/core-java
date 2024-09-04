package com.coinbase.core.credentials;

import java.net.URI;
import java.util.Map;

public interface CoinbaseCredentials {
    Map<String, String> generateAuthHeaders(String httpMethod, URI uri, String body);
}
