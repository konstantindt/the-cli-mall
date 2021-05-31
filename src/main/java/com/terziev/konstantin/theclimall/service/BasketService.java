package com.terziev.konstantin.theclimall.service;

import com.terziev.konstantin.theclimall.model.Product;
import com.terziev.konstantin.theclimall.service.ProductService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import lombok.NonNull;

import org.javamoney.moneta.Money;

public class BasketService {

    public void addProduct(@NonNull final List<Product> basket, @NonNull final Product product) {
        basket.add(product);
    }

    public boolean removeProduct(@NonNull final List<Product> basket,
                                 @NonNull final Product product) {
        final var productLastIndex = basket.lastIndexOf(product);

        if (-1 < productLastIndex) {
            basket.remove(productLastIndex);
            return true;
        }

        return false;
    }

    public MonetaryAmount getTotal(@NonNull final List<Product> basket,
                                   @NonNull final CurrencyUnit currencyUnit,
                                   @NonNull final Function<List<Product>, Optional<MonetaryAmount>> discountCalculator) {
        final var total = basket.stream()
            .map(Product::getPrice)
            .reduce(Money.of(0, currencyUnit), MonetaryAmount::add);
        final var optionalDiscount = discountCalculator.apply(basket);

        if (optionalDiscount.isPresent()) {
            return total.subtract(optionalDiscount.get());
        }

        return total;
    }

    public String getSuccessMessage() {
        return "Done!";
    }

}
