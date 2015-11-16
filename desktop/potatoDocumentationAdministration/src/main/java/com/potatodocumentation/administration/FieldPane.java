/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.potatodocumentation.administration.utils.JsonUtils;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author fiel
 */
public class FieldPane extends VBox {

    private Label title;
    private Button updateButton;
    private AnchorPane header;
    private VBox fieldBox;
    private ScrollPane scrollPane;

    public FieldPane() {
        super(10);

        title = initTitle();
        updateButton = initUpdateButton();
        header = initHeader();
        fieldBox = initFieldBox();
        scrollPane = initScrollPane();

        getChildren().add(header);
        getChildren().add(scrollPane);

        ThreadUtils.runAsTask(() -> updateFieldBox());
    }

    private void newField(int index) {
        String text = Integer.toString(index);
        Button but = new Button(text);
        but.setStyle("-fx-font-size: 100");
        this.getChildren().add(0, but);
    }

    private void onPlusClicked(int rowNr) {
        //add new field
        addField(rowNr);

        //UpdateList
        ThreadUtils.runAsTask(() -> updateFieldBox());
    }

    private void addField(Integer rowNr) {

        HashMap<String, String> params = new HashMap<>();
        params.put("row_nr", rowNr.toString());

        boolean success = getJsonSuccessStatus("insertFeldIntoRow.php", params);

        String status = "Feld wurde " + (success ? "" : "nicht ")
                + "erfolgreich eingefügt!";

        if (success) {
            new Alert(Alert.AlertType.INFORMATION, status).showAndWait()
                    .filter(response -> response == ButtonType.OK);
        } else {
            new Alert(Alert.AlertType.ERROR).showAndWait()
                    .filter(response -> response == ButtonType.OK);
        }
    }

    private Void updateFieldBox() {

        Platform.runLater(() -> fieldBox.getChildren().clear());

        //Get all rows
        ObservableList<String> rows = getJsonResultObservableList(
                "row_nr", "selectFeldRows.php", null);

        //Add one empty row at the end
        int rowSize = rows.size(), maxRow;
        if (rowSize == 0) {
            maxRow = rowSize;
        } else {
            maxRow = Integer.parseInt(rows.get(rows.size() - 1));
        }
        rows.add("" + (++maxRow));

        //Go through all rows and add all fields of the row
        for (String rowNr : rows) {

            HBox rowBox = new HBox(10);
            rowBox.setId("rowBox");
            rowBox.setAlignment(Pos.CENTER_LEFT);

            //Get all field of this row
            HashMap<String, String> params = new HashMap<>();
            params.put("row_nr", rowNr);

            //add label with the rowNr at the beginning of the row
            Label rowLabel = new Label(rowNr);
            rowLabel.setId("rowLabel");
            rowLabel.setRotate(270);
            rowBox.getChildren().add(rowLabel);

            ObservableList<String> fieldsOfRow = getJsonResultObservableList(
                    "feld_id", "selectFeldByRow.php", params);

            //Add all fields to rowBox
            for (String field : fieldsOfRow) {
                //Numbers are going to have at least 2 digits
                Button fieldButton
                        = new Button(String.format("%2s", field).replace(" ", "0"));
                fieldButton.setId("fieldButton");

                rowBox.getChildren().add(fieldButton);
            }
            //Add the addButton
            rowBox.getChildren().add(initAddButton(rowNr));
            VBox.setMargin(rowBox, new Insets(5));
            
            Platform.runLater(() -> fieldBox.getChildren().add(rowBox));
        }

        return null;
    }

    private Button initAddButton(String rowNr) {
        Button addButton = new Button("+");
        addButton.setId("createButton");
        addButton.setOnAction((ActionEvent e) -> {
            onPlusClicked(Integer.parseInt(rowNr));
        });

        return addButton;
    }

    private AnchorPane initHeader() {
        AnchorPane anchorPane = new AnchorPane(title, updateButton);

        AnchorPane.setLeftAnchor(title, 10.0);
        AnchorPane.setRightAnchor(updateButton, 10.0);

        return anchorPane;
    }

    private Label initTitle() {
        return new Label("Felder");
    }

    private Button initUpdateButton() {

        Image updateIcon = new Image(getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateFieldBox());

            //Rotate the icon
            RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;

    }

    private VBox initFieldBox() {
        VBox vBox = new VBox(10);
        
        return vBox;
    }

    private ScrollPane initScrollPane() {
        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setContent(fieldBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setId("scrollPane");

        return scrollPane;
    }

}
