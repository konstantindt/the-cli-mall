package com.terziev.konstantin.theclimall.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.terziev.konstantin.theclimall.model.Product;

import java.util.List;
import java.util.Locale;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryFormats;
import javax.money.format.MonetaryParseException;

import lombok.NonNull;

import org.apache.commons.csv.CSVRecord;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

public class ProductMapper {

    public Product toData(@NonNull final CSVRecord record, @NonNull final Locale locale) {
        final var product = new Product();

        product.setId(record.get(Product.Fields.id));
        product.setName(record.get(Product.Fields.name));

        final var priceStr = record.get(Product.Fields.price);
        if (priceStr != null) {
            try {
                product.setPrice(this.priceStrToData(priceStr, locale));
            } catch(MonetaryParseException e) {

            }
        }

        return product;
    }

    public Product toData(@NonNull final List<Product> products, @NonNull final String id) {
        return products.stream()
            .filter(product -> id.equals(product.getId()))
            .findFirst()
            .orElse(null);
    }

    public MonetaryAmount priceStrToData(@NonNull final String priceStr, @NonNull final Locale locale) throws MonetaryParseException {
        final var formatQuery = AmountFormatQueryBuilder.of(locale)
            .set(CurrencyStyle.SYMBOL)
            .build();

        return MonetaryFormats.getAmountFormat(formatQuery).parse(priceStr);
    }

    public String priceToStr(@NonNull final MonetaryAmount price, @NonNull final Locale locale) {
        final var formatQuery = AmountFormatQueryBuilder.of(locale)
            .set(CurrencyStyle.SYMBOL)
            .build();

        return MonetaryFormats.getAmountFormat(formatQuery).format(price);
    }

    public String toPrettyJsonStr(@NonNull final List<Product> products) {
        final var gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(products);
    }

}
