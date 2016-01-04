/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    /**
     * Indicated the width of all ParcelBoxes t
     */
    public static DoubleProperty maxWidth = new SimpleDoubleProperty(10);

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

        bindWidth();

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

    private void bindWidth() {
        //Set maxWidth on width change
        widthProperty().addListener((ObservableValue<? extends Number> ov, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() > maxWidth.doubleValue()) {
                try {
                    maxWidth.setValue(newValue);
                } catch (Exception e) {

                }
            }
        });

        minWidthProperty().bind(maxWidth);
    }

}
