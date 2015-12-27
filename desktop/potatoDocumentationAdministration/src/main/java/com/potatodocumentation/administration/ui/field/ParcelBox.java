/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 *
 * @author Ochi
 */
public class ParcelBox extends VBox {

    private int id;
    private String sort;

    private Label idLabel;
    private Label sortLabel;
    private AnchorPane header;

    public ParcelBox(int id, String sort) {
        super(2);

        this.id = id;
        this.sort = sort;

        idLabel = initIdLabel();
        sortLabel = initSortLabel();
        header = initHeader();

        VBox.setVgrow(header, Priority.NEVER);
        VBox.setVgrow(sortLabel, Priority.ALWAYS);

        setAlignment(Pos.CENTER);

        getChildren().add(header);
        getChildren().add(sortLabel);
    }

    private AnchorPane initHeader() {
        AnchorPane ap = new AnchorPane(idLabel);

        ap.setMinWidth(50);
        ap.setPrefWidth(50);

        AnchorPane.setLeftAnchor(idLabel, 1.0);

        return ap;
    }


    private Label initSortLabel() {
        Label label = new Label();

        if (sort == null || sort.isEmpty()) {
            label.setText("Keine");
            Font font = Font.getDefault();
            label.setFont(Font.font(font.getName(), FontPosture.ITALIC,
                    font.getSize()));
        } else {
            label.setText(sort);
        }

        return label;
    }

    private Label initIdLabel() {
        return new Label("ID: " + id);
    }




    public int getParcelId() {
        return id;
    }

}
