package sample;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CartPane extends Pane{
    private static Statement statement;
    protected static HashMap<String, Book> bookList;
    private ListView<String> cartListView;
    private Stage primaryStage;
    private Pane cartPane;
    private GridPane outerGrid;
    public Cart orderCart;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String defaultLocation = "Toronto, ON, Canada";

    public CartPane(Stage stage, GridPane outerGridPane, Cart cart) {
        primaryStage = stage;
        outerGrid = outerGridPane;
        orderCart = cart;
        getBookList();
        cartPane = createCartPane();
    }

    public Pane getCartPane() {
        return cartPane;
    }

    public Pane createCartPane(){
        Pane cartPane = new Pane();
        int cartPosX = 20;
        int cartPosY = 5;
        Label cartLabel = makeLabel ("View Cart", cartPosX+100, cartPosY, 200, 10);
        cartLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));

        // Book ListView
        cartListView = new ListView<String>();
        cartListView.relocate(cartPosX, cartPosY+35);
        cartListView.setPrefSize(300, 205);

        // Complete Order Button
        cartPosY = 260;
        Button completeOrderButton = makeButton("Complete Order", cartPosX, cartPosY, 300,40);
        Font arialFont16 = new Font("Arial", 16);
        completeOrderButton.setFont(arialFont16);
        completeOrderButton.setDisable(true);

        // Complete Order label
        Label completeLabel = makeLabel("Order Complete!", 20, -50, 250,25);
        Label moreBooksLabel = makeLabel("An email has been sent to order more books!", 20, -50, 300,25);
        completeLabel.setFont(arialFont16);
        moreBooksLabel.setFont(arialFont16);

        populateListView(cartListView);
        cartPane.getChildren().addAll(cartLabel, cartListView, completeOrderButton, completeLabel, moreBooksLabel);

        createShippingPane(cartPane, completeOrderButton);
        // Handle Buttons
        handleCompleteOrderButton(completeOrderButton, completeLabel, moreBooksLabel);

        return cartPane;
    }

    public void createShippingPane(Pane shippingPane, Button completeOrderButton) {
        // Shipping section
        int loginPosX = 370;
        int loginPosY = 10;

        // Payment Method
        Label titleLabel = makeLabel("Billing & Shipping", loginPosX+100, loginPosY, 200,25);
        Label paymentLabel = makeLabel("Payment Method: ", loginPosX, loginPosY+30, 150,25);
        String payMethods[] = { "VISA", "MasterCard", "American Express" };
        ComboBox rPaymentMethod = makeComboBox(payMethods, loginPosX+150, loginPosY+30, 175, 25);
        TextField rCreditCard = makeTextField("Credit Card Number", loginPosX+150, loginPosY+60, 175, 25);
        TextField rExpDate = makeTextField("Expiry Date (MM/YY)", loginPosX+150, loginPosY+90, 140, 25);
        TextField rCVV = makeTextField("CVV", loginPosX+150, loginPosY+120, 50, 25);

        // Billing Information
        int billingPosX = 360;
        int billingPosY = 180;
        Label billingLabel = makeLabel("Billing Information: ", billingPosX+10, billingPosY-30, 150,25);
        TextField billing_name = makeTextField("Name on Credit Card", billingPosX, billingPosY, 200, 25);
        TextField billing_address = makeTextField("Billing Address", billingPosX, billingPosY+30, 200, 25);
        TextField billing_city = makeTextField("Billing City", billingPosX, billingPosY+60, 200, 25);
        String provinces[] = { "Alberta", "British Columbia", "Ontario", "Manitoba", "New Brunswick", "Newfoundland", "Nova Scotia", "PEI", "Quebec"};
        ComboBox billing_province = makeComboBox(provinces, billingPosX, billingPosY+90, 200, 25);
        TextField billing_country = makeTextField("Billing Country", billingPosX, billingPosY+120, 200, 25);

        // Shipping Information
        int shippingPosX = 590;
        int shippingPosY = 180;
        Label shippingLabel = makeLabel("Shipping Information: ", shippingPosX+10, shippingPosY-30, 150,25);
        TextField shipping_name = makeTextField("Shipping Name", shippingPosX, shippingPosY, 200, 25);
        TextField shipping_address = makeTextField("Shipping Address", shippingPosX, shippingPosY+30, 200, 25);
        TextField shipping_city = makeTextField("Shipping City", shippingPosX, shippingPosY+60, 200, 25);
        ComboBox shipping_province = makeComboBox(provinces, shippingPosX, shippingPosY+90, 200, 25);
        TextField shipping_country = makeTextField("Shipping Country", shippingPosX, shippingPosY+120, 200, 25);

        //  Register Button, Back Button, Warning Message
        Button shippingOptionsButton = makeButton("Use Registration Info", loginPosX, loginPosY+330, 200, 40);
        Button updateButton = makeButton("Update Info", shippingPosX, loginPosY+330, 120, 40);
        Label warningLabel = makeLabel("Currently using shipping information on file.", loginPosX+20, loginPosY+380, 300,25);

        // Set Font
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));
        Font arial16 = new Font("Arial", 16);
        shippingOptionsButton.setFont(arial16);
        updateButton.setFont(arial16);
        Font arial14 = new Font("Arial", 14);
        paymentLabel.setFont(arial14);
        billingLabel.setFont(arial14);
        shippingLabel.setFont(arial14);
        warningLabel.setFont(arial14);

        // Add components to Registration Pane
        shippingPane.getChildren().addAll(titleLabel, paymentLabel, rPaymentMethod, rCreditCard, rExpDate, rCVV,
                billingLabel, billing_name, billing_address, billing_city, billing_province, billing_country,
                shippingLabel, shipping_name, shipping_address, shipping_city, shipping_province, shipping_country,
                shippingOptionsButton, updateButton, warningLabel);

        shippingOptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                warningLabel.setText("Use billing & shipping information on file.");
                warningLabel.relocate(loginPosX+20, loginPosY+380);
                completeOrderButton.setDisable(false);
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Update Button Clicked");
                String emailAddress = orderCart.getUserEmail();
                String paymentMethod = "''" + (String) rPaymentMethod.getValue();
                String creditCard = rCreditCard.getText();
                String expDate = rExpDate.getText();
                String CVV = rCVV.getText();
                String billingName = billing_name.getText();
                String billingAddress = billing_name.getText();
                String billingCity = billing_city.getText();
                String billingProvince = (String) billing_province.getValue();
                String billingCountry = billing_country.getText();
                String shippingName = shipping_name.getText();
                String shippingAddress = shipping_address.getText();
                String shippingCity = shipping_city.getText();
                String shippingProvince = (String) shipping_province.getValue();
                String shippingCountry = shipping_country.getText();
                // Check if TextFields are empty
                if(!creditCard.isEmpty() && !expDate.isEmpty() && !CVV.isEmpty() &&
                        !billingName.isEmpty() && !billingAddress.isEmpty() && !billingCity.isEmpty() && !billingProvince.isEmpty() &&
                        !shippingName.isEmpty() && !shippingAddress.isEmpty() && !shippingCity.isEmpty() && !shippingProvince.isEmpty())
                {
                    System.out.println("Update information in users table");
                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                        try (Statement s = connection.createStatement()) {
                            s.executeUpdate(String.format("UPDATE users SET " +
                                            "payment_method = '%s', " +
                                            "credit_card_number = '%s', " +
                                            "exp_date = '%s', " +
                                            "CVV = '%s', " +
                                            "billing_name = '%s', " +
                                            "billing_address = '%s', " +
                                            "billing_city = '%s', " +
                                            "billing_province = '%s', " +
                                            "billing_country = '%s', " +
                                            "shipping_name = '%s', " +
                                            "shipping_address = '%s', " +
                                            "shipping_city = '%s', " +
                                            "shipping_province = '%s', " +
                                            "shipping_country = '%s' " +
                                            "WHERE user_email = '%s';",
                                    paymentMethod, creditCard, expDate, CVV,
                                    billingName, billingAddress, billingCity, billingProvince, billingCountry,
                                    shippingName, shippingAddress, shippingCity, shippingProvince, shippingCountry,
                                    emailAddress));
                            warningLabel.setText("Billing and Shipping Information updated.");
                            warningLabel.relocate(loginPosX+20, loginPosY+380);
                            completeOrderButton.setDisable(false);
                        }
                    }
                    catch (Exception e){
                        System.out.println("Outside Error: " + e);
                    }
                }
                else {
                    warningLabel.setText("Please fill out billing & shipping information.");
                    warningLabel.relocate(loginPosX+100, loginPosY+380);
                }
            }
        });
    }
    public void populateListView(ListView cartListView) {
        HashMap<Long, Integer> bookCart = orderCart.getCart();
        HashMap<String, Integer> finalCart = new HashMap<String, Integer>();
        for (long ISBN13 : bookCart.keySet()) {
            int quantity = bookCart.get(ISBN13);
            ArrayList<String> bookTitles = searchByISBN(ISBN13 + "");
            System.out.println(bookTitles);
            if(!bookTitles.isEmpty()) {
                finalCart.put(bookTitles.get(0), quantity);
            }
        }
        // Print out Final Cart
        ArrayList<String> finalCartDetails = new ArrayList<String>();
        if(!finalCart.isEmpty()) {
            for (String item : finalCart.keySet()) {
                int quantity = finalCart.get(item);
                if(bookList.containsKey(item)) {
                    Book selectedBook = bookList.get(item);
                    finalCartDetails.add(item);
                    float finalBookPrice = selectedBook.price*quantity;
                    String stringBookPrice = String.format("%.2f", finalBookPrice);
                    finalCartDetails.add("Quantity: " + quantity + " | Price: $" + stringBookPrice);
                }
            }
        }
        ObservableList<String> cartDetails = FXCollections.observableArrayList (finalCartDetails);
        cartListView.setItems(cartDetails);
    }

    public void handleCompleteOrderButton(Button checkoutButton, Label completeLabel, Label moreBooksLabel) {
        // View Cart Button
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Complete Order Button clicked");
                HashMap<Long, Integer> bookCart = orderCart.getCart();
                String orderNumber = "00"+getOrderNumber();
                String realOrderNumber = "not complete.";
                completeLabel.relocate(120, 310);
                String userEmail = orderCart.getUserEmail();

                if(bookCart.isEmpty())
                    completeLabel.setText("Cart is Empty!");
                else if(userEmail.isEmpty())
                    completeLabel.setText("Please login!");
                else {
                    checkoutButton.setDisable(true);
                    // Add to Users table
                    for (Map.Entry<Long, Integer> item : bookCart.entrySet()) {
                        realOrderNumber = "5167" + orderNumber;
                        String ISBN13 = item.getKey().toString();
                        String quantity = item.getValue().toString();
                        String bookTotal = orderCart.getBookTotal(ISBN13, item.getValue());
                        Float finalBookTotal = Float.parseFloat(bookTotal);
                        String orderTime = dtf.format(now);
                        String trackingNumber = "97814" + orderNumber;
                        String currentLocation = defaultLocation;

                        // Write to order table
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                s.executeUpdate(String.format("INSERT INTO orders VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')",
                                realOrderNumber, ISBN13, userEmail, quantity, finalBookTotal, orderTime, trackingNumber, currentLocation));
                            }
                        }
                        catch (Exception e){
                            System.out.println("Outside Error: " + e);
                        }

                        // Get Publisher's Balance
                        Book book = getBook(ISBN13);
                        float publisherBalance = 0.0f;
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                ResultSet resultSet = s.executeQuery(String.format("SELECT publisher_balance FROM publisher WHERE publisher_name = '%s';", book.publisher_name));
                                while (resultSet.next()) {
                                    publisherBalance = resultSet.getFloat("publisher_balance");
                                }
                            }
                        }
                        catch (Exception e){
                            System.out.println("Publisher's Balance Error: " + e);
                        }

                        // Get Info to Update Publisher's Cut
                        Float publisherPercentage = Float.parseFloat(String.valueOf(book.publisher_cut))/100;
                        System.out.println("Publisher Percentage: " + publisherPercentage);
                        Float publisherCut = publisherPercentage * finalBookTotal;
                        System.out.println("Publisher Cut: " + publisherCut);
                        System.out.println("Publisher Balance Before: " + publisherBalance);
                        publisherBalance += publisherCut;
                        System.out.println("Publisher Balance After: " + publisherBalance);

                        // Update Publisher's Balance and Deduct Inventory
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                s.executeUpdate(String.format("UPDATE publisher SET publisher_balance = '%s' WHERE publisher_name = '%s';",
                                        publisherBalance, book.publisher_name));
                            }
                        }
                        catch (Exception e){
                            System.out.println("Book Inventory Error: " + e);
                        }

                        // Update Inventory
                        int updatedInventory = book.inventory - item.getValue();
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                s.executeUpdate(String.format("UPDATE book SET inventory = '%s' WHERE ISBN13 = '%s';",
                                        updatedInventory, ISBN13));
                                if(updatedInventory < book.minInventory) {
                                    moreBooksLabel.setText("More books have been ordered via email!");
                                    moreBooksLabel.relocate(20, 340);
                                }
                            }
                        }
                        catch (Exception e){
                            System.out.println("Outside Error: " + e);
                        }

                    }

                    completeLabel.relocate(80, 310);
                    completeLabel.setText("Your order # is: "+ realOrderNumber);
                    orderCart.removeAll();
                }
                // Refresh Screen
                primaryStage.show();
            }
        });
    }

    private int getOrderNumber() {
        // Add to Users table
        int count = 0;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery("SELECT * FROM orders");
                while (resultSet.next()) {
                    count++;
                }
            }
        }
        catch (Exception e){
            System.out.println("Outside Error: " + e);
        }
        return count;
    }
    // Search Book by ISBN
    private static ArrayList<String> searchByISBN(String ISBN) {
        ArrayList<String> searchList = new ArrayList<String>();
        String searchString = "'" + ISBN + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE book.ISBN13 = " + searchString + ";");
                while (resultSet.next()) {
                    String book = resultSet.getString("title");
                    searchList.add(book);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return searchList;
    }

    // Get Book by ISBN
    private static Book getBook(String ISBN) {
        Book book = new Book();
        String searchString = "'" + ISBN + "'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE book.ISBN13 = " + searchString + ";");
                while (resultSet.next()) {
                    long ISBN13 = resultSet.getLong("isbn13");
                    String author = resultSet.getString("author");
                    String bookTitle = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String publisherName = resultSet.getString("publisher_name");
                    float publisherCut = resultSet.getFloat("publisher_cut");
                    float price = resultSet.getFloat("price");
                    int pages = resultSet.getInt("num_pages");
                    int inventory = resultSet.getInt("inventory");
                    int minInventory = resultSet.getInt("min_inventory");
                    // Add all books to bookList
                    book = new Book(ISBN13, author, bookTitle, genre, publisherName, publisherCut, price, pages, inventory, minInventory);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return book;
    }

    public static void getBookList() {
        bookList = new HashMap <String, Book>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery("SELECT * FROM book");
                while (resultSet.next()) {
                    long ISBN13 = resultSet.getLong("isbn13");
                    String author = resultSet.getString("author");
                    String bookTitle = resultSet.getString("title");
                    String genre = resultSet.getString("genre");
                    String publisherName = resultSet.getString("publisher_name");
                    float publisherCut = resultSet.getFloat("publisher_cut");
                    float price = resultSet.getFloat("price");
                    int pages = resultSet.getInt("num_pages");
                    int inventory = resultSet.getInt("inventory");
                    int minInventory = resultSet.getInt("min_inventory");
                    // Add all books to bookList
                    Book book = new Book(ISBN13, author, bookTitle, genre, publisherName, publisherCut, price, pages, inventory, minInventory);
                    bookList.put(bookTitle, book);
                }
                statement = s;
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
    }

    public Label makeLabel(String name, Integer posX, Integer posY, Integer width, Integer height){
        Label label = new Label(name);
        label.relocate(posX, posY);
        label.setPrefSize(width, height);
        return label;
    }

    public Button makeButton(String name, Integer posX, Integer posY, Integer width, Integer height){
        Button button = new Button(name);
        button.relocate(posX, posY);
        button.setPrefSize(width, height);
        return button;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public ComboBox makeComboBox(String[] lis, Integer posX, Integer posY, Integer width, Integer height){
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(lis));
        combo_box.getSelectionModel().selectFirst();
        combo_box.relocate(posX, posY);
        combo_box.setPrefSize(width,height);
        return combo_box;
    }
}