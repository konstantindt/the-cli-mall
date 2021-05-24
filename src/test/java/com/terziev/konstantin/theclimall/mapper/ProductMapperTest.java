package com.terziev.konstantin.theclimall.mapper;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.model.Product;

import java.util.Collections;
import java.util.Locale;
import java.util.List;

import javax.money.Monetary;

import org.apache.commons.csv.CSVRecord;

import org.javamoney.moneta.Money;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTest {

    @Spy
    private ProductMapper productMapper;

    @Test
    public void csvRecordToDataTest() {
        final var productId = "1";
        final var name = "name";
        final var priceStr = "£3.00";
        final var price = Money.of(3, Monetary.getCurrency(Locale.UK));
        var record = Mockito.mock(CSVRecord.class);

        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.id))).thenReturn(productId);
        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.name))).thenReturn(name);
        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.price))).thenReturn(priceStr);

        final var product = this.productMapper.toData(record, Locale.UK);

        Mockito.verify(productMapper).priceStrToData(
            ArgumentMatchers.eq(priceStr),
            ArgumentMatchers.eq(Locale.UK)
        );

        Assertions.assertNotNull(product);
        Assertions.assertEquals(productId, product.getId());
        Assertions.assertEquals(name, product.getName());
        Assertions.assertEquals(price, product.getPrice());
    }

    @Test
    public void csvRecordWithNullPriceToDataTest() {
        final var productId = "1";
        final var name = "name";
        final var priceStr = "£3.00";
        final var price = Money.of(3, Monetary.getCurrency(Locale.UK));
        var record = Mockito.mock(CSVRecord.class);

        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.id))).thenReturn(productId);
        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.name))).thenReturn(name);
        Mockito.when(record.get(ArgumentMatchers.eq(Product.Fields.price))).thenReturn(null);

        final var product = this.productMapper.toData(record, Locale.UK);

        Mockito.verify(productMapper, Mockito.never()).priceStrToData(
            ArgumentMatchers.eq(priceStr),
            ArgumentMatchers.eq(Locale.UK)
        );

        Assertions.assertNotNull(product);
        Assertions.assertEquals(productId, product.getId());
        Assertions.assertEquals(name, product.getName());
        Assertions.assertNull(product.getPrice());
    }

    @Test
    public void productsProductIdToDataTest() {
        final var productId = "1";
        final var product = new Product();
        final var products = List.of(product);

        product.setId(productId);

        final var productData = this.productMapper.toData(products, productId);

        Assertions.assertNotNull(productData);
        Assertions.assertEquals(product, productData);
    }

    @Test
    public void productsProductIdToNullDataTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product = new Product();
        final var products = List.of(product);

        product.setId(productId0);

        final var productData = this.productMapper.toData(products, productId1);

        Assertions.assertNull(productData);
    }

    @Test
    public void emptyProductsProductIdToNullDataTest() {
        final var productId0 = "1";
        final List<Product> products = Collections.emptyList();

        final var productData = this.productMapper.toData(products, productId0);

        Assertions.assertNull(productData);
    }

    @Test
    public void priceStrToDataTest() {
        final var priceStr = "£3.00";
        final var price = Money.of(3, Monetary.getCurrency(Locale.UK));

        final var priceData = this.productMapper.priceStrToData(priceStr, Locale.UK);

        Assertions.assertNotNull(priceData);
        Assertions.assertEquals(price, priceData);
    }

    @Test
    public void priceToStrTest() {
        final var priceStr = "£3.00";
        final var price = Money.of(3, Monetary.getCurrency(Locale.UK));

        final var priceStrResult = this.productMapper.priceToStr(price, Locale.UK);

        Assertions.assertNotNull(priceStrResult);
        Assertions.assertEquals(priceStr, priceStrResult);
    }

    @Test
    public void productsToPrettyJsonStrTest() {
        final var productId0 = "1";
        final var product = new Product();
        final var products = List.of(product);
        final var prettyJsonString = "[\n  {\n    \"id\": \"1\"\n  }\n]";

        product.setId(productId0);

        final var prettyJsonStringResult = this.productMapper.toPrettyJsonStr(products);

        Assertions.assertNotNull(prettyJsonStringResult);
        Assertions.assertEquals(prettyJsonString, prettyJsonStringResult);
    }

    @Test
    public void emptyProductsToPrettyJsonStrTest() {
        final List<Product> products = Collections.emptyList();
        final var prettyJsonString = "[]";

        final var prettyJsonStringResult = this.productMapper.toPrettyJsonStr(products);

        Assertions.assertNotNull(prettyJsonStringResult);
        Assertions.assertEquals(prettyJsonString, prettyJsonStringResult);
    }

}
