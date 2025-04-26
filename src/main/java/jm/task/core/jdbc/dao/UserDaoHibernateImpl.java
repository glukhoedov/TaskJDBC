package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static jm.task.core.jdbc.util.Util.*;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        executeInsideTransaction(s -> s.createSQLQuery(CREATE_TABLE).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        executeInsideTransaction(s -> s.createSQLQuery(DROP_TABLE).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeInsideTransaction(s -> s.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        executeInsideTransaction(s -> {
            User user = s.get(User.class, id);
            if (user != null) {
                s.delete(user);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = Util.getSessionFactory().openSession()) {
            users = session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            System.out.println("Ошибка поиска пользователей: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        executeInsideTransaction(s -> s.createQuery("delete from User").executeUpdate());
    }

    private void executeInsideTransaction(Consumer<Session> action) {
        Transaction transaction = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Ошибка при выполнении запроса к БД:" + e.getMessage());
        }
    }
}
