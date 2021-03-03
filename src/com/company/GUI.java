package com.company;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.event.ChangeListener;
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
    private JLabel exec1;
    private JLabel cpu1;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollQueue;
    private JScrollPane scrollPane;
    public JTextArea reports;
    private JScrollPane scrollPane1;
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
        buttonPanel = new JPanel(new GridLayout(1, 2, 45, 0));
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
        timeUnitField = new JTextField(10);
        timeUnitField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        c.gridy = 4;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel2, c);

        String[] columnNames = {"Process Name", "Service Time"};

        //TODO: Make the table uneditable
        tableModel = new DefaultTableModel(0,2);
        table = new JTable(tableModel);

        tableModel.setColumnIdentifiers(columnNames);
        //tableModel.addRow();
        //table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        //scrollQueue = new JScrollPane(table);
        //add(scrollQueue, BorderLayout.CENTER);

        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.gridheight = 2;
        scrollPane = new JScrollPane(table);
        main.add(scrollPane, c);

        //Reports Area
        reports = new JTextArea(10, 10);
        scrollPane1 = new JScrollPane(reports);
        reports.setEditable(false);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.fill = 1;
        c.insets = new Insets(0, 30, 30, 30);  //Adds padding around the text area
        main.add(scrollPane1, c);

        mainMenu.setVisible(true);

        try{
            result = JOptionPane.showInputDialog(mainMenu, "Enter the file path: ");
        } catch (HeadlessException e) {
            System.out.println("Error opening file.");
        }

        if (result == null) {
            return;
        }
        reports.append("File Path entered: " + result + "\n");

        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateTableView();
                exec1.setText("exec: " +model.cpu1.getRunThis().getProcessID());
                timeRemaining1.setText(" Time remaining: " + model.cpu1.getRunTime());
            }
        });

        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateTableView();
                exec2.setText("exec: " +model.cpu2.getRunThis().getProcessID());
                timeRemaining2.setText(" Time remaining: " + model.cpu2.getRunTime());
            }
        });
        pauseSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Paused");
            }
        });
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

    public void updateTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                tableModel.setRowCount(0);
                loadTableData();
            }
        });
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
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

    public synchronized void loadTableData(){
        for (int i = 0; i < model.processQueue.size(); i++) {
            tableModel.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()});
        }
        table.setModel(tableModel);
    }
}
