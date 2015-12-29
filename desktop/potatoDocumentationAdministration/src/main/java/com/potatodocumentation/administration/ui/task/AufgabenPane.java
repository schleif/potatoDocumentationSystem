/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.task;

import com.potatodocumentation.administration.MainApplication;
import com.potatodocumentation.administration.ui.field.map.FieldMap;
import static com.potatodocumentation.administration.utils.JsonUtils.*;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class AufgabenPane extends HBox implements EventHandler<KeyEvent> {

    Label taskLabel;
    Label propertyLabel;
    Label dateLabel;
    Label parzellenLabel;
    Label detailLabel;
    ListView<String> taskList;
    ListView<String> propertyList;
    ListView<String> dateList;
    ScrollPane mapPane;
    FieldMap map;
    VBox detailBox;
    VBox taskBox;
    AnchorPane taskBoxHeader;
    AnchorPane detailBoxHeader;
    Button updateButton;
    Button deleteButton;
    Button createButton;
    private String activeTask;

    ArrayList<Node> allItems = new ArrayList<>();

    String typedKeys;

    public AufgabenPane() {
        super(10);
        setFillHeight(true);

        //Initialize nodes
        //TaskBox
        taskLabel = initTaskLabel();
        updateButton = initUpdateButton();
        taskList = initTaskList();
        createButton = initCreateButton();
        taskBoxHeader = initTaskBoxHeader();
        taskBox = initTaskBox();

        //OptionBox
        detailLabel = initDetailLabel();
        deleteButton = initDeleteButton();
        detailBoxHeader = initDetailBoxHeader();

        //DetailBox
        mapPane = initMapPane();
        propertyLabel = initPropertyLabel();
        propertyList = initPropertyList();
        dateLabel = initDateLabel();
        dateList = initDateList();
        parzellenLabel = initParzellenLabel();
        detailBox = initDetailBox();

        initLayout();

        //Add needed children
        getChildren().add(taskBox);
        getChildren().add(detailBox);

        allItems.add(taskLabel);
        allItems.add(propertyLabel);
        allItems.add(dateLabel);
        allItems.add(parzellenLabel);
        allItems.add(detailLabel);
        allItems.add(taskList);
        allItems.add(propertyList);
        allItems.add(mapPane);
        allItems.add(dateList);
        allItems.add(detailBox);
        allItems.add(taskBox);
        allItems.add(taskBoxHeader);
        allItems.add(detailBoxHeader);
        allItems.add(updateButton);
        allItems.add(deleteButton);
        allItems.add(createButton);

        //Update TaskList on startup
        ThreadUtils.runAsTask(() -> updateTaskList());

        this.setOnKeyTyped(this);
        typedKeys = "";
    }

    private ListView<String> initTaskList() {

        ListView<String> listView = new ListView<>();

        //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            onTaskListValueChanged();
        });

        return listView;

    }

    private Label initTaskLabel() {
        return new Label("Aufgaben:");
    }

    private Label initPropertyLabel() {
        return new Label("Eigenschaften:");
    }

    private ListView<String> initPropertyList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
    }

    private Label initDateLabel() {
        return new Label("Termine:");
    }

    private ListView<String> initDateList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
    }

    private Label initParzellenLabel() {
        return new Label("Verknüpfte Parzellen:");
    }

    private ListView<String> initParzellenList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
    }

    private VBox initDetailBox() {
        VBox vBox = new VBox(10);

        //Set ID for the css
        vBox.setId("detailBox");

        return vBox;
    }

    private void initLayout() {

        detailBox.getChildren().add(detailBoxHeader);

        HBox detailSouth = new HBox(10);

        VBox propertyVBox = new VBox(10);
        propertyVBox.getChildren().addAll(propertyLabel, propertyList);

        VBox dateVBox = new VBox(10);
        dateVBox.getChildren().addAll(dateLabel, dateList);

        VBox parzellenVBox = new VBox(10);
        parzellenVBox.getChildren().addAll(parzellenLabel, mapPane);

        detailSouth.getChildren().addAll(propertyVBox, dateVBox, parzellenVBox);

        detailSouth.getChildren().stream().forEach((child) -> {
            HBox.setMargin(child, new Insets(10));
        });

        detailBox.getChildren().add(detailSouth);
    }

    private void onTaskListValueChanged() {
        activeTask = taskList.getSelectionModel().getSelectedItem();

        if (activeTask == null) {
            return;
        }
        ThreadUtils.runAsTask(() -> updatePropertyList());
        ThreadUtils.runAsTask(() -> updateDateList());
        ThreadUtils.runAsTask(() -> updateMap());

        deleteButton.setDisable(false);

        detailLabel.setText("Details (" + activeTask + ")");
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updatePropertyList() {
        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> propertyList.setItems(items));

        HashMap params = new HashMap();
        params.put("aufg_name", activeTask);

        ObservableList<String> newItems = getJsonResultObservableList(
                "eig_name", "selectEigenschaftByAufgabe.php", params);

        Platform.runLater(() -> propertyList.setItems(newItems));

        return null;
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateDateList() {
        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> dateList.setItems(items));

        HashMap params = new HashMap();

        params.put("aufg_name", activeTask);

        ArrayList<Map<String, Object>> jsonResult
                = getJsonResultArray("selectDateByAufgabe.php", params);

        ObservableList<String> newItems = FXCollections.observableArrayList();

        //Check if both lists have the same length
        if (jsonResult != null) {
            jsonResult.stream().forEach((date) -> {
                String from = (String) date.get("fromDate");
                String to = (String) date.get("toDate");

                newItems.add(from + " - " + to);
            });
        } else {
            newItems.add("Ladefehler!");
        }

        Platform.runLater(() -> dateList.setItems(newItems));

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

        Button button = new Button("Neue Aufgabe erstellen", new ImageView(createIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("createButton");

        button.setOnAction((ActionEvent event) -> {
            CreateNewTask stage = new CreateNewTask();
            stage.initModality(Modality.APPLICATION_MODAL);

            //Refresh the TaskList after the window is closed
            stage.setOnCloseRequest((WindowEvent event1) -> {
                ThreadUtils.runAsTask(() -> updateTaskList());
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

    private VBox initTaskBox() {
        VBox vBox = new VBox(taskBoxHeader, taskList, createButton);

        vBox.getChildren().stream().forEach((child) -> {
            VBox.setMargin(child, new Insets(10));
        });

        return vBox;
    }

    private AnchorPane initTaskBoxHeader() {
        AnchorPane anchorPane = new AnchorPane(taskLabel, updateButton);

        AnchorPane.setLeftAnchor(taskLabel, 10.0);
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

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateTaskList());

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

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateTaskList() {
        Platform.runLater(() -> updateButton.setDisable(true));

        ObservableList<String> items = FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> taskList.setItems(items));

        ObservableList<String> tasks
                = getJsonResultObservableList("aufg_name", "selectAufgabe.php", null);

        Platform.runLater(() -> taskList.setItems(tasks));
        Platform.runLater(() -> updateButton.setDisable(false));

        return null;
    }

    public void rotate(Node n) {
        RotateTransition rt = new RotateTransition(Duration.seconds(new Random().nextInt(10)), n);
        rt.setCycleCount(rt.INDEFINITE);
        rt.setByAngle(360);

        rt.setAutoReverse(true);

        rt.play();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(5), n);
        tt.setFromX(10);
        tt.setFromY(10);
        tt.setToX(200);
        tt.setToY(200);
        tt.setCycleCount(tt.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();
    }

    public void rotateAll() {
        for (Node n : allItems) {
            rotate(n);
        }
    }

    @Override
    public void handle(KeyEvent event) {
        typedKeys = typedKeys + event.getCharacter();
        if (typedKeys.contains("dance")) {
            MainApplication.getInstance().go();
            this.rotateAll();
        }
    }

    private Void updateMap() {

        Platform.runLater(() -> mapPane.setContent(new Label("Lädt...")));

        HashMap params = new HashMap();
        params.put("aufg_name", activeTask);  
        ObservableList<String> selectedParcels = getJsonResultObservableList(
                "parz_id", "selectParzelleByAufgabe.php", params);
        
        //Parse Strings to Integer
        Collection<Integer> parsedCol = 
                MiscUtils.parseCollectionToInteger(selectedParcels);
        
        ObservableList<Integer> parsedList = 
                FXCollections.observableArrayList(parsedCol);
        
        FieldMap fieldMap = new FieldMap(parsedList);
        
        fieldMap.setStyle("-fx-font-size: 9;");

        Platform.runLater(() -> mapPane.setContent(fieldMap));
        
        return null;
    }

    private ScrollPane initMapPane() {
        ScrollPane scrollPane = new ScrollPane(new Label("Aufgabe auswählen"));
        
      //  scrollPane.prefHeightProperty().bind(dateList.heightProperty());
                
        return scrollPane;
    }
}
