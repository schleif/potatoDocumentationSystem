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

        getChildren().add(processLabel);
        getChildren().add(icon);
        
        setWork(false);
    }

    private ImageView initIcon() {
        Image ico = new Image(
                getClass().getResourceAsStream("/drawables/loadIcon2_16x16.gif")
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

    /**
     * This functions is the main controll Element for the Footer.
     * @param isWorking true if the bar should shown
     */
    public final void setWork(boolean isWorking) {
        processLabel.setVisible(isWorking);
        icon.setVisible(isWorking);
    }

}
