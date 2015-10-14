/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

// Components
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

//HTTP Connection
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


/**
 *
 * @author fiel
 */
public class CreateNewProperty extends Application {

    // Components
    Button okButton;
    Button cancelButton;
    TextField name;

    @Override
    public void start(Stage primaryStage) {

        //Init all Components
        okButton = initOkButton();
        cancelButton = initCancelButton();
        name = initNameTextField();

        // Init the Layout
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(4);
        flow.setHgap(4);

        //Adding the Components
        flow.getChildren().add(name);
        flow.getChildren().add(okButton);
        flow.getChildren().add(cancelButton);

        Scene scene = new Scene(flow, 350, 40);

        primaryStage.setTitle("Neue Eigenschaft hinzufügen!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
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
        String propName = name.getText();
        if (!propName.equals("Name...")) {

            HashMap<String, String> values = new HashMap<>();
            values.put("eig_name", propName);

            Connection conn = new Connection("insertEigenschaft.php", values);
            InputStream res = conn.getInputStream();

            String out = "Error occured :/";
            try {
                out = new BufferedReader(
                        new InputStreamReader(res)
                ).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(AlertType.INFORMATION, out);

            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK);
        }
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

    private TextField initNameTextField() {
        return new TextField("Name...");
    }

}
