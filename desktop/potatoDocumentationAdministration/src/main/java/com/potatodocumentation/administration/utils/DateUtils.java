/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author Ochi
 */
public class DateUtils {

    public static LocalDate goToDay(int day, LocalDate date) {
        int initDay = date.getDayOfWeek().getValue();
        int goalDay = day;

        //calculate how many days left
        int daysLeft = (7 - (initDay - goalDay)) % 7;

        return date.plusDays(daysLeft);
    }

    public static LocalDate goToDay(DayOfWeek day, LocalDate date) {
        return goToDay(day.getValue(), date);
    }

    public static LocalDate goToLastDayOfMonth(LocalDate date) {
        int year = date.getYear();
        Month month = date.getMonth();
        int dayOfMonth = month.length(date.isLeapYear());

        return LocalDate.of(year, month, dayOfMonth);
    }

    public static LocalDate goToLastDayOfYear(LocalDate date) {
        //Get the max days of the year
        int maxDays = (date.isLeapYear() ? 366 : 365);

        return LocalDate.ofYearDay(date.getYear(), maxDays);
    }
}
