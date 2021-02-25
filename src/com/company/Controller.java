package com.company;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

public class Controller {
    public PC model;
    public GUI view;
    //private ArrayList<Process> processQueue;

    public Controller(PC model, GUI view) {
        this.model = model;
        this.view = view;
        //view.getFilePath();
        createQueue();
        populateTable();
        view.startSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!model.processQueue.isEmpty()) {
                    //model.processQueue.remove(0);
                    //populateTable();
                    //view.getTableModel().fireTableDataChanged();
                    model.start();
                    view.reports.append(model.cpu1.runThis.processID + " ran for:  " + model.cpu1.runThis.serviceTime + " seconds." + "\n");
                }
                view.updateTableView();
                populateTable();
            }
        });
    }

    public void createQueue() {
        model.processQueue = model.ReadFromFile(view.getFilePath());
    }

    //public void setPath(String path){
//        model.setFilePath(path);
    //  }
    //public String getPath(){ return model.getFilePath(); }

    public int queueSize() {
        return model.processQueue.size();
    }

    public ArrayList<Process> getQueue() {
        return model.processQueue;
    }

    public void updateTableView() {
        view.updateTableView();
    }

    public void populateTable() {
        DefaultTableModel table = view.getTableModel();
        for (int i = 0; i < model.processQueue.size(); i++) {
            table.addRow(new Object[]{String.valueOf(model.processQueue.get(i).processID), model.processQueue.get(i).serviceTime});

        }
    }
}


