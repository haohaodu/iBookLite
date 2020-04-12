package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class OptionsPane {
    private Stage primaryStage;
    private GridPane outerGrid;
    private Pane optionsPane;
    public Cart orderCart;

    public OptionsPane(Stage stage, GridPane outerGridPane, Cart cart) {
        primaryStage = stage;
        outerGrid = outerGridPane;
        orderCart = cart;
        optionsPane = createOptionsPane();
    }

    public Pane getOptionsPane() {
        return optionsPane;
    }

    public Pane createOptionsPane() {
        Pane leftPane = new Pane();
        int leftPosX = 20;
        int leftPosY = 0;

        // Create and position the Options Menu
        javafx.scene.control.Label menuLabel = makeLabel("Menu", leftPosX+40, leftPosY+5, 100,25);
        menuLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        javafx.scene.control.Button bookSearchButton = makeButton("Book Search", leftPosX, leftPosY+40, 130, 25);
        javafx.scene.control.Button orderSearchButton = makeButton("Order Search", leftPosX, leftPosY+80, 130, 25);
        javafx.scene.control.Button viewCartButton = makeButton("View Cart", leftPosX, leftPosY+120, 130, 25);

        javafx.scene.control.Button loginButton = makeButton("Admin Login", leftPosX, leftPosY+160, 130, 25);
        javafx.scene.control.Button addBookButton = makeButton("Add Book", leftPosX, leftPosY+200, 130, 25);
        javafx.scene.control.Button removeBookButton = makeButton("Remove Book", leftPosX, leftPosY+240, 130, 25);
        javafx.scene.control.Button viewReportsButton = makeButton("View Reports", leftPosX, leftPosY+280, 130, 25);
        javafx.scene.control.Button newPublisherButton = makeButton("New Publisher", leftPosX, leftPosY+320, 130, 25);

        // Add all the components to the window
        leftPane.getChildren().addAll(menuLabel, bookSearchButton, orderSearchButton, addBookButton, removeBookButton,
                viewReportsButton, loginButton, viewCartButton, newPublisherButton);

        addBookButton.setDisable(true);
        removeBookButton.setDisable(true);
        newPublisherButton.setDisable(true);
        viewReportsButton.setDisable(true);

        // Book Search Button
        bookSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Book Search Button clicked");
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                Pane rightPane = new BookSearchPane(primaryStage, outerGrid, orderCart).getBookSearchPane();
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // Order Search Button
        orderSearchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Order Search Button clicked");
                Pane rightPane = new OrderSearchPane(primaryStage, outerGrid, orderCart).getOrderPane();
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // Add Book Button
        addBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("New Book Button clicked");
                Pane rightPane = new NewBookPane(primaryStage, outerGrid).getNewBookPane();
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // Add Publisher Button
        newPublisherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pane rightPane = new PublisherPane(primaryStage, outerGrid).getPublisherPane();
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // Remove Book Button
        removeBookButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pane rightPane = new RemoveBookPane(primaryStage, outerGrid).getRemoveBookPane();
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // View Reports Button
        viewReportsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Pane rightPane = new ReportsPane(primaryStage).getReportsPane();
                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // Admin Login Button
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addBookButton.setDisable(false);
                newPublisherButton.setDisable(false);
                removeBookButton.setDisable(false);
                viewReportsButton.setDisable(false);

                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                primaryStage.show();

                // rightPane.getChildren().clear();
                Pane rightPane = new LoginPane(primaryStage, outerGrid, orderCart).getLoginPane();
                outerGrid.add(rightPane, 1, 0);
                primaryStage.show();
            }
        });

        // View Cart Button
        viewCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("View Cart Button clicked");
                Pane rightPane = new CartPane(primaryStage, outerGrid, orderCart).getCartPane();
                // Pane secondaryPane = new ShippingPane(primaryStage, outerGrid, orderCart).getShippingPane();

                // Clear the Old Pane
                outerGrid.getChildren().clear();
                outerGrid.add(leftPane, 0, 0);
                outerGrid.add(rightPane, 1, 0);
                // outerGrid.add(secondaryPane, 2, 0);
                primaryStage.show();
            }
        });
        return leftPane;
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
