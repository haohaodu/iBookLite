package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginPane extends Pane{
    public static HashMap<Long, Book> bookHashMap = new HashMap<Long,Book>();
    private static Statement statement;
    private Stage primaryStage;
    private GridPane outerGridPane;
    public Cart cart;
    private Pane LoginPane;

    public LoginPane(Stage stage, GridPane outerGrid, Cart orderCart) {
        primaryStage = stage;
        outerGridPane = outerGrid;
        cart = orderCart;
        LoginPane = createLoginPane();
    }

    public Pane getLoginPane() {
        return LoginPane;
    }

    public Pane createLoginPane(){
        Pane loginPane = new Pane();
        int loginPosX = 80;
        int loginPosY = 10;

        Label loginLabel = makeLabel("Login", loginPosX+10, loginPosY, 100, 25);
        loginLabel.setFont(new Font("Arial", 20));

        TextField lEmail = makeTextField("Email", loginPosX-40, loginPosY+40, 150, 25);
        PasswordField lPassword = makePasswordField("Password", loginPosX-40, loginPosY+80, 150,25);
        Button loginButton = makeButton("Login", loginPosX, loginPosY+120, 80, 25);
        Button registerButton = makeButton("Register", loginPosX, loginPosY+160, 80, 25);
        Label warningLabel = makeLabel("Login Incorrect. Please try again!", 30, -50, 300,25);
        loginPane.getChildren().addAll(loginLabel, lEmail, lPassword, loginButton, registerButton, warningLabel);

        // Handle Buttons
        handleLoginButton(loginButton, lEmail, lPassword, loginPane, warningLabel);
        handleRegisterButton(registerButton);
        return loginPane;
    }

    public void handleLoginButton(Button loginButton, TextField lEmail, PasswordField lPassword, Pane loginPane, Label warningLabel) {
        // Handle Login Button Click
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Login Button Clicked");
                boolean successfulLogin = false;

                try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                    try (Statement s = connection.createStatement()) {
                        ResultSet resultSet = s.executeQuery("SELECT * FROM users");
                        while (resultSet.next()) {
                            String email = resultSet.getString("user_email");
                            String password = resultSet.getString("password");
                            if((lEmail.getText()).equals(email) && (lPassword.getText()).equals(password)){
                                successfulLogin = true;
                                System.out.println("Login successful");
                                cart.userEmail = email;
                                break;
                            }
                        }
                    }
                }
                catch (Exception e){
                    System.out.println("Inside Error: " + e);
                }

                if(successfulLogin) {
                    System.out.println("Navigate to Checkout Pane");
                    loginPane.getChildren().clear();

                    Pane optionsPane = new OptionsPane(primaryStage, outerGridPane, cart).getOptionsPane();
                    Pane cartPane = new CartPane(primaryStage, outerGridPane, cart).getCartPane();

                    // Clear the Old Pane
                    outerGridPane.getChildren().remove(loginPane);
                    outerGridPane.getChildren().clear();
                    primaryStage.show();
                    // Add the New Panes
                    outerGridPane.add(optionsPane, 0, 0);
                    outerGridPane.add(cartPane, 1, 0);
                    primaryStage.show();
                }
                else {
                    System.out.println("Login Incorrect. Please try again!");
                    warningLabel.relocate(30, 210);
                }
            }
        });
    }

    public void handleRegisterButton(Button registerButton) {
        // Handle Register Button
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                registerButton.setDisable(true);
                Pane registrationPane = new RegistrationPane(primaryStage, outerGridPane, cart).getRegistrationPane();
                outerGridPane.add(registrationPane, 2, 0);
                primaryStage.show();
            }
        });
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
