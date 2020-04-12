package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ShippingPane {
    private Stage primaryStage;
    private Pane shippingPane;
    private GridPane outerGridPane;
    private Cart orderCart;

    public ShippingPane(Stage stage, GridPane outerGrid, Cart bookCart) {
        primaryStage = stage;
        outerGridPane = outerGrid;
        orderCart = bookCart;
        shippingPane = createShippingPane();
    }

    public Pane getShippingPane() {
        return shippingPane;
    }

    public Pane createShippingPane(){
        shippingPane = new Pane();
        createComponents(shippingPane);
        return shippingPane;
    }

    public Pane createComponents(Pane shippingPane) {
        // Shipping section
        int loginPosX = 20;
        int loginPosY = 10;

        // Payment Method
        Label titleLabel = makeLabel("Billing & Shipping", loginPosX+140, loginPosY, 200,25);
        Label paymentLabel = makeLabel("Payment Method: ", loginPosX, loginPosY+30, 150,25);
        String payMethods[] = { "VISA", "MasterCard", "American Express" };
        ComboBox rPaymentMethod = makeComboBox(payMethods, loginPosX+150, loginPosY+30, 175, 25);
        TextField rCreditCard = makeTextField("Credit Card Number", loginPosX+150, loginPosY+60, 175, 25);
        TextField rExpDate = makeTextField("Expiry Date (MM/YY)", loginPosX+150, loginPosY+90, 140, 25);
        TextField rCVV = makeTextField("CVV", loginPosX+150, loginPosY+120, 50, 25);

        // Billing Information
        int billingPosX = 20;
        int billingPosY = 180;
        Label billingLabel = makeLabel("Billing Information: ", billingPosX+10, billingPosY-30, 150,25);
        TextField billing_name = makeTextField("Name on Credit Card", billingPosX, billingPosY, 200, 25);
        TextField billing_address = makeTextField("Billing Address", billingPosX, billingPosY+30, 200, 25);
        TextField billing_city = makeTextField("Billing City", billingPosX, billingPosY+60, 200, 25);
        String provinces[] = { "Alberta", "British Columbia", "Ontario", "Manitoba", "New Brunswick", "Newfoundland", "Nova Scotia", "PEI", "Quebec"};
        ComboBox billing_province = makeComboBox(provinces, billingPosX, billingPosY+90, 200, 25);
        TextField billing_country = makeTextField("Billing Country", billingPosX, billingPosY+120, 200, 25);

        // Shipping Information
        int shippingPosX = 250;
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
                System.out.println("Use Info on File Button clicked");
            }
        });

        shippingOptionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                warningLabel.setText("Use billing & shipping information on file.");
                warningLabel.relocate(loginPosX+20, loginPosY+380);
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
        return shippingPane;
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
