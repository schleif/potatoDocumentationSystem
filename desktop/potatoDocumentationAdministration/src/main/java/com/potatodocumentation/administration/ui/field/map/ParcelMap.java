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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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

    /**
     * All selected parcels
     */
    private ObservableList<Integer> selectedParcels;
    private EventHandler parcelSelectionHandler;
    private final boolean isEditable;

    private int fieldID;
    private List<ParcelBox> parcels;
    private boolean isInSelectionMode = false;
    private final boolean isSelectable;

    private Button updateButton;
    private Button selectButton;
    private Button selectAllButton;
    private Button deselectAllButton;
    private Button editButton;
    private Button deleteButton;
    private HBox editModeBox;
    private HBox buttonBox;

    private VBox parcelBox;

    private Label loadLabel;

    private ImageView dragIcon = new ImageView();

    public ParcelMap(int fieldID, ObservableList<Integer> selectedParcels,
            boolean editable, boolean selectable) {
        super(10);

        this.fieldID = fieldID;

        this.isEditable = editable;
        this.isSelectable = selectable;
        this.parcels = new ArrayList<>();
        this.selectedParcels = selectedParcels;

        updateButton = initUpdateButton();
        selectButton = initSelectButton();
        selectAllButton = initSelectAllButton();
        deselectAllButton = initDeselectAllButton();
        editButton = initEditButton();
        deleteButton = initDeleteButton();
        editModeBox = initEditModeBox();
        buttonBox = initButtonBox();

        parcelBox = new VBox(2);

        loadLabel = initLoadLabel();

        selectedParcels.addListener(
                (ListChangeListener.Change<? extends Integer> c) -> {
                    updateStyle();
                    System.out.println(selectedParcels);
                });

        getChildren().addAll(buttonBox, loadLabel, parcelBox);
        
        //Add margin to all children
        for(Node n : getChildren()){
            VBox.setMargin(n, new Insets(10));
        }
    }

    //Updates the parcels
    //Should run as a new task
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
            rowBox.setAlignment(Pos.CENTER_LEFT);

            params.put("parz_row", row);
            ArrayList<Map<String, Object>> resultArray
                    = getJsonResultArray("selectParzellenByRow.php", params);

            for (Map<String, Object> parcelMap : resultArray) {
                String idString = (String) parcelMap.get("parz_id");
                int id = Integer.parseInt(idString);

                //Create new ParcelBox
                String sorte = (String) parcelMap.get("sorte");
                ParcelBox parcel = newParcelBox(id, sorte);

                rowBox.getChildren().add(parcel);
            }
            //Create an Add Button to create new parcels
            Button addButton = newAddButton(Integer.parseInt(row), true);
            rowBox.getChildren().add(addButton);

            //add the row
            Platform.runLater(() -> parcelBox.getChildren().add(rowBox));
        }

        //Add addButton to new Row 
        int maxRow = 1;
        if (!rows.isEmpty()) {
            maxRow = Integer.parseInt(MiscUtils.getMax(rows));
        }
        Button addButton = newAddButton(++maxRow, false);
        Platform.runLater(() -> parcelBox.getChildren().add(addButton));

        updateStyle();

        indicateLoading(false);

        return null;
    }

    private Button initUpdateButton() {
        Button button = new Button("Aktualisieren");
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateParcelBox());
        });

        return button;
    }

    private Button initEditButton() {
        Button editButton = new Button("Bearbeite markierte");

        return editButton;
    }

    private Button initSelectButton() {
        Button button = new Button("Markieren ist aus");

        button.setOnAction((ActionEvent event) -> {
            onSelectButtonPressed();
        });

        return button;
    }

    private HBox initButtonBox() {
        HBox hBox = new HBox(10, editModeBox, selectButton, selectAllButton,
                deselectAllButton, updateButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.setVisible(isSelectable);
        hBox.setManaged(isSelectable);

        return hBox;
    }

    private void onSelectButtonPressed() {
        isInSelectionMode = !isInSelectionMode;

        editModeBox.setVisible(isEditable && isInSelectionMode);

        //Change text of the select Button
        String buttonString = "Markieren ist "
                + (isInSelectionMode ? "an" : "aus");
        selectButton.setText(buttonString);

        for (ParcelBox parcel : parcels) {
            if (isInSelectionMode) {
                parcel.setOnMouseClicked(parcelSelectionHandler());
            } else {
                parcel.setOnMouseClicked((MouseEvent event) -> {
                });
            }
        }

    }

    private void addDragFeature(ParcelBox parcel) {
        //Only add feature if in Edit mode
        if (!isEditable) {
            return;
        }

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

    private void createNewParcel(int row, int nrRows, int parPerRow,
            boolean fixedRows) {
        CreateNewParcel stage
                = new CreateNewParcel(fieldID, row, nrRows, parPerRow,
                        fixedRows);
        stage.initModality(Modality.APPLICATION_MODAL);

        //Refresh the TaskList after the window is closed
        stage.setOnCloseRequest((WindowEvent event1) -> {
            ThreadUtils.runAsTask(() -> updateParcelBox());
        });
        stage.show();
    }

    private Button newAddButton(int row, boolean fixedRows) {
        Button addButton = new Button("+");
        addButton.setId("createButton");
        addButton.setOnAction((ActionEvent event) -> {
            createNewParcel(row, 1, 1, fixedRows);
        });

        addButton.setVisible(isEditable);
        addButton.setManaged(isEditable);

        return addButton;
    }

    private Label initLoadLabel() {
        Label label = new Label("Lädt...");

        label.setVisible(false);
        label.setManaged(false);

        return label;
    }

    private void indicateLoading(boolean isLoading) {
        Platform.runLater(() -> parcelBox.setManaged(!isLoading));
        Platform.runLater(() -> parcelBox.setVisible(!isLoading));

        Platform.runLater(() -> loadLabel.setManaged(isLoading));
        Platform.runLater(() -> loadLabel.setVisible(isLoading));
    }

    private ParcelBox newParcelBox(int id, String sorte) {
        ParcelBox parcel = new ParcelBox(id, sorte);

        //Add the drag feature
        addDragFeature(parcel);

        //Add parcel to the parcel-list
        parcels.add(parcel);

        return parcel;
    }

    /**
     * Adds/Removes a ParcelBox to/from selectedParcels. Can only be used on
     * instanced of ParcelBox.
     *
     * @return
     */
    private EventHandler<MouseEvent> parcelSelectionHandler() {
        //Singelton
        if (parcelSelectionHandler == null) {

            EventHandler<MouseEvent> eventHandler = (MouseEvent event) -> {
                ParcelBox parcel = (ParcelBox) event.getSource();
                int parcelId = parcel.getParcelId();

                // Add/remove parcel and update style
                if (selectedParcels.contains(parcelId)) {
                    selectedParcels.remove(new Integer(parcelId));
                } else {
                    selectedParcels.add(parcelId);
                }

                System.out.println(selectedParcels);
            };

            parcelSelectionHandler = eventHandler;

        }

        return parcelSelectionHandler;
    }

    //return a list of the IDs of parcels included in this map
    public List<Integer> getParcels() {
        List<Integer> list = new ArrayList<>();
        for (ParcelBox parcel : parcels) {
            list.add(parcel.getParcelId());
        }

        return list;
    }

    private void updateStyle() {
        for (ParcelBox parcel : parcels) {
            if (selectedParcels.contains(parcel.getParcelId())) {
                parcel.getStyleClass().add("marked-parcel-box");
            } else {
                parcel.getStyleClass().clear();
            }
        }
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

    private Button initSelectAllButton() {
        Button button = new Button("Alle auswählen");

        button.setOnAction((ActionEvent event) -> {
            selectAll();
        });

        return button;
    }

    private Button initDeselectAllButton() {
        Button button = new Button("Alle abwählen");

        button.setOnAction((ActionEvent event) -> {
            deselectAll();
        });

        return button;
    }

    public void selectAll() {
        for (ParcelBox parcel : parcels) {
            int id = parcel.getParcelId();
            if (!selectedParcels.contains(id)) {
                selectedParcels.add(id);
            }
        }
        updateStyle();
    }

    public void deselectAll() {
        for (ParcelBox parcel : parcels) {
            selectedParcels.remove(new Integer(parcel.getParcelId()));
        }
        updateStyle();
    }
}
