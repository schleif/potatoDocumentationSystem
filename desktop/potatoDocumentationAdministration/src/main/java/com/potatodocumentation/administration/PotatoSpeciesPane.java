/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import com.potatodocumentation.administration.utils.ThreadUtils;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author fiel
 */
public class PotatoSpeciesPane extends VBox {

    // Components
    Label infoLabel;
    ListView<String> speciesList;
    Button createButton;
    Button deleteButton;
    Button updateButton;
    
    public PotatoSpeciesPane() {
        super();

        // Layouting
        this.setPadding(new Insets(5, 5, 5, 5));

        // Init nodes
        infoLabel = initInfoLabel();
        speciesList = initSpeciesList();
        createButton = initCreateButton();
        deleteButton = initDelButton();
        updateButton = initUpdateButton();

        // adding nodes
        this.getChildren().add(infoLabel);
        this.getChildren().add(speciesList);
        HBox box = new HBox();
        box.setPadding(new Insets(5, 5, 5, 5));
        box.setSpacing(10);
        box.getChildren().add(createButton);
        box.getChildren().add(deleteButton);
        box.getChildren().add(updateButton);
        this.getChildren().add(box);

        // finishing tasks
        ThreadUtils.runAsTask(() -> updateSpecies());
    }
    
    private ListView<String> initSpeciesList() {
        ObservableList<String> items
                = FXCollections.observableArrayList("Keine Sorte ausgewählt");
        
        ListView<String> listView = new ListView<>(items);
        
        listView.setOnMouseClicked((MouseEvent event) -> {
            deleteButton.setDisable(false);
        });
        
        return listView;
    }
    
    private Void updateSpecies() {
        ObservableList<String> species
                = getJsonResultObservableList("sort_name", "selectSorte.php", null);
        
        Platform.runLater(() -> speciesList.setItems(species));
        
        return null;
    }
    
    private Button initCreateButton() {
        Image createIcon = new Image(getClass().getResourceAsStream("/drawables/createIcon.png"),
                16.0, 16.0, true, true);
        
        Button button = new Button("Neue Sorte erstellen", new ImageView(createIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("createButton");
        
        button.setOnAction((ActionEvent event) -> {
            CreateNewSort stage = new CreateNewSort();
            stage.initModality(Modality.APPLICATION_MODAL);
            //Refresh the TaskList after the window is closed
            stage.setOnCloseRequest((WindowEvent event1) -> {
                updateSpecies();
            });
            stage.show();
        });
        
        return button;
    }
    
    private Button initDelButton() {
        Image deleteIcon = new Image(getClass().getResourceAsStream("/drawables/deleteIcon.png"),
                16.0, 16.0, true, true);
        
        Button button = new Button("Löschen", new ImageView(deleteIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setDisable(true);
        button.setId("deleteButton");
        
        button.setOnAction((ActionEvent event) -> {
            onDelButtonClicked();
        });
        
        return button;
    }

    private void onDelButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    private Button initUpdateButton() {
        Image updateIcon = new Image(getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);
        
        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            updateSpecies();
            
            //Rotate the icon
            RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
            rotate.setByAngle(360.0);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;
    }

    private Label initInfoLabel() {
        return new Label("Sorten verwalten");
    }
}
