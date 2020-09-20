package com.internet.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find MySQL Driver", e);
        }
    }

    public static Connection getConnection() {
        Properties doProperties = new Properties();
        doProperties.put("user", "test_admin");
        doProperties.put("password", "12345");
        String url = "jdbc:mysql://localhost:3306/internet_shop?serverTimeZone=UTC";
        try {
            return DriverManager.getConnection(url, doProperties);
        } catch (SQLException e) {
            throw new RuntimeException("Can't establish the connection to DB", e);
        }
    }
}
