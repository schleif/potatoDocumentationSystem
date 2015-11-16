/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author fiel
 */
public class TestPane extends HBox{
    
    Label l;
    Button b;
    
    Button recursiveButton;
    
    ScrollPane scrollPane;
    
    public TestPane() {
        super(10);
        
        l = initLLabel();
        
        b = initBButton();
        
        recursiveButton = initRecursiveButton(100);
        
        
        scrollPane = new ScrollPane();
        scrollPane.setContent(recursiveButton);
        
        
        this.getChildren().add(scrollPane);
    }

    private Button initBButton() {
        Button innerButton = new Button("Button in Button");
        innerButton.setOnAction((ActionEvent) -> {
            onInnerButtonClicked();
        });
        Button outerButton = new Button("Crazy Shiet!", innerButton);
        outerButton.setOnAction((ActionEvent -> {
            onOuterButtonClicked();
        }));
        return outerButton;
    }

    private Label initLLabel() {
        Label l = new Label("Ausgabe: ");
        return l;
    }

    private void onInnerButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    private void onOuterButtonClicked() {
        throw new UnsupportedOperationException("Not supported yet.");
        //To change body of generated methods, choose Tools | Templates.
    }

    private Button initRecursiveButton(int i) {
        if(i>0) {
            return new Button(Integer.toString(i), initRecursiveButton(i-1));
        } else {
            return new Button("Letzter!");
        }
    }
    
}
