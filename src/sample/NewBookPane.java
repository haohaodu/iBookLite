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

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class NewBookPane {
    protected static ArrayList<Long> bookList;
    protected static HashSet<String> publisherList;
    private static Statement statement;
    private Pane addBookPane;

    public NewBookPane(Stage stage, GridPane outerGrid) {
        getBookList();
        addBookPane = createAddBookPane();
    }

    public Pane getNewBookPane() {
        return addBookPane;
    }

    public Pane createAddBookPane() {
        addBookPane = new Pane();
        int PosX = 20;
        int PosY = 10;
        // Title
        Label title = makeLabel ("Add Book to Bookstore", PosX+10, PosY, 400, 10);
        title.setFont(Font.font("Arial", FontWeight.BOLD,20));

        // Instruction Label
        Label instructionLabel = makeLabel("Please enter the following information to add a book:", PosX, PosY+30, 400,25);
        instructionLabel.setFont(Font.font("Arial",14));

        // Book Information
        int bookPosX = 20;
        int bookPosY = 70;
        TextField title_field = makeTextField("Book Title:", bookPosX, bookPosY, 200, 25);
        TextField author_field = makeTextField("Author:", bookPosX, bookPosY+30, 200, 25);
        TextField genre_field = makeTextField("Genre:", bookPosX, bookPosY+60, 200, 25);
        TextField ISBN_field = makeTextField("ISBN Number (13 digits):", bookPosX, bookPosY+90, 200, 25);
        TextField pages_field = makeTextField("Number of Pages:", bookPosX, bookPosY+120, 200, 25);

        bookPosX = 250;
        TextField publisher_field = makeTextField("Publisher Name:", bookPosX, bookPosY, 200, 25);
        TextField cut_field = makeTextField("Publisher's Cut (percentage):", bookPosX, bookPosY+30, 200, 25);
        TextField price_field = makeTextField("Book Price:", bookPosX, bookPosY+60, 200, 25);
        TextField inventory_field = makeTextField("Inventory:", bookPosX, bookPosY+90, 200, 25);
        TextField minInventory_field = makeTextField("Minimum Inventory:", bookPosX, bookPosY+120, 200, 25);

        bookPosX = 20;
        Button addBookButton = makeButton("Add Book", bookPosX+120, bookPosY+160, 200, 40);
        Font arial16 = new Font("Arial", 16);
        addBookButton.setFont(arial16);
        Label warningLabel = makeLabel("Please fill out all fields.", bookPosX, bookPosY-250, 350,25);
        Font arial14 = new Font("Arial", 14);
        warningLabel.setFont(arial14);

        addBookPane.getChildren().addAll(title, instructionLabel,
                title_field, author_field, genre_field, ISBN_field, pages_field,
                publisher_field, cut_field, price_field, inventory_field, minInventory_field,
                addBookButton, warningLabel);

        // Handle Buttons
        addBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ISBN13 = ISBN_field.getText();
                Long ISBN = Long.parseLong(ISBN13);
                String title = title_field.getText();
                String author = "" + author_field.getText();
                String genre = genre_field.getText();
                String publisherName = publisher_field.getText();
                String publisherCut = cut_field.getText();

                String bookPrice = price_field.getText();
                String numPages = pages_field.getText();
                String inventory = inventory_field.getText();
                String minInventory = minInventory_field.getText();

                if(!ISBN13.isEmpty() && !title.isEmpty() && !author.isEmpty() && !genre.isEmpty() &&
                        !publisherName.isEmpty() && !publisherCut.isEmpty() && !bookPrice.isEmpty() &&
                        !numPages.isEmpty() && !inventory.isEmpty()) {

                    if(bookList.contains(ISBN)) {
                        warningLabel.setText("Book already exists. Please add a different book.");
                        warningLabel.relocate(80, 270);
                    } else if(!publisherList.contains(publisherName)) {
                        warningLabel.setText("Publisher not found in database. Please add new publisher");
                        warningLabel.relocate(50, 270);
                    } else {
                        // Add to Users table
                        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                            try (Statement s = connection.createStatement()) {
                                // Format Publisher Cut
                                publisherCut = formatPublisherCut(publisherCut, warningLabel);
                                s.executeUpdate(String.format("INSERT INTO book VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                                        ISBN13, author, title, genre, publisherName, publisherCut,
                                        bookPrice, numPages, inventory, minInventory));
                                warningLabel.setText("Successfully Added New Book!");
                                warningLabel.relocate(120,270);
                            }
                        }
                        catch (Exception e){
                            System.out.println("Add Book Error: " + e);
                        }
                    }
                } else {
                    warningLabel.relocate(60, 270);
                }
            }
        });
        return addBookPane;
    }

    public String formatPublisherCut(String input, Label warningLabel) {
        String publisherCut = input.replace("%","");
        Float pc = Float.parseFloat(publisherCut);
        if(pc <= 1.00 && pc > 0) {
            pc = Math.round(pc * 10000)/100.0f;
        } else if (pc > 1) {
            pc = Math.round(pc * 100)/100.0f;
        } else {
            warningLabel.setText("Please use percentage for publisher's cut.");
        }
        publisherCut = pc + "";
        return publisherCut;
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

    public static void getBookList() {
        bookList = new ArrayList<Long>();
        publisherList = new HashSet<String>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery("SELECT * FROM book");
                while (resultSet.next()) {
                    Long ISBN = resultSet.getLong("isbn13");
                    bookList.add(ISBN);
                    String publisher = resultSet.getString("publisher_name");
                    publisherList.add(publisher);
                }
                statement = s;
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
    }
}

