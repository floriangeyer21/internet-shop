package com.internet.shop.controllers.product;

import com.internet.shop.lib.Injector;
import com.internet.shop.service.ProductService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/product/delete")
public class DeleteProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String userId = req.getParameter("id");
        Long id = Long.valueOf(userId);
        productService.delete(id);
        resp.sendRedirect(req.getContextPath() + "/product/all");
    }
}
