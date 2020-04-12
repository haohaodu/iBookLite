package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegistrationPane extends Pane
{
    private Stage primaryStage;
    private Pane registrationPane;
    private GridPane outerGridPane;
    private Cart orderCart;

    public RegistrationPane(Stage stage, GridPane outerGrid, Cart bookCart) {
        primaryStage = stage;
        outerGridPane = outerGrid;
        orderCart = bookCart;
        registrationPane = createRegisterPane();
    }

    public Pane getRegistrationPane() {
        return registrationPane;
    }

    public Pane createRegisterPane(){
        registrationPane = new Pane();
        createComponents(registrationPane);
        return registrationPane;
    }

    public Pane createComponents(Pane registrationPane) {
        // Registration section
        int loginPosX = 20;
        int loginPosY = 10;

        Label registrationLabel = makeLabel("Registration", loginPosX, loginPosY, 200,25);
        Label emailLabel = makeLabel("Email Address: ", loginPosX, loginPosY+40, 100,25);
        Label passwordLabel = makeLabel("Password: ", loginPosX, loginPosY+80, 100,25);

        TextField rEmail = makeTextField("Email Address", loginPosX+150, loginPosY+40, 175, 25);
        PasswordField rPassword = makePasswordField("", loginPosX+150, loginPosY+80, 175, 25);

        // Payment Method
        Label paymentLabel = makeLabel("Payment Method: ", loginPosX, loginPosY+120, 150,25);
        String payMethods[] = { "VISA", "MasterCard", "American Express" };
        ComboBox rPaymentMethod = makeComboBox(payMethods, loginPosX+150, loginPosY+120, 175, 25);
        TextField rCreditCard = makeTextField("Credit Card Number", loginPosX+150, loginPosY+150, 175, 25);
        TextField rExpDate = makeTextField("Expiry Date (MM/YY)", loginPosX+150, loginPosY+180, 140, 25);
        TextField rCVV = makeTextField("CVV", loginPosX+150, loginPosY+210, 50, 25);

        // Billing Information
        int billingPosX = 20;
        int billingPosY = 270;
        Label billingLabel = makeLabel("Billing Information: ", billingPosX, billingPosY-30, 150,25);
        TextField billing_name = makeTextField("Name on Credit Card", billingPosX, billingPosY, 200, 25);
        TextField billing_address = makeTextField("Billing Address", billingPosX, billingPosY+30, 200, 25);
        TextField billing_city = makeTextField("Billing City", billingPosX, billingPosY+60, 200, 25);
        String provinces[] = { "Alberta", "British Columbia", "Ontario", "Manitoba", "New Brunswick", "Newfoundland", "Nova Scotia", "PEI", "Quebec"};
        ComboBox billing_province = makeComboBox(provinces, billingPosX, billingPosY+90, 200, 25);
        TextField billing_country = makeTextField("Billing Country", billingPosX, billingPosY+120, 200, 25);
        billing_country.setText("Canada");

        // Shipping Information
        int shippingPosX = 280;
        int shippingPosY = 270;
        Label shippingLabel = makeLabel("Shipping Information: ", shippingPosX, shippingPosY-30, 150,25);
        TextField shipping_name = makeTextField("Shipping Name", shippingPosX, shippingPosY, 200, 25);
        TextField shipping_address = makeTextField("Shipping Address", shippingPosX, shippingPosY+30, 200, 25);
        TextField shipping_city = makeTextField("Shipping City", shippingPosX, shippingPosY+60, 200, 25);
        ComboBox shipping_province = makeComboBox(provinces, shippingPosX, shippingPosY+90, 200, 25);
        TextField shipping_country = makeTextField("Shipping Country", shippingPosX, shippingPosY+120, 200, 25);
        shipping_country.setText("Canada");

        //  Register Button, Back Button, Warning Message
        Button submitRegistrationButton = makeButton("Register", billingPosX+30, billingPosY+160, 150, 25);
        Button duplicateButton = makeButton("Same as Billing", shippingPosX+30, shippingPosY+160, 150, 25);
        Label warningLabel = makeLabel("Email address already exists! Please login.", loginPosX, loginPosY-50, 300,25);

        // Set Font
        Font arialFont20 = new Font("Arial", 16);
        registrationLabel.setFont(arialFont20);
        submitRegistrationButton.setFont(arialFont20);
        duplicateButton.setFont(arialFont20);
        Font arialFont = new Font("Arial", 14);
        emailLabel.setFont(arialFont);
        passwordLabel.setFont(arialFont);
        paymentLabel.setFont(arialFont);
        billingLabel.setFont(arialFont);
        shippingLabel.setFont(arialFont);
        warningLabel.setFont(arialFont);

        // Add components to Registration Pane
        registrationPane.getChildren().addAll(registrationLabel, emailLabel, passwordLabel, rEmail, rPassword,
                paymentLabel, rPaymentMethod, rCreditCard, rExpDate, rCVV,
                billingLabel, billing_name, billing_address, billing_city, billing_province, billing_country,
                shippingLabel, shipping_name, shipping_address, shipping_city, shipping_province, shipping_country,
                submitRegistrationButton, duplicateButton, warningLabel);

        // Duplicate Billing Info Button
        duplicateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String billingName = billing_name.getText();
                String billingAddress = billing_address.getText();
                String billingCity = billing_city.getText();
                String billingProvince = (String) billing_province.getValue();
                String billingCountry = billing_country.getText();

                shipping_name.setText(billingName);
                shipping_address.setText(billingAddress);
                shipping_city.setText(billingCity);
                shipping_province.setValue(billingProvince);
                shipping_country.setText(billingCountry);
            }
        });

        submitRegistrationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String emailAddress = rEmail.getText();
                String password = rPassword.getText();
                String paymentMethod = "" + rPaymentMethod.getValue();
                String creditCard = rCreditCard.getText();
                String expDate = rExpDate.getText();
                String CVV = rCVV.getText();
                String billingName = billing_name.getText();
                String billingAddress = billing_address.getText();
                String billingCity = billing_city.getText();
                String billingProvince = (String) billing_province.getValue();
                String billingCountry = billing_country.getText();
                String shippingName = shipping_name.getText();
                String shippingAddress = shipping_address.getText();
                String shippingCity = shipping_city.getText();
                String shippingProvince = (String) shipping_province.getValue();
                String shippingCountry = shipping_country.getText();
                // Check if TextFields are empty
                if(!emailAddress.isEmpty() && !password.isEmpty()) {
                    // Check if email address exists in the users table
                    boolean duplicateEmail = checkDuplicateEmail(emailAddress);
                    if(!duplicateEmail) {
                        warningLabel.relocate(20, -50);
                        // Add to Users table
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                s.executeUpdate(String.format("INSERT INTO users VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                                        emailAddress, password, paymentMethod, creditCard, expDate, CVV,
                                        billingName, billingAddress, billingCity, billingProvince, billingCountry,
                                        shippingName, shippingAddress, shippingCity, shippingProvince, shippingCountry));
                                System.out.println("Inserted customer into users table");

                                // Navigate back to Login Pane
                                registrationPane.getChildren().clear();
                            }
                        }
                        catch (Exception e){
                            System.out.println("Outside Error: " + e);
                        }

                    } else {
                        warningLabel.setText("User already exists! Please login.");
                        warningLabel.relocate(100, 480);
                    }
                }
                else {
                    warningLabel.setText("Please fill out registration information.");
                    warningLabel.relocate(100, 480);
                }
            }
        });
        return registrationPane;
    }

    public boolean checkDuplicateEmail(String emailAddress)
    {
        boolean duplicateEmail = false;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery("SELECT * FROM users");
                while (resultSet.next()) {
                    String existingEmail = resultSet.getString("user_email");
                    if(emailAddress.equals(existingEmail)) {
                        duplicateEmail = true;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("Outside Error: " + e);
        }
        return duplicateEmail;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public PasswordField makePasswordField(String name, Integer posX, Integer posY, Integer width, Integer height){
        PasswordField passwordField = new PasswordField();
        passwordField.setText(name);
        passwordField.relocate(posX, posY);
        passwordField.setPrefSize(width, height);
        return passwordField;
    }

    public ComboBox makeComboBox(String[] lis, Integer posX, Integer posY, Integer width, Integer height){
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(lis));
        combo_box.getSelectionModel().selectFirst();
        combo_box.relocate(posX, posY);
        combo_box.setPrefSize(width,height);
        return combo_box;
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
}
