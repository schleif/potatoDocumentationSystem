/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 *
 * @author Ochi
 */
public class MenuButton extends Button{
    private final Pane pane;
    
    public MenuButton(String text, Node graphic, Pane pane){
        super(text, graphic);
        
        this.pane = pane;
    }
    
    public Pane getPane(){
        return this.pane;
    }
    
}
