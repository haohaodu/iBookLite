package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class BookSearchPane extends Pane {
    protected static HashMap<String, Book> bookList;
    private static Statement statement;
    private Stage primaryStage;
    private Pane bookSearchPane;

    public BookSearchPane(Stage stage) {
        primaryStage = stage;
        getBookList();
        bookSearchPane = createBookSearchPane();
    }

    public Pane getBookSearchPane() {
        return bookSearchPane;
    }

    public Pane createBookSearchPane() {
        Pane rightPane = new Pane();
        // Search Field
        TextField searchField = new TextField();
        searchField.relocate(10, 10);
        searchField.setPrefSize(300, 25);

        // Search Button
        Button seachButton = new Button("Search");
        seachButton.setPrefSize(130, 25);

        // Search Type Combo Box
        ObservableList<String> options = FXCollections.observableArrayList ("Search by Title", "Search by Genre", "Search by Author", "Search by ISBN");
        ComboBox searchTypeBox = new ComboBox(options);
        searchTypeBox.setPrefWidth(130);
        searchTypeBox.setValue("Search by Title");

        // Select Book Button
        Button selectBookButton = new Button("Select Book");
        selectBookButton.setPrefSize(130, 25);

        // Position Components
        seachButton.relocate(320, 10);
        searchTypeBox.relocate(320, 50);
        selectBookButton.relocate(320, 90);

        // Book ListView
        ListView<String> bookListView = new ListView<String>();
        bookListView.relocate(10, 45);
        bookListView.setPrefSize(300, 205);

        rightPane.getChildren().addAll(searchField, bookListView, seachButton, selectBookButton, searchTypeBox);

        // Book Search
        selectBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Handle selectBookButton
                String selectedBookTitle = bookListView.getSelectionModel().getSelectedItem().toString();
                Book selectedBook = new Book();

                if(bookList.containsKey(selectedBookTitle)) {
                    selectedBook = bookList.get(selectedBookTitle);
                    System.out.println(selectedBook.ISBN);
                    System.out.println(selectedBook.title);
                    System.out.println(selectedBook.author);
                    System.out.println(selectedBook.genre);
                    System.out.println(selectedBook.publisher_name);
                    System.out.println(selectedBook.price);
                    System.out.println(selectedBook.pages);
                    System.out.println(selectedBook.inventory);
                }

                if(!selectedBookTitle.equals("")) {
                    ObservableList<String> bookDetails = FXCollections.observableArrayList (
                            "ISBN: " + + selectedBook.ISBN,
                            "Title: " + selectedBook.title,
                            "Author: " + selectedBook.author,
                            "Genre: " + selectedBook.genre,
                            "Publisher: " + selectedBook.publisher_name,
                            "Price: " + selectedBook.price,
                            "Number of Pages: " + selectedBook.pages,
                            "Inventory: " + selectedBook.inventory
                    );
                    bookListView.setItems(bookDetails);
                }

                primaryStage.show();
            }
        });

        // Book Search
        seachButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchString = searchField.getText();
                String searchType = searchTypeBox.getValue().toString();

                if(!searchString.isEmpty() && !searchString.equals(" ")) {
                    ArrayList<String> searchList = new ArrayList<String>();
                    if (searchType.equals("Search by Title")) {
                        searchList = searchButton(searchString);
                    } else if (searchType.equals("Search by Author")) {
                        searchList = searchByAuthor(searchString);
                    } else if (searchType.equals("Search by Genre")) {
                        searchList = searchByGenre(searchString);
                    } else if (searchType.equals("Search by ISBN")) {
                        searchList = searchByISBN(searchString);
                    }
                    bookListView.setItems(FXCollections.observableArrayList(searchList));
                    primaryStage.show();
                }
            }
        });

        return rightPane;
    }

    public static void getBookList() {
        bookList = new HashMap <String, Book>(); // user email, pass word, itrerate through the emails, verify it password
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
                    int min_inventory = resultSet.getInt("min_inventory");
                    // Add all books to bookList
                    Book book = new Book(ISBN, author, bookTitle, genre, publisherName, publisherCut, price, pages, inventory, min_inventory);
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
                ResultSet resultSet = statement.executeQuery("SELECT * FROM book WHERE book.ISBN = " + searchString + ";");
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
}
