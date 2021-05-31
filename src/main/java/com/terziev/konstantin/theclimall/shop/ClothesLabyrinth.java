package com.terziev.konstantin.theclimall.shop;

import com.budhash.cliche.Command;
import com.budhash.cliche.Param;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.model.Product;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.service.BasketService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.money.MonetaryAmount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.javamoney.moneta.Money;

@RequiredArgsConstructor
public class ClothesLabyrinth extends Shop {

    private final ProductMapper productMapper;

    private final ProductService productService;

    private final BasketService basketService;

    public Product getProduct(final String productId) {
        if (productId == null) {
            return null;
        }

        return this.productMapper.toData(this.getProducts(), productId);
    }

    public void loadProducts() throws IOException {
        this.productService.load(
            this.productMapper,
            this.getProducts(),
            "clothes-labyrinth-products"
        );
    }

    @Command(name="line", abbrev="ln", description="see product line")
    public String listProducts() {
        return this.productMapper.toPrettyJsonStr(this.getProducts());
    }

    @Command(name="put", abbrev="pt", description="put product in basket by product id")
    public String addProductToBasket(@Param(name="productId") final String productId) {
        final var product =  this.getProduct(productId);

        if (this.productService.isValid(product)) {
            this.basketService.addProduct(this.getBasket(), product);
            return this.basketService.getSuccessMessage();
        }

        return this.productService.getUnknownMessage();
    }

    @Command(name="return", abbrev="rn", description="return product in basket by product id")
    public String removeProductFromBasket(@Param(name="productId") final String productId) {
        final var product =  this.getProduct(productId);

        if (this.productService.isNotValid(product)) {
            return this.productService.getUnknownMessage();
        }

        if (this.basketService.removeProduct(this.getBasket(), product)) {
            return this.basketService.getSuccessMessage();
        }

        return this.productService.getUnknownMessage();
    }

    @Command(name="total", abbrev="t", description="see basket total price")
    public String getBasketTotal() {
        final var totalPrice = this.basketService.getTotal(
            this.getBasket(),
            this.productService.getCurrencyUnit(),
            this::discountCheapestFreeEvery3
        );

        return this.productMapper.priceToStr(totalPrice, this.productService.getLocale());
    }

    @Command(name="leave", abbrev="lv")
    public void exit() {
        System.exit(0);
    }

    private Optional<MonetaryAmount> discountCheapestFreeEvery3(@NonNull final List<Product> basket) {
        final var discounts = basket.size() / 3;

        if (discounts < 1) {
            return Optional.empty();
        }

        final var discount = basket.stream()
            .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
            .limit(discounts)
            .map(Product::getPrice)
            .reduce(Money.of(0, this.productService.getCurrencyUnit()), MonetaryAmount::add);

        return Optional.of(discount);
    }

}

