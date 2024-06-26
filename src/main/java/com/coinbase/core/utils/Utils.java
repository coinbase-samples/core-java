/*
 * Copyright 2024-present Coinbase Global, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.coinbase.core.utils;

import com.coinbase.core.errors.CoinbaseClientException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.coinbase.core.utils.Constants.EMPTY_STRING;

public class Utils {
    public static String toJsonString(ObjectMapper mapper, Object requestObject) throws CoinbaseClientException {
        if (requestObject == null) {
            return EMPTY_STRING;
        }

        try {
            return mapper.writeValueAsString(requestObject);
        } catch (Throwable e) {
            throw new CoinbaseClientException("Failed to write request object as String", e);
        }
    }

    public static String appendQueryParams(String queryParams, String key, String value) {
        if (isNullOrEmpty(value)) {
            return queryParams;
        }
        return String.format("%s%s%s=%s", queryParams, queryParams.isEmpty() ? "?" : "&", key, value);
    }

    public static String appendAllQueryParams(Object[] params, String key, String queryString) {
        if (params != null) {
            for (Object param : params) {
                if (param == null) {
                    continue;
                }
                queryString = appendQueryParams(queryString, key, param.toString());
            }
        }
        return queryString;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
