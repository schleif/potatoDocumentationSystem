/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.potatodocumentation.administration;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author fiel
 */
public class DefineParcel extends JFrame {

    // Frame measures
    int frameWidth = 900;
    int frameHeight = 600;

    // Combobox: show all parcels
    JComboBox parcelComboBox;

    // List: show all the tasks for the selected parcel
    JList taskList;

    // Button: for creating new task
    JButton newTask;

    public DefineParcel(String title) {
        // set Frame title
        super(title);
        // Default oprerations
        initFrame();

        Container cp = getContentPane();
        cp.setLayout(null);

        // adding ComboBox parcel
        cp.add(initComboBox());

        // adding task list with pre selectedItem
        cp.add(initTasks((String) parcelComboBox.getSelectedItem()));

        // adding new task button
        cp.add(initTaskButton());

        this.setVisible(true);
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(frameWidth, frameHeight);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;
        setLocation(x, y);
        setResizable(true);
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JComboBox initComboBox() {
        // String[] getParcels = getParcels();
        String[] parcels = {"p1", "p2", "p3"};
        parcelComboBox = new JComboBox(parcels);
        parcelComboBox.setBounds(50, 50, 100, 30);
        return parcelComboBox;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String[] getParcels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JScrollPane initTasks(String selectedParcel) {
        // String[] getTasks = getTasks(selectedParcel);
        String[] tasks = {"t1", "t2"};
        taskList = new JList(tasks);
        taskList.setLayoutOrientation(ListSelectionModel.SINGLE_SELECTION);
        taskList.setLayoutOrientation(JList.VERTICAL);

        JScrollPane listScroller = new JScrollPane(taskList);
        listScroller.setBounds(180, 50, 100, 180);
        return listScroller;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String[] getTasks(String selectedParcel) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JButton initTaskButton() {
        newTask = new JButton("neue Aufgabe");
        newTask.addActionListener((ActionEvent e) -> {
            onNewTaskButtonClicked();
        });
        newTask.setBounds(310, 50, 120, 30);
        return newTask;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void onNewTaskButtonClicked() {
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
