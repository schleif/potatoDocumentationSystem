/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potatodocumentation.administration;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 *
 * @author fiel
 */
public class CreateNewTask extends Application {

    // Components
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

        primaryStage.setTitle("Hello World!");
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

    private ComboBox initPropertyCB() {
        Connection conn = new Connection("selectEigenschaft.php");
        // Getting JsonObject
        String jsonStr = "";
        try {
            jsonStr
                    = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()))
                    .readLine();
        } catch (IOException e) {
            // No connection
            e.printStackTrace();
        }

        

        return new ComboBox();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
