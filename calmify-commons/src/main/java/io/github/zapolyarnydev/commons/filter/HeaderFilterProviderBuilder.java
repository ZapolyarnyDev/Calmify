package io.github.zapolyarnydev.commons.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderFilterProviderBuilder implements FilterProviderBuilder<HeaderFilterProviderBuilder> {

    private final Map<Endpoint, List<String>> prebuildEndpoints = new HashMap<>();

    protected HeaderFilterProviderBuilder() {

    }

    public HeaderFilterProvider build() {
        return new HeaderFilterProvider(prebuildEndpoints);
    }

    public HeaderGate forEndpoints(List<String> endpoints, List<String> methods) {
        List<Endpoint> preparedEndpoints = new ArrayList<>();
        for(String path : endpoints) {
            preparedEndpoints.add(new Endpoint(path, methods));
        }
        return new HeaderGate(this, preparedEndpoints);
    }

    public HeaderGate forEndpoints(List<String> endpoints) {
        List<Endpoint> preparedEndpoints = new ArrayList<>();
        for(String path : endpoints) {
            preparedEndpoints.add(new Endpoint(path, null));
        }
        return new HeaderGate(this, preparedEndpoints);
    }

    protected void registerEndpointHeaders(Endpoint endpoint, List<String> endpoints) {
        prebuildEndpoints.put(endpoint, endpoints);
    }

}
