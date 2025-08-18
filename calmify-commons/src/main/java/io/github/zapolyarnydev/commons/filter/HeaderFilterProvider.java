package io.github.zapolyarnydev.commons.filter;

import io.github.zapolyarnydev.commons.exception.MissingCalmifyHeadersException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HeaderFilterProvider {

    private final Map<Endpoint, List<String>> endpoints;
    private final Map<String, Endpoint> pathCache = new ConcurrentHashMap<>();

    protected HeaderFilterProvider(Map<Endpoint, List<String>> endpoints) {
        this.endpoints = endpoints;
    }

    public static HeaderFilterProviderBuilder builder() {
        return new HeaderFilterProviderBuilder();
    }

    public void validatedHeaders(String path,
                                 UnaryOperator<String> headerOperator,
                                 Supplier<String> methodOperator) throws MissingCalmifyHeadersException {

        var endpoint = getEndpointByPath(path);
        if(endpoint == null) return;

        String method = methodOperator.get().toUpperCase(Locale.ROOT);

        if(endpoint.methods() != null && !endpoint.methods().contains(method)) return;

        List<String> missingHeaders = endpoints.get(endpoint).stream()
                .filter(h -> {
                    String header = headerOperator.apply(h);
                    return header == null || header.isBlank();
                })
                .collect(Collectors.toList());

        if(!missingHeaders.isEmpty()) throw new MissingCalmifyHeadersException(path, method, missingHeaders);
    }

    private Endpoint getEndpointByPath(String path) {
        return pathCache.computeIfAbsent(path, this::searchByPath);
    }

    private Endpoint searchByPath(String path) {
        for (var endpoint : endpoints.keySet()) {
            String epPath = endpoint.path();

            if (epPath.endsWith("/*")) {
                String prefix = epPath.substring(0, epPath.length() - 2);
                if (path.startsWith(prefix)) {
                    return endpoint;
                }
            } else {
                if (path.equals(epPath)) {
                    return endpoint;
                }
            }
        }
        return null;
    }

}
