package com.terziev.konstantin.theclimall.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.lang.reflect.Type;

import javax.money.MonetaryAmount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductPriceSerializer implements JsonSerializer<MonetaryAmount> {

    private final ProductMapper productMapper;

    private final ProductService productService;

    public ProductPriceSerializer() {
        final var resourcesProducsCsvPathResolver = new ResourcesProductsCsvPathResolver();

        this.productMapper = new ProductMapper();
        this.productService = new ProductService(resourcesProducsCsvPathResolver);
    }

    @Override
    public JsonPrimitive serialize(MonetaryAmount price, Type type, JsonSerializationContext context) {
        var priceStr = (price != null)
                ? this.productMapper.priceToStr(price, this.productService.getLocale())
                : "(missing)";

        return new JsonPrimitive(priceStr);
    }

}
