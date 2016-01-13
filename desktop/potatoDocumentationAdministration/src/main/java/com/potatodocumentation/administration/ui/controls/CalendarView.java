/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.ui.controls;

import com.potatodocumentation.administration.LocalDateInterval;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

/**
 *
 * @author Ochi
 */
public class CalendarView extends BorderPane {

    private DatePicker datePicker;
    private DatePickerSkin skin;
    private Node popup;
    Collection<LocalDateInterval> dates;

    public CalendarView() {
        this(new ArrayList<LocalDateInterval>());
    }

    public CalendarView(Collection<LocalDateInterval> dates) {

        this.dates = dates;
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setDayCellFactory(dayCellFactory());

        skin = new DatePickerSkin(datePicker);
        popup = skin.getPopupContent();

        setCenter(popup);
    }

    private Callback<DatePicker, DateCell> dayCellFactory() {

        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                for (LocalDateInterval interval : dates) {
                                    if (interval.includes(item)) {
                                        
                                        getStyleClass().remove("day-cell");
                                        
                                        if (item.equals(interval.getFrom())) {
                                            if (item.equals(interval.getTo())) {
                                                getStyleClass().add("single-date");
                                            } else {
                                                getStyleClass().add("beginning-date");
                                            }
                                        } else if (item.equals(interval.getTo())) {
                                            getStyleClass().add("ending-date");
                                        } else {
                                            getStyleClass().add("included-date");
                                        }

                                    }
                                }
                                
                            }
                        };
                    }
                };

        return dayCellFactory;
    }

    public void setDates(Collection<LocalDateInterval> dates) {
        this.dates = dates;
        datePicker = new DatePicker(LocalDate.now());
        datePicker.setDayCellFactory(dayCellFactory());

        skin = new DatePickerSkin(datePicker);
        popup = skin.getPopupContent();

        setCenter(popup);
    }

}
