/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field.map;

import com.potatodocumentation.administration.ui.field.ParcelBox;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultArray;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Ochi
 */
public class ParcelMap extends VBox {

    private int fieldID;
    private List<String> taggedParcels;
    private List<ParcelBox> parcels;
    private List<ParcelBox> markedParcels;
    private boolean isInMarkMode = false;

    private Button updateButton;
    private Button editButton;
    private Button markButton;
    private HBox buttonBox;

    private VBox parcelBox;

    public ParcelMap(int fieldID, List<String> taggedParcels) {
        super(10);

        this.fieldID = fieldID;
        this.taggedParcels
                = (taggedParcels == null ? new ArrayList<>() : taggedParcels);

        this.parcels = new ArrayList<>();
        this.markedParcels = new ArrayList<>();

        updateButton = initUpdateButton();
        editButton = initEditButton();
        markButton = initMarkButton();
        buttonBox = initButtonBox();

        parcelBox = new VBox(2);

        getChildren().addAll(buttonBox, parcelBox);
    }

    public Void updateParcelBox() {

        Platform.runLater(() -> parcelBox.getChildren().clear());
        parcels.clear();

        HashMap<String, String> params = new HashMap<>();
        params.put("feld_nr", Integer.toString(fieldID));

        ObservableList<String> rows
                = getJsonResultObservableList("parz_row",
                        "selectParzellenRows.php", params);

        //Iterate trough all rows
        for (String row : rows) {
            HBox rowBox = new HBox(2);

            params.put("parz_row", row);
            ArrayList<Map<String, Object>> resultArray
                    = getJsonResultArray("selectParzellenByRow.php", params);

            for (Map<String, Object> parcelMap : resultArray) {
                String idString = (String) parcelMap.get("parz_id");
                int id = Integer.parseInt(idString);

                String sorte = (String) parcelMap.get("sorte");

                ParcelBox parcel = new ParcelBox(id, sorte);
                
                //TODO: Übersichtlicher gestalten
                
                parcel.setOnMouseClicked((MouseEvent event) -> {
                    parcel.mark();
                    if(parcel.isMarked()){
                        System.out.println("Marked: " + parcel.getParcelId());
                        markedParcels.add(parcel);
                    } else {
                        System.out.println("Unmarked: " + parcel.getParcelId());
                        markedParcels.remove(parcel);
                    }
                    
                    for(ParcelBox p : markedParcels){
                        System.out.println(p.getParcelId());
                    }
                });
                
                parcels.add(parcel);
                rowBox.getChildren().add(parcel);
            }

            Platform.runLater(() -> parcelBox.getChildren().add(rowBox));
        }

        return null;
    }

    private Button initUpdateButton() {
        Button button = new Button(" ");
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updateParcelBox());
        });

        return button;
    }

    private Button initEditButton() {
        Button editButton = new Button("Bearbeite markierte");

        editButton.setVisible(false);

        return editButton;
    }

    private Button initMarkButton() {
        Button button = new Button("Markieren");

        button.setOnAction((ActionEvent event) -> {
            onMarkButtonPressed();
        });

        return button;
    }

    private HBox initButtonBox() {
        HBox hBox = new HBox(editButton, markButton, updateButton);
        hBox.setAlignment(Pos.CENTER_RIGHT);

        return hBox;
    }

    private void onMarkButtonPressed() {
        isInMarkMode = !isInMarkMode;

        editButton.setVisible(isInMarkMode);

        markedParcels.clear();

        for (ParcelBox parcel : parcels) {
            parcel.setMarkMode(isInMarkMode);
        }
    }
}
