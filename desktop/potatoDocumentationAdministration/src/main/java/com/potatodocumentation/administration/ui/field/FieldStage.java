/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import com.potatodocumentation.administration.ui.field.map.ParcelMap;
import com.potatodocumentation.administration.utils.JsonUtils;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultArray;
import static com.potatodocumentation.administration.utils.JsonUtils
        .getJsonResultObservableList;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class FieldStage extends Stage {

    private final int FIELD_ID;
    private Button updateButton;
    private Button deleteButton;
    private Label title;
    private ParcelMap map;

    public FieldStage(int fieldId) {
        super();

        FIELD_ID = fieldId;
        title = initTitle();
        deleteButton = initDeleteButton();
        updateButton = initUpdateButton();
        map = new ParcelMap(FIELD_ID, null);
        
        ThreadUtils.runAsTask(() -> map.updateParcelBox());
        
        Scene scene = new Scene(map);
        
        scene.getStylesheets().add(getClass()
                .getResource("/styles/potatoStyle.css").toExternalForm());
        
        setTitle("JaudiDauuu");
        setScene(scene);
        
        
        
    }

    private Label initTitle() {
        Label label = new Label("Parzellen");
        label.setId("title");

        return label;
    }

    private Button initDeleteButton() {
        Image deleteIcon = new Image(getClass()
                .getResourceAsStream("/drawables/deleteIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button("Feld löschen", new ImageView(deleteIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setDisable(true);
        button.setId("deleteButton");
        
        button.setOnAction((ActionEvent event) -> {
            deleteField();
        });

        return button;
    }
    
    public void deleteField(){
        
    }

    private VBox initParcelBox() {
        VBox vBox = new VBox(10);
        
        return vBox;
    }

    private Button initUpdateButton() {
        
        Image updateIcon = new Image(getClass()
                .getResourceAsStream("/drawables/updateIcon.png"), 16.0, 16.0, 
                true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> map.updateParcelBox());
            
            //Rotate the icon
            RotateTransition rotate = new RotateTransition(Duration.seconds(2), 
                    imageView);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;
    }
  

}
