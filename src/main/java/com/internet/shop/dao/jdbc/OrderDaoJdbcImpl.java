package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoJdbcImpl implements OrderDao {
    @Override
    public List<Order> getUserOrders(Long userId) {
        String query = "SELECT order_id, user_id FROM orders "
                + " WHERE user_id = ? AND deleted = FALSE";
        List<Order> orders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user orders by user id "
                    + userId + " failed. ", e);
        }
    }

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            Order newOrder = new Order(order.getUserId(), order.getProducts());
            PreparedStatement statement = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
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
        Long orderId = order.getId();
        for (Product product : order.getProducts()) {
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, orderId);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Insert to orders_products with order id  "
                        + orderId + " has failed. ");
            }
        }
        return order;
    }

    @Override
    public Optional<Order> getById(Long orderId) {
        String query = "SELECT * FROM orders "
                + "WHERE order_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet);
                return Optional.of(order);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting order by id "
                    + orderId + " failed. ", e);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders "
                + "WHERE deleted = FALSE";
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
    public boolean delete(Long orderId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET deleted = TRUE "
                            + "WHERE order_id = ?");
            statement.setLong(1, orderId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order with id " + orderId
                    + " failed. ", e);
        }
    }

    private Order addProductToOrderInDB(Order order) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        Long orderId = order.getId();
        for (Product product : order.getProducts()) {
            try (Connection connection = ConnectionUtil.getConnection()) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setLong(1, orderId);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Insert to orders_products with order_id "
                        + orderId + "is failed. ");
            }
        }
        return order;
    }

    private Order getOrderFromResultSet(ResultSet resultSet) {
        try {
            Long orderId = resultSet.getLong("order_id");
            Long userId = resultSet.getLong("user_id");
            Order order = new Order(userId, getProductToOrderFromDB(orderId));
            order.setId(orderId);
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get order from resultSet", e);
        }
    }

    private List<Product> getProductToOrderFromDB(Long orderId) {
        String query = "SELECT orders_products.product_id "
                + " FROM orders_products "
                + " JOIN products "
                + " ON orders_products.product_id = products.product_id "
                + " WHERE orders_products.order_id = ? ";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromDbById(resultSet.getLong("product_id")));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products list by order id "
                    + orderId, e);
        }
    }

    private Product getProductFromDbById(Long productId) {
        String query = "SELECT name, price FROM products  "
                + "WHERE product_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, productId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                Product product = new Product(name, price);
                product.setId(productId);
                return product;
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting product by id "
                    + productId + " failed. ", e);
        }
        return null;
    }
}
