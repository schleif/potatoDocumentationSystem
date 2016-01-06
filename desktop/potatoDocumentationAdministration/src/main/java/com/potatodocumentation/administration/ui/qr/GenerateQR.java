/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.qr;

import com.potatodocumentation.administration.MainApplication;
import com.potatodocumentation.administration.ui.field.FieldBox;
import com.potatodocumentation.administration.ui.field.map.FieldMap;
import com.potatodocumentation.administration.utils.QrUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.naming.Binding;

/**
 *
 * @author fiel
 */
public class GenerateQR extends Stage {

    BooleanProperty generatingProperty = new SimpleBooleanProperty(false);

    // Components
    Label title;

    FieldMap map = new FieldMap(false, true);
    ScrollPane mapPane = new ScrollPane(map);

    Button genSelectedButton;
    Button genAllbutton;
    ProgressIndicator progressIndicator = new ProgressIndicator(0);
    Label progressLabel = new Label("Fortschritt:");
    HBox footer;

    VBox contentBox;

    public GenerateQR() {
        super();

        //Init nodes
        title = initTitle();
        genAllbutton = initGenAllButton();
        genSelectedButton = initGenSelectedButton();
        footer = new HBox(10, genSelectedButton, genAllbutton, progressLabel, progressIndicator);

        contentBox = new VBox(10, title, map, footer);

        // Adding the Components
        Scene scene = new Scene(contentBox, 600, 700);

        this.setTitle("Neue Aufgabe hinzufügen");
        MainApplication.getInstance().setCSS(scene);
        this.setScene(scene);

    }

    private Label initTitle() {
        Label label = new Label("QR Codes generieren");
        label.setId("title");

        return label;
    }

    private Button initGenAllButton() {
        Button button = new Button("QR für alle erstellen");
        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> generateAll());
        });

        button.disableProperty().bind(generatingProperty);

        return button;
    }

    public Void generateAll() {

        generatingProperty.setValue(true);

        double size = (double) map.parcelCount();
        double i = 0;

        for (FieldBox field : map.getFields()) {
            for (Integer parId : field.getParcels()) {
                String fileName = "f" + field.getFieldId() + "p" + parId;
                String path = QrUtils.qrPath + "f" + field.getFieldId()
                        + "\\";
                String header = "Feld: " + field.getFieldId()
                        + ", Parzelle: " + parId;
                QrUtils.generateQrFromString(fileName, path, header);

                double progress = ++i / size;
                Platform.runLater(() -> progressIndicator.setProgress(progress));
            }
        }

        generatingProperty.setValue(false);

        return null;
    }

    private Button initGenSelectedButton() {
        Button button = new Button("QR für ausgewählte erstellen");
        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> generateSelected());
        });

        button.disableProperty().bind(generatingProperty);

        return button;
    }

    private Void generateSelected() {

        List<Integer> selected = map.getSelectedParcels();

        if (selected.isEmpty()) {
            return null;
        }
        
        //Indicate generating has begun
        generatingProperty.setValue(true);

        double size = (double) selected.size();
        double i = 0;

        for (FieldBox field : map.getFields()) {
            for (Integer parId : field.getParcels()) {
                if (!selected.contains(parId)) {
                    continue;
                }

                String fileName = "f" + field.getFieldId() + "p" + parId;
                String path = QrUtils.qrPath + "f" + field.getFieldId()
                        + "\\";
                String header = "Feld: " + field.getFieldId()
                        + ", Parzelle: " + parId;
                QrUtils.generateQrFromString(fileName, path, header);

                double progress = ++i / size;
                Platform.runLater(() -> progressIndicator.setProgress(progress));
            }
        }

        generatingProperty.setValue(false);

        return null;

    }

}
