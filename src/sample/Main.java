package sample;

import java.sql.*;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    protected static Statement statement;
    protected static HashMap<String,String> userList;
    protected static Cart orderCart;
    public GridPane outerGrid;
    protected Pane leftPane;
    protected Pane rightPane;

    @Override
    public void start(Stage primaryStage) throws Exception
    {

        orderCart = new Cart();
        outerGrid = addGridPane();
        // outerGrid.setGridLinesVisible(true);

        // Left Pane = Options Pane
        leftPane = new OptionsPane(primaryStage, outerGrid, orderCart).getOptionsPane();
        outerGrid.add(leftPane, 0, 0);

        // Right Pane = Book Search Pane
        rightPane = new BookSearchPane(primaryStage, outerGrid, orderCart).getBookSearchPane();;
        outerGrid.add(rightPane, 1, 0);

        // Primary Stage
        primaryStage.setTitle("Look Inna Book"); // Set title of window
        primaryStage.setScene(new Scene(outerGrid, 1000,500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        getUserList();
        launch(args);
    }

    public GridPane addGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        return grid;
    }

    public static void getUserList() {
        userList = new HashMap <String,String>(); // user email, pass word, itrerate through the emails, verify it password
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement s = connection.createStatement()) {

                ResultSet resultSet = s.executeQuery("SELECT * FROM users");
                while (resultSet.next()) {
                    String userEmail = resultSet.getString("user_email");
                    String password = resultSet.getString("password");
                    userList.put(userEmail, password);
                    System.out.println(userEmail);
                }
                statement = s;
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
    }
}