/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.field;

import com.potatodocumentation.administration.ui.field.map.FieldMap;
import com.potatodocumentation.administration.utils.QrUtils;
import com.potatodocumentation.administration.utils.ThreadUtils;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 *
 * @author fiel
 */
public class FieldPane extends VBox {

    private Label title;
    private AnchorPane header;
    private FieldMap fieldMap;
    private ScrollPane mapPane;
    Button qrButton;

    public FieldPane() {
        super(10);

        title = initTitle();
        qrButton = initQrButton();
        header = initHeader();
        fieldMap = new FieldMap(true, true);
        mapPane = new ScrollPane(fieldMap);

        getChildren().add(header);
        getChildren().add(mapPane);

        fieldMap.setStyle("-fx-font-size: " + 20 + ";");
    }

    private AnchorPane initHeader() {
        AnchorPane anchorPane = new AnchorPane(title, qrButton);

        AnchorPane.setLeftAnchor(title, 10.0);
        AnchorPane.setRightAnchor(qrButton, 10.0);

        return anchorPane;
    }

    private Label initTitle() {
        Label title = new Label("Felder");
        title.setId("title");
        return title;
    }

    private Button initUpdateButton() {

        Image updateIcon = new Image(getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> fieldMap.update());

            //Rotate the icon
            RotateTransition rotate = new RotateTransition(Duration.seconds(2), imageView);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;

    }

    private Button initQrButton() {
        Button button = new Button("QR");

        //Create QR-Codes for all parcels with header
        button.setOnAction((ActionEvent event) -> {
            for (FieldBox field : fieldMap.getFields()) {
                for (Integer parId : field.getParcels()) {

                    String fileName = "f" + field.getFieldId() + "p" + parId;
                    String path = QrUtils.qrPath + "f" + field.getFieldId()
                            + "\\";
                    String header = "Feld: " + field.getFieldId()
                            + ", Parzelle :" + parId;
                    QrUtils.generateQrFromString(fileName, path, header);

                }
            }
        });

        return button;
    }

}
