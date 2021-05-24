package com.terziev.konstantin.theclimall.util;

import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ResourcesProductsCsvPathResolverTest {

    @Spy
    private ResourcesProductsCsvPathResolver resourcesProductsCsvPathResolver;

    @Test
    public void resolveTest() {
        final var productsFileName = "clothes-labyrinth-products";
        final var productsFilePath = String.format("products/%s.csv", productsFileName);

        final var optionalProductsFilePath = this.resourcesProductsCsvPathResolver
            .resolve(productsFileName);

        Assertions.assertNotNull(optionalProductsFilePath);
        Assertions.assertTrue(optionalProductsFilePath.isPresent());
        Assertions.assertTrue(optionalProductsFilePath.get().endsWith(productsFilePath));
    }

    @Test
    public void badResolveTest() {
        final var productsFileName = "clothes-labyronth-products";
        final var productsFilePath = String.format("products/%s.csv", productsFileName);

        final var optionalProductsFilePath = this.resourcesProductsCsvPathResolver
            .resolve(productsFileName);

        Assertions.assertNotNull(optionalProductsFilePath);
        Assertions.assertTrue(optionalProductsFilePath.isEmpty());
    }

}
