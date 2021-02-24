package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    GUI(){}

    public void run(){

        // Create the main frame and layout
        JFrame mainMenu = new JFrame("CPU Processor");
        mainMenu.setSize(1000, 750);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.getContentPane().setBackground(new Color(230, 245, 255));

        Container main = mainMenu.getContentPane();
        main.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20,0,0,0);    //Padding between edge of window and top row

        //Status Label
        JLabel status = new JLabel();
        status.setText("System Starting...");
        c.gridx = 2; c.gridy = 0;
        c.weightx = 10;
        main.add(status, c);

        //Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1,2,45,0));
        buttonPanel.setBackground(new Color(230, 245, 255));

        JButton startSystem = new JButton("Start System");  //Start Button
        buttonPanel.add(startSystem);

        JButton pauseSystem = new JButton("Pause System");  //Pause Button
        buttonPanel.add(pauseSystem);


        //Button Functionality
        startSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Running");
            }
        });

        pauseSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Paused");
            }
        });

        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2;
        main.add(buttonPanel, c);


        //Time unit entry
        JPanel time = new JPanel(new FlowLayout());
        time.setBackground(new Color(230, 245, 255));
        JLabel timeunit = new JLabel();
        timeunit.setText("1 time unit = ");
        JTextField timeUnitField = new JTextField(10);
        JLabel timeunit2 = new JLabel();
        timeunit2.setText("ms");
        time.add(timeunit);
        time.add(timeUnitField);
        time.add(timeunit2);

        c.gridx = 2; c.gridy = 1;
        c.weightx = 10; c.weighty = 10;
        main.add(time, c);


        //CPU Panel (Will change this to a function later to create multiple CPUs and pass in variables)
        int cpuNumber = 1;
        String execStatus = "idle";
        String tRemaining = "8";

        JPanel cpuPanel = new JPanel(new GridLayout(3,1));
        cpuPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel.setPreferredSize(new Dimension(200,100));
        cpuPanel.setBackground(new Color(255, 204, 204));

        JLabel cpu = new JLabel();  //CPU label
        cpu.setText(" CPU " + cpuNumber);
        cpuPanel.add(cpu);

        JLabel exec = new JLabel(); //Current process executing
        exec.setText(" exec: " + execStatus);
        cpuPanel.add(exec);

        JLabel timeRemaining = new JLabel();    //Time remaining for current process
        timeRemaining.setText(" time remaining: " + tRemaining);
        cpuPanel.add(timeRemaining);

        c.gridx = 2; c.gridy = 2;
        c.weightx = 10; c.weighty = 5;
        main.add(cpuPanel, c);

        //Process table
        //This data is just filler for now, it will come from the processes read from the file
        Object[][] data = {
                {"process a",10},
                {"process b",13}
        };
        String[] columnNames = {"Process Name", "Service Time"};

        //TODO: Make the table uneditable
        JTable table = new JTable(data, columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        c.gridx = 0; c.gridy = 1;
        c.ipadx = 0;
        c.gridheight = 2;
        JScrollPane scrollPane = new JScrollPane(table);
        main.add(scrollPane, c);

        //Reports Area
        JTextArea reports = new JTextArea(10,10);
        JScrollPane scrollPane1 = new JScrollPane(reports);
        reports.setEditable(false);
        c.gridx = 0; c.gridy = 3;
        c.gridwidth = 2;
        c.fill = 1;
        c.insets = new Insets(0,30,30,30);  //Adds padding around the text area
        main.add(scrollPane1, c);

        mainMenu.setVisible(true);
    }
}
