package sample;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class dbMethods {
    public void insertUsers(String userType, String email, String password, String creditCard, String billing, String shipping) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "wallBANG666!");
        Statement s = connection.createStatement();
        s.executeQuery("INSERT INTO users" +
                "VALUES (" + userType +  "  , " + email + ", " + password + " , " + creditCard +", " + billing + ", " + shipping +")");
    }
}
