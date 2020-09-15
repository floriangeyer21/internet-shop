package com.internet.shop.controllers.cart;

import com.internet.shop.controllers.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/shoppingCart/products/")
public class GetShoppingCartController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(LoginController.getUserId());
        ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);
        List<Product> products = shoppingCart.getProducts();
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/shoppingCart/shoppingCart.jsp").forward(req, resp);
    }
}
