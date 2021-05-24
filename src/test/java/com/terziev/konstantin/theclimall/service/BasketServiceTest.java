package com.terziev.konstantin.theclimall.service;

import com.terziev.konstantin.theclimall.service.BasketService;
import com.terziev.konstantin.theclimall.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.List;

import javax.money.Monetary;

import org.javamoney.moneta.Money;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasketServiceTest {

    @Spy
    private BasketService basketService;

    @Test
    public void addProductToEmptyBasketTest() {
        final var productId = "1";
        final var product = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 1;

        product.setId(productId);

        this.basketService.addProduct(basket, product);

        Assertions.assertFalse(basket.isEmpty());
        Assertions.assertEquals(expected, basket.size());
        Assertions.assertEquals(product, basket.get(0));
    }

    @Test
    public void addProductToBasketTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product0 = new Product();
        final var product1 = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 2;

        product0.setId(productId0);
        product1.setId(productId1);

        basket.add(product0);

        this.basketService.addProduct(basket, product1);

        Assertions.assertFalse(basket.isEmpty());
        Assertions.assertEquals(expected, basket.size());
        Assertions.assertEquals(product0, basket.get(0));
        Assertions.assertEquals(product1, basket.get(1));
    }

    @Test
    public void addProductToBasketTwiceTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product0 = new Product();
        final var product1 = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 2;

        product0.setId(productId0);
        product1.setId(productId1);

        this.basketService.addProduct(basket, product0);
        this.basketService.addProduct(basket, product1);

        Assertions.assertFalse(basket.isEmpty());
        Assertions.assertEquals(expected, basket.size());
        Assertions.assertEquals(product0, basket.get(0));
        Assertions.assertEquals(product1, basket.get(1));
    }

    @Test
    public void removeProductFromEmptyBasketTest() {
        final var productId = "1";
        final var product = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 0;

        product.setId(productId);

        final var removed = this.basketService.removeProduct(basket, product);

        Assertions.assertFalse(removed);
        Assertions.assertEquals(expected, basket.size());
    }

    @Test
    public void removeProductFromBasketTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product0 = new Product();
        final var product1 = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 1;

        product0.setId(productId0);
        product1.setId(productId1);

        basket.add(product0);
        basket.add(product1);

        final var removed = this.basketService.removeProduct(basket, product1);

        Assertions.assertFalse(basket.isEmpty());
        Assertions.assertEquals(expected, basket.size());
        Assertions.assertEquals(product0, basket.get(0));
    }

    @Test
    public void removeProductFromBasketWithRepetitionTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var product0 = new Product();
        final var product1 = new Product();
        final var basket = new ArrayList<Product>();
        final var expected = 2;

        product0.setId(productId0);
        product1.setId(productId1);

        basket.add(product0);
        basket.add(product1);
        basket.add(product0);

        final var removed = this.basketService.removeProduct(basket, product0);

        Assertions.assertFalse(basket.isEmpty());
        Assertions.assertEquals(expected, basket.size());
        Assertions.assertEquals(product0, basket.get(0));
        Assertions.assertEquals(product1, basket.get(1));
    }

    @Test
    public void basketGetTotalTest() {
        final var currencyUnit = Monetary.getCurrency(Locale.UK);
        final var productId0 = "1";
        final var productId1 = "2";
        final var price0 = Money.of(3, currencyUnit);
        final var price1 = Money.of(4, currencyUnit);
        final var total = Money.of(7, currencyUnit);
        final var product0 = new Product();
        final var product1 = new Product();
        final var basket = new ArrayList<Product>();

        product0.setId(productId0);
        product0.setPrice(price0);
        product1.setId(productId1);
        product1.setPrice(price1);

        basket.add(product0);
        basket.add(product1);

        final var totalResult = this.basketService.getTotal(basket, currencyUnit);

        Assertions.assertNotNull(totalResult);
        Assertions.assertEquals(total, totalResult);
    }

    @Test
    public void oneItemBasketGetTotalTest() {
        final var currencyUnit = Monetary.getCurrency(Locale.UK);
        final var productId = "1";
        final var price = Money.of(3, currencyUnit);
        final var total = Money.of(3, currencyUnit);
        final var product = new Product();
        final var basket = new ArrayList<Product>();

        product.setId(productId);
        product.setPrice(price);

        basket.add(product);

        final var totalResult = this.basketService.getTotal(basket, currencyUnit);

        Assertions.assertNotNull(totalResult);
        Assertions.assertEquals(total, totalResult);
    }

    @Test
    public void emptyItemBasketGetTotalTest() {
        final var currencyUnit = Monetary.getCurrency(Locale.UK);
        final var total = Money.of(0, currencyUnit);
        final List<Product> basket = Collections.emptyList();

        final var totalResult = this.basketService.getTotal(basket, currencyUnit);

        Assertions.assertNotNull(totalResult);
        Assertions.assertEquals(total, totalResult);
    }

    @Test
    public void getSuccessMessageTest() {
        final var successMessage = "Done!";

        final var successMessageResult = this.basketService.getSuccessMessage();

        Assertions.assertNotNull(successMessageResult);
        Assertions.assertEquals(successMessage, successMessageResult);
    }

}
