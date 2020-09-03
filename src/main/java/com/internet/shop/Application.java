package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);

        Product iphone10 = new Product("Iphone 10", 1_200);
        Product iphone11 = new Product("Iphone 11", 1_400);
        Product iphoneX = new Product("Iphone X", 1_600);

        productService.create(iphone10);
        productService.create(iphone11);
        productService.create(iphoneX);

        System.out.println(productService.getAll());
        productService.deleteById(iphone11.getId());
        System.out.println(productService.getAll());
        Product newIphone = new Product("Iphone 12",1800);
        newIphone.setId(3L);
        productService.update(newIphone);
        System.out.println(productService.getAll());
        System.out.println(productService.getById(iphone10.getId()));
    }
}
