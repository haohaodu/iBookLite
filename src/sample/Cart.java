package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    String userEmail;
    HashMap<Long, Integer> storage;

    public Cart(){
        userEmail = "";
        storage = new HashMap<>();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public HashMap<Long, Integer> getCart() {
        return storage;
    }

    public void add(Long ISBN, Integer quantity){
        storage.put(ISBN, quantity);
    }

    public String getBookTotal(String ISBN13, int quantity){
        Float total = 0.00f;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery(String.format("SELECT * FROM book WHERE book.ISBN13 = %s", ISBN13));
                while (resultSet.next()) {
                    float price = Float.parseFloat(resultSet.getString("price"));
                    total = price * quantity*100;
                    int subtotal = Math.round(total);
                    total = (float) subtotal/100;
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println("Outside Error: " + e);
        }
        return total.toString();
    }

    public void removeAll(){
        storage.clear();
    }

    public Integer size(){
        return storage.size();
    }
}