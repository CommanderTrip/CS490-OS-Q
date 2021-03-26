package com.company;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controller {
    public PC model;
    public GUI view;
    public Clock clock;

    public Controller(PC model, GUI view) {
        this.model = model;
        this.view = view;
        this.createQueue();
        view.loadQueueTableData();
        view.startSystem.addActionListener(new ActionListener() {
            //Action listener for the start button on the GUI.
            @Override
            public void actionPerformed(ActionEvent e) {
                //If the process queue is not empty: do these things
                if (!model.processQueue.isEmpty()) {
                    model.start();

                    // Get the clock and set a thread for it.
                    Thread clock = model.getThread(Clock.getInstance());
                    clock.start();

                    // Get the CPU threads and start them
                    Thread cpu1Thread = model.getThreadCPU1(model.cpu1);
                    Thread cpu2Thread = model.getThreadCPU2(model.cpu2);
                    cpu1Thread.start();
                    cpu2Thread.start();

                    //view.reports.append(model.cpu1.getRunThis().getProcessID()
                    //        + " ran for:  " + model.cpu1.getRunThis().getServiceTime() + " seconds." + "\n");
                }

            }
        });
    }
    //Initial creation of the process queue from a user inputted file path
    public void createQueue() {model.ReadFromFile(view.getFilePath());}

    //Returns the process queue
    public ArrayList<Process> getQueue() {return model.processQueue;}

    //Updates the GUI view of the table of processes
    public void updateTableView() {view.updateQueueTableView();}

    //Method to populate the table of processes in the GUI
    /*public void populateTable() {
        DefaultTableModel table = view.getTableModel();
        for (int i = 0; i < model.processQueue.size(); i++) {
            table.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()});
        }
    }*/

    public PC getModel(){
        return this.model;
    }
}


