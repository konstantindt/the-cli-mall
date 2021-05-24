package com.terziev.konstantin.theclimall.shop;

import com.terziev.konstantin.theclimall.model.Product;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

abstract class Shop {

    @Getter
    private List<Product> products = new ArrayList<>();

    @Getter
    private List<Product> basket = new ArrayList<>();

    abstract void loadProducts() throws java.io.IOException;

    abstract String listProducts();

    abstract String addProductToBasket(final String productId);

    abstract String removeProductFromBasket(final String productId);

    abstract String getBasketTotal();

    abstract void exit();

}
