/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui;

import java.applet.AudioClip;
import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author fiel
 */
public class TestPane extends HBox {

    Label l;
    Button b;

    Button recursiveButton;
    ScrollPane scrollPane;
    
    public static MediaPlayer mediaPlayer;

    public TestPane() {
        super(10);

        l = initLLabel();

        b = initBButton();

        recursiveButton = initRecursiveButton(30);

        scrollPane = new ScrollPane();
        scrollPane.setContent(recursiveButton);

        this.getChildren().add(scrollPane);

        Media sound = new Media("http://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3");
        mediaPlayer = new MediaPlayer(sound);
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
        if (i > 0) {
            Button button = new Button(Integer.toString(i), initRecursiveButton(i - 1));
            boolean mod = i % 2 == 0;

            button.setContentDisplay(ContentDisplay.CENTER);

            RotateTransition rt = new RotateTransition(Duration.seconds(i * 10), button);
            rt.setCycleCount(rt.INDEFINITE);
            rt.setByAngle(360);

            rt.setAutoReverse(true);

            rt.play();
            
            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), button);
            tt.setFromX(5);
            tt.setFromY(5);
            tt.setToX(15);
            tt.setToY(15);
            tt.setCycleCount(tt.INDEFINITE);
            tt.setAutoReverse(true);
            tt.play();

            return button;
        } else {
            return new Button("Letzter!");
        }
    }

}
