package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private static final String CREATE_TABLE = "create table if not exists users (\n" +
            "    id        int auto_increment\n" +
            "        primary key,\n" +
            "    name      varchar(45) not null,\n" +
            "    last_name varchar(45) not null,\n" +
            "    age       int         not null\n" +
            ")";
    private static final String DROP_TABLE = "drop table if exists users";
    private static final String INSERT_NEW = "insert into users (name, last_name, age) values (?, ?, ?)";
    private static final String REMOVE_BY_ID = "delete from users where id = ?";
    private static final String GET_ALL = "select * from users";
    private static final String CLEAN = "delete from users";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(CREATE_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка создания таблицы: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(DROP_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления таблицы: " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(INSERT_NEW)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения пользователя: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(REMOVE_BY_ID)) {
            preparedStatement.setLong(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Запись с ID " + id + " успешно удалена");
            } else {
                System.out.println("Запись с ID " + id + " не найдена");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка удаления пользователя: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(GET_ALL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка вывода списка пользователей: " + e.getMessage());
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement(CLEAN)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Ошибка отчистки таблицы: " + e.getMessage());
        }
    }
}
