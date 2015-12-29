/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.potatodocumentation.administration.ui.TestPane;
import com.potatodocumentation.administration.ui.FootPane;
import com.potatodocumentation.administration.ui.menu.MenuPane;
import com.potatodocumentation.administration.ui.task.AufgabenPane;
import javafx.application.Application;
import javafx.event.EventType;
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
    
    private static MainApplication instance;
    
    MenuPane menu;
    BorderPane mainPane;
    Pane contentPane;
    Pane footer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //set the global instance
        instance = this;
        
        mainPane = new BorderPane();
        
        footer = new FootPane();
        menu = new MenuPane(this);
        contentPane = new AufgabenPane();        

        //Add panes/nodes
        mainPane.setBottom(footer);
        mainPane.setLeft(menu);
        mainPane.setCenter(contentPane);        
        
        BorderPane.setMargin(contentPane, new Insets(10, 10, 10, 10));
        
        Scene scene = new Scene(mainPane);
        //Add css
        scene.getStylesheets().add(getClass()
                .getResource("/styles/potatoStyle.css").toExternalForm());
        
         scene.getStylesheets().add(getClass()
                .getResource("/styles/scrollBarStyle.css").toExternalForm());
        
        primaryStage.setTitle("PotatoDocumetation");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    public void setContent(Pane contentPane){
        this.contentPane = contentPane;
        mainPane.setCenter(contentPane);
        BorderPane.setMargin(contentPane, new Insets(10, 10, 10, 10));
    }
    
    public void showLoadBar(boolean isLoading){
        ((FootPane) footer).setWork(isLoading);
    }
    
    public void updateConnections(){
        ((FootPane) footer).updateConnections();
    }
    
    public static MainApplication getInstance(){
        return instance;
    }
    
    public static void main(String[] args){
            launch(args);
    }
    
    public void go(){
        menu.rotateAll();
        TestPane.mediaPlayer.play();
    }
}
