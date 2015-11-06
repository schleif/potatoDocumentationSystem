/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import static com.potatodocumentation.administration.utils.JsonUtils.getJsonResultObservableList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
                null, "selectParzelle.php", null);

        ComboBox cb = new ComboBox(items);
        return cb;
    }

}
