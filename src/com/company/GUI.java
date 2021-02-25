package com.company;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JPanel cpuPanel;
    private JLabel timeRemaining;
    private JLabel exec;
    private JLabel cpu;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollQueue;
    private JScrollPane scrollPane;
    public JTextArea reports;
    private JScrollPane scrollPane1;
    String result;


    public GUI() {
        //run();
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
        int cpuNumber = 1;
        String execStatus = "idle";
        String tRemaining = "8";

        cpuPanel = new JPanel(new GridLayout(3, 1));
        cpuPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel.setPreferredSize(new Dimension(200, 100));
        cpuPanel.setBackground(new Color(255, 204, 204));

        cpu = new JLabel();  //CPU label
        cpu.setText(" CPU " + cpuNumber);
        cpuPanel.add(cpu);

        exec = new JLabel(); //Current process executing
        exec.setText(" exec: " + execStatus);
        cpuPanel.add(exec);

        timeRemaining = new JLabel();    //Time remaining for current process
        timeRemaining.setText(" time remaining: " + tRemaining);
        cpuPanel.add(timeRemaining);

        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel, c);

        String[] columnNames = {"Process Name", "Service Time"};

        //TODO: Make the table uneditable
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        tableModel.setColumnIdentifiers(columnNames);
        //table.getTableHeader().setReorderingAllowed(false);
        //table.setModel(tableModel);
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

        result = JOptionPane.showInputDialog(mainMenu, "Enter the file path: ");
        if (result == null) {
            return;
        }
        reports.append("File Path entered: " + result + "\n");

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
        tableModel.setRowCount(0);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;

    }

    public JButton getStartSystem() {
        return startSystem;
    }
}
