/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultArray;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
public class CreateNewTask extends Application {

    // Components
    TextField nameField;
    DatePicker fromDP;
    DatePicker toDP;
    Label repeatLabel;
    ComboBox repeatCB;
    Button okButton;
    Button cancelButton;
    ListView<String> propertyList;
    Label multiSelectLabel;

    @Override
    public void start(Stage primaryStage) {

        //Init the components
        nameField = initNameField();
        fromDP = initFromDP();
        toDP = initToDP();
        repeatLabel = initRepeatLabel();
        repeatCB = initReapeatCB();
        okButton = initOkButton();
        cancelButton = initCancelButton();
        propertyList = initPropertyList();
        multiSelectLabel = initMultiSelectLabel();

        // Init the Layout
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(4);
        flow.setHgap(4);

        // Adding the Components
        Scene scene = new Scene(flow, 300, 450);
        flow.getChildren().add(nameField);
        flow.getChildren().add(fromDP);
        flow.getChildren().add(toDP);
        flow.getChildren().add(repeatLabel);
        flow.getChildren().add(repeatCB);
        flow.getChildren().add(multiSelectLabel);
        flow.getChildren().add(propertyList);
        flow.getChildren().add(okButton);
        flow.getChildren().add(cancelButton);

        primaryStage.setTitle("Neue Aufgabe hinzufügen");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Make sure no field is focused per default
        flow.requestFocus();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private DatePicker initFromDP() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Startdatum");
        return datePicker;
    }

    private DatePicker initToDP() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Enddatum");
        return datePicker;
    }

    private Label initRepeatLabel() {
        return new Label("Wiederholung:");
    }

    private ComboBox initReapeatCB() {
        // Adding Options
        ObservableList<String> options
                = FXCollections.observableArrayList(
                        "Einmalig",
                        "Jeden Tag",
                        "Jede Woche",
                        "Jeden Monat",
                        "Jedes Jahr"
                );
        ComboBox comboBox = new ComboBox(options);
        
        //Select first item in List
        comboBox.getSelectionModel().selectFirst();
        
        return comboBox;
    }

    private Button initCancelButton() {
        Button btn = new Button();
        btn.setText("Abbrechen");
        btn.setOnAction((ActionEvent event) -> {
            onCancelClicked();
        });
        return btn;
    }

    private void onCancelClicked() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Button initOkButton() {
        Button btn = new Button();
        btn.setText("Ok");
        btn.setOnAction((ActionEvent event) -> {
            onOkClicked();
        });
        return btn;
    }

    private void onOkClicked() {
        //check if name is valid
        String propName = nameField.getText();
        boolean nameIsValid = !propName.isEmpty();

        //check if dates are set
        boolean timeIsset = (toDP.getValue() != null) && (fromDP.getValue() != null);

        //check if propeties are selected and get them
        ObservableList<String> properties = propertyList.getSelectionModel().getSelectedItems();
        boolean propsSelected = (properties != null) && !(properties.isEmpty());
        
        //check if repeat CB is selected
        String repeat = (String) repeatCB.getValue();

        if (nameIsValid && timeIsset && propsSelected) {
            
            //Prepare the get-paramter
            HashMap<String, String> values = new HashMap<>();
            values.put("aufg_name", propName);

            //Get the dates within the default timezone
            LocalDate localDate = fromDP.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            Long fromMillis = date.getTime();
            values.put("from", fromMillis.toString());

            localDate = toDP.getValue();
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
            Long toMillis = date.getTime();
            values.put("to", toMillis.toString());
            
            

            //Get all selected properties
            for (String s : properties) {
                System.out.println(s);
            }

            //TODO: Check other fields
        }
    }

    private TextField initNameField() {
        TextField textField = new TextField();
        textField.setPromptText("Name");
        return textField;
    }

    private ListView<String> initPropertyList() {

        //Create List of all properties
        ObservableList<String> observables = FXCollections.observableArrayList();

        //Get all properties from the serviceURL
        ArrayList<Map<String, Object>> jsonResult = getJsonResultArray("selectEigenschaft.php");

        if (jsonResult != null) {
            //Add all properties to the ObservableList
            for (Map<String, Object> property : jsonResult) {
                observables.add((String) property.get("eig_name"));
            }
        }

        ListView listView = new ListView<>(observables);

        //Allow Multiselect
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //Set the size so 10 items
        listView.setPrefHeight(10 * (24 + 2));
        //Change the width
        listView.setPrefWidth(150);

        return listView;
    }

    private Label initMultiSelectLabel() {
        return new Label("Für Mehrfachauswahl STRG gedrückt halten:");
    }

}
