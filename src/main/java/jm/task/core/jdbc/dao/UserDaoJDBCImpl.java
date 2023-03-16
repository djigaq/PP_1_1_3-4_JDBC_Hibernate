package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    static Connection connection;
    private static List<User> userList;
    //Queries fields
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS user (
                id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                name VARCHAR(30) NOT NULL,
                lastname VARCHAR(40) NOT NULL,
                age TINYINT NULL)""";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS user";
    private static final String SAVE_USER = "INSERT INTO user (name, lastname, age) VALUES(?, ?, ?)";
    private static final String REMOVE_USER = "DELETE FROM user WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT id, name, lastname, age FROM user";
    private static final String CLEAN_TABLE = "TRUNCATE TABLE user";

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        connection = Util.getConnection();
        try (Statement state = connection.createStatement()) {
            state.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        connection = Util.getConnection();
        try (Statement state = connection.createStatement()) {
            state.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        connection = Util.getConnection();
        try (PreparedStatement pState = connection.prepareStatement(SAVE_USER)) {
            pState.setString(1, name);
            pState.setString(2, lastName);
            pState.setByte(3, age);
            pState.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        connection = Util.getConnection();
        try (PreparedStatement pState = connection.prepareStatement(REMOVE_USER)) {
            pState.setLong(1, id);
            pState.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        userList = new ArrayList<>();
        connection = Util.getConnection();
        try (Statement state = connection.createStatement()) {
            ResultSet resultSet = state.executeQuery(GET_ALL_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        connection = Util.getConnection();
        try (Statement state = connection.createStatement()) {
            state.executeUpdate(CLEAN_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}