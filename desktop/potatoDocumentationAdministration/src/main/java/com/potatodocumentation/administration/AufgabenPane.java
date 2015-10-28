/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import static com.potatodocumentation.administration.utils.JsonUtils.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ochi
 */
public class AufgabenPane extends HBox {

    Label taskLabel;
    Label propertyLabel;
    Label dateLabel;
    Label parzellenLabel;
    Label detailLabel;
    ListView<String> taskList;
    ListView<String> propertyList;
    ListView<String> parzellenList;
    ListView<String> dateList;
    VBox detailBox;
    VBox taskBox;
    AnchorPane taskBoxHeader;
    AnchorPane detailBoxHeader;
    Button updateButton;
    Button deleteButton;
    Button editButton;
    Button createButton;
    private String activeTask;

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
        editButton = initEditButton();
        deleteButton = initDeleteButton();
        detailBoxHeader = initDetailBoxHeader();

        //DetailBox
        propertyLabel = initPropertyLabel();
        propertyList = initPropertyList();
        dateLabel = initDateLabel();
        dateList = initDateList();
        parzellenLabel = initParzellenLabel();
        parzellenList = initParzellenList();
        detailBox = initDetailBox();

        initLayout();

        //Add needed children
        getChildren().add(taskBox);
        getChildren().add(detailBox);

        //Update TaskList on startup
        updateTaskList();

    }

    private ListView<String> initTaskList() {

        ListView<String> listView = new ListView<>();

        //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                onTaskListValueChanged();
            }
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
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Label initDateLabel() {
        return new Label("Termine:");
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ListView<String> initDateList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Label initParzellenLabel() {
        return new Label("Verknüpfte Parzellen:");
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ListView<String> initParzellenList() {
        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

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

        VBox propertyVBox = new VBox(10);
        propertyVBox.getChildren().addAll(propertyLabel, propertyList);

        VBox dateVBox = new VBox(10);
        dateVBox.getChildren().addAll(dateLabel, dateList);

        VBox parzellenVBox = new VBox(10);
        parzellenVBox.getChildren().addAll(parzellenLabel, parzellenList);

        detailSouth.getChildren().addAll(propertyVBox, dateVBox, parzellenVBox);

        detailSouth.getChildren().stream().forEach((child) -> {
            HBox.setMargin(child, new Insets(10));
        });

        detailBox.getChildren().add(detailSouth);
    }

    private void onTaskListValueChanged() {
        activeTask = taskList.getSelectionModel().getSelectedItem();

        updatePropertyList();
        updateDateList();
        updateParzellenList();

        editButton.setDisable(false);
        deleteButton.setDisable(false);

        detailLabel.setText("Details (" + activeTask + ")");
    }

    private void updatePropertyList() {
        HashMap params = new HashMap();
        params.put("aufg_name", activeTask);

        ObservableList<String> newItems = getJsonResultObservableList(
                "eig_name", "selectEigenschaftByAufgabe.php", params);

        propertyList.setItems(newItems);

    }

    private void updateDateList() {
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

        dateList.setItems(newItems);
    }

    private void updateParzellenList() {
        HashMap params = new HashMap();

        params.put("aufg_name", activeTask);

        ObservableList<String> newItems = getJsonResultObservableList(
                "eig_name", "selectParzellenByAufgabe.php", params);

        parzellenList.setItems(newItems);
    }

    private Label initDetailLabel() {
        Label label = new Label("Details");
        label.setId("detailLabel");

        return label;

    }

    private Button initEditButton() {
        Button button = new Button("Bearbeiten");

        button.setDisable(true);

        return button;
    }

    private Button initDeleteButton() {
        Button button = new Button("Löschen");

        button.setDisable(true);

        return button;
    }

    private Button initCreateButton() {
        Button button = new Button("Neue Aufgabe erstellen");

        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                CreateNewTask stage = new CreateNewTask();
                stage.initModality(Modality.APPLICATION_MODAL);
                
                //Refresh the TaskList after the window is closed
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        updateTaskList();
                    }
                });
                stage.show();
            }
        });

        return button;
    }

    private AnchorPane initDetailBoxHeader() {
        AnchorPane anchorPane
                = new AnchorPane(detailLabel, editButton, deleteButton);

        AnchorPane.setLeftAnchor(detailLabel, 10.0);
        AnchorPane.setRightAnchor(editButton, 20.0 + 70);
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
        Button button = new Button("Update");

        return button;
    }

    private void updateTaskList() {
        ObservableList<String> tasks
                = getJsonResultObservableList("aufg_name", "selectAufgabe.php", null);

        taskList.setItems(tasks);
    }

}
