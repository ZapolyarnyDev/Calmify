package io.github.zapolyarnydev.commons.filter;

import java.util.List;

public class HeaderGate{

    private final List<Endpoint> endpoints;

    private final HeaderFilterProviderBuilder builder;

    protected HeaderGate(HeaderFilterProviderBuilder builder, List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        this.builder = builder;
    }

    public HeaderFilterProviderBuilder requireHeaders(List<String> headers) {
        for(var endpoint : endpoints) {
            builder.registerEndpointHeaders(endpoint, headers);
        }
        return builder;
    }
}
