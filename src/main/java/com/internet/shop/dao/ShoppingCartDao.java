package com.internet.shop.dao;

import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartDao extends GenericDao<ShoppingCart, Long> {

    Optional<ShoppingCart> getByUserId(Long userId);

  /*  boolean clear(ShoppingCart shoppingCart);

    boolean deleteProduct(Long cartId, Product product);*/
}
