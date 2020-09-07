package com.internet.shop.service.impl;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.Order;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ShoppingCartService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private OrderDao orderDao;
    @Inject
    private ShoppingCartService shoppingCartService;

    @Override
    public Order completeOrder(ShoppingCart shoppingCart) {
        Order order = new Order(shoppingCart.getUserId(), List.copyOf(shoppingCart.getProducts()));
        shoppingCartService.clear(shoppingCart);
        return orderDao.create(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return getAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Order getById(Long id) {
        return orderDao.getById(id).get();
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public boolean delete(Long id) {
        return orderDao.delete(id);
    }
}
