/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.layout.TilePane;
import javafx.util.Duration;

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
            button.setOnAction((ActionEvent event) -> {
                mainApp.setContent(button.getPane());
            });

            getChildren().add(button);
        }
    }

    public static ArrayList<MenuButton> menuButtons() {
        ArrayList<MenuButton> menuButtons = new ArrayList<>();

        MenuButton testButton = new MenuButton("Test", null, new TestPane());
        
        // Adding Buttons
        menuButtons.add(new MenuButton("Aufgaben", null, new AufgabenPane()));
        menuButtons.add(new MenuButton("Eigenschaften", null, new PropertyPane()));
        menuButtons.add(new MenuButton("Sorten", null, new PotatoSpeciesPane()));
        menuButtons.add(new MenuButton("Felder", null, new FieldPane()));
        menuButtons.add(new MenuButton("Felder 2.0", null, new FieldPane2()));
        menuButtons.add(new MenuButton("Parzellen", null, new ParcelPane()));
        menuButtons.add(testButton);

        testButton.setVisible(false);
        
        final Glow glow = new Glow();
        glow.setLevel(1.0);
        testButton.setEffect(glow);

        final Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(glow.levelProperty(), 0.3);
        final KeyFrame kf = new KeyFrame(Duration.millis(700), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        return menuButtons;
    }

    public void setContentToParcel(String fieldname) {

    }
    
    public void rotateAll(){
        
        
        for(Node n : menuButtons){

            n.setVisible(true);
            
        RotateTransition rt = new RotateTransition(Duration.seconds(new Random().nextInt(10)), n);
            rt.setCycleCount(rt.INDEFINITE);
            rt.setByAngle(360);

            rt.setAutoReverse(true);

            rt.play();
        }
    }

}
