package com.terziev.konstantin.theclimall.service;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.model.Product;
import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.StreamSupport;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.csv.CSVFormat;

@RequiredArgsConstructor
public class ProductService {

    private final ResourcesProductsCsvPathResolver resourcesProductsCsvPathResolver;

    public void loadFromFile(@NonNull final ProductMapper productMapper,
                             @NonNull final List<Product> products,
                             @NonNull final String productsFilePath) throws IOException {
        final var csvFormat = CSVFormat.DEFAULT
            .withHeader(Product.Fields.id, Product.Fields.name, Product.Fields.price)
            .withSkipHeaderRecord(true)
            .withTrim();

        try(final var productsFile = new FileReader(productsFilePath);
            final var records = csvFormat.parse(productsFile)) {
            StreamSupport.stream(records.spliterator(), false)
                .map(record -> productMapper.toData(record, this.getLocale()))
                .filter(this::isValid)
                .forEach(products::add);
        }
    }

    public void load(@NonNull final ProductMapper productMapper,
                     @NonNull final List<Product> products,
                     @NonNull final String productsFileName) throws IOException {
        final var optionalProductsFilePath = this.resourcesProductsCsvPathResolver.resolve(productsFileName);

        if (optionalProductsFilePath.isPresent()) {
            this.loadFromFile(productMapper, products, optionalProductsFilePath.get());
        }
    }

    public Locale getLocale() {
        return Locale.UK;
    }

    public CurrencyUnit getCurrencyUnit() {
        return Monetary.getCurrency(this.getLocale());
    }

    public boolean isValid(final Product product) {
        return product != null && product.getId() != null;
    }

    public boolean isNotValid(final Product product) {
        return !this.isValid(product);
    }

    public String getUnknownMessage() {
        return ":/";
    }

}
