package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public List<Order> getUserOrders(Long user_id) {
        String query = "SELECT order_id, user_id FROM orders "
                + " WHERE user_id = ? AND deleted = FALSE";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, user_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user orders by user id "
                    + user_id + " failed. ", e);
        }
    }

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            Order newOrder = new Order(order.getUserId(), order.getProducts());
            PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newOrder.setId(resultSet.getLong(1));
            }
            return addProductToOrderInDB(newOrder);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create order with user id "
                    + order.getUserId(), e);
        }
    }

    @Override
    public Order update(Order order) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        Long order_id = order.getId();
        for (Product product : order.getProducts()) {
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement statement = connection
                        .prepareStatement(query);
                statement.setLong(1, order_id);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Insert to orders_products with order id  "
                        + order_id + "is failed. ");
            }
        }
        return order;
    }

    @Override
    public Optional<Order> getById(Long order_id) {
        String query = "SELECT * FROM orders "
                + " WHERE order_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet);
                return Optional.of(order);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting order by id "
                    + order_id + " failed. ", e);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders "
                + " WHERE deleted = FALSE";
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet);
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all orders from db failed. ", e);
        }
    }

    @Override
    public boolean delete(Long order_id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET deleted = TRUE "
                            + "WHERE order_id = ?");
            statement.setLong(1, order_id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order with id " + order_id
                    + " failed. ", e);
        }
    }

    private Order addProductToOrderInDB(Order order) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        Long order_id = order.getId();
        for (Product product : order.getProducts()) {
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement statement = connection
                        .prepareStatement(query);
                statement.setLong(1, order_id);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Insert to orders_products with order_id "
                        + order_id + "is failed. ");
            }
        }
        return order;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) {
        try {
            Long order_id = resultSet.getLong("order_id");
            Long user_id = resultSet.getLong("user_id");
            Order order = new Order(user_id, getProductToOrderFromDB(order_id));
            order.setId(order_id);
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get order from resultSet", e);
        }
    }

    private List<Product> getProductToOrderFromDB(Long order_id) {
        String query = "SELECT orders_products.product_id "
                + " FROM orders_products "
                + " JOIN products "
                + " ON orders_products.product_id = products.product_id "
                + " WHERE orders_products.order_id = ? ";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, order_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromDbById(resultSet.getLong("product_id")));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products list by order id "
                    + order_id, e);
        }
    }

    private Product getProductFromDbById(Long product_id) {
        String query = "SELECT name, price FROM products "
                + "WHERE product_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, product_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                Product product = new Product(name, price);
                product.setId(product_id);
                return product;
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting product by id "
                    + product_id + " failed. ", e);
        }
        return null;
    }
}
