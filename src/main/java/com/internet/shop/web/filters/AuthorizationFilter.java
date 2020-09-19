package com.internet.shop.web.filters;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {
    private static final String USER_ID = "user_id";
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private Map<String, List<Role.RoleName>> protectedUrls = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedUrls.put("/users/all", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/orders", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/add", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/orders/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/manage", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/user/delete", List.of(Role.RoleName.ADMIN));
        protectedUrls.put("/user/orders", List.of(Role.RoleName.USER));
        protectedUrls.put("/product/all", List.of(Role.RoleName.USER));
        protectedUrls.put("/shoppingCart/products/add", List.of(Role.RoleName.USER));
        protectedUrls.put("/shoppingCart/products/delete", List.of(Role.RoleName.USER));
        protectedUrls.put("/shoppingCart/products", List.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestUrl = req.getServletPath();
        if (protectedUrls.get(requestUrl) == null) {
            filterChain.doFilter(req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(USER_ID);
        User user = userService.getById(userId);
        if (isAuthorized(user, protectedUrls.get(requestUrl))) {
            filterChain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(User user, List<Role.RoleName> authorizedRoles) {
        for (Role.RoleName roleName : authorizedRoles) {
            for (Role role : user.getRoles()) {
                if (roleName.equals(role.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
