/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import static com.potatodocumentation.administration.utils.JsonUtils.*;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class FieldPane2 extends HBox {

    Label fieldLabel;
    Label parzellenLabel;
    Label sortsLabel;
    Label connectedTaskLabel;
    Label detailLabel;
    ListView<String> fieldList;
    ListView<String> parzellenList;
    ListView<String> connectedTaskList;
    ListView<String> sortsList;
    VBox detailBox;
    VBox fieldBox;
    AnchorPane fieldBoxHeader;
    AnchorPane detailBoxHeader;
    Button updateButton;
    Button deleteButton;
    Button createButton;
    private String activeField;
    private String activeParzelle;

    public FieldPane2() {
        super(10);
        setFillHeight(true);

        //Initialize nodes
        //TaskBox
        fieldLabel = initFieldLabel();
        updateButton = initUpdateButton();
        fieldList = initFieldList();
        createButton = initCreateButton();
        fieldBoxHeader = initFieldBoxHeader();
        fieldBox = initFieldBox();

        //OptionBox
        detailLabel = initDetailLabel();
        deleteButton = initDeleteButton();
        detailBoxHeader = initDetailBoxHeader();

        //DetailBox
        parzellenLabel = initParzellenLabel();
        parzellenList = initParzellenList();
        sortsLabel = initSortsLabel();
        sortsList = initSortsList();
        connectedTaskLabel = initConnectedTaskLabel();
        connectedTaskList = initConnectedTaskList();
        detailBox = initDetailBox();

        initLayout();

        //Add needed children
        getChildren().add(fieldBox);
        getChildren().add(detailBox);

        //Update TaskList on startup
        ThreadUtils.runAsTask(() -> updateFieldList());

    }

    private ListView<String> initFieldList() {

        ListView<String> listView = new ListView<>();

        //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onFieldListValueChanged();
            }
        });

        return listView;

    }

    private Label initFieldLabel() {
        return new Label("Felder:");
    }

    private Label initParzellenLabel() {
        return new Label("Parzellen:");
    }

    private ListView<String> initParzellenList() {
        ObservableList<String> items = FXCollections.observableArrayList("Kein Feld ausgewählt");

        ListView<String> listView = new ListView<>(items);
        
         //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onParzelleListValueChanged();
            }
        });

        return listView;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Label initSortsLabel() {
        return new Label("Angebaute Sorten:");
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ListView<String> initSortsList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Parzelle ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Label initConnectedTaskLabel() {
        return new Label("Verknüpfte Aufgaben:");
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ListView<String> initConnectedTaskList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Parzelle ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private VBox initDetailBox() {
        VBox vBox = new VBox(10);

        //Set ID for the css
        vBox.setId("detailBox");

        return vBox;
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initLayout() {

        detailBox.getChildren().add(detailBoxHeader);

        HBox detailSouth = new HBox(10);

        VBox parzellenVBox = new VBox(10);
        parzellenVBox.getChildren().addAll(parzellenLabel, parzellenList);

        VBox sortsVBox = new VBox(10);
        sortsVBox.getChildren().addAll(sortsLabel, sortsList);

        VBox connectedTaskVBox = new VBox(10);
        connectedTaskVBox.getChildren().addAll(connectedTaskLabel, connectedTaskList);

        detailSouth.getChildren().addAll(parzellenVBox, sortsVBox, connectedTaskVBox);

        detailSouth.getChildren().stream().forEach((child) -> {
            HBox.setMargin(child, new Insets(10));
        });

        detailBox.getChildren().add(detailSouth);
    }
    
     private void onParzelleListValueChanged() {
        activeParzelle = parzellenList.getSelectionModel().getSelectedItem();

        if (activeParzelle == null) {
            return;
        }
     }

    private void onFieldListValueChanged() {
        activeField = fieldList.getSelectionModel().getSelectedItem();

        if (activeField == null) {
            return;
        }
        ThreadUtils.runAsTask(() -> updateParzellenList());
        ThreadUtils.runAsTask(() -> updateSortsList());
        ThreadUtils.runAsTask(() -> updateConnectedTaskList());

        deleteButton.setDisable(false);

        detailLabel.setText("Details (" + activeField +")");
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateParzellenList() {
        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> parzellenList.setItems(items));

        HashMap params = new HashMap();
        params.put("feld_nr", activeField);

        ObservableList<String> newItems = getJsonResultObservableList(
                "parz_id", "selectParzellenByFeld.php", params);

        Platform.runLater(() -> parzellenList.setItems(newItems));

        return null;
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateSortsList() {
        
        if (activeParzelle == null) {
            return null;
        }
        
        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> sortsList.setItems(items));

        HashMap params = new HashMap();

        params.put("parz_id", activeParzelle);

        ArrayList<Map<String, Object>> jsonResult
                = getJsonResultArray("selectDateByAufgabe.php", params);

        ObservableList<String> newItems = FXCollections.observableArrayList();

        //Check if both lists have the same length
        /*
        if (jsonResult != null) {
            jsonResult.stream().forEach((date) -> {
                String from = (String) date.get("fromDate");
                String to = (String) date.get("toDate");

                newItems.add(from + " - " + to);
            });
        } else {
            newItems.add("Ladefehler!");
        }*/

        Platform.runLater(() -> sortsList.setItems(newItems));

        return null;
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateConnectedTaskList() {
        
        if (activeParzelle == null) {
            return null;
        }
        
        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> connectedTaskList.setItems(items));

        HashMap params = new HashMap();

        params.put("parz_id", activeParzelle);

        ObservableList<String> newItems = getJsonResultObservableList(
                "aufg_name", "selectDateByParzelle.php", params);

        Platform.runLater(() -> connectedTaskList.setItems(newItems));

        return null;
    }

    private Label initDetailLabel() {
        Label label = new Label("Details");
        label.setId("detailLabel");

        return label;

    }

    private Button initDeleteButton() {
        Image deleteIcon = new Image(getClass().getResourceAsStream("/drawables/deleteIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button("Löschen", new ImageView(deleteIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setDisable(true);
        button.setId("deleteButton");

        return button;
    }

    private Button initCreateButton() {
        Image createIcon = new Image(getClass().getResourceAsStream("/drawables/createIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button("Neues Feld erstellen", new ImageView(createIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("createButton");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                CreateNewField stage = new CreateNewField();
                stage.initModality(Modality.APPLICATION_MODAL);

                //Refresh the TaskList after the window is closed
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        ThreadUtils.runAsTask(() -> updateFieldList());
                    }
                });
                stage.show();
            }
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

    private VBox initFieldBox() {
       
            VBox vBox = new VBox(fieldBoxHeader, fieldList, createButton);

        vBox.getChildren().stream().forEach((child) -> {
            VBox.setMargin(child, new Insets(10));
        });
        
        

        return vBox;
    }

    private AnchorPane initFieldBoxHeader() {
        AnchorPane anchorPane = new AnchorPane(fieldLabel, updateButton);

        AnchorPane.setLeftAnchor(fieldLabel, 10.0);
        AnchorPane.setRightAnchor(updateButton, 10.0);

        anchorPane.getChildren().stream().forEach((child) -> {
            AnchorPane.setTopAnchor(child, 10.0);
        });

        return anchorPane;
    }

    private Button initUpdateButton() {

        Image updateIcon = new Image(getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                ThreadUtils.runAsTask(() -> updateFieldList());

                //Rotate the icon
                RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
                rotate.setByAngle(360.0);
                rotate.setCycleCount(1);
                rotate.setInterpolator(Interpolator.EASE_BOTH);
                rotate.play();

            }
        });

        return button;
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateFieldList() {
        Platform.runLater(() -> updateButton.setDisable(true));

        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> fieldList.setItems(items));

        ObservableList<String> fields
                = getJsonResultObservableList("field_id", "selectFeld.php", null);

        Platform.runLater(() -> fieldList.setItems(fields));
        Platform.runLater(() -> updateButton.setDisable(false));

        return null;
    }

}
