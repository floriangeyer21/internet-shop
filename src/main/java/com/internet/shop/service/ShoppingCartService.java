package com.internet.shop.service;

import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;

public interface ShoppingCartService extends GenericService<ShoppingCart, Long> {

    ShoppingCart addProduct(ShoppingCart shoppingCart, Product product);

    ShoppingCart getByUserId(Long userId);

    ShoppingCart deleteProduct(ShoppingCart shoppingCart, Product product);

    ShoppingCart clear(ShoppingCart shoppingCart);
}
