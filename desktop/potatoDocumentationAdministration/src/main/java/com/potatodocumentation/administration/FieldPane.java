/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import com.potatodocumentation.administration.utils.JsonUtils;
import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    ObservableList<String> field;

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
        ThreadUtils.runAsTask(() -> updateFieldList());
    }

    private void newField(int index) {
        String text = Integer.toString(index);
        Button but = new Button(text);
        but.setStyle("-fx-font-size: 100");
        this.getChildren().add(0, but);
    }

    private void onPlusClicked() {
        JsonUtils.getJsonSuccessStatus("insertFeld.php");
        ThreadUtils.runAsTask(() -> updateFieldList());
    }

    private Void updateFieldList() {
        ObservableList<String> newItems = getJsonResultObservableList(
                "feld_id", "selectFeld.php", null);
        field = newItems.sorted();
        this.getChildren().clear();
        this.getChildren().add(createField);
        Iterator<String> fieldIte = field.iterator();
        while (fieldIte.hasNext()) {
            newField(Integer.parseInt(fieldIte.next()));
        }
        return null;
    }

}
