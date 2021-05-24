package com.terziev.konstantin.theclimall.service;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.model.Product;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.Optional;

import javax.money.Monetary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ResourcesProductsCsvPathResolver resourcesProductsCsvPathResolver;

    @InjectMocks
    @Spy
    private ProductService productService;

    @Test
    public void loadFromFileTest()  throws IOException {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product0 = new Product();
        final var product1 = new Product();
        final var productMapper = Mockito.mock(ProductMapper.class);
        final var fakeProducts = new ArrayList<Product>();
        final var products = new ArrayList<Product>();
        final var productsFilePath = getClass()
            .getClassLoader()
            .getResource("products/clothes-labyrinth-products.csv")
            .getPath();
        final var locale = this.productService.getLocale();
        final var expected = 2;

        product0.setId(productId0);
        product1.setId(productId1);

        fakeProducts.add(product0);
        fakeProducts.add(product1);

        Mockito.when(productMapper.toData(
            ArgumentMatchers.any(),
            ArgumentMatchers.eq(locale)
        )).thenAnswer(invocation -> fakeProducts.remove(0));

        this.productService.loadFromFile(productMapper, products, productsFilePath);

        Mockito.verify(productMapper, Mockito.times(2)).toData(
            ArgumentMatchers.any(),
            ArgumentMatchers.eq(locale)
        );

        Assertions.assertFalse(products.isEmpty());
        Assertions.assertEquals(expected, products.size());
        Assertions.assertEquals(product0, products.get(0));
        Assertions.assertEquals(product1, products.get(1));
    }

    @Test
    public void loadOkFilePathTest()  throws IOException {
        final var productMapper = Mockito.mock(ProductMapper.class);
        final var products = new ArrayList<Product>();
        final var productsFileName = "clothes-labyrinth-products";
        final var productsFilePath = getClass()
            .getClassLoader()
            .getResource(String.format("products/%s.csv", productsFileName))
            .getPath();

        Mockito.when(resourcesProductsCsvPathResolver.resolve(
            ArgumentMatchers.eq(productsFileName)
        )).thenReturn(Optional.of(productsFilePath));

        this.productService.load(productMapper, products, productsFileName);

        Mockito.verify(productService).loadFromFile(
            ArgumentMatchers.eq(productMapper),
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productsFilePath)
        );
    }

    @Test
    public void loadBadFilePathTest()  throws IOException {
        final var productMapper = Mockito.mock(ProductMapper.class);
        final var products = new ArrayList<Product>();
        final var productsFileName = "clothes-labyrinth-products";
        final var productsFilePath = getClass()
            .getClassLoader()
            .getResource(String.format("products/%s.csv", productsFileName))
            .getPath();

        Mockito.when(resourcesProductsCsvPathResolver.resolve(
            ArgumentMatchers.eq(productsFileName)
        )).thenReturn(Optional.empty());

        this.productService.load(productMapper, products, productsFileName);

        Mockito.verify(productService, Mockito.never()).loadFromFile(
            ArgumentMatchers.eq(productMapper),
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productsFilePath)
        );
    }

    @Test
    public void getLocaleTest() {
        final var locale = Locale.UK;

        final var localeResult = this.productService.getLocale();

        Assertions.assertNotNull(localeResult);
        Assertions.assertEquals(locale, localeResult);
    }

    @Test
    public void getCurrencyTest() {
        final var currencyUnit = Monetary.getCurrency(this.productService.getLocale());

        final var currencyUnitResult = this.productService.getCurrencyUnit();

        Assertions.assertNotNull(currencyUnitResult);
        Assertions.assertEquals(currencyUnit, currencyUnitResult);
    }

    @Test
    public void isValidProductTest() {
        final var productId = "1";
        final var product = new Product();

        product.setId(productId);
        
        final var valid = this.productService.isValid(product);

        Assertions.assertTrue(valid);
    }

    @Test
    public void isValidNullProductTest() {
        Assertions.assertFalse(this.productService.isValid(null));
    }

    @Test
    public void isValidProductMissingIdTest() {
        final var product = new Product();
        
        final var valid = this.productService.isValid(product);

        Assertions.assertFalse(valid);
    }

    @Test
    public void isNotValidProductTest() {
        final var productId = "1";
        final var product = new Product();

        product.setId(productId);

        final var valid = this.productService.isNotValid(product);

        Assertions.assertFalse(valid);
    }

    @Test
    public void isNotValidNullProductTest() {
        Assertions.assertTrue(this.productService.isNotValid(null));
    }

    @Test
    public void isNotValidProductMissingIdTest() {
        final var product = new Product();

        final var valid = this.productService.isNotValid(product);

        Assertions.assertTrue(valid);
    }

    @Test
    public void getUnknownMessageTest() {
        final var unknownMessage = ":/";

        final var unknownMessageResult = this.productService.getUnknownMessage();

        Assertions.assertNotNull(unknownMessageResult);
        Assertions.assertEquals(unknownMessage, unknownMessageResult);
    }

}
