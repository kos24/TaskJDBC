package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    Connection connection = Util.getDBConnection();

    public void createUsersTable() {


        String sql = "CREATE TABLE `mydb`.`User` (`id` INT NOT NULL," +
                                                    "`name` VARCHAR(45) NULL," +
                                                    "`lastName` VARCHAR(55) NULL," +
                                                    "`age` INT NULL," +
                                                    "PRIMARY KEY (`id`)" +
                                                    ");";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            System.out.println("Таблица USER создана");
        } catch (SQLException e) {
            System.out.println("Таблица USER уже существует");
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE mydb.USER;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.execute();
            System.out.println("Таблица USER удалена");
        } catch (SQLException e) {
            System.out.println("Таблица USER не существует");
        }
    }


    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO mydb.USER(id, name, lastName, age) VALUES (?,?,?,?);";
        String getId = "SELECT MAX(ID) as ID FROM mydb.USER";
        Long id = 0L;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getId);
            while (resultSet.next()) {
                id = resultSet.getLong("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1,id + 1);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,lastName);
            preparedStatement.setByte(4,age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.out.println("Не удалось добавить данные в таблицу USER");
        }
    }

    public void removeUserById(long id) {
        String getUserId = "DELETE FROM mydb.USER WHERE ID = ?;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(getUserId)) {
           preparedStatement.setLong(1,id);
           preparedStatement.executeUpdate();
            System.out.println("User с номером ID = " + id + "удален из базы");
        } catch (SQLException e) {
            e.printStackTrace();
//            System.out.println("Не найден User с ID = " + id);
        }

    }

    public List<User> getAllUsers() {

        List<User> userList = new ArrayList<>();
        String getAll = "SELECT * FROM mydb.USER;";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAll);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));

                userList.add(user);
                System.out.println(user.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        String cleanUsers = "DELETE FROM mydb.USER;";

        try (PreparedStatement preparedStatement = connection.prepareStatement(cleanUsers)) {
            preparedStatement.execute();
            System.out.println("Таблица USER очищена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
