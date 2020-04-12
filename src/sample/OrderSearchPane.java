package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderSearchPane extends Pane
{
    private static Statement statement;
    private Stage primaryStage;
    private GridPane outerGridPane;
    public Cart cart;
    private Pane orderPane;

    public OrderSearchPane(Stage stage, GridPane outerGrid, Cart orderCart) {
        primaryStage = stage;
        outerGridPane = outerGrid;
        cart = orderCart;
        orderPane = createOrderPane();
    }

    public Pane getOrderPane() {
        return orderPane;
    }

    public Pane createOrderPane() {
        Pane rightPane = new Pane();
        rightPane.setMinSize(300,300);
        // Order Search
        TextField searchField = makeTextField("Enter Order Number", 10, 10, 300, 25);
        Button searchButton = makeButton("Search", 320, 10, 130, 25);

        // Order ListView
        ListView<String> orderListView = new ListView<String>();
        orderListView.relocate(10, 45);
        orderListView.setPrefSize(300, 205);

        ArrayList<String> searchList = new ArrayList<String>();
        orderListView.setItems(FXCollections.observableArrayList(searchList));

        rightPane.getChildren().addAll(
                searchField, searchButton,
                orderListView);

        // Order Search
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Order Search Button");
                String searchString = searchField.getText();
                if(!searchString.isEmpty() && !searchString.equals(" ")) {
                    // Search the orders table by order number
                    HashMap<String, Order> orderList = searchByOrderNumber(searchString);
                    // Print the details of each order in the listView
                    ArrayList<String> orderCartDetails = new ArrayList<String>();
                    for(String orderNumber : orderList.keySet()) {
                        orderCartDetails.add("Order #: " + orderNumber);
                        orderCartDetails.add("   Tracking #: " + orderList.get(orderNumber).getTrackingNumber());
                        orderCartDetails.add("   Current Location: " + orderList.get(orderNumber).getCurrentLocation());
                    }
                    ObservableList<String> orderDetails = FXCollections.observableArrayList (orderCartDetails);
                    orderListView.setItems(orderDetails);
                }
                else {
                    System.out.println("Please enter order number.");
                }
                primaryStage.show();
            }
        });

        return rightPane;
    }

    private static HashMap<String, Order> searchByOrderNumber(String order_number) {
        HashMap<String, Order> orderList = new HashMap<String, Order>();
        String searchString = "'" + order_number + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM orders WHERE orders.order_number = " + searchString + ";");
                while (resultSet.next()) {
                    String orderNumber = resultSet.getString("order_number");
                    String userEmail = resultSet.getString("user_email");
                    String ISBN13 = resultSet.getString("ISBN13");
                    String quantity = resultSet.getString("quantity");
                    String orderTotal = resultSet.getString("order_total");
                    String orderTime = resultSet.getString("order_time");
                    String trackingNumber = resultSet.getString("tracking_number");
                    String currentLocation = resultSet.getString("current_location");

                    Order currentOrder = new Order(orderNumber, userEmail, ISBN13, quantity, orderTotal, orderTime, trackingNumber, currentLocation);
                    orderList.put(orderNumber, currentOrder);
                    break;
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return orderList;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public Label makeLabel(String name, Integer posX, Integer posY, Integer width, Integer height){
        Label label = new Label(name);
        label.relocate(posX, posY);
        label.setPrefSize(width, height);
        return label;
    }

    public Button makeButton(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Button button = new javafx.scene.control.Button(name);
        button.relocate(posX, posY);
        button.setPrefSize(width, height);
        return button;
    }
}