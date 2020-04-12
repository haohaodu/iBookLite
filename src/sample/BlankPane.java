package sample;

import java.awt.TextArea;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class BlankPane extends Pane {
    private Pane blankPane;

    public BlankPane() {
        blankPane = createBlankPane();
    }

    public Pane getBlankPane() {
        return blankPane;
    }

    public Pane createBlankPane() {
        Pane rightPane = new Pane();
        rightPane.setMinSize(300,300);
        // Search Field
/*        TextField searchField = new TextField();
        searchField.relocate(10, 10);
        searchField.setPrefSize(300, 25);

        // Book ListView
        ListView<String> bookListView = new ListView<String>();
        bookListView.relocate(10, 45);
        bookListView.setPrefSize(300, 205);


        ArrayList<String> searchList = new ArrayList<String>();
        bookListView.setItems(FXCollections.observableArrayList(searchList));

        rightPane.getChildren().addAll(
                searchField,
                bookListView);*/

        return rightPane;
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