/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author fiel
 */
public class ParcelPane extends BorderPane {

    // Components
    ComboBox fieldChooser;

    public ParcelPane() {
        super();
        // Init north
        fieldChooser = initFieldChooser();
        this.setTop(fieldChooser);

    }

    private ComboBox initFieldChooser() {
        ObservableList<String> items = getJsonResultObservableList(
                "parz_id", "selectParzelle.php", null);

        ComboBox cb = new ComboBox(items);
        return cb;
    }

}
