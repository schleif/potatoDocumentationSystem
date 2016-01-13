/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observable;
import javafx.collections.ObservableList;

/**
 *
 * @author Ochi
 */
public class MiscUtils {

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if Collection b is a subset of collection b. Returns true if all
     * elements of b are contained in a. Returns null if not there is at least
     * one element of b contained in a but not all elements are. Else returns
     * false.
     *
     */
    public static <T> Boolean isSubset(Collection<T> a, Collection<T> b) {
        
        int count = 0;

        for (T x : b) {
            if (a.contains(x)) {
                count++;
            }
        }
        
        if(count < b.size() && count > 0){
            return null;
        }

        return (!b.isEmpty()) && count == b.size();
    }
    
    public static Integer getMax(Collection<String> collection){

        Integer max = null; 
        
        for(String x : collection){
            Integer  i = Integer.parseInt(x);
            if(max == null || i.compareTo(max) > 0){
                max = i;
            }
        }
        
        return max;
    }
    
    public static Collection<Integer> 
        parseCollectionToInteger(Collection<String> collection){
            
            Collection<Integer> newCol = new ArrayList<>();
        
            for(String s : collection){
                newCol.add(Integer.parseInt(s));
            }
            
            return newCol;
    }

}
