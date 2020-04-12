package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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

public class PublisherPane {
    private Pane publisherPane;

    public PublisherPane(Stage stage, GridPane outerGrid) {
        publisherPane = createPublisherPane();
    }

    public Pane getPublisherPane() {
        return publisherPane;
    }

    public Pane createPublisherPane() {
        publisherPane = new Pane();

        int PosX = 20;
        int PosY = 10;
        // Title
        Label title = makeLabel ("Add New Publisher", PosX+15, PosY, 350, 10);
        title.setFont(Font.font("Arial", FontWeight.BOLD,20));

        // Instruction Label
        Label instructionLabel = makeLabel("Please enter the following information:", PosX-10, PosY+30, 400,25);
        instructionLabel.setFont(Font.font("Arial",14));

        // Book Information
        PosX = 20;
        PosY = 70;
        TextField publisher_field = makeTextField("Publisher Name:", PosX, PosY, 200, 25);
        TextField email_field = makeTextField("Email Address:", PosX, PosY+30, 200, 25);
        TextField address_field = makeTextField("Address:", PosX, PosY+60, 200, 25);
        TextField phone_field = makeTextField("Phone Number:", PosX, PosY+90, 200, 25);
        TextField bank_account_field = makeTextField("Bank Account Number:", PosX, PosY+120, 200, 25);


        Button addPublisherButton = makeButton("Add Publisher", PosX, PosY+150, 200, 40);
        Font arial16 = new Font("Arial", 16);
        addPublisherButton.setFont(arial16);
        Label warningLabel = makeLabel("Please fill out all fields.", PosX, PosY-250, 300,25);
        
        publisherPane.getChildren().addAll(title, instructionLabel,
                publisher_field, email_field, address_field, phone_field, bank_account_field,
                addPublisherButton, warningLabel);

        // Handle Buttons
        addPublisherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String publisher_name = publisher_field.getText();
                String bank_account = bank_account_field.getText();
                String publisher_email = "" + email_field.getText();
                String publisher_address = address_field.getText();
                String publisher_phone_number = phone_field.getText();
                String publisher_balance = "0";

                if(!publisher_name.isEmpty() && !bank_account.isEmpty() && !publisher_email.isEmpty() &&
                        !publisher_address.isEmpty() && !publisher_phone_number.isEmpty()) {
                    // Add to Publisher table
                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                        try (Statement s = connection.createStatement()) {
                            s.executeUpdate(String.format("INSERT INTO publisher VALUES ('%s','%s','%s','%s','%s','%s')",
                                    publisher_name, bank_account, publisher_email, publisher_address,
                                    publisher_phone_number, publisher_balance));
                            System.out.println("Successfully inserted new publisher!");
                        }
                    }
                    catch (Exception e){
                        System.out.println("Add Book Error: " + e);
                    }
                } else {
                    warningLabel.relocate(70, 270);
                }
            }
        });
        return publisherPane;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public javafx.scene.control.Label makeLabel(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Label label = new javafx.scene.control.Label(name);
        label.relocate(posX, posY);
        label.setPrefSize(width, height);
        return label;
    }

    public javafx.scene.control.Button makeButton(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Button button = new javafx.scene.control.Button(name);
        button.relocate(posX, posY);
        button.setPrefSize(width, height);
        return button;
    }
}
