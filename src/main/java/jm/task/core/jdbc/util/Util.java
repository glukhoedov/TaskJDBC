package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private final static String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private final static String USERNAME = "root";
    private final static String PASSSWORD = "qwerty123";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSSWORD);
        } catch (SQLException e) {
            System.err.println("Ошибка создания соединения с БД: " + e.getMessage());
        }
        return null;
    }
}
