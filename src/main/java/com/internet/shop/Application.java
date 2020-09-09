package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");
    private static ProductService productService;
    private static OrderService orderService;
    private static ShoppingCartService shoppingCartService;
    private static UserService userService;

    public static void main(String[] args) {
        productService = (ProductService) injector.getInstance(ProductService.class);
        orderService = (OrderService) injector.getInstance(OrderService.class);
        shoppingCartService = (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
        userService = (UserService) injector.getInstance(UserService.class);

        Product iphone10 = new Product("Iphone 10", 1_200);
        Product iphone11 = new Product("Iphone 11", 1_400);
        Product iphoneX = new Product("Iphone X", 1_600);
        productService.create(iphone10);
        productService.create(iphone11);
        productService.create(iphoneX);
        User firstUser = new User("First User", "first_user", "5364");
        User secondUser = new User("Second User", "second_user", "7536");
        userService.create(firstUser);
        userService.create(secondUser);
        ShoppingCart firstUserCart = new ShoppingCart(firstUser.getId());
        ShoppingCart secondUserCart = new ShoppingCart(secondUser.getId());
        shoppingCartService.create(firstUserCart);
        shoppingCartService.create(secondUserCart);

        System.out.println(productService.getAll());
        productService.delete(iphone11.getId());
        System.out.println(productService.getAll());
        Product iphone12 = new Product("Iphone 12",1800);
        iphone12.setId(iphoneX.getId());
        productService.update(iphone12);
        System.out.println(productService.getAll());
        System.out.println(productService.getById(iphone10.getId()));

        productService.create(iphone11);
        System.out.println(productService.getAll());
        System.out.println();

        System.out.println("All users: ");
        userService.getAll().forEach(System.out::println);
        System.out.println();

        System.out.println("Adding 3 products to firstUser cart:");
        shoppingCartService.addProduct(firstUserCart, productService.getById(iphone10.getId()));
        shoppingCartService.addProduct(firstUserCart, productService.getById(iphone11.getId()));
        shoppingCartService.addProduct(firstUserCart, productService.getById(iphoneX.getId()));
        firstUserCart.getProducts().forEach(System.out::println);
        System.out.println();

        System.out.println("Deleting 1 product from firstUserCart cart:");
        shoppingCartService.deleteProduct(firstUserCart, productService.getById(iphone11.getId()));
        firstUserCart.getProducts().forEach(System.out::println);
        System.out.println();

        System.out.println("Adding an order to firstUser cart:");
        orderService.completeOrder(firstUserCart);
        System.out.println("first_userCart cart : " + firstUserCart.getProducts());
        System.out.println();

        System.out.println("Adding more order to firstUserCart and secondUserCart");
        shoppingCartService.addProduct(firstUserCart, productService.getById(iphoneX.getId()));
        orderService.completeOrder(firstUserCart);
        shoppingCartService.addProduct(secondUserCart, productService.getById(iphone10.getId()));
        orderService.completeOrder(secondUserCart);
        System.out.println("First_user's orders:");
        System.out.println(orderService.getUserOrders(firstUser.getId()));
        System.out.println("Second_user's orders:");
        System.out.println(orderService.getUserOrders(secondUser.getId()));
    }
}
