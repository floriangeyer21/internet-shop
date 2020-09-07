package com.internet.shop.dao.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.ShoppingCart;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Dao
public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        Storage.addShoppingCart(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        IntStream.range(0, Storage.shoppingCarts.size())
                .filter(i -> Storage.shoppingCarts.get(i).getId().equals(shoppingCart.getId()))
                .forEach(i -> Storage.shoppingCarts.set(i, shoppingCart));
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        return getAll().stream()
                .filter(shoppingCart -> shoppingCart.getId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean delete(ShoppingCart shoppingCart) {
        return Storage.shoppingCarts
                .removeIf(shoppingCart1 -> shoppingCart.equals(shoppingCart1));
    }

    private List<ShoppingCart> getAll() {
        return Storage.shoppingCarts;
    }
}
