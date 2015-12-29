/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field.map;

import com.potatodocumentation.administration.ui.field.FieldBox;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ochi
 */
public class FieldMap extends ScrollPane {

    private List<FieldBox> fields = new ArrayList<>();
    private ObservableList<Integer> selectedParcels = FXCollections
            .observableArrayList();

    private int maxColumn = 10;
    private boolean smallMode;

    private Label loadLabel;
    private VBox fieldBox;

    public FieldMap(ObservableList<Integer> selectedParcels) {
        this();

        this.selectedParcels = selectedParcels;

        selectedParcels.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            updateStyle();
        });

    }

    public FieldMap() {

        this.smallMode = smallMode;

        loadLabel = new Label("Lädt...");
        fieldBox = new VBox(10);

        ThreadUtils.runAsTask(() -> update());

        selectedParcels.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            updateStyle();
        });

    }

    //Should be called as new Task
    public Void update() {

        //Indicate loading by setting the load Label as content
        Platform.runLater(() -> setContent(loadLabel));

        Platform.runLater(() -> fieldBox.getChildren().clear());
        fields.clear();

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
        rows.add(Integer.toString(++maxRow));

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
            rowLabel.setRotate(270);
            rowBox.getChildren().add(rowLabel);

            ObservableList<String> fieldsOfRow = getJsonResultObservableList(
                    "feld_id", "selectFeldByRow.php", params);

            //Add all fields to rowBox
            for (String fieldNr : fieldsOfRow) {

                FieldBox field = new FieldBox(Integer.parseInt(fieldNr),
                        selectedParcels);

                fields.add(field);
                field.update();
                rowBox.getChildren().add(field);
            }
            //Add the addButton
            rowBox.getChildren().add(initAddButton(rowNr));
            VBox.setMargin(rowBox, new Insets(5));

            Platform.runLater(() -> fieldBox.getChildren().add(rowBox));
        }

        Platform.runLater(() -> setContent(fieldBox));

        updateStyle();

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

    private void onPlusClicked(int rowNr) {
        //add new field
        addField(rowNr);
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
            //Update List on success
            ThreadUtils.runAsTask(() -> update());
        } else {
            new Alert(Alert.AlertType.ERROR).showAndWait()
                    .filter(response -> response == ButtonType.OK);
        }
    }

    /**
     * Updates the style of all FieldBoxes in fields, depending on how many of
     * the parcels of the field are contained in selectedParcels
     */
    private void updateStyle() {
        for (FieldBox field : fields) {
            Boolean isSubset = MiscUtils.isSubset(selectedParcels,
                    field.getParcels());

            if (isSubset == null) {
                field.setId("partialSelectedFieldBox");
            } else if (isSubset) {
                field.setId("selectedFieldBox");
            } else {
                field.setId(null);
            }
        }
    }
}
