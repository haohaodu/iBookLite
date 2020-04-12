package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RemoveBookPane {
    protected static HashMap<String, Book> bookList;
    private static Statement statement;
    private Stage primaryStage;
    private Book selectedBook;
    private GridPane outerGridPane;
    public ListView<String> bookListView;
    private Pane removeBookPane;

    public RemoveBookPane(Stage stage, GridPane outerGrid) {
        primaryStage = stage;
        outerGridPane = outerGrid;
        getBookList();
        removeBookPane = createAddBookPane();
    }

    public Pane getRemoveBookPane() {
        return removeBookPane;
    }

    public Pane createAddBookPane() {
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

        // Remove Book Button
        Button removeButton = new Button("Remove Book");
        removeButton.setPrefSize(130, 25);

        // View Cart Button
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setMinWidth(130);
        viewCartButton.setPrefSize(130, 25);

        // Checkout Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setPrefSize(130, 25);

        // Warning label
        Label warningLabel = makeLabel("Book Removed", 90, -50, 300,25);
        warningLabel.setFont(new Font("Arial", 16));

        // Position Components
        quantityLabel.relocate(320, -50);
        removeButton.relocate(320, -50);
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
                removeButton,
                quantityLabel,
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
                    removeButton.setDisable(true);
                    quantityLabel.relocate(320, -50);
                    removeButton.relocate(320, -50);
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

                    // Enable Remove Book
                    removeButton.setDisable(false);
                    removeButton.relocate(320, 130);
                }

                if(!selectedBookTitle.equals("")) {
                    ObservableList<String> bookDetails = FXCollections.observableArrayList (
                            "ISBN: " + + selectedBook.ISBN13,
                            "Title: " + selectedBook.title,
                            "Author: " + selectedBook.author,
                            "Genre: " + selectedBook.genre,
                            "Publisher: " + selectedBook.publisher_name,
                            "Price: $" + selectedBook.price,
                            "Number of Pages: " + selectedBook.pages,
                            "Inventory: " + selectedBook.inventory
                    );
                    bookListView.setItems(bookDetails);
                }
                // Refresh Screen
                primaryStage.show();
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Remove Book Button Click");
                String ISBN13 = selectedBook.ISBN13 + "";
                if(!ISBN13.isEmpty()) {
                    System.out.println("Selected Book ISBN: " + ISBN13);
                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448")) {
                        try (Statement s = connection.createStatement()) {
                            s.executeUpdate(String.format("DELETE FROM book WHERE book.ISBN13 = '%s';",ISBN13));
                            warningLabel.setText("Selected book has been removed.");
                            warningLabel.relocate(30,260);
                            removeButton.setDisable(true);
                        }
                    }
                    catch (Exception e){
                        System.out.println("Remove Book Error: " + e);
                    }
                }

                // Refresh Screen
                primaryStage.show();
            }
        });
        return rightPane;
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

    public ComboBox makeComboBox(String[] lis, Integer posX, Integer posY, Integer width, Integer height){
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(lis));
        combo_box.getSelectionModel().selectFirst();
        combo_box.relocate(posX, posY);
        combo_box.setPrefSize(width,height);
        return combo_box;
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
}