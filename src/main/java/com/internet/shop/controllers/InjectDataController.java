package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectDataController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private final ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user1 = new User("user1", "user_1", "324324");
        User user2 = new User("user2", "user_2", "534654");
        User admin = new User("admin", "admin", "345684");
        user1.setRoles(Set.of(Role.of("USER")));
        user2.setRoles(Set.of(Role.of("USER")));
        admin.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(user1);
        userService.create(user2);
        userService.create(admin);
        ShoppingCart shoppingCart1 = new ShoppingCart(user1.getId());
        ShoppingCart shoppingCart2 = new ShoppingCart(user2.getId());
        shoppingCartService.create(shoppingCart1);
        shoppingCartService.create(shoppingCart2);
        req.getRequestDispatcher("/WEB-INF/views/injectData.jsp").forward(req, resp);
    }
}
