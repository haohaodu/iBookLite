package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class ReportsPane {
    private static Statement statement;
    private Stage primaryStage;
    private Pane reportsPane;
    ListView<String> reportListView;

    public ReportsPane(Stage stage) {
        primaryStage = stage;
        reportsPane = createReportsPane();
    }

    public Pane getReportsPane() {
        return reportsPane;
    }

    public Pane createReportsPane() {
        Pane rightPane = new Pane();
        int PosX = 130;
        int PosY = 5;

        Label title = makeLabel ("View Reports", PosX, PosY, 400, 10);
        title.setFont(Font.font("Arial", FontWeight.BOLD,20));

        // Search Type Combo Box
        PosX = 10;
        PosY = 40;
        String searchType[] = { "Sales vs. Expenditures", "Sales by Genre", "Sales by Author", "Sales by Publisher" };
        ComboBox searchTypeBox = makeComboBox(searchType, PosX+20, PosY, 180, 25);
        // Select Report Button
        Button reportButton = makeButton("Run Report", PosX+220, PosY, 120, 25);

        // Warning label
        Label warningLabel = makeLabel("Book Removed", 90, -50, 300,25);
        warningLabel.setFont(new Font("Arial", 16));

        // Book ListView
        reportListView = new ListView<String>();
        reportListView.relocate(PosX, 80);
        reportListView.setPrefSize(380, 205);

        rightPane.getChildren().addAll(
                title,
                searchTypeBox,
                reportListView,
                reportButton,
                warningLabel);

        // Book Search
        reportButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String searchType = searchTypeBox.getValue().toString();
                ArrayList<String> reportList = new ArrayList<String>();

                if (searchType.equals("Sales vs. Expenditures")) {
                    reportList = salesVsExpenditures();
                } else if (searchType.equals("Sales by Genre")) {
                    reportList = reportByGenre();
                } else if (searchType.equals("Sales by Author")) {
                    reportList = reportByAuthor();
                } else if (searchType.equals("Sales by Publisher")) {
                    reportList = reportByPublisher();
                }
                reportListView.setItems(FXCollections.observableArrayList(reportList));
                primaryStage.show();
            }
        });
        
        return rightPane;
    }

    // Sales Vs. Expenses Report
    private static ArrayList<String> salesVsExpenditures() {
        ArrayList<String> searchList = new ArrayList<String>();
        searchList.add("   Sales vs. Expenditures:");
        float totalSales = 0.0f;
        float totalExpenses = 0.0f;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT SUM(order_total) FROM orders;");
                // Total Sales
                while (resultSet.next()) {
                    totalSales = resultSet.getFloat("sum");
                }
                // Total Expenditures
                resultSet = statement.executeQuery("SELECT SUM(publisher.publisher_balance) FROM publisher;");
                while (resultSet.next()) {
                    totalExpenses = resultSet.getFloat("sum");
                }

            }
        } catch(SQLException error) {
            System.out.println("Sales vs. Expenditures: " + error);
        }

        // Update ListView
        String stringTotalSales = String.format("%.2f", totalSales);
        String stringTotalExpenses = String.format("%.2f", totalExpenses);
        Float netProfit = totalSales - totalExpenses;
        String stringNetProfit = String.format("%.2f", netProfit);
        searchList.add("         Total Sales  = $"+stringTotalSales);
        searchList.add("Total Expenses  = $"+stringTotalExpenses);
        searchList.add("           Net Profit   = $"+stringNetProfit);

        return searchList;
    }

    private static ArrayList<String> reportByGenre() {
        ArrayList<String> searchList = new ArrayList<String>();
        searchList.add("Sales by Genre:");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT book.genre, SUM(orders.order_total) FROM orders, book WHERE orders.ISBN13 = book.ISBN13 GROUP BY book.genre;");
                while (resultSet.next()) {
                    String genre = resultSet.getString("genre");
                    System.out.println("Genre: " + genre);
                    Float total = resultSet.getFloat("sum");
                    System.out.printf("Total: %.2f", total);
                    String stringTotal = String.format("%.2f", total);
                    searchList.add(genre + " = $"+stringTotal);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return searchList;
    }

    private static ArrayList<String> reportByAuthor() {
        ArrayList<String> searchList = new ArrayList<String>();
        searchList.add("Sales by Author:");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT book.author, SUM(orders.order_total) FROM orders, book WHERE orders.ISBN13 = book.ISBN13 GROUP BY book.author;");
                while (resultSet.next()) {
                    String author = resultSet.getString("author");
                    System.out.println("Author: " + author);
                    Float total = resultSet.getFloat("sum");
                    System.out.printf("Total: %.2f", total);
                    String stringTotal = String.format("%.2f", total);
                    searchList.add(author + " = $"+stringTotal);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return searchList;
    }

    private static ArrayList<String> reportByPublisher() {
        ArrayList<String> searchList = new ArrayList<String>();
        searchList.add("Sales by Publisher:");
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "sS043250448"))
        {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("SELECT book.publisher_name, SUM(orders.order_total) FROM orders, book WHERE orders.ISBN13 = book.ISBN13 GROUP BY book.publisher_name;");
                while (resultSet.next()) {
                    String publisher = resultSet.getString("publisher_name");
                    System.out.println("Publisher: " + publisher);
                    Float total = resultSet.getFloat("sum");
                    System.out.printf("Total: %.2f", total);
                    String stringTotal = String.format("%.2f", total);
                    searchList.add(publisher + " = $"+stringTotal);
                }
            }
        }
        catch(SQLException error) {
            System.out.println("Could not create connection." + error);
        }
        return searchList;
    }

    public ComboBox makeComboBox(String[] lis, Integer posX, Integer posY, Integer width, Integer height){
        ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(lis));
        combo_box.getSelectionModel().selectFirst();
        combo_box.relocate(posX, posY);
        combo_box.setPrefSize(width,height);
        return combo_box;
    }

    public TextField makeTextField(String name, Integer posX, Integer posY, Integer width, Integer height){
        TextField makeText = new TextField();
        makeText.setPromptText(name);
        makeText.relocate(posX, posY);
        makeText.setPrefSize(width, height);
        return makeText;
    }

    public Label makeLabel(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Label label = new javafx.scene.control.Label(name);
        label.relocate(posX, posY);
        label.setPrefSize(width, height);
        return label;
    }

    public Button makeButton(String name, Integer posX, Integer posY, Integer width, Integer height){
        javafx.scene.control.Button button = new javafx.scene.control.Button(name);
        button.relocate(posX, posY);
        button.setPrefSize(width, height);
        return button;
    }
}
