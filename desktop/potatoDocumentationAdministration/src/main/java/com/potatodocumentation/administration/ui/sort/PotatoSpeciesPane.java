/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.sort;

import com.potatodocumentation.administration.ui.field.map.FieldMap;
import static com.potatodocumentation.administration.utils.JsonUtils.*;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.Collection;
import java.util.HashMap;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class PotatoSpeciesPane extends HBox {

    Label parcelLabel;
    Label speciesLabel;
    Label detailLabel;
    ListView<String> speciesList;
    VBox detailBox;
    VBox speciesBox;
    ScrollPane mapPane;
    FieldMap map;
    AnchorPane speciesBoxHeader;
    AnchorPane detailBoxHeader;
    Button updateButton;
    Button deleteButton;
    Button createButton;
    private String activeSpecies;

    public PotatoSpeciesPane() {
        super(10);
        setFillHeight(true);

        //Initialize nodes
        //SpeciesBox
        speciesLabel = initSpeciesLabel();
        updateButton = initUpdateButton();
        speciesList = initSpeciesList();
        createButton = initCreateButton();
        speciesBoxHeader = initSpeciesBoxHeader();
        speciesBox = initSpeciesBox();

        //OptionBox
        detailLabel = initDetailLabel();
        deleteButton = initDeleteButton();
        detailBoxHeader = initDetailBoxHeader();

        //DetailBox
        parcelLabel = initParcelLabel();
        mapPane = initMapPane();
        detailBox = initDetailBox();

        initLayout();

        //Add needed children
        getChildren().add(speciesBox);
        getChildren().add(detailBox);

        //Update TaskList on startup
        ThreadUtils.runAsTask(() -> updateSpeciesList());

    }

    private ListView<String> initParcelList() {

        ObservableList<String> items = FXCollections.observableArrayList("Keine Sorte ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;

    }

    private Label initParcelLabel() {
        return new Label("Verknüpfte Parzellen:");
    }

    private Label initSpeciesLabel() {
        return new Label("Eigenschaften:");
    }

    private ListView<String> initSpeciesList() {

        ListView<String> listView = new ListView<>();

        //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty()
                .addListener(
                        (ObservableValue<? extends String> observable,
                                String oldValue,
                                String newValue) -> {
                            onSpeciesListValueChanged();
                        });

        return listView;

    }

    private VBox initDetailBox() {
        VBox vBox = new VBox(10);

        //Set ID for the css
        vBox.setId("detailBox");

        setHgrow(vBox, Priority.ALWAYS);

        return vBox;
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initLayout() {

        detailBox.getChildren().add(detailBoxHeader);

        HBox detailSouth = new HBox(10);

        VBox taskVBox = new VBox(10);
        taskVBox.getChildren().addAll(parcelLabel, mapPane);

        detailSouth.getChildren().addAll(taskVBox);

        detailSouth.getChildren().stream().forEach((child) -> {
            HBox.setMargin(child, new Insets(10));
        });

        detailBox.getChildren().add(detailSouth);
    }

    private void onSpeciesListValueChanged() {
        activeSpecies = speciesList.getSelectionModel().getSelectedItem();

        if (activeSpecies == null) {
            return;
        }
        ThreadUtils.runAsTask(() -> updateMap());

        deleteButton.setDisable(false);

        detailLabel.setText("Details (" + activeSpecies + ")");
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateSpeciesList() {

        Platform.runLater(() -> updateButton.setDisable(true));

        ObservableList<String> items
                = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> speciesList.setItems(items));

        ObservableList<String> newItems = getJsonResultObservableList(
                "sort_name", "selectSorte.php", null);

        Platform.runLater(() -> speciesList.setItems(newItems));
        Platform.runLater(() -> updateButton.setDisable(false));

        return null;
    }
    
        private Void updateMap() {

        Platform.runLater(() -> mapPane.setContent(new Label("Lädt...")));

        HashMap params = new HashMap();
        params.put("sorte", activeSpecies);
        ObservableList<String> selectedParcels = getJsonResultObservableList(
                        "parz_id", "selectParzelleBySorte.php", params);

        //Parse Strings to Integer
        Collection<Integer> parsedCol
                = MiscUtils.parseCollectionToInteger(selectedParcels);

        ObservableList<Integer> parsedList
                = FXCollections.observableArrayList(parsedCol);

        FieldMap fieldMap = new FieldMap(parsedList, false, false);

        fieldMap.setStyle("-fx-font-size: 9;");

        Platform.runLater(() -> mapPane.setContent(fieldMap));

        return null;
    }

    private Label initDetailLabel() {
        Label label = new Label("Details");
        label.setId("detailLabel");

        return label;

    }

    private Button initDeleteButton() {
        Image deleteIcon = new Image(
                getClass().getResourceAsStream(
                        "/drawables/deleteIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button("Löschen", new ImageView(deleteIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setDisable(true);
        button.setId("deleteButton");

        return button;
    }

    private Button initCreateButton() {
        Image createIcon = new Image(
                getClass().getResourceAsStream(
                        "/drawables/createIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button(
                "Neue Sorte erstellen", new ImageView(createIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("createButton");

        button.setOnAction((ActionEvent event) -> {
            CreateNewSort stage = new CreateNewSort();
            stage.initModality(Modality.APPLICATION_MODAL);

            //Refresh the TaskList after the window is closed
            stage.setOnCloseRequest((WindowEvent event1) -> {
                ThreadUtils.runAsTask(() -> updateSpeciesList());
            });
            stage.show();
        });

        return button;
    }

    private AnchorPane initDetailBoxHeader() {
        AnchorPane anchorPane
                = new AnchorPane(detailLabel, deleteButton);

        AnchorPane.setLeftAnchor(detailLabel, 10.0);
        AnchorPane.setRightAnchor(deleteButton, 10.0);

        anchorPane.getChildren().stream().forEach((child) -> {
            AnchorPane.setTopAnchor(child, 10.0);
        });

        return anchorPane;
    }

    private VBox initSpeciesBox() {
        VBox vBox = new VBox(speciesBoxHeader, speciesList, createButton);

        vBox.getChildren().stream().forEach((child) -> {
            VBox.setMargin(child, new Insets(10));
        });

        return vBox;
    }

    private AnchorPane initSpeciesBoxHeader() {
        AnchorPane anchorPane = new AnchorPane(speciesLabel, updateButton);

        AnchorPane.setLeftAnchor(speciesLabel, 10.0);
        AnchorPane.setRightAnchor(updateButton, 10.0);

        anchorPane.getChildren().stream().forEach((child) -> {
            AnchorPane.setTopAnchor(child, 10.0);
        });

        return anchorPane;
    }

    private Button initUpdateButton() {

        Image updateIcon = new Image(
                getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateSpeciesList());

            //Rotate the icon
            RotateTransition rotate
                    = new RotateTransition(Duration.seconds(2), imageView);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;
    }

    private ScrollPane initMapPane() {
        ScrollPane scrollPane = new ScrollPane(new FieldMap(false, false));

        return scrollPane;
    }

}
