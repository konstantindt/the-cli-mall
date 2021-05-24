package com.terziev.konstantin.theclimall.model;

import com.google.gson.annotations.JsonAdapter;

import com.terziev.konstantin.theclimall.json.ProductPriceSerializer;

import javax.money.MonetaryAmount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@FieldNameConstants
public class Product {

    private String id;

    @EqualsAndHashCode.Exclude
    private String name;

    @EqualsAndHashCode.Exclude
    @JsonAdapter(ProductPriceSerializer.class)
    private MonetaryAmount price;

}
