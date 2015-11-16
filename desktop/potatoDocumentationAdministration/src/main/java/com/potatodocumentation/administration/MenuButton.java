/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class MenuButton extends Button{
    private final Pane pane;
    
    public MenuButton(String text, Node graphic, Pane pane){
        super(text, graphic);
        
        this.pane = pane;
        
        
        RotateTransition rt = new RotateTransition(Duration.seconds(new Random().nextInt(10)), this);
            rt.setCycleCount(rt.INDEFINITE);
            rt.setByAngle(360);

            rt.setAutoReverse(true);

            rt.play();
    }
    
    public Pane getPane(){
        return this.pane;
    }
    
}
