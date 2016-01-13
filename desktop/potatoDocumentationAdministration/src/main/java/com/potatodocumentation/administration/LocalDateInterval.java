/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Ochi
 */
public class LocalDateInterval {

    private LocalDate from;
    private LocalDate to;
    boolean valid = true;

    public LocalDateInterval(LocalDate from, LocalDate to) {

        //'from' must be <= 'to'
        if (from == null || to == null || from.compareTo(to) > 0) {
            valid = false;
        }

        this.from = from;
        this.to = to;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    public boolean isValid() {
        return valid;
    }

    public static LocalDateInterval parse(String from, String to) {

        LocalDate fromParsed = null;
        LocalDate toParsed = null;

        try {
            fromParsed = LocalDate.parse(from);
            toParsed = LocalDate.parse(to);
        } catch (DateTimeParseException e) {
        }

        return new LocalDateInterval(fromParsed, toParsed);
    }
    
    public boolean includes(LocalDate date){
        return isValid() && date.compareTo(from) >= 0 
                && date.compareTo(to) <= 0;
    }
    
    public String toString(){
        return "[" + from + " - " + to + "]";
    }

}
