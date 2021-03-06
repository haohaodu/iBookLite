package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BookSearchPane extends Pane {
    protected static HashMap<String, Book> bookList;
    private static Statement statement;
    private Stage primaryStage;
    private Pane bookSearchPane;
    private Book selectedBook;
    private Cart orderCart;
    private GridPane outerGridPane;
    public ListView<String> bookListView;

    public BookSearchPane(Stage stage, GridPane outerGrid, Cart cart) {
        primaryStage = stage;
        orderCart = cart;
        outerGridPane = outerGrid;
        getBookList();
        bookSearchPane = createBookSearchPane();
    }

    public Pane getBookSearchPane() {
        return bookSearchPane;
    }

    public Pane createBookSearchPane() {
        Pane rightPane = new Pane();
        // Book Search
        TextField searchField = makeTextField("Search for Book", 10, 10, 300, 25);
        Button searchButton = makeButton("Search", 320, 10, 130, 25);

        // Search Type Combo Box
        String searchType[] = { "Search by Title", "Search by Genre", "Search by Author", "Search by ISBN" };
        ComboBox searchTypeBox = makeComboBox(searchType, 320, 50, 130, 25);

        // Select Book Button
        Button selectBookButton = makeButton("Select Book", 320, 90, 130, 25);

        // Quantity Label
        Text quantityLabel = new Text("Quantity:");
        quantityLabel.setFont(Font.font("Arial", FontWeight.BOLD,12));

        // Quantity TextField
        TextField qtyField = new TextField();
        qtyField.setPrefSize(100, 25);

        // Add to Cart Button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setPrefSize(130, 25);

        // View Cart Button
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setMinWidth(130);
        viewCartButton.setPrefSize(130, 25);

        // Checkout Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setPrefSize(130, 25);

        // Warning label
        Label warningLabel = makeLabel("Not enough inventory!", 90, -50, 200,25);
        warningLabel.setFont(new Font("Arial", 16));

        // Position Components
        quantityLabel.relocate(320, -50);
        qtyField.relocate(320, -50);
        addToCartButton.relocate(320, -50);
        viewCartButton.relocate(320,-50);
        checkoutButton.relocate(320, -50);

        // Book ListView
        bookListView = new ListView<String>();
        bookListView.relocate(10, 45);
        bookListView.setPrefSize(300, 205);

        rightPane.getChildren().addAll(
                searchField,
                searchTypeBox,
                bookListView,
                searchButton,
                selectBookButton,
                addToCartButton,
                quantityLabel,
                qtyField,
                viewCartButton,
                warningLabel,
                checkoutButton);

        // Book Search
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchString = searchField.getText();
                String searchType = searchTypeBox.getValue().toString();
                ArrayList<String> searchList = new ArrayList<String>();
                if(!searchString.isEmpty() && !searchString.equals(" ")) {
                    if (searchType.equals("Search by Title")) {
                        searchList = searchButton(searchString);
                    } else if (searchType.equals("Search by Author")) {
                        searchList = searchByAuthor(searchString);
                    } else if (searchType.equals("Search by Genre")) {
                        searchList = searchByGenre(searchString);
                    } else if (searchType.equals("Search by ISBN")) {
                        searchList = searchByISBN(searchString);
                    }
                    selectBookButton.setDisable(false);
                    addToCartButton.setDisable(true);
                    quantityLabel.relocate(320, -50);
                    qtyField.relocate(320, -50);
                    addToCartButton.relocate(320, -50);
                }
                bookListView.setItems(FXCollections.observableArrayList(searchList));
                primaryStage.show();

            }
        });

        // Select Book
        selectBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Handle selectBookButton
                String selectedBookTitle = bookListView.getSelectionModel().getSelectedItem().toString();
                selectedBook = new Book();

                if(bookList.containsKey(selectedBookTitle)) {
                    selectedBook = bookList.get(selectedBookTitle);
                    // Disable Select Book Button
                    selectBookButton.setDisable(true);
                    addToCartButton.setDisable(false);

                    // Enable Add to Cart
                    quantityLabel.relocate(320, 130);
                    qtyField.relocate(320, 145);
                    addToCartButton.relocate(320, 180);
                }

                if(!selectedBookTitle.equals("")) {
                    // Format Book Price to 2 Decimal Places
                    String bookPrice = String.format("%.2f", selectedBook.price);
                    ObservableList<String> bookDetails = FXCollections.observableArrayList (
                            "ISBN: " + + selectedBook.ISBN13,
                            "Title: " + selectedBook.title,
                            "Author: " + selectedBook.author,
                            "Genre: " + selectedBook.genre,
                            "Publisher: " + selectedBook.publisher_name,
                            "Price: $" + bookPrice,
                            "Number of Pages: " + selectedBook.pages,
                            "Inventory: " + selectedBook.inventory
                    );
                    bookListView.setItems(bookDetails);
                }
                // Refresh Screen
                primaryStage.show();
            }
        });

        // Add to Cart
        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Add Book to Cart if Quantity Field is not empty
                if(!qtyField.getText().isEmpty() && isNumeric(qtyField.getText())) {
                    int quantity = Integer.parseInt(qtyField.getText());
                    int inventory = selectedBook.inventory;
                    if(quantity > inventory) {
                        // Warning Message: Not enough inventory!
                        warningLabel.relocate(90, 260);
                    } else {
                        warningLabel.relocate(90, -50);
                        System.out.println("Added to Cart");
                        orderCart.add(selectedBook.ISBN13, Integer.parseInt(qtyField.getText()));
                        ObservableList<String> cartDetails = FXCollections.observableArrayList (
                                selectedBook.title,
                                "Quantity: " + qtyField.getText() + " Added to Cart");
                        bookListView.setItems(cartDetails);
                        addToCartButton.setDisable(true);
                        qtyField.setText("");
                        viewCartButton.relocate(320,210);
                        checkoutButton.relocate(320, 240);
                    }
                }

                // Refresh Screen
                primaryStage.show();
            }
        });

        // View Cart Button
        viewCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("View Cart Button clicked");

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
                System.out.println("Final Cart");
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
                bookListView.setItems(cartDetails);

                // Refresh Screen
                primaryStage.show();
            }
        });

        // Checkout Button
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Checkout Button Click");
                rightPane.getChildren().clear();
                // System.out.println(orderCart);
                Pane loginPane = new LoginPane(primaryStage, outerGridPane, orderCart).getLoginPane();
                outerGridPane.add(loginPane, 1, 0);
                // Refresh Screen
                primaryStage.show();
            }
        });

        return rightPane;
    }

    public static void getBookList() {
        bookList = new HashMap <String, Book>();
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement s = connection.createStatement()) {
                ResultSet resultSet = s.executeQuery("SELECT * FROM book");
                while (resultSet.next()) {
                    long ISBN = resultSet.getLong("isbn13");
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
                    Book book = new Book(ISBN, author, bookTitle, genre, publisherName, publisherCut, price, pages, inventory, minInventory);
                    bookList.put(bookTitle, book);
                }
                statement = s;
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
    }

    private static ArrayList<String> searchButton(String title) {
        ArrayList<String> searchList = new ArrayList<String>();
        String searchString = "'%" + title.toLowerCase().replace("'", "''") + "%'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE lower(title) like " + searchString + ";");
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("title");
                    searchList.add(bookTitle);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return searchList;
    }

    private static ArrayList<String> searchByGenre(String genre) {
        ArrayList<String> searchList = new ArrayList<String>();
        String searchString = "'%" + genre.toLowerCase() + "%'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE lower(genre) like " + searchString + ";");
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

    private static ArrayList<String> searchByAuthor(String author) {
        ArrayList<String> searchList = new ArrayList<String>();
        String searchString = "'%" + author.toLowerCase() + "%'";
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE lower(author) like " + searchString + ";");
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

    // Check if Quantity is Numeric
    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public Button makeButton(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Button button = new javafx.scene.control.Button(name);
        button.relocate(posX, posY);
        button.setPrefSize(width, height);
        return button;
    }

    public Label makeLabel(String name, Integer posX, Integer posY, Integer width, Integer height){
        Label label = new Label(name);
        label.relocate(posX, posY);
        label.setPrefSize(width, height);
        return label;
    }

    public ComboBox makeComboBox(String[] lis, Integer posX, Integer posY, Integer width, Integer height){
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(lis));
        combo_box.getSelectionModel().selectFirst();
        combo_box.relocate(posX, posY);
        combo_box.setPrefSize(width,height);
        return combo_box;
    }
}
