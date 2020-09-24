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
                Order order = getOrderFromResultSet(resultSet, connection);
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user orders by user id "
                    + userId + " failed. ", e);
        }
        return orders;
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
            return addProductToOrderInDB(newOrder, connection);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create order with user id "
                    + order.getUserId(), e);
        }
    }

    @Override
    public Order update(Order order) {
        String query = "UPDATE orders_products SET deleted = TRUE "
                + "WHERE order_id = ?";
        Long orderId = order.getId();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, orderId);
            statement.executeUpdate();
            statement.close();
            return addProductToOrderInDB(order, connection);
        } catch (SQLException e) {
            throw new DataProcessingException("Insert to orders_products with order id  "
                    + orderId + " has failed. ");
        }
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
                Order order = getOrderFromResultSet(resultSet, connection);
                return Optional.of(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting order by id "
                    + orderId + " failed. ", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders WHERE deleted = FALSE";
        List<Order> orderList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Order order = getOrderFromResultSet(resultSet, connection);
                orderList.add(order);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all orders from db failed. ", e);
        }
        return orderList;
    }

    @Override
    public boolean delete(Long orderId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE orders SET deleted = TRUE WHERE order_id = ?");
            statement.setLong(1, orderId);
            return statement.executeUpdate() > 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order with id " + orderId
                    + " failed. ", e);
        }
    }

    private Order addProductToOrderInDB(Order order, Connection connection) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        Long orderId = order.getId();
        for (Product product : order.getProducts()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
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

    private Order getOrderFromResultSet(ResultSet resultSet, Connection connection) {
        try {
            Long orderId = resultSet.getLong("order_id");
            Long userId = resultSet.getLong("user_id");
            Order order = new Order(userId, getProductToOrderFromDB(orderId, connection));
            order.setId(orderId);
            return order;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get order from resultSet", e);
        }
    }

    private List<Product> getProductToOrderFromDB(Long orderId, Connection connection) {
        String query = "SELECT op.product_id "
                + " FROM orders_products op "
                + " JOIN products p "
                + " ON op.product_id = p.product_id "
                + " WHERE op.order_id = ? ";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getProductFromDbById(resultSet.getLong("product_id"),
                        connection));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products list by order id "
                    + orderId, e);
        }
        return products;
    }

    private Product getProductFromDbById(Long productId, Connection connection) {
        String query = "SELECT name, price FROM products  "
                + "WHERE product_id = ? AND deleted = FALSE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
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
        return new Product();
    }
}
