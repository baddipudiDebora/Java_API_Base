package org.db.apicore.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class QueryParametersHandler {

    /**
     * Removes parameters from a URL based on rules similar to the C# version.
     */
    public static String removeParameters(String url, List<String> parameters) {
        if (url == null || url.isBlank() || parameters == null) {
            return url;
        }

        try {
            URI uri = new URI(url);
            String query = uri.getQuery();

            if (query == null) {
                return url;
            }

            Map<String, String> queryMap = parseQuery(query);

            for (String param : parameters) {
                if (queryMap.containsKey(param)) {
                    queryMap.remove(param);
                }
            }

            String newQuery = buildQuery(queryMap);

            return newQuery.isEmpty()
                    ? url.substring(0, url.indexOf("?"))
                    : url.substring(0, url.indexOf("?")) + "?" + newQuery;

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URL: " + url, e);
        }
    }

    /**
     * Adds parameters to a URL.
     */
    public static String addParameters(String url, List<String> parameters) {
        if (url == null || url.isBlank() || parameters == null) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);

        for (String param : parameters) {
            if (sb.toString().contains("?")) {
                sb.append("&").append(param);
            } else {
                sb.append("?").append(param);
            }
        }

        return sb.toString();
    }

    // -------------------------
    // Helper Methods
    // -------------------------

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new LinkedHashMap<>();

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            String key = parts[0];
            String value = parts.length > 1 ? parts[1] : "";
            map.put(key, value);
        }

        return map;
    }

    private static String buildQuery(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }
}
