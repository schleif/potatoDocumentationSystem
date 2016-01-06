/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field.map;

import com.potatodocumentation.administration.ui.field.FieldBox;
import com.potatodocumentation.administration.ui.field.ParcelBox;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ochi
 */
public class FieldMap extends VBox {

    private List<FieldBox> fields = new ArrayList<>();
    private ObservableList<Integer> selectedParcels;
    private ObservableList<Integer> selectedFields = FXCollections
            .observableArrayList();

    public ObservableList<Integer> getSelectedParcels() {
        return selectedParcels;
    }

    public ObservableList<Integer> getSelectedFields() {
        return selectedFields;
    }
    private EventHandler fieldSelectionHandler;

    private int maxColumn = 10;
    private boolean isEditable;
    private final boolean isSelectable;
    private boolean isInSelectionMode = false;

    private Label loadLabel;
    private VBox fieldBox;

    private Button updateButton;
    private Button selectButton;
    private Button editButton;
    private Button deleteButton;
    private HBox editModeBox;
    private HBox buttonBox;

    public FieldMap(ObservableList<Integer> selectedParcels, boolean editable,
            boolean selectable) {
        super(10);

        this.selectedParcels = selectedParcels;
        this.isEditable = editable;
        this.isSelectable = selectable;

        loadLabel = new Label("Lädt...");
        fieldBox = new VBox(10);

        updateButton = initUpdateButton();
        selectButton = initSelectButton();
        editButton = initEditButton();
        deleteButton = initDeleteButton();
        editModeBox = initEditModeBox();
        buttonBox = initButtonBox();

        getChildren().addAll(buttonBox, loadLabel, fieldBox);

        ThreadUtils.runAsTask(() -> update());

        selectedParcels.addListener((ListChangeListener.Change<? extends Integer> c) -> {
            updateStyle();
        });

        for (Node node : getChildren()) {
            VBox.setMargin(node, new Insets(10));
        }

    }

    public FieldMap(boolean editable, boolean selectable) {
        //Start new FieldMap with empty selectedParcels list
        this(FXCollections.observableArrayList(), editable, selectable);
    }

    //Should be called as new Task
    public Void update() {

        //Indicate loading
        indicateLoading(true);

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

                FieldBox field = createNewField(Integer.parseInt(fieldNr));

                fields.add(field);
                field.update();
                rowBox.getChildren().add(field);
            }
            //Add the addButton
            rowBox.getChildren().add(initAddButton(rowNr));
            VBox.setMargin(rowBox, new Insets(5));

            Platform.runLater(() -> fieldBox.getChildren().add(rowBox));
        }

        indicateLoading(false);

        updateStyle();

        return null;
    }

    private Button initAddButton(String rowNr) {

        Button addButton = new Button("+");
        addButton.setId("createButton");
        addButton.setOnAction((ActionEvent e) -> {
            onPlusClicked(Integer.parseInt(rowNr));
        });

        //Only show Button if Map is editable
        addButton.setVisible(isEditable);
        addButton.setManaged(isEditable);

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

    private FieldBox createNewField(int fieldNr) {

        ObservableList list = (isEditable ? FXCollections.observableArrayList()
                : selectedParcels);

        FieldBox field = new FieldBox(fieldNr, list, isEditable, isSelectable);

        return field;
    }

    private Button initUpdateButton() {
        Button button = new Button("Aktualisieren");
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> update());
        });

        return button;
    }

    private Button initSelectButton() {
        Button button = new Button("Markieren ist aus");

        button.setOnAction((ActionEvent event) -> {
            onSelectButtonPressed();
        });

        return button;
    }

    private Button initEditButton() {
        Button editButton = new Button("Bearbeite markierte");

        return editButton;
    }

    private Button initDeleteButton() {
        Button button = new Button("Lösche markierte");
        button.setId("deleteButton");

        return button;
    }

    private HBox initEditModeBox() {
        HBox hBox = new HBox(10, deleteButton, editButton);

        hBox.setVisible(false);
        hBox.setManaged(isEditable);

        return hBox;
    }

    private HBox initButtonBox() {
        HBox hBox = new HBox(10, editModeBox, selectButton, updateButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.setVisible(isSelectable);
        hBox.setManaged(isSelectable);

        return hBox;
    }

    private void indicateLoading(boolean isLoading) {
        Platform.runLater(() -> fieldBox.setManaged(!isLoading));
        Platform.runLater(() -> fieldBox.setVisible(!isLoading));

        Platform.runLater(() -> loadLabel.setManaged(isLoading));
        Platform.runLater(() -> loadLabel.setVisible(isLoading));
    }

    private void onSelectButtonPressed() {
        isInSelectionMode = !isInSelectionMode;

        editModeBox.setVisible(isEditable && isInSelectionMode);

        //Change text of the select Button
        String buttonString = "Markieren ist "
                + (isInSelectionMode ? "an" : "aus");
        selectButton.setText(buttonString);

        for (FieldBox field : fields) {
            if (isInSelectionMode) {
                field.setOnMouseClicked(fieldSelectionHandler());
            } else {
                field.setOnMouseClicked(field.openFieldStageHandler());
            }
        }
    }

    /**
     * Adds/Removes a FieldBox to/from selectedField. Can only be used on
     * instances of FieldBox.
     *
     * @return
     */
    private EventHandler<MouseEvent> fieldSelectionHandler() {
        //Singelton
        if (fieldSelectionHandler == null) {

            EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
                FieldBox field = (FieldBox) event.getSource();
                if (!isEditable) {
                    /**
                     * If the map is not editable add all parcels instead of
                     * adding the fields to selectedFields
                     *
                     */
                    if (field.isFullySelected()) {
                        field.getParcel().deselectAll();
                    } else {
                        field.getParcel().selectAll();
                    }
                } else {

                    int fieldID = field.getFieldId();

                    // Add/remove parcel and update style
                    if (selectedFields.contains(fieldID)) {
                        selectedFields.remove(new Integer(fieldID));
                        field.getStyleClass().remove("marked-field");
                    } else {
                        selectedFields.add(fieldID);
                        field.getStyleClass().add("marked-field");
                    }
                }
            };

            fieldSelectionHandler = eventHandler;

        }

        return fieldSelectionHandler;
    }

    public List<FieldBox> getFields() {
        return fields;
    }
}
