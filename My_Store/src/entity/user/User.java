package entity.user;

import common.exception.UserNotExistsException;
import entity.db.AIMSDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String password;
    private boolean isAdmin;

    public User(int id, String name, String email, String address, String phone, String password, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public User() {

    }


    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String
     */
    // override toString method
    @Override
    public String toString() {
        return "{" +
                "  username='" + name + "'" +
                ", email='" + email + "'" +
                ", address='" + address + "'" +
                ", phone='" + phone + "'" +
                "}";
    }


    /**
     * @return String
     */
    // getter and setter
    public String getName() {
        return this.name;
    }


    /**
     * @param name
     */
    public void setusername(String name) {
        this.name = name;
    }


    /**
     * @return String
     */
    public String getEmail() {
        return this.email;
    }


    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * @return String
     */
    public String getAddress() {
        return this.address;
    }


    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }


    /**
     * @return String
     */
    public String getPhone() {
        return this.phone;
    }


    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List getAllUser() throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from User");
        ArrayList users = new ArrayList<>();
        while (res.next()) {
            User user = new User();
            user.setId(res.getInt("id"));
            user.setName(res.getString("name"));
            user.setEmail(res.getString("email"));
            user.setAddress(res.getString("address"));
            user.setPhone(res.getString("phone"));
            user.setAdmin(res.getInt("isadmin") == 1);
            user.setPassword(res.getString("password"));
            users.add(user);
        }
        return users;
    }

    public void addUser() throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String query = "INSERT INTO User (name, email, address, phone, password, isadmin) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, address);
            statement.setString(4, phone);
            statement.setString(5, password);
            statement.setInt(6, isAdmin ? 1 : 0);
            statement.executeUpdate();
        }
    }

    public void editUser() throws SQLException, UserNotExistsException {
        if (!isUserExists(id)) {
            throw new UserNotExistsException();
        }

        Connection connection = AIMSDB.getConnection();
        String query = "UPDATE User SET name = ?, email = ?, address = ?, phone = ?, password = ?, isadmin = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, address);
            statement.setString(4, phone);
            statement.setString(5, password);
            statement.setInt(6, isAdmin ? 1 : 0);
            statement.setInt(7, id);
            statement.executeUpdate();
        }
    }

    public void deleteUser() throws SQLException, UserNotExistsException {
        if (!isUserExists(id)) {
            throw new UserNotExistsException();
        }

        Connection connection = AIMSDB.getConnection();
        String query = "DELETE FROM User WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private boolean isUserExists(int userId) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String query = "SELECT COUNT(*) FROM User WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    public User login(String email, String password, boolean isAdmin) throws SQLException {
        Connection connection = AIMSDB.getConnection();
        String query = "SELECT * FROM User WHERE email = ? AND password = ? AND isadmin = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setInt(3, isAdmin ? 1 : 0);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setAddress(resultSet.getString("address"));
                user.setPhone(resultSet.getString("phone"));
                user.setAdmin(resultSet.getInt("isadmin") == 1);

                return user;
            }
        }

        return null;
    }
}
