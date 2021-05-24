package com.terziev.konstantin.theclimall.json;

import com.google.gson.JsonPrimitive;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.util.Locale;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

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
public class ProductPriceSerializerTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductPriceSerializer productPriceSerializer;

    @Test
    public void serializePriceTest() {
        final var price = Money.of(3, Monetary.getCurrency(Locale.UK));
        final var priceStr = "£3.00";

        Mockito.when(this.productService.getLocale()).thenReturn(Locale.UK);
        Mockito.when(this.productMapper.priceToStr(
            ArgumentMatchers.eq(price),
            ArgumentMatchers.eq(Locale.UK)
        )).thenReturn(priceStr);

        final var primitive = this.productPriceSerializer.serialize(price, null, null);

        Mockito.verify(productMapper).priceToStr(
            ArgumentMatchers.eq(price),
            ArgumentMatchers.eq(Locale.UK)
        );

        Assertions.assertNotNull(primitive);
        Assertions.assertEquals(priceStr, primitive.getAsString());
    }

    @Test
    public void serializeNullPriceTest() {
        final MonetaryAmount price = null;
        final var priceStr = "(missing)";

        final var primitive = this.productPriceSerializer.serialize(price, null, null);

        Mockito.verify(this.productMapper, Mockito.never()).priceToStr(
            ArgumentMatchers.eq(price),
            ArgumentMatchers.eq(Locale.UK)
        );

        Assertions.assertNotNull(primitive);
        Assertions.assertEquals(priceStr, primitive.getAsString());
    }

}
