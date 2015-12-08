/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field.map;

import com.potatodocumentation.administration.ui.field.ParcelBox;
import com.potatodocumentation.administration.ui.field.CreateNewParcel;
import com.potatodocumentation.administration.utils.AnimationUtils;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultArray;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class ParcelMap extends VBox {

    private int fieldID;
    private List<String> taggedParcels;
    private List<ParcelBox> parcels;
    private List<ParcelBox> selectedParcels;
    private boolean isInSelectionMode = false;

    private Button updateButton;
    private Button editButton;
    private Button selectButton;
    private HBox buttonBox;

    private VBox parcelBox;

    private Label loadLabel;

    private ImageView dragIcon = new ImageView();

    public ParcelMap(int fieldID, List<String> taggedParcels) {
        super(10);

        this.fieldID = fieldID;
        this.taggedParcels
                = (taggedParcels == null ? new ArrayList<>() : taggedParcels);

        this.parcels = new ArrayList<>();
        this.selectedParcels = new ArrayList<>();

        updateButton = initUpdateButton();
        editButton = initEditButton();
        selectButton = initselectButton();
        buttonBox = initButtonBox();

        parcelBox = new VBox(2);

        loadLabel = initLoadLabel();

        getChildren().addAll(buttonBox, loadLabel, parcelBox);
    }

    public Void updateParcelBox() {
        
        indicateLoading(true);
        
        Platform.runLater(() -> parcelBox.getChildren().clear());
        parcels.clear();

        HashMap<String, String> params = new HashMap<>();
        params.put("feld_nr", Integer.toString(fieldID));

        ObservableList<String> rows
                = getJsonResultObservableList("parz_row",
                        "selectParzellenRows.php", params);

        //Iterate trough all rows
        for (String row : rows) {
            HBox rowBox = new HBox(2);

            params.put("parz_row", row);
            ArrayList<Map<String, Object>> resultArray
                    = getJsonResultArray("selectParzellenByRow.php", params);

            for (Map<String, Object> parcelMap : resultArray) {
                String idString = (String) parcelMap.get("parz_id");
                int id = Integer.parseInt(idString);

                //Create new ParcelBox
                String sorte = (String) parcelMap.get("sorte");
                ParcelBox parcel = new ParcelBox(id, sorte);

                //Add the drag feature
                addDragFeature(parcel);

                //Add parcel to the parcel-list
                parcels.add(parcel);

                rowBox.getChildren().add(parcel);
            }
            //Create an Add Button to create new parcels
            Button addButton = newAddButton(fieldID, Integer.parseInt(row));
            rowBox.getChildren().add(addButton);

            Platform.runLater(() -> parcelBox.getChildren().add(rowBox));
        }

        indicateLoading(false);
        
        return null;
    }

    private Button initUpdateButton() {
        Button button = new Button(" ");
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateParcelBox());
        });

        return button;
    }

    private Button initEditButton() {
        Button editButton = new Button("Bearbeite markierte");

        editButton.setVisible(false);

        return editButton;
    }

    private Button initselectButton() {
        Button button = new Button("Markieren");

        button.setOnAction((ActionEvent event) -> {
            onMarkButtonPressed();
        });

        return button;
    }

    private HBox initButtonBox() {
        HBox hBox = new HBox(editButton, selectButton, updateButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        return hBox;
    }

    private void onMarkButtonPressed() {
        isInSelectionMode = !isInSelectionMode;

        editButton.setVisible(isInSelectionMode);

        selectedParcels.clear();

        for (ParcelBox parcel : parcels) {
            parcel.setMarkMode(isInSelectionMode);
        }
    }

    private void addDragFeature(ParcelBox parcel) {
        //The animation to be played with associated parcels     
        Timeline draggedAnimation = AnimationUtils.opacity(parcel);

        //Init the drag n drop event
        parcel.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = parcel.startDragAndDrop(TransferMode.MOVE);

            db.setDragView(parcel.snapshot(null, null));

            //Put the parcels id in the Dragboard
            ClipboardContent content = new ClipboardContent();
            content.putString(Integer.toString(parcel.getParcelId()));
            db.setContent(content);

            draggedAnimation.play();

            event.consume();
        });

        //Only accept Drag n Drop if it's from a different parcel and contains
        //a valid Integer
        parcel.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != parcel) {

                Dragboard db = event.getDragboard();
                if (db.hasString()) {

                    String dbContent = db.getString();
                    if (MiscUtils.isInteger(dbContent)) {

                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        });

        //Change the appereance if a drag n drop enters
        parcel.setOnDragEntered((DragEvent event) -> {
            if (event.getGestureSource() != parcel) {

                Dragboard db = event.getDragboard();
                if (db.hasString()) {

                    String dbContent = db.getString();
                    if (MiscUtils.isInteger(dbContent)) {

                        draggedAnimation.play();
                    }
                }
            }
            event.consume();
        });

        //change the appereance back if the drag n drop exits
        parcel.setOnDragExited((DragEvent event) -> {
            if (event.getGestureSource() != parcel) {
                draggedAnimation.jumpTo(Duration.ZERO);
                draggedAnimation.stop();
            }

            event.consume();
        });

        //Switch the parcels
        parcel.setOnDragDropped((DragEvent event) -> {
            String dbContent = event.getDragboard().getString();

            ThreadUtils.runAsTask(() -> switchParcels(parcel.getParcelId(),
                    Integer.parseInt(dbContent)));

            event.setDropCompleted(true);

            event.consume();
        });

        //Change appereance of the source back if drag n drop finished
        parcel.setOnDragDone((DragEvent event) -> {
            draggedAnimation.jumpTo(Duration.ZERO);
            draggedAnimation.stop();
        });
    }

    private Void switchParcels(int parA, int parB) {
        indicateLoading(true);
        
        HashMap params = new HashMap();
        params.put("parA", Integer.toString(parA));
        params.put("parB", Integer.toString(parB));

        boolean success = getJsonSuccessStatus("switchParzellen.php", params);

        ThreadUtils.runAsTask(() -> updateParcelBox());
        
        return null;
    }

    private void createNewParcel(int fieldID, int row, int nrRows, int parPerRow) {
        CreateNewParcel stage
                = new CreateNewParcel(fieldID, row, nrRows, parPerRow);
        stage.initModality(Modality.APPLICATION_MODAL);

        //Refresh the TaskList after the window is closed
        stage.setOnCloseRequest((WindowEvent event1) -> {
            ThreadUtils.runAsTask(() -> updateParcelBox());
        });
        stage.show();
    }

    private Button newAddButton(int fieldID, int row) {
        Button addButton = new Button("+");
        addButton.setId("createButton");
        addButton.setOnAction((ActionEvent event) -> {
            createNewParcel(fieldID, row, 1, 1);
        });

        return addButton;
    }

    private Label initLoadLabel() {
        Label label = new Label("Lädt...");
        label.setVisible(false);
        
        return label;
    }
    
    private void indicateLoading(boolean isLoading){
        Platform.runLater(() -> parcelBox.setVisible(!isLoading));
        Platform.runLater(() -> loadLabel.setVisible(isLoading));    
    }
}
