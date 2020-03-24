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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class LoginPane extends Application{
    public static HashMap<Long, Book> bookHashMap = new HashMap<Long,Book>();
    public Cart cart = new Cart();

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        // cart section
        int cartPosY = 10;
        int cartPosX = 10;
        Pane loginPane = new Pane();

        // temporary dummy code
        cart.add((long) 5123, 3);
        cart.add((long) 4212, 5);
        cart.add((long) 4134, 1);

        bookHashMap.put((long)5123, new Book((long)5123, "R.L. Stine", "GooseBumps", "Horror", "Scholastic", 70, (float)199.99, 346, 50, 10));
        bookHashMap.put((long)4212, new Book((long)4212, "R.L. Stine1", "GooseBumps", "Horror", "Scholastic", 70, (float)199.99, 346, 50, 10));
        bookHashMap.put((long)4134, new Book((long)4134, "R.L. Stine2", "GooseBumps", "Horror", "Scholastic", 70, (float)199.99, 346, 50, 10));

        Label cartLabel = makeLabel ("Cart Screen", cartPosX, cartPosY, 200, 10);

        float cartPrice = 0;
        for(int i = 0; i < cart.size(); i++){
            Book element = bookHashMap.get((long)5123);
            Label cartElement = makeLabel(String.format("%s\t\t%s\t x %s: \t%s", element.ISBN, element.title, element.inventory, Float.toString(element.inventory*element.price)), cartPosX,cartPosY+40 ,300,10);
            loginPane.getChildren().add(cartElement);
            cartPosY+=40;
            cartPrice+=element.inventory*element.inventory;
        }

        loginPane.getChildren().addAll(
                cartLabel);
        // Primary Stage
        primaryStage.setTitle("User Screen"); // Set title of window
        primaryStage.setScene(new Scene(loginPane, 1285,605));
        primaryStage.show();
    }
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
}
