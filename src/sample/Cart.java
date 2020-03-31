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
        userEmail = "hdu2899@gmail.com";
        storage = new HashMap<>();
    }

    public Integer getCartTotal(){
        Integer calculation = 0;

        /*
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "wallBANG666!")) {
            try (Statement s = connection.createStatement()) {
                for (Map.Entry<Long, Integer> c : storage.entrySet()) {
                    ResultSet resultSet = s.executeQuery("SELECT * FROM book");
                    while (resultSet.next()) {
                        String isbn = resultSet.getString("isbn");
                        int price = Integer.parseInt(resultSet.getString("price"));
                        if((c.getKey().toString()).equals(isbn)){
                            calculation+=price;
                        }
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Outside Error: " + e);
        }*/

        calculation+=999;

        return calculation;
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

    public void removeAll(){
        storage.clear();
    }

    public Integer size(){
        return storage.size();
    }
}