package com.internet.shop.controllers.order;

import com.internet.shop.controllers.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.service.OrderService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user/orders")
public class GetAllUserOrdersController extends HttpServlet {
    private static final Long USER_ID = 1L;
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute(LoginController.getUserId());
        List<Order> orders = orderService.getUserOrders(userId);
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/WEB-INF/views/order/allUserOrders.jsp").forward(req, resp);
    }
}
