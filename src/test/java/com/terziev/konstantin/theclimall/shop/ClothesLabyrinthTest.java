package com.terziev.konstantin.theclimall.shop;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.model.Product;
import com.terziev.konstantin.theclimall.service.BasketService;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.shop.ClothesLabyrinth;

import java.io.IOException;
import java.util.Locale;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClothesLabyrinthTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductService productService;

    @Mock
    private BasketService basketService;

    @InjectMocks
    private ClothesLabyrinth clothesLabyrinth;

    @Test
    public void loadProductsTest() throws IOException {
        final var productsFileName = "clothes-labyrinth-products";
        final var products = this.clothesLabyrinth.getProducts();

        this.clothesLabyrinth.loadProducts();

        Mockito.verify(productService).load(
            ArgumentMatchers.eq(this.productMapper),
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productsFileName)
        );
    }

    @Test
    public void listProductsTest() throws IOException {
        final var products = this.clothesLabyrinth.getProducts();

        this.clothesLabyrinth.listProducts();

        Mockito.verify(productMapper).toPrettyJsonStr(
            ArgumentMatchers.eq(products)
        );
    }

    @Test
    public void getProductTest() {
        final var productId = "1";
        final var products = this.clothesLabyrinth.getProducts();

        this.clothesLabyrinth.getProduct(productId);

        Mockito.verify(productMapper).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        );
    }

    @Test
    public void getNullProductTest() {
        final String productId = null;
        final var products = this.clothesLabyrinth.getProducts();

        this.clothesLabyrinth.getProduct(productId);

        Mockito.verify(productMapper, Mockito.never()).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        );
    }

    @Test
    public void addProductToBasketTest() {
        final var productId = "1";
        final var product = new Product();
        final var products = this.clothesLabyrinth.getProducts();
        final var basket = this.clothesLabyrinth.getBasket();

        product.setId(productId);

        Mockito.when(productMapper.toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        )).thenReturn(product);
        Mockito.when(productService.isValid(ArgumentMatchers.eq(product))).thenReturn(true);

        this.clothesLabyrinth.addProductToBasket(productId);

        Mockito.verify(productService).isValid(ArgumentMatchers.eq(product));
        Mockito.verify(productMapper).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        );
        Mockito.verify(basketService).addProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();
    }

    @Test
    public void addProductToBasketNullInputTest() {
        final var productId0 = "1";
        final String productId1 = null;
        final var product0 = new Product();
        final Product product1 = null;
        final var products = this.clothesLabyrinth.getProducts();
        final var basket = this.clothesLabyrinth.getBasket();

        product0.setId(productId0);

        Mockito.when(productService.isValid(ArgumentMatchers.eq(product1))).thenReturn(false);

        this.clothesLabyrinth.addProductToBasket(productId1);

        Mockito.verify(productService).isValid(ArgumentMatchers.eq(product1));
        Mockito.verify(productMapper, Mockito.never()).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId0)
        );
        Mockito.verify(basketService, Mockito.never()).addProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product0)
        );
        Mockito.verify(productService).getUnknownMessage();
    }

    @Test
    public void removeProductFromBasketTest() {
        final var productId = "1";
        final var product = new Product();
        final var products = this.clothesLabyrinth.getProducts();
        final var basket = this.clothesLabyrinth.getBasket();

        product.setId(productId);

        Mockito.when(productMapper.toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        )).thenReturn(product);
        Mockito.when(productService.isNotValid(ArgumentMatchers.eq(product))).thenReturn(false);
        Mockito.when(basketService.removeProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product)
        )).thenReturn(true);

        this.clothesLabyrinth.removeProductFromBasket(productId);

        Mockito.verify(productService).isNotValid(ArgumentMatchers.eq(product));
        Mockito.verify(productMapper).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        );
        Mockito.verify(basketService).removeProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();
    }

    @Test
    public void removeProductNotInBasketFromBasketTest() {
        final var productId = "1";
        final var product = new Product();
        final var products = this.clothesLabyrinth.getProducts();
        final var basket = this.clothesLabyrinth.getBasket();

        product.setId(productId);

        Mockito.when(productMapper.toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        )).thenReturn(product);
        Mockito.when(productService.isNotValid(ArgumentMatchers.eq(product))).thenReturn(false);
        Mockito.when(basketService.removeProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product)
        )).thenReturn(false);

        this.clothesLabyrinth.removeProductFromBasket(productId);

        Mockito.verify(productService).isNotValid(ArgumentMatchers.eq(product));
        Mockito.verify(productMapper).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId)
        );
        Mockito.verify(basketService).removeProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product)
        );
        Mockito.verify(productService).getUnknownMessage();
    }

    @Test
    public void removeProductFromBasketNullInputTest() {
        final var productId0 = "1";
        final String productId1 = null;
        final var product0 = new Product();
        final Product product1 = null;
        final var products = this.clothesLabyrinth.getProducts();
        final var basket = this.clothesLabyrinth.getBasket();

        product0.setId(productId0);

        Mockito.when(productService.isNotValid(ArgumentMatchers.eq(product1))).thenReturn(true);

        this.clothesLabyrinth.removeProductFromBasket(productId1);

        Mockito.verify(productService).isNotValid(ArgumentMatchers.eq(product1));
        Mockito.verify(productMapper, Mockito.never()).toData(
            ArgumentMatchers.eq(products),
            ArgumentMatchers.eq(productId0)
        );
        Mockito.verify(basketService, Mockito.never()).removeProduct(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(product0)
        );
        Mockito.verify(productService).getUnknownMessage();
    }

    @Test
    public void getBasketTotalTest() {
        final var locale = Locale.UK;
        final var currencyUnit = Monetary.getCurrency(locale);
        final var total = Money.of(0, currencyUnit);
        final var basket = this.clothesLabyrinth.getBasket();

        Mockito.when(productService.getLocale()).thenReturn(locale);
        Mockito.when(productService.getCurrencyUnit()).thenReturn(currencyUnit);
        Mockito.when(basketService.getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            ArgumentMatchers.any()
        )).thenReturn(total);

        this.clothesLabyrinth.getBasketTotal();

        Mockito.verify(basketService).getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            ArgumentMatchers.any()
        );
        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(total),
            ArgumentMatchers.eq(locale)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();
    }

    @Test
    public void getBasketTotalNoDiscountTest() {
        final var productId = "1";
        final var product = new Product();
        final var locale = Locale.UK;
        final var currencyUnit = Monetary.getCurrency(locale);
        final var price = Money.of(3, currencyUnit);
        final var total = Money.of(3, currencyUnit);
        final var basket = this.clothesLabyrinth.getBasket();
        final ArgumentCaptor<Function<List<Product>, Optional<MonetaryAmount>>> discountCalculatorCaptor = ArgumentCaptor.forClass(Function.class);

        product.setId(productId);
        product.setPrice(price);

        basket.add(product);

        Mockito.when(productService.getLocale()).thenReturn(locale);
        Mockito.when(productService.getCurrencyUnit()).thenReturn(currencyUnit);
        Mockito.when(basketService.getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            ArgumentMatchers.any()
        )).thenReturn(total);

        this.clothesLabyrinth.getBasketTotal();

        Mockito.verify(basketService).getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            discountCalculatorCaptor.capture()
        );
        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(total),
            ArgumentMatchers.eq(locale)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();

        final var optionalDiscount = discountCalculatorCaptor.getValue().apply(basket);

        Assertions.assertFalse(optionalDiscount.isPresent());
    }

    @Test
    public void getBasketTotal1DiscountTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var productId2 = "3";
        final var productId3 = "4";
        final var productId4 = "5";
        final var product0 = new Product();
        final var product1 = new Product();
        final var product2 = new Product();
        final var product3 = new Product();
        final var product4 = new Product();
        final var locale = Locale.UK;
        final var currencyUnit = Monetary.getCurrency(locale);
        final var price0 = Money.of(5, currencyUnit);
        final var price1 = Money.of(4, currencyUnit);
        final var price2 = Money.of(3, currencyUnit);
        final var price3 = Money.of(2, currencyUnit);
        final var price4 = Money.of(1, currencyUnit);
        final var total = Money.of(14, currencyUnit);
        final var basket = this.clothesLabyrinth.getBasket();
        final ArgumentCaptor<Function<List<Product>, Optional<MonetaryAmount>>> discountCalculatorCaptor = ArgumentCaptor.forClass(Function.class);

        product0.setId(productId0);
        product0.setPrice(price0);
        product1.setId(productId1);
        product1.setPrice(price1);
        product2.setId(productId2);
        product2.setPrice(price2);
        product3.setId(productId3);
        product3.setPrice(price3);
        product4.setId(productId4);
        product4.setPrice(price4);

        basket.add(product0);
        basket.add(product1);
        basket.add(product2);
        basket.add(product3);
        basket.add(product4);

        Mockito.when(productService.getLocale()).thenReturn(locale);
        Mockito.when(productService.getCurrencyUnit()).thenReturn(currencyUnit);
        Mockito.when(basketService.getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            ArgumentMatchers.any()
        )).thenReturn(total);

        this.clothesLabyrinth.getBasketTotal();

        Mockito.verify(basketService).getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            discountCalculatorCaptor.capture()
        );
        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(total),
            ArgumentMatchers.eq(locale)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();

        final var optionalDiscount = discountCalculatorCaptor.getValue().apply(basket);

        Assertions.assertTrue(optionalDiscount.isPresent());
        Assertions.assertEquals(price4, optionalDiscount.get());
    }

    @Test
    public void getBasketTotal2DiscountsTest() {
        final var productId0 = "1";
        final var productId1 = "2";
        final var productId2 = "3";
        final var productId3 = "4";
        final var productId4 = "5";
        final var productId5 = "6";
        final var productId6 = "7";
        final var product0 = new Product();
        final var product1 = new Product();
        final var product2 = new Product();
        final var product3 = new Product();
        final var product4 = new Product();
        final var product5 = new Product();
        final var product6 = new Product();
        final var locale = Locale.UK;
        final var currencyUnit = Monetary.getCurrency(locale);
        final var price0 = Money.of(7, currencyUnit);
        final var price1 = Money.of(6, currencyUnit);
        final var price2 = Money.of(5, currencyUnit);
        final var price3 = Money.of(4, currencyUnit);
        final var price4 = Money.of(3, currencyUnit);
        final var price5 = Money.of(2, currencyUnit);
        final var price6 = Money.of(1, currencyUnit);
        final var total = Money.of(25, currencyUnit);
        final var basket = this.clothesLabyrinth.getBasket();
        final ArgumentCaptor<Function<List<Product>, Optional<MonetaryAmount>>> discountCalculatorCaptor = ArgumentCaptor.forClass(Function.class);

        product0.setId(productId0);
        product0.setPrice(price0);
        product1.setId(productId1);
        product1.setPrice(price1);
        product2.setId(productId2);
        product2.setPrice(price2);
        product3.setId(productId3);
        product3.setPrice(price3);
        product4.setId(productId4);
        product4.setPrice(price4);
        product5.setId(productId5);
        product5.setPrice(price5);
        product6.setId(productId6);
        product6.setPrice(price6);

        basket.add(product0);
        basket.add(product1);
        basket.add(product2);
        basket.add(product3);
        basket.add(product4);
        basket.add(product5);
        basket.add(product6);

        Mockito.when(productService.getLocale()).thenReturn(locale);
        Mockito.when(productService.getCurrencyUnit()).thenReturn(currencyUnit);
        Mockito.when(basketService.getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            ArgumentMatchers.any()
        )).thenReturn(total);

        this.clothesLabyrinth.getBasketTotal();

        Mockito.verify(basketService).getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit),
            discountCalculatorCaptor.capture()
        );
        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(total),
            ArgumentMatchers.eq(locale)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();

        final var optionalDiscount = discountCalculatorCaptor.getValue().apply(basket);

        Assertions.assertTrue(optionalDiscount.isPresent());
        Assertions.assertEquals(price5.add(price6), optionalDiscount.get());
    }

}
