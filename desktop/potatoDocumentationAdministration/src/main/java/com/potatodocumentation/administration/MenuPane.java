/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;

/**
 *
 * @author Ochi
 */
public class MenuPane extends TilePane {
    
    private ArrayList<MenuButton> menuButtons;
    private MainApplication mainApp;
    
    public MenuPane(MainApplication mainApp) {
        super(Orientation.HORIZONTAL);
        
        this.menuButtons = MenuPane.menuButtons();
        populateMenu();
        
        this.mainApp = mainApp;

        //style
        setPrefColumns(1);
        setMinWidth(TilePane.USE_PREF_SIZE);
        
    }
    
    private void populateMenu() {
        for (MenuButton button : menuButtons) {
            //Make all Buttons same width
            button.setMinWidth(Button.USE_PREF_SIZE);
            button.setMaxWidth(Double.MAX_VALUE);
            
            button.setId("menuButton");

            //set onClick event
            button.setOnAction(new EventHandler<ActionEvent>() {
                
                @Override
                public void handle(ActionEvent event) {
                    mainApp.setContent(button.getPane());
                }
            });
            
            getChildren().add(button);
        }
    }
    
    public static ArrayList<MenuButton> menuButtons() {
        ArrayList<MenuButton> menuButtons = new ArrayList<>();
        
        //Testing FootPane;
        FootPane fp = new FootPane();
        fp.setWork(true);
        
        // Adding Buttons
        menuButtons.add(new MenuButton("Aufgaben", null, new AufgabenPane()));
        menuButtons.add(new MenuButton("Eigenschaften", null, new PropertyPane()));
        menuButtons.add(new MenuButton("Sorten", null, new PotatoSpeciesPane()));
        menuButtons.add(new MenuButton("Test", null, fp));
        
        return menuButtons;
    }
    
}
