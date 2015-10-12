/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package potatodocumentation.administration;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;

// Components
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

//HTTP Connection
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author fiel
 */
public class newProperty extends Application {

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

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 5, 5, 5));
        flow.setVgap(4);
        flow.setHgap(4);

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
            String url_str = "http://134.169.47.160/insertEigenschaft.php?";
            url_str = url_str + "eig_name=" + propName;
            
            
            // TODO: Conntection Class with file based Connection Handling
            try {
                URL url = new URL(url_str);
                HttpURLConnection conn = (HttpURLConnection) 
                        url.openConnection();
                InputStream res = conn.getInputStream();
                System.out.println(
                        new BufferedReader(
                                new InputStreamReader(res)
                        ).readLine());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
