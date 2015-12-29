/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import com.potatodocumentation.administration.ui.field.map.ParcelMap;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

/**
 *
 * @author Ochi
 */
public class FieldBox extends VBox {

    private int fieldID;
    private ParcelMap parcel;
    private List<Integer> selectedParcels;

    private Label label;

    public FieldBox(int fieldID, List<Integer> selectedParcels) {
        super(10);
        
        setStyle();

        //Init private variables
        this.fieldID = fieldID;

        label = initLabel();
        this.selectedParcels = selectedParcels;

        parcel = new ParcelMap(fieldID, selectedParcels);

        //Set OnClick
        setOnMouseClicked((MouseEvent e) -> {
            openFieldStage(fieldID);
        });

        //add nodes
        getChildren().add(label);
    }

    private Label initLabel() {
        String labelString = String.format("%3d", fieldID);

        return new Label(labelString);
    }

    private void openFieldStage(int field) {
        FieldStage stage = new FieldStage(field, parcel);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    private void setStyle() {
        //minHeightProperty().bind(widthProperty().multiply(1.5));
        setAlignment(Pos.CENTER);
    }

    public List<Integer> getParcels() {
        return parcel.getParcels();
    }

    //Updates parcelmap in SAME thread. Can be called as new task 
    public Void update() {
        parcel.updateParcelBox();
        return null;
    }

    public ParcelMap getParcel() {
        return parcel;
    }

    public int getFieldId() {
        return fieldID;
    }

}
