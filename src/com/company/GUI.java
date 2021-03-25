package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GUI extends DefaultTableModel {

    private JFrame mainMenu;
    private Container main;
    private GridBagConstraints c;
    private JLabel status;
    private JPanel buttonPanel;
    public JButton startSystem;
    private JButton pauseSystem;
    private JPanel time;
    private JLabel timeunit;
    private JLabel timeunit2;
    private JTextField timeUnitField;
    private JPanel cpuPanel1;
    private JLabel timeRemaining1;
    private JLabel cpu1;
    private JPanel tables;
    private DefaultTableModel queueTableModel;
    private DefaultTableModel reportsTableModel;
    private JTable queueTable;
    private JTable reportsTable;
    private JScrollPane queueScrollPane;
    private JScrollPane reportsScrollPane;
    String result;
    int cpuNumber1;
    String execStatus1;
    int tRemaining1;
    int cpuNumber2;
    String execStatus2;
    int tRemaining2;

    PC model;


    public GUI(PC model) {
        this.model = model;

        mainMenu = new JFrame("CPU Processor");
        mainMenu.setSize(1000, 750);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.getContentPane().setBackground(new Color(230, 245, 255));

        main = mainMenu.getContentPane();
        main.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 0, 0);    //Padding between edge of window and top row


        //Status Label
        status = new JLabel();
        status.setText("System Starting...");
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 10;
        main.add(status, c);

        //Button Panel
        buttonPanel = new JPanel(new GridLayout(1, 3, 45, 0));
        buttonPanel.setBackground(new Color(230, 245, 255));

        startSystem = new JButton("Start System");  //Start Button
        buttonPanel.add(startSystem);

        pauseSystem = new JButton("Pause System");  //Pause Button
        buttonPanel.add(pauseSystem);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        main.add(buttonPanel, c);

        //Time unit entry
        time = new JPanel(new FlowLayout());
        time.setBackground(new Color(230, 245, 255));
        timeunit = new JLabel();
        timeunit.setText("1 time unit = ");
        timeUnitField = new JTextField(Integer.toString(model.cpu1.getTimeScale()),10);
        timeUnitField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.cpu1.setTimeScale(Integer.parseInt(timeUnitField.getText()));
                model.cpu2.setTimeScale(Integer.parseInt(timeUnitField.getText()));
                Clock clock = Clock.getInstance();
                clock.setTimeStep(Integer.parseInt(timeUnitField.getText()));
            }
        });
        timeunit2 = new JLabel();
        timeunit2.setText("ms");
        time.add(timeunit);
        time.add(timeUnitField);
        time.add(timeunit2);

        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 10;
        c.weighty = 10;
        main.add(time, c);

        //CPU Panel (Will change this to a function later to create multiple CPUs and pass in variables)
        cpuNumber1 = 1;
        execStatus1 = model.cpu1.getStatus();
        tRemaining1 = 0;

        cpuPanel1 = new JPanel(new GridLayout(3, 1));
        cpuPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel1.setPreferredSize(new Dimension(200, 100));
        cpuPanel1.setBackground(new Color(255, 204, 204));

        cpu1 = new JLabel();  //CPU label
        cpu1.setText(model.cpu1.getName());
        cpuPanel1.add(cpu1);

        JLabel exec1 = new JLabel(); //Current process executing
        exec1.setText(execStatus1);
        //exec.addPropertyChangeListener("value", this);
        cpuPanel1.add(exec1);

        timeRemaining1 = new JLabel();    //Time remaining for current process
        timeRemaining1.setText(" Time remaining: " + tRemaining1);
        cpuPanel1.add(timeRemaining1);

        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel1, c);

        //        CPU PANEL 2
        cpuNumber2 = 2;
        execStatus2 = model.cpu2.getStatus();
        tRemaining2 = model.cpu2.getRunThis().getServiceTime();

        JPanel cpuPanel2 = new JPanel(new GridLayout(3, 1));
        cpuPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel2.setPreferredSize(new Dimension(200, 100));
        cpuPanel2.setBackground(new Color(255, 204, 204));

        JLabel cpu2 = new JLabel();  //CPU label
        cpu2.setText(model.cpu2.getName());
        cpuPanel2.add(cpu2);

        JLabel exec2 = new JLabel(); //Current process executing
        exec2.setText(execStatus2);
        //exec.addPropertyChangeListener("value", this);
        cpuPanel2.add(exec2);

        JLabel timeRemaining2 = new JLabel();    //Time remaining for current process
        timeRemaining2.setText(" Time remaining: " + tRemaining2);
        //timeRemaining.addPropertyChangeListener("value", this);
        cpuPanel2.add(timeRemaining2);

        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel2, c);

        tables = new JPanel(new GridLayout(2,1));
        tables.setPreferredSize(new Dimension(500,600));

        //Process Queue
        String[] queueColumnNames = {"Process Name", "Service Time"};
        queueTableModel = new DefaultTableModel(0,2);
        queueTableModel.setColumnIdentifiers(queueColumnNames);
        queueTable = new JTable(queueTableModel);
//        queueTable.setModel(queueTableModel);
        queueScrollPane = new JScrollPane(queueTable);
        tables.add(queueScrollPane);

        //Reports Area
        String[] reportsColumnNames = {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"};
        reportsTableModel = new DefaultTableModel(0,6);
        reportsTableModel.setColumnIdentifiers(reportsColumnNames);
        reportsTable = new JTable(reportsTableModel);
//        reportsTable.setModel(reportsTableModel);
        reportsScrollPane = new JScrollPane(reportsTable);
        tables.add(reportsScrollPane);

        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 3;
        main.add(tables, c);
        mainMenu.setVisible(true);

        try{
            result = JOptionPane.showInputDialog(mainMenu, "Enter the file path: ");
        } catch (HeadlessException e) {
            System.out.println("Error opening file.");
        }

        if (result == null) {
            return;
        }
//        reports.append("File Path entered: " + result + "\n");

        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateQueueTableView();
                exec1.setText("exec: " +model.cpu1.getRunThis().getProcessID());
                timeRemaining1.setText(" Time remaining: " + model.cpu1.getRunTime());
            }
        });

        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateQueueTableView();
                exec2.setText("exec: " +model.cpu2.getRunThis().getProcessID());
                timeRemaining2.setText(" Time remaining: " + model.cpu2.getRunTime());
            }
        });

        // Pause the System
        pauseSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Paused");
            }
        });

        // Start and play the system
        startSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Running");
            }
        });

    }

    public void run() {
    }

    public String getFilePath() {
        return result;
    }

    public void updateQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                queueTableModel.setRowCount(0);
                loadQueueTableData();

            }
        });
    }

    public DefaultTableModel getQueueTableModel() {
        return queueTableModel;
    }

    public JButton getStartSystem() {
        return startSystem;
    }

    public void setExecStatus(String s){
        this.execStatus1 = s;
        }
    public String getExecStatus(){
        return this.execStatus1;
    }
    public void setTRemaining(int i){
        this.tRemaining1 = i;
    }
    public int gettRemaining(){
        return this.tRemaining1;
    }

    public synchronized void loadQueueTableData(){
        try {
            for (int i = 0; i < model.processQueue.size(); i++) {
                queueTableModel.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()});
            }
            queueTable.setModel(queueTableModel);
        } catch (IndexOutOfBoundsException e){System.out.println("Out of bounds");}
    }
}
