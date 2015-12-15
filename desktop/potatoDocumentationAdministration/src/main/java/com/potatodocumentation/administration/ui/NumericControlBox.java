/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A simple HBox to control a numeric value by clicking increase / decrease
 * buttons. It consists of following nodes: A label named Title, a Button named
 * DecButton, a Label named Value and a Button names IncButton. By clicking the
 * DecButton the Value gets decresed to a given minimal Value, by clicking
 * IncButton it's gets increased to a given maximal value. The box is structured
 * as follows: Title | DecButton | Value | IncButton
 *
 * @author Ochi
 */
public class NumericControlBox extends HBox {

    int val;
    int minValue;
    int maxValue;

    Label title;
    Button decButton;
    Label value;
    Button incButton;

    public NumericControlBox(String title, int value, int minValue, int maxValue) {
        super(10);

        this.val = value;
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.title = new Label(title);
        this.decButton = initDecButton();
        this.incButton = initIncButton();
        this.value = new Label(Integer.toString(val));
        
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(this.title, this.decButton, this.value, 
                this.incButton);
    }

    private Button initDecButton() {
        Button button = new Button("-");

        button.setOnAction((ActionEvent e) -> {
            decreaseParPerRow();
        });

        return button;
    }

    private void decreaseParPerRow() {
        if (--val < minValue) {
            val = minValue;
        }
        value.setText(Integer.toString(val));
    }

    private Button initIncButton() {
        Button button = new Button("+");

        button.setOnAction((ActionEvent e) -> {
            increaseParPerRow();
        });

        return button;
    }

    private void increaseParPerRow() {
        if (++val > maxValue) {
            val = maxValue;
        }
        value.setText(Integer.toString(val));
    }
    
    public int getValue(){
        return val;
    }

}
