/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author fiel
 */
public class FieldPane extends FlowPane {

    Button createField = new Button("+");

    int count = 0;

    public FieldPane() {
        super();
        this.setVgap(10);
        this.setHgap(10);
        this.setPadding(new Insets(30, 30, 30, 30));
        createField.setStyle("-fx-font-size: 100");
        this.getChildren().add(createField);
        createField.setOnAction((ActionEvent event) -> {
            onPlusClicked();
        });
        count++;
        for (int i = 1; i <= 3; i++) {
            newField(i);
            count++;
        }
    }

    private void newField(int index) {
        String text = Integer.toString(index);
        Button but = new Button(text);
        but.setStyle("-fx-font-size: 100");
        this.getChildren().add(0, but);
    }

    private void onPlusClicked() {
        newField(count++);
    }

}
