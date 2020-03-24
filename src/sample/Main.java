package sample;

import java.awt.*;
import java.awt.TextArea;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


import javax.swing.*;
import javax.xml.soap.Text;

public class Main extends Application {

    public static HashMap<String,String> userList;
    public static ArrayList<String> userInfo;
    public static Statement statement;
    public static HashMap<Long, Book> bookHashMap;
    public Cart cart;

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public TextArea makeTextArea(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextArea makeText = new TextArea(name);
        makeText.setLocation(posX, posY);
        makeText.setSize(width, height);
        return makeText;
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

    public void updateUserInfo(String s1, String s2, String s3, String s4, String s5, String s6){
        userInfo.clear();
        userInfo.add(s1);userInfo.add(s2);userInfo.add(s3);userInfo.add(s4);userInfo.add(s5);userInfo.add(s6);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("start program");
        Pane aPane = new Pane();
        cart = new Cart();
        bookHashMap = new HashMap<Long,Book>();

        // cart section

        int cartPosY = 10;
        int cartPosX = 10;

        // temporary dummy code

        cart.add((long) 5123, 3);
        cart.add((long) 4212, 5);
        cart.add((long) 4134, 1);

        bookHashMap.put((long)5123, new Book((long)5123, "R.L. Stine", "GooseBumps", "Horror", "Scholastic", (float)70, (float)199.99, 346, 50, 10));
        bookHashMap.put((long)4212, new Book((long)4212, "R.L. Stine1", "GooseBumps", "Horror", "Scholastic", (float)70, (float)199.99, 346, 50, 10));
        bookHashMap.put((long)4134, new Book((long)4134, "R.L. Stine2", "GooseBumps", "Horror", "Scholastic", (float)70, (float)199.99, 346, 50, 10));
        //public Book (long ISB, String a, String t, String g, String pn, float pc, float p, int num_pages, int inv) {

        Label cartLabel = makeLabel ("Cart Screen", cartPosX, cartPosY, 200, 10);

        float cartPrice = 0;
        for(int i = 0; i < cart.size(); i++){
            Book element = bookHashMap.get((long)5123);
            Label cartElement = makeLabel(String.format("%s\t\t%s\t x %s: \t%s", element.ISBN, element.title, element.inventory, Float.toString(element.inventory*element.price)), cartPosX,cartPosY+40 ,300,10);
            aPane.getChildren().add(cartElement);
            cartPosY+=40;
            cartPrice+=element.inventory*element.inventory;
        }

        Label totalPriceLabel = makeLabel(String.format("Total Price is: %s", cartPrice), cartPosX, cartPosY+30, 200,50);

        int completionPosY = cartPosY+40;
        int completionPosX = 10;
        String orderNum = "1821957251";
        String trackingNum = "55192582";
        Label completionLabel = makeLabel (String.format("Confirmation Screen\n\nOrder Number:\t #%s.\n\nTracking Number: %s\n\nYour order is now confirmed.", orderNum, trackingNum), completionPosX, completionPosY, 300, 250);

        // login section

        int loginPosY = 210;
        int loginPosX = 300;

        Label loginLabel = makeLabel("Login Screen", loginPosX, loginPosY-200, 100, 25);

        TextField lEmail = makeTextField("Email", loginPosX, loginPosY-160, 175, 25);
        TextField lPassword = makeTextField("Password", loginPosX,  loginPosY-120, 175, 25);
        Button submitLoginButton = makeButton("Login", loginPosX, loginPosY-80, 80, 25);





        // registration section

        Label registration = makeLabel("Registration", loginPosX, loginPosY-40, 100,25);

        TextField rEmail = makeTextField("Email", loginPosX, loginPosY, 75, 25);
        TextField rCreditCard = makeTextField("Password", loginPosX+100, loginPosY, 75, 25);
        TextField rPassword = makeTextField("Credit Card Number", loginPosX, loginPosY+40, 175, 25);
        TextField rBilling = makeTextField("Billing Address", loginPosX, loginPosY+80, 175, 25);
        TextField rShipping = makeTextField("Shipping Address", loginPosX, loginPosY+120, 175, 25);

        Button submitRegistrationButton = makeButton("Register", loginPosX, loginPosY+160, 80, 25);

        // billing section

        int billShipPosX = 500;
        int billShipPosY = 50;

        Label billingTitle = makeLabel("Billing and Shipping Screen", billShipPosX, billShipPosY-40, 200, 25);
        Label billingLabel = makeLabel("Billing Info", billShipPosX, billShipPosY, 200, 25);
        TextField billingName = makeTextField("Name", billShipPosX, billShipPosY+40, 100, 25);
        TextField billingAddress = makeTextField("Address", billShipPosX, billShipPosY+80, 150, 25);
        TextField billingCity = makeTextField("City", billShipPosX, billShipPosY+120, 150, 25);
        TextField billingProvince = makeTextField("Province", billShipPosX, billShipPosY+160, 150, 25);
        TextField billingCountry = makeTextField("Country", billShipPosX, billShipPosY+200, 150, 25);

        Label shippingLabel = makeLabel("Shipping Info", billShipPosX+175, billShipPosY, 400, 25);
        TextField shippingName = makeTextField("Name", billShipPosX+175, billShipPosY+40, 100, 25);
        TextField shippingAddress = makeTextField("Address", billShipPosX+175, billShipPosY+80, 150, 25);
        TextField shippingCity = makeTextField("City", billShipPosX+175, billShipPosY+120, 150, 25);
        TextField shippingProvince = makeTextField("Province", billShipPosX+175, billShipPosY+160, 150, 25);
        TextField shippingCountry = makeTextField("Country", billShipPosX+175, billShipPosY+200, 150, 25);

        Button automaticShipBill = makeButton("Use Information on File", billShipPosX,  billShipPosY+240, 150, 25);
        Button manualShipBill = makeButton("Use Entered Information", billShipPosX, billShipPosY+280, 150, 25);

        // completion page


        // Add all the components to the window
        aPane.getChildren().addAll(
                lPassword, lEmail, loginLabel, submitLoginButton,
                billingTitle, registration, rEmail, rPassword, rCreditCard, rBilling, rShipping, submitRegistrationButton,
                billingLabel, billingName, billingAddress, billingCity, billingProvince, billingCountry,
                shippingLabel, shippingName, shippingAddress, shippingCity, shippingProvince, shippingCountry,
                automaticShipBill, manualShipBill, completionLabel,
                cartLabel, totalPriceLabel);

        StackPane rootPane = new StackPane();

        Pane pane1 = new Pane();
        Pane pane2 = new Pane();

        // Primary Stage
        primaryStage.setTitle("User Screen"); // Set title of window
        primaryStage.setScene(new Scene(aPane, 1285,605));
        primaryStage.show();


        submitRegistrationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "wallBANG666!")) {
                    try (Statement s = connection.createStatement()) {
                        s.executeUpdate(String.format("INSERT INTO users VALUES ( 'userNibboDodddle' ,  '%s' , '%s' , '%s' , '%s' )", rEmail.getText(),  rPassword.getText(), rCreditCard.getText(),rBilling.getText(),rShipping.getText() ));
                        getUserList();
                    }
                }
                catch (Exception e){
                    System.out.println("Outside Error: " + e);
                }
            }
        });

        submitLoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "wallBANG666!")) {
                    try (Statement s = connection.createStatement()) {
                        boolean flag = false;
                        ResultSet resultSet = s.executeQuery("SELECT * FROM users");
                        while (resultSet.next()) {
                            String email = resultSet.getString("user_email");
                            String password = resultSet.getString("password");
                            if((lEmail.getText()).equals(email) && (lPassword.getText()).equals(password)){
                                flag = true;
                            }
                        }
                        System.out.println(flag);
                    }
                }
                catch (Exception e){
                    System.out.println("Outside Error: " + e);
                }
            }
        });

    }

    public static void main(String[] args) {
        System.out.println("Hello World");
        getUserList();
        launch(args);
    }

    public static void getUserList() {
        userList = new HashMap <String,String>(); // user email, pass word, itrerate through the emails, verify it password
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "wallBANG666!"))
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
