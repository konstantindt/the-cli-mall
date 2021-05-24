package com.terziev.konstantin.theclimall.main;

import com.budhash.cliche.ShellFactory;

import com.terziev.konstantin.theclimall.mapper.ProductMapper;
import com.terziev.konstantin.theclimall.service.BasketService;
import com.terziev.konstantin.theclimall.service.ProductService;
import com.terziev.konstantin.theclimall.shop.ClothesLabyrinth;
import com.terziev.konstantin.theclimall.util.ResourcesProductsCsvPathResolver;

import java.io.IOException;

public class Mall {

    public static void main(String[] args) throws IOException {
        final var resourcesProducsCsvPathResolver = new ResourcesProductsCsvPathResolver();
        final var productMapper = new ProductMapper();
        final var productService = new ProductService(resourcesProducsCsvPathResolver);
        final var basketService = new BasketService();
        final var clothesLabyrinth = new ClothesLabyrinth(
            productMapper,
            productService,
            basketService
        );

        clothesLabyrinth.loadProducts();

        ShellFactory.createConsoleShell(
            "the-cli-mall/clothes-labyrinth",
            "The CLI Mall :: Clothes Labyrinth\nEnter ?l to list available action.",
            clothesLabyrinth
        ).commandLoop();
    }

}
