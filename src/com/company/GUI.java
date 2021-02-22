package com.company;

import javax.swing.*;
import java.awt.*;

public class GUI {
    GUI(){}

    public void run(){
        JFrame mainMenu = new JFrame("CPU Processor");
        mainMenu.setSize(1000, 1000);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container main = mainMenu.getContentPane();
        main.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        //Start Button
        JButton startSystem = new JButton("Start System");
        c.gridx = 0; c.gridy = 0;
        c.weighty =0.5;
        main.add(startSystem, c);

        //Pause Button
        JButton pauseSystem = new JButton("Pause System");
        c.gridx = 1; c.gridy = 0;
        c.ipadx =0;
        main.add(pauseSystem, c);

        //Status Label
        JLabel status = new JLabel();
        status.setText("System Starting...");
        c.gridx = 2; c.gridy = 0;
        c.weightx = 10;
        main.add(status, c);


        //Time unit enter
        JPanel time = new JPanel(new FlowLayout());
        JLabel timeunit = new JLabel();
        timeunit.setText("1 time unit = ");
        JTextField timeUnitField = new JTextField(10);
        JLabel timeunit2 = new JLabel();
        timeunit2.setText("ms");
        time.add(timeunit);
        time.add(timeUnitField);
        time.add(timeunit2);

        c.gridx = 2; c.gridy = 1;
        c.weightx = 10;
        main.add(time, c);






        //Table
        Object[][] data = {
                {0,0},
                {0,0}
        };
        String[] coulumnNames = {"Process Name", "Service Time"};
        JTable table = new JTable(data, coulumnNames);
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
        main.add(scrollPane1, c);

        mainMenu.setVisible(true);
    }
}
