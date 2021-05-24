package com.terziev.konstantin.theclimall.util;

import java.net.URL;
import java.util.Optional;

import lombok.NonNull;

public class ResourcesProductsCsvPathResolver {

    public Optional<String> resolve(@NonNull final String productsFileName) {
        final var path = String.format("products/%s.csv", productsFileName);

        return Optional.ofNullable(getClass().getClassLoader().getResource(path))
            .map(URL::getPath);
    }

}
