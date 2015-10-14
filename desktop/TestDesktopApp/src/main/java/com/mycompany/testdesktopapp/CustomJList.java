/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testdesktopapp;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author Ochi
 */
public class CustomJList extends JList<Object>{
    
    private DefaultListModel listModel;
    
    public CustomJList(){
        super();
    }
    
    public CustomJList(DefaultListModel listModel){
        super(listModel);
        
        this.listModel = listModel;
    }
    
    public void setModel(DefaultListModel listModel){
        super.setModel(listModel);
        
        this.listModel = listModel;
    }
    
    public void addListElement(String e){
        listModel.addElement(e);
    }
    
    public void clearList(){
        listModel.clear();
    }
}
