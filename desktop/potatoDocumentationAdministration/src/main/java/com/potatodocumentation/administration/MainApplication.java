/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Ochi
 */
public class MainApplication extends Application {

    MenuPane menu;
    BorderPane mainPane;
    Pane contentPane;
    Pane footer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainPane = new BorderPane();

        menu = new MenuPane(this);
        contentPane = new AufgabenPane();        
        footer = new FootPane();

        //Add panes/nodes
        mainPane.setLeft(menu);
        mainPane.setCenter(contentPane);        
        mainPane.setBottom(footer);
        
        
        BorderPane.setMargin(contentPane, new Insets(10, 10, 10, 10));
        
        Scene scene = new Scene(mainPane);
        //Add css
        scene.getStylesheets().add(getClass()
                .getResource("/styles/potatoStyle.css").toExternalForm());
        
        primaryStage.setTitle("PotatoDocumetation");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Test if footer works 
        // Unfortunally java based the footer has to be casted to own class
        FootPane castFooter = (FootPane) footer;
        castFooter.setWork(false);
        // castFooter.setWork(true);

    }
    
    public void setContent(Pane contentPane){
        this.contentPane = contentPane;
        mainPane.setCenter(contentPane);
    }
    
    public static void main(String[] args){
            launch(args);
    }
}
