package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {

    public static final String CREATE_TABLE = "create table if not exists user (\n" +
            "    id        int auto_increment\n" +
            "        primary key,\n" +
            "    name      varchar(45) not null,\n" +
            "    last_name varchar(45) not null,\n" +
            "    age       int         not null\n" +
            ")";
    public static final String DROP_TABLE = "drop table if exists user";
    public static final String INSERT_NEW = "insert into user (name, last_name, age) values (?, ?, ?)";
    public static final String REMOVE_BY_ID = "delete from user where id = ?";
    public static final String GET_ALL = "select * from user";
    public static final String CLEAN = "delete from user";

    private final static String URL = "jdbc:mysql://localhost:3306/mydbtest?useSSL=false&serverTimezone=UTC";
    private final static String USERNAME = "root";
    private final static String PASSSWORD = "qwerty123";

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();
            settings.setProperty("hibernate.connection.url", URL);
            settings.setProperty("hibernate.connection.username", USERNAME);
            settings.setProperty("hibernate.connection.password", PASSSWORD);
            settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
            settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
            settings.put("hibernate.show_sql", "true");
            settings.put("hibernate.hbm2ddl.auto", "update");
            configuration.setProperties(settings);

            configuration.addAnnotatedClass(User.class);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            System.err.println("Ошибка создания SessionFactory: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSSWORD);
        } catch (SQLException e) {
            System.err.println("Ошибка создания соединения с БД: " + e.getMessage());
        }
        return null;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
