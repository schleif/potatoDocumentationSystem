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
import java.util.Scanner;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
class CreateNewProp extends Stage {

    //Components
    Label description;
    TextField nameField;
    Button okButton;
    TextArea moreNames;
    Button more;
    Button cancel;

    VBox box;

    // State: if on should inserted or more
    boolean oneProp = true;

    public CreateNewProp() {
        super();

        description = initDescripLabel();
        nameField = initNameField();
        okButton = initOkButton();
        moreNames = initMoreNames();
        more = initMoreButton();
        cancel = initCancelButton();

        // Init Layout
        box = new VBox();
        box.setPadding(new Insets(5, 5, 5, 5));

        //Adding Components
        Scene scene = new Scene(box, 300, 400);
        box.getChildren().add(description);
        box.getChildren().add(nameField);
        
        //Show Buttons in row
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(5, 5, 5, 5));
        buttonBox.setSpacing(5);
        buttonBox.getChildren().add(okButton);
        buttonBox.getChildren().add(cancel);   
        
        box.getChildren().add(buttonBox);
        box.getChildren().add(more);

        this.setTitle("Neue Eigenschaft hinzufügen");
        this.setScene(scene);

        //Make sure no field is focused per default
        box.requestFocus();
    }

    private TextField initNameField() {
        TextField tf = new TextField("Name...");
        tf.setOnMouseClicked((MouseEvent event) -> {
            if (tf.getText().equals("Name...")) {
                tf.setText("");
            }
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
        if (oneProp) {
            insertProperty(nameField.getText());
        } else {
            String values = moreNames.getText();
            if (values.isEmpty() || values.equals("Eigenschaft1;"
                    + "\n"
                    + "Eigenschaft2;")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.showAndWait()
                        .filter(response -> response == ButtonType.OK);
                return;
            }
            Scanner sc = new Scanner(values);
            while (sc.hasNext()) {
                String propName = sc.nextLine();
                insertProperty(propName);
            }
        }
    }

    private void insertProperty(String propName) {
        if (!propName.equals("Name...") && !propName.equals("")) {

            HashMap<String, String> values = new HashMap<>();
            values.put("eig_name", propName);

            boolean success = getJsonSuccessStatus("insertEigenschaft.php", values);

            String status = "Eigenschaft wurde " + (success ? "" : "nicht ")
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

    private Label initDescripLabel() {
        return new Label("Geben Sie in das Feld den Namen"
                + " der Eigschaft an.");
    }

    private TextArea initMoreNames() {
        String defStr = "Eigenschaft1"
                + "\n"
                + "Eigenschaft2";
        TextArea ta = new TextArea(defStr);
        ta.setOnMouseClicked((MouseEvent event) -> {
            if (ta.getText().equals(defStr)) {
                ta.setText("");
            }
        });
        return ta;
    }

    private Button initMoreButton() {
        Button but = new Button("Mehrere Eigenschaften hinzufügen");
        but.setOnAction((ActionEvent event) -> {
            onMoreClicked();
        });
        return but;
    }

    private void onMoreClicked() {
        box.getChildren().remove(1);
        box.getChildren().add(1, moreNames);
        box.getChildren().remove(3);
        oneProp = false;
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
}
