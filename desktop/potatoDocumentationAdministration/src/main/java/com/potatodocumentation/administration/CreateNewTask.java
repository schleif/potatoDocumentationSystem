/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
    ComboBox propertyCB;
    Button okButton;
    Button cancelButton;

    @Override
    public void start(Stage primaryStage) {

        //Init the components
        nameField = initNameField();
        fromDP = initFromDP();
        toDP = initToDP();
        repeatLabel = initRepeatLabel();
        repeatCB = initReapeatCB();
        propertyCB = initPropertyCB();
        okButton = initOkButton();
        cancelButton = initCancelButton();

        // Init the Layout
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(4);
        flow.setHgap(4);

        // Adding the Components
        Scene scene = new Scene(flow, 300, 250);
        flow.getChildren().add(nameField);
        flow.getChildren().add(fromDP);
        flow.getChildren().add(toDP);
        flow.getChildren().add(repeatLabel);
        flow.getChildren().add(repeatCB);
        flow.getChildren().add(propertyCB);
        flow.getChildren().add(okButton);
        flow.getChildren().add(cancelButton);
        

        primaryStage.setTitle("Neue Aufgabe hinzufügen");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private DatePicker initFromDP() {
        return new DatePicker();
    }

    private DatePicker initToDP() {
        return new DatePicker();
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
        return new ComboBox(options);
    }

    private ComboBox initPropertyCB(){
        Connection conn = new Connection("selectEigenschaft.php");
        // Mapping Json to Map<>
        ObjectMapper mapper = new ObjectMapper();
        
        Map<String, Object> result = null;
        
        try {
            result = mapper.readValue(conn.getServiceURL(), Map.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        //check is succeded
        boolean success = (result != null) && (result.get("success").equals(1));
        
        //Create List of all properties
        ObservableList<String> propertyList = FXCollections.observableArrayList();
        
        if(success){
            //Get all results
            ArrayList<Map<String,Object>> resultList = (ArrayList<Map<String,Object>>) result.get("Result");

            //Run through all results and add them to list
            for(Map<String,Object> property : resultList){
                propertyList.add((String) property.get("eig_name"));
            }
        }

        return new ComboBox(propertyList);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
         String propName = nameField.getText();
         
         boolean nameIsValid = !propName.equals("Name...");
         boolean timeIsset = (toDP.getValue() != null) && (fromDP.getValue() != null);

        if (nameIsValid && timeIsset) {

            HashMap<String, String> values = new HashMap<>();
            values.put("aufg_name", propName);
            
            //Get the dates with the default timezone
            LocalDate localDate = fromDP.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            Long fromMillis = date.getTime();
            
            localDate = toDP.getValue();
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            date = Date.from(instant);
            Long toMillis = date.getTime();
            
            //TODO: Check other fields
        }
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private TextField initNameField() {
        return new TextField("Name...");
    }

}
