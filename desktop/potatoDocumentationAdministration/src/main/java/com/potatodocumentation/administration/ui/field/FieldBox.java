/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import com.potatodocumentation.administration.ui.field.map.ParcelMap;
import com.potatodocumentation.administration.utils.MiscUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.List;
import java.util.Observable;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
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
    private ObservableList<Integer> selectedParcels;

    private Label label;
    private final boolean isEditable;
    private final boolean isSelectable;

    public FieldBox(int fieldID, ObservableList<Integer> selectedParcels,
            boolean editable, boolean selectable) {
        super(10);

        setStyle();

        //Init private variables
        this.fieldID = fieldID;
        this.isEditable = editable;
        this.isSelectable = selectable;

        label = initLabel();
        this.selectedParcels = selectedParcels;

        parcel = new ParcelMap(fieldID, selectedParcels, isEditable,
                isSelectable);

        //Set OnClick
        setOnMouseClicked(openFieldStageHandler());

        //add nodes
        getChildren().add(label);
    }

    private Label initLabel() {
        String labelString = String.format("%3d", fieldID);

        return new Label(labelString);
    }

    public EventHandler openFieldStageHandler() {
        EventHandler mouseClickHandler
                = (EventHandler<MouseEvent>) (MouseEvent event) -> {
                    FieldStage stage = new FieldStage(fieldID, parcel);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.show();
                };

        return mouseClickHandler;
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
    
    public boolean isFullySelected(){
        
        Boolean bool = MiscUtils.isSubset(selectedParcels, getParcels());
        
        boolean result = (bool != null) && bool.equals(Boolean.TRUE);
        
        return result;
    }

}
