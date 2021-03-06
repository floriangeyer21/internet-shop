package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public Optional<User> findByLogin(String login) {
        String query = "SELECT * FROM users"
                + " WHERE users.login = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getUserFromResultSet(resultSet, connection);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user by id "
                    + login + " failed. ", e);
        }
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users (name, login, password, salt) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            User newUser = new User(user.getName(), user.getLogin(), user.getPassword());
            PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                newUser.setId(resultSet.getLong(1));
            }
            newUser.setRoles(user.getRoles());
            statement.close();
            addRolesToUser(newUser, connection);
            return newUser;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create user with name "
                    + user.getName(), e);
        }
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET name = ?, "
                + "login = ?, password = ?, salt = ? WHERE user_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.setLong(5, user.getId());
            int changedRows = statement.executeUpdate();
            statement.close();
            deleteRolesFromDB(user.getId(), connection);
            addRolesToUser(user, connection);
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Updating user with id "
                    + user.getId() + " is failed. ", e);
        }
    }

    @Override
    public Optional<User> getById(Long userId) {
        String query = "SELECT * FROM users"
                + " WHERE users.user_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = getUserFromResultSet(resultSet, connection);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user by id "
                    + userId + " failed. ", e);
        }
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM users WHERE deleted = FALSE";
        List<User> userList = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                userList.add(getUserFromResultSet(resultSet, connection));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all products", e);
        }
        return userList;
    }

    @Override
    public boolean delete(Long userId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET deleted = TRUE "
                            + "WHERE user_id = ?");
            statement.setLong(1, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting user with id " + userId
                    + " failed. ", e);
        }
    }

    private Set<Role> getUserRolesFromDB(User user, Connection connection) {
        String query = "SELECT roles.role_id, roles.role_name "
                + " FROM users_roles"
                + " JOIN roles"
                + " ON users_roles.role_id = roles.role_id"
                + " WHERE users_roles.user_id = ?";
        Set<Role> roleSet = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                roleSet.add(getRoleFromResultSet(resultSet));
            }
            return roleSet;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get role set by user id "
                    + user.getId(), e);
        }
    }

    private Role getRoleFromResultSet(ResultSet resultSet) {
        try {
            long roleId = resultSet.getLong("role_id");
            String roleName = resultSet.getString("role_name");
            Role role = new Role(Role.of(roleName).getRoleName());
            role.setId(roleId);
            return role;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get role from resultSet", e);
        }
    }

    private void addRolesToUser(User user, Connection connection) {
        String query = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
        Long userId = user.getId();
        for (Role role : user.getRoles()) {
            try (PreparedStatement statement = connection.prepareStatement(query);) {
                Role.RoleName roleName = role.getRoleName();
                Long roleId = getRoleIdByName(roleName, connection);
                role.setId(roleId);
                statement.setLong(1, userId);
                statement.setLong(2, roleId);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Insert to users_roles with user_id "
                        + user.getId() + "is failed. ");
            }
        }
    }

    private Long getRoleIdByName(Role.RoleName roleName, Connection connection) {
        String query = "SELECT role_id FROM roles WHERE role_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setString(1, roleName.name());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("role_id");
            }
            throw new DataProcessingException("Cant get rode id by role name" + roleName.name());
        } catch (SQLException e) {
            throw new DataProcessingException("Getting role_id by role_name "
                    + roleName.name() + " failed. ", e);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet, Connection connection) {
        try {
            long userId = resultSet.getLong("user_id");
            String name = resultSet.getString("name");
            String login = resultSet.getString("login");
            String password = resultSet.getString("password");
            byte[] salt = resultSet.getBytes("salt");
            User user = new User(name, login, password);
            user.setSalt(salt);
            user.setId(userId);
            user.setRoles(getUserRolesFromDB(user, connection));
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get user from resultSet", e);
        }
    }

    private void deleteRolesFromDB(Long userId, Connection connection) throws SQLException {
        String sql = "DELETE FROM users_roles WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.executeUpdate();
        }
    }
}
