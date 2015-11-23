/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import java.util.HashMap;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
public class CreateNewParcel extends Stage {

    private int fieldNr;
    private int targetRow;
    private int nrOfRows;
    private int parPerRow;
    
    VBox content;
    Label header;
    TextField sort;
    HBox buttonBox;
    Button okButton;
    Button cancelButton;
    
    public CreateNewParcel(int fieldNr, int targetRow, int nrOfRows, 
            int parPerRow) {
        super();
        
        this.fieldNr = fieldNr;
        this.targetRow = targetRow;
        this.nrOfRows = nrOfRows;
        this.parPerRow = parPerRow;

        header = initHeader();
        sort = initSort();
        okButton = initOkButton();
        cancelButton = initCancelButton();
        buttonBox = initButtonBox();
        
        content = initContent();
  
        Scene scene = new Scene(content);
        this.setTitle("Neue Parzellen hinzufügen");
        this.setScene(scene);

        header.requestFocus();
    }


    private Button initOkButton() {
        Button but = new Button("Hinzufügen");
        but.setOnAction((ActionEvent event) -> {
            onOkClicked();
        });
        return but;
    }

    private void onOkClicked() {
        
    }

    private void insertField(String fieldName) {
        if (!fieldName.equals("Name...") && !fieldName.equals("")) {

            HashMap<String, String> values = new HashMap<>();
            values.put("field_id", fieldName);

            boolean success = getJsonSuccessStatus("insertField.php", values);

            String status = "Feld wurde " + (success ? "" : "nicht ")
                    + "erfolgreich eingefügt!";

            Alert alert = new Alert(Alert.AlertType.INFORMATION, status);

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);
        }
    }

    private Button initCancelButton() {
        Button but = new Button("Abbrechen");
        but.setOnAction((ActionEvent event) -> {
            onCancelClicked();
        });
        return but;
    }

    private void onCancelClicked() {
        this.close();
    }

    private Label initHeader() {
        int parsToInsert = nrOfRows * parPerRow;
        
        String newString = (parsToInsert > 1 ? "neue" : "neues");
        String fieldString = (parsToInsert > 1 ? "Felder" : "Feld");
                
        Label label = new Label("Füge " + parsToInsert + " " + newString + " " 
                + fieldString + " ein:");
        
        return label;
    }

    private TextField initSort() {
        TextField textField = new TextField();
        textField.setPromptText("Sorte eingeben");
        
        return textField;
    }

    private HBox initButtonBox() {
        HBox hBox = new HBox(10, okButton, cancelButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        
        return hBox;
    }

    private VBox initContent() {
        VBox vBox = new VBox(10, header, sort, buttonBox);
        
        return vBox;
    }
}