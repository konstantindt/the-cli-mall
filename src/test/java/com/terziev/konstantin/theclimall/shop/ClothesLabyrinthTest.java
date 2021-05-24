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

import javax.money.Monetary;

import org.javamoney.moneta.Money;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

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
            ArgumentMatchers.eq(currencyUnit)
        )).thenReturn(total);

        this.clothesLabyrinth.getBasketTotal();

        Mockito.verify(basketService).getTotal(
            ArgumentMatchers.eq(basket),
            ArgumentMatchers.eq(currencyUnit)
        );
        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(total),
            ArgumentMatchers.eq(locale)
        );
        Mockito.verify(productService, Mockito.never()).getUnknownMessage();
    }

}
