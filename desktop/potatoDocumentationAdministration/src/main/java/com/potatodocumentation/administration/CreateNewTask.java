/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.potatodocumentation.administration.utils.DateUtils.*;
import static com.potatodocumentation.administration.utils.JsonUtils.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
public class CreateNewTask extends Stage {

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

    public CreateNewTask() {
        super();

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
        VBox flow = new VBox();
        // flow.setOrientation(Orientation.VERTICAL);
        flow.setPadding(new Insets(5, 5, 5, 5));
        // flow.setVgap(4);
        // flow.setHgap(4);

        // Adding the Components
        Scene scene = new Scene(flow, 300, 400);
        flow.getChildren().add(nameField);
        flow.getChildren().add(fromDP);
        flow.getChildren().add(toDP);
        flow.getChildren().add(repeatLabel);
        flow.getChildren().add(repeatCB);
        flow.getChildren().add(multiSelectLabel);
        flow.getChildren().add(propertyList);

        // Button Box
        FlowPane buttonLine = new FlowPane();
        buttonLine.setOrientation(Orientation.HORIZONTAL);
        buttonLine.setPadding(new Insets(5, 5, 5, 5));
        buttonLine.setVgap(4);
        buttonLine.setHgap(4);
        buttonLine.getChildren().add(okButton);
        buttonLine.getChildren().add(cancelButton);
        
        flow.getChildren().add(buttonLine);

        this.setTitle("Neue Aufgabe hinzufügen");
        this.setScene(scene);

        //Make sure no field is focused per default
        flow.requestFocus();
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
        this.close();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

        //get repeatCB value
        String repeat = (String) repeatCB.getValue();

        if (nameIsValid && timeIsset && propsSelected) {

            //Prepare the get-paramter as json
            HashMap<String, Object> jsonValues = new HashMap<>();
            jsonValues.put("aufg_name", propName);

            //Add all selected properties
            jsonValues.put("eigenschaften", properties);

            jsonValues.put("dates", getAllDates());

            //Create JSON
            ObjectMapper mapper = new ObjectMapper();

            String jsonRequest = null;
            try {
                jsonRequest = mapper.writeValueAsString(jsonValues);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(CreateNewTask.class.getName()).log(Level.SEVERE, null, ex);
            }

            //Send to server
            HashMap<String, String> values = new HashMap<>();
            values.put("json", jsonRequest);

            boolean success = getJsonSuccessStatus("insertAufgabeComplete.php", values);

            //Show success status
            String status = "Eigenschaft wurde " + (success ? "" : "nicht ")
                    + "erfolgreich eingefügt!";

            Alert alert = new Alert(AlertType.INFORMATION, status);

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);

            //TODO: Send JSON to server and process it
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

    private ArrayList<String> getAllDates() {

        ArrayList<String> allDates = new ArrayList<>();

        //Get Dates from DP's
        LocalDate fromDate = fromDP.getValue();
        LocalDate toDate = toDP.getValue();

        //Add first day
        allDates.add(fromDate.toString());

        //Keep track if the length of the list is Odd
        boolean isOdd = true;

        //Get repeatCB Value
        String repeat = (String) repeatCB.getValue();

        switch (repeat) {
            case "Jeden Tag": {
                //Start one day after teh initial day
                LocalDate tempDate = fromDate;
                allDates.add(tempDate.toString());
                isOdd = !isOdd;

                tempDate = tempDate.plusDays(1);

                //Iterate until last day is reached
                while (tempDate.compareTo(toDate) < 0) {
                    //add all day twice
                    allDates.add(tempDate.toString());
                    allDates.add(tempDate.toString());
                    tempDate = tempDate.plusDays(1);
                }
                break;
            }
            case "Jede Woche": {

                //Go to the end of the week
                LocalDate tempDate = goToDay(7, fromDate);

                boolean isEnd = true;

                while (tempDate.compareTo(toDate) < 0) {
                    allDates.add(tempDate.toString());
                    isOdd = !isOdd;

                    if (isEnd) {
                        //go to first day of next week
                        tempDate = tempDate.plusDays(1);
                    } else {
                        //go to last day of week
                        tempDate = goToDay(7, tempDate);
                    }

                    isEnd = !isEnd;
                }

                break;
            }
            case "Jeden Monat": {

                //Go to the end of the month
                LocalDate tempDate = goToLastDayOfMonth(fromDate);

                boolean isEnd = true;

                while (tempDate.compareTo(toDate) < 0) {
                    allDates.add(tempDate.toString());
                    isOdd = !isOdd;

                    //Check if needed to go to beggining or end
                    if (isEnd) {
                        //go to first day of next month
                        tempDate = tempDate.plusDays(1);
                    } else {
                        //go to last day of month
                        tempDate = goToLastDayOfMonth(tempDate);
                    }

                    isEnd = !isEnd;
                }

                break;
            }

            case "Jedes Jahr": {

                //Go to the end of the year
                LocalDate tempDate = goToLastDayOfYear(fromDate);

                boolean isEnd = true;

                while (tempDate.compareTo(toDate) < 0) {
                    allDates.add(tempDate.toString());
                    isOdd = !isOdd;

                    //Check if needed to go to beggining or end
                    if (isEnd) {
                        //go to first day of next year
                        tempDate = tempDate.plusDays(1);
                    } else {
                        //go to last day of year
                        tempDate = goToLastDayOfYear(tempDate);
                    }

                    isEnd = !isEnd;
                }

                break;
            }
        }

        //Might have to add the last date twice:
        //If toDate is the first day of a of the choosen period type
        if (!isOdd) {
            allDates.add(toDate.toString());
        }
        //add the last day
        allDates.add(toDate.toString());

        return allDates;
    }

}
