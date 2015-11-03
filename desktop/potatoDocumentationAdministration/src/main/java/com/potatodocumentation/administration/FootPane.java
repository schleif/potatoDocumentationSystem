/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 *
 * @author fiel
 */
class FootPane extends HBox {
    
    ImageView icon;
    Label processLabel;

    public FootPane() {
        super(2);
        this.setFillHeight(true);
        this.setAlignment(Pos.BOTTOM_RIGHT);
        
        //Initialize nodes
        icon = initIcon();
        processLabel = initProcessLabel();
        
        getChildren().add(icon);
        getChildren().add(processLabel);
    }

    private ImageView initIcon() {
        Image ico = new Image(
                getClass().getResourceAsStream("/drawables/loadIcon20.gif")
                // 16.0, 16.0, true, true
        );
        ImageView iv = new ImageView(ico);
        iv.setSmooth(true);
        iv.setCache(true);
        return iv;
    }

    private Label initProcessLabel() {
        return new Label("Beschäftigt...");
    }
    
    public void setWork(boolean isWorking) {
        if(isWorking) {
            processLabel.setText("Beschäftigt...");
            icon.setImage(new Image(getClass().getResourceAsStream("/drawables/loadIcon20.gif")));
        } else {
            processLabel.setText("");
            icon.setImage(null);
        }
    }
    
}
