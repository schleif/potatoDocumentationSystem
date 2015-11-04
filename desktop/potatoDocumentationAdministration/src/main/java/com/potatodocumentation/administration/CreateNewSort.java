/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonSuccessStatus;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
class CreateNewSort extends Stage {

    //Components
    Label description;
    TextField nameField;
    Button okButton;
    TextArea moreNames;
    Button more;
    
    VBox box;

    public CreateNewSort() {
        super();

        description = initDescripLabel();
        nameField = initNameField();
        okButton = initOkButton();
        moreNames = initMoreNames();
        more = initMoreButton();

        // Init Layout
        box = new VBox();
        box.setPadding(new Insets(5, 5, 5, 5));

        //Adding Components
        Scene scene = new Scene(box, 300, 400);
        box.getChildren().add(description);
        box.getChildren().add(nameField);
        box.getChildren().add(okButton);
        box.getChildren().add(more);

        this.setTitle("Neue Sorte hinzufügen");
        this.setScene(scene);

        //Make sure no field is focused per default
        box.requestFocus();
    }

    private TextField initNameField() {
        TextField tf = new TextField("Name...");
        tf.setOnMouseClicked((MouseEvent event) -> {
            tf.setText("");
        });
        return tf;
    }

    private Button initOkButton() {
        Button but = new Button("Hinzufügen");
        but.setOnAction((ActionEvent event) -> {
            onOkClicked();
        });
        return but;
    }

    private void onOkClicked() {
        String sortName = nameField.getText();
        // Break if name is not set
        if (sortName.isEmpty()) {
            return;
        }

        //Prepare the get-paramter as json
        HashMap<String, Object> jsonValues = new HashMap<>();
        jsonValues.put("sort_name", sortName);

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

        boolean success = getJsonSuccessStatus("insertSorte.php", values);

        //Show success status
        String status = "Eigenschaft wurde " + (success ? "" : "nicht ")
                + "erfolgreich eingefügt!";

        Alert alert = new Alert(Alert.AlertType.INFORMATION, status);

        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);

    }

    private Label initDescripLabel() {
        return new Label("Geben Sie in das Feld den Namen"
                + " der Sorte an.");
    }

    private TextArea initMoreNames() {
        String defStr = "Sorte1;"
        + "\n"
        + "Sorte2;";
        TextArea ta = new TextArea(defStr);
        ta.setOnMouseClicked((MouseEvent event) -> {
            ta.setText("");
        });
        return ta;
    }

    private Button initMoreButton() {
        Button but = new Button("Mehrere Sorten hinzufügen");
        but.setOnAction((ActionEvent event) -> {
            onMoreClicked();
        });
        return but;
    }

    private void onMoreClicked() {
        box.getChildren().remove(1);
        box.getChildren().add(1, moreNames);
    }
}
