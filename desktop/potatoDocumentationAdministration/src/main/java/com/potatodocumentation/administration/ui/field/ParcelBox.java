/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 *
 * @author Ochi
 */
public class ParcelBox extends VBox {

    private int id;
    private String sort;
    boolean isInMarkMode = false;
    private boolean isMarked = false;

    private Label idLabel;
    private Label sortLabel;
    private AnchorPane header;
    private Button deleteButton;

    public ParcelBox(int id, String sort) {
        super(2);

        this.id = id;
        this.sort = sort;

        idLabel = initIdLabel();
        sortLabel = initSortLabel();
        deleteButton = initDeleteButton();
        header = initHeader();

        VBox.setVgrow(header, Priority.NEVER);
        VBox.setVgrow(sortLabel, Priority.ALWAYS);

        setAlignment(Pos.CENTER);

        getChildren().add(header);
        getChildren().add(sortLabel);
        
    }

    private AnchorPane initHeader() {
        AnchorPane ap = new AnchorPane(idLabel, deleteButton);

        ap.setMinWidth(50);
        ap.setPrefWidth(50);

        AnchorPane.setLeftAnchor(idLabel, 1.0);
        AnchorPane.setRightAnchor(deleteButton, 1.0);

        return ap;
    }

    private Button initDeleteButton() {
        Image deleteIcon = new Image(getClass().getResourceAsStream("/drawables/deleteIcon.png"),
                8.0, 8.0, true, true);

        Button button = new Button(null, new ImageView(deleteIcon));
        button.setMinSize(12, 12);
        button.setPrefSize(12, 12);
        button.setMaxSize(12, 12);
        button.setId("deleteButton");

        return button;
    }

    private Label initSortLabel() {
        Label label = new Label();

        if (sort == null || sort.isEmpty()) {
            label.setText("Keine");
            Font font = Font.getDefault();
            label.setFont(Font.font(font.getName(), FontPosture.ITALIC, 
                    font.getSize()));
        } else {
            label.setText(sort);
        }
        
        return label;
    }

    private Label initIdLabel() {
        return new Label("ID: " + id);
    }
    
    public void mark(){
        isMarked = isInMarkMode  && !isMarked;
        
        if(isMarked){
            getStyleClass().add("marked-parcel-box");
        } else {
            getStyleClass().remove("marked-parcel-box");
        }
    }
    
    public void setMarkMode(boolean isInMarkMode){
        this.isInMarkMode = isInMarkMode;

        //Set isMarked so box isn't marked in the beginning
        this.isMarked = this.isInMarkMode;
        
        mark();
    }
    
    public boolean isMarked(){
        return isMarked;
    }
    
    public int getParcelId(){
        return id;
    }
}
