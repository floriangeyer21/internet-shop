package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
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
public class ShoppingCartDaoJdbcImpl implements ShoppingCartDao {
    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        String query = "SELECT * FROM shopping_carts "
                + " WHERE user_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = getShoppingCartFromResultSet(resultSet, connection);
                shoppingCart.setProducts(getProductToShoppingCartFromDB(
                        shoppingCart.getId(), connection));
                return Optional.of(shoppingCart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting shopping cart by user id "
                    + userId + " failed. ", e);
        }
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            ShoppingCart newShoppingCart = new ShoppingCart(shoppingCart.getUserId());
            PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newShoppingCart.setId(resultSet.getLong(1));
            }
            return newShoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create shopping cart with user id "
                    + shoppingCart.getUserId(), e);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        String query = "UPDATE shopping_carts_products SET deleted = TRUE "
                + "WHERE cart_id = ?";
        Long cartId = shoppingCart.getId();
        try (Connection connection = ConnectionUtil.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, cartId);
            statement.executeUpdate();
            addProductsToDB(shoppingCart, connection);
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Update to shopping_carts_products with cart_id "
                    + shoppingCart.getId() + " is failed. ");
        }
    }

    @Override
    public Optional<ShoppingCart> getById(Long cartId) {
        String query = "SELECT * FROM shopping_carts "
                + "WHERE cart_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = getShoppingCartFromResultSet(resultSet, connection);
                shoppingCart.setProducts(getProductToShoppingCartFromDB(cartId, connection));
                return Optional.of(shoppingCart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting shopping cart by id "
                    + cartId + " failed. ", e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        String query = "SELECT * FROM shopping_carts "
                + "WHERE deleted = FALSE";
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = getShoppingCartFromResultSet(resultSet, connection);
                return shoppingCarts;
            }
            return shoppingCarts;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all shopping carts from db failed. ", e);
        }
    }

    @Override
    public boolean delete(Long cartId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE shopping_carts SET deleted = TRUE "
                            + "WHERE cart_id = ?");
            statement.setLong(1, cartId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting shopping cart with id " + cartId
                    + " failed. ", e);
        }
    }

    private ShoppingCart getShoppingCartFromResultSet(ResultSet resultSet, Connection connection) {
        try {
            Long cartId = resultSet.getLong("cart_id");
            Long userId = resultSet.getLong("user_id");
            ShoppingCart shoppingCart = new ShoppingCart(userId, cartId);
            shoppingCart.setProducts(getProductToShoppingCartFromDB(cartId, connection));
            return shoppingCart;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get product from resultSet", e);
        }
    }

    private List<Product> getProductToShoppingCartFromDB(Long cartId, Connection connection) {
        String query = "SELECT * FROM shopping_carts_products sc "
                + " JOIN products p "
                + " ON sc.product_id = p.product_id "
                + " WHERE sc.cart_id = ? "
                + " AND sc.deleted = FALSE ";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add((new Product(resultSet.getLong("product_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price"))));
            }
            return products;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products list by cart id "
                    + cartId, e);
        }
    }

    private void addProductsToDB(ShoppingCart shoppingCart, Connection connection)
            throws SQLException {
        String query = "INSERT INTO shopping_carts_products (cart_id, product_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, shoppingCart.getId());
            for (Product product : shoppingCart.getProducts()) {
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        }
    }
}
