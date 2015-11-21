/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import static com.potatodocumentation.administration.utils.JsonUtils.*;
import com.potatodocumentation.administration.utils.ThreadUtils;
import java.util.HashMap;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Ochi
 */
public class PropertyPane extends HBox {

    Label taskLabel;
    Label propertyLabel;
    Label dateLabel;
    Label parzellenLabel;
    Label detailLabel;
    ListView<String> taskList;
    ListView<String> propertyList;
    ListView<String> parzellenList;
    ListView<String> dateList;
    VBox detailBox;
    VBox propertyBox;
    AnchorPane propertyBoxHeader;
    AnchorPane detailBoxHeader;
    Button updateButton;
    Button deleteButton;
    Button createButton;
    private String activeProperty;

    public PropertyPane() {
        super(10);
        setFillHeight(true);

        //Initialize nodes
        //PropertyBox
        propertyLabel = initPropertyLabel();
        updateButton = initUpdateButton();
        propertyList = initPropertyList();
        createButton = initCreateButton();
        propertyBoxHeader = initPropertyBoxHeader();
        propertyBox = initPropertyBox();

        //OptionBox
        detailLabel = initDetailLabel();
        deleteButton = initDeleteButton();
        detailBoxHeader = initDetailBoxHeader();

        //DetailBox
        taskLabel = initTaskLabel();
        taskList = initTaskList();
        detailBox = initDetailBox();

        initLayout();

        //Add needed children
        getChildren().add(propertyBox);
        getChildren().add(detailBox);

        //Update TaskList on startup
        ThreadUtils.runAsTask(() -> updatePropertyList());

    }

    private ListView<String> initTaskList() {

        ObservableList<String> items = FXCollections.observableArrayList("Keine Aufgabe ausgewählt");

        ListView<String> listView = new ListView<>(items);

        return listView;

    }

    private Label initTaskLabel() {
        return new Label("Verknüpfte Aufgaben:");
    }

    private Label initPropertyLabel() {
        return new Label("Eigenschaften:");
    }

    private ListView<String> initPropertyList() {
        
        ListView<String> listView = new ListView<>();

        //Add listener on change of selected value
        listView.getSelectionModel().selectedItemProperty()
                .addListener(
                        (ObservableValue<? extends String>
                                observable,
                                String oldValue,
                                String newValue) -> {
            onPropertyListValueChanged();
        });

        return listView;
        
    }

    private VBox initDetailBox() {
        VBox vBox = new VBox(10);

        //Set ID for the css
        vBox.setId("detailBox");
        
        setHgrow(vBox, Priority.ALWAYS);

        return vBox;
// throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void initLayout() {

        detailBox.getChildren().add(detailBoxHeader);

        HBox detailSouth = new HBox(10);

        VBox taskVBox = new VBox(10);
        taskVBox.getChildren().addAll(taskLabel, taskList);

        detailSouth.getChildren().addAll(taskVBox);

        detailSouth.getChildren().stream().forEach((child) -> {
            HBox.setMargin(child, new Insets(10));
        });

        detailBox.getChildren().add(detailSouth);
    }

    private void onPropertyListValueChanged() {
        activeProperty = propertyList.getSelectionModel().getSelectedItem();

        if (activeProperty == null) {
            return;
        }
        ThreadUtils.runAsTask(() -> updateTaskList());

        deleteButton.setDisable(false);

        detailLabel.setText("Details (" + activeProperty +")");
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updatePropertyList() {
        
        Platform.runLater(() -> updateButton.setDisable(true));
        
        ObservableList<String> items =
                FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> propertyList.setItems(items));

        ObservableList<String> newItems = getJsonResultObservableList(
                "eig_name", "selectEigenschaft.php", null);

        Platform.runLater(() -> propertyList.setItems(newItems));
        Platform.runLater(() -> updateButton.setDisable(false));

        return null;
    }



    private Label initDetailLabel() {
        Label label = new Label("Details");
        label.setId("detailLabel");

        return label;

    }

    private Button initDeleteButton() {
        Image deleteIcon = new Image(
                getClass().getResourceAsStream(
                                "/drawables/deleteIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button("Löschen", new ImageView(deleteIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setDisable(true);
        button.setId("deleteButton");

        return button;
    }

    private Button initCreateButton() {
        Image createIcon = new Image(
                getClass().getResourceAsStream(
                        "/drawables/createIcon.png"),
                16.0, 16.0, true, true);

        Button button = new Button(
                "Neue Eigenschaft erstellen", new ImageView(createIcon));
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("createButton");

        button.setOnAction((ActionEvent event) -> {
            CreateNewProp stage = new CreateNewProp();
            stage.initModality(Modality.APPLICATION_MODAL);
            
            //Refresh the TaskList after the window is closed
            stage.setOnCloseRequest((WindowEvent event1) -> {
                ThreadUtils.runAsTask(() -> updatePropertyList());
            });
            stage.show();
        });

        return button;
    }

    private AnchorPane initDetailBoxHeader() {
        AnchorPane anchorPane
                = new AnchorPane(detailLabel, deleteButton);

        AnchorPane.setLeftAnchor(detailLabel, 10.0);
        AnchorPane.setRightAnchor(deleteButton, 10.0);

        anchorPane.getChildren().stream().forEach((child) -> {
            AnchorPane.setTopAnchor(child, 10.0);
        });

        return anchorPane;
    }

    private VBox initPropertyBox() {
        VBox vBox = new VBox(propertyBoxHeader, propertyList, createButton);

        vBox.getChildren().stream().forEach((child) -> {
            VBox.setMargin(child, new Insets(10));
        });

        return vBox;
    }

    private AnchorPane initPropertyBoxHeader() {
        AnchorPane anchorPane = new AnchorPane(propertyLabel, updateButton);

        AnchorPane.setLeftAnchor(propertyLabel, 10.0);
        AnchorPane.setRightAnchor(updateButton, 10.0);

        anchorPane.getChildren().stream().forEach((child) -> {
            AnchorPane.setTopAnchor(child, 10.0);
        });

        return anchorPane;
    }

    private Button initUpdateButton() {

        Image updateIcon = new Image(
                getClass().getResourceAsStream("/drawables/updateIcon.png"),
                16.0, 16.0, true, true);

        ImageView imageView = new ImageView(updateIcon);

        Button button = new Button("Neu Laden", imageView);
        button.setContentDisplay(ContentDisplay.RIGHT);
        button.setId("updateButton");

        button.setOnAction((ActionEvent event) -> {
            ThreadUtils.runAsTask(() -> updatePropertyList());
            
            //Rotate the icon
            RotateTransition rotate =
                    new RotateTransition(Duration.seconds(2), imageView);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(1);
            rotate.setInterpolator(Interpolator.EASE_BOTH);
            rotate.play();
        });

        return button;
    }

    /**
     * This method should be called in another Task since it establishes a
     * network connection! Therefore it uses Platform.runLater() to update the
     * UI of the Application.
     */
    private Void updateTaskList() {
        ObservableList<String> items =
                FXCollections.observableArrayList("Lädt...");
        Platform.runLater(() -> taskList.setItems(items));
        
        HashMap params = new HashMap();
        params.put("eig_name", activeProperty);

        ObservableList<String> tasks
                = getJsonResultObservableList(
                        "aufg_name",
                        "selectAufgabeByEigenschaft.php",
                        params);

        Platform.runLater(() -> taskList.setItems(tasks));


        return null;
    }

}
