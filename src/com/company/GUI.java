package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This implements the GUI interface made and the filepath input window.
 */
public class GUI extends DefaultTableModel {

    //Main frame variables
    private JFrame mainMenu;
    private Container main;
    private GridBagConstraints c;

    //Status Label variable
    private JLabel status;

    //Button Panel variables
    private JPanel buttonPanel;
    public JButton startSystem;
    private JButton pauseSystem;

    //Time Unit variables
    private JPanel time;
    private JLabel timeunit;
    private JLabel timeunit2;
    private JTextField timeUnitField;

    //CPU1 variables
    private JPanel cpuPanel1;
    private JLabel cpu1;
    private JLabel exec1;
    private JLabel timeRemaining1;
    String execStatus1;
    int tRemaining1;

    //CPU2 variables
    private JPanel cpuPanel2;
    private JLabel cpu2;
    private JLabel exec2;
    private JLabel timeRemaining2;
    String execStatus2;
    int tRemaining2;

    //Table variables
    private JPanel tables;
        //Process Queue Table variables
    private DefaultTableModel queueTableModel;
    private JTable queueTable;
    private JScrollPane queueScrollPane;
        //Reports Area Table variables
    private DefaultTableModel reportsTableModel;
    private JTable reportsTable;
    private JScrollPane reportsScrollPane;

    //Time Unit variables
    private JLabel currentThroughput;

    //Filepath Input variables
    String result;

    //PC model variable
    PC model;

    private double throughput;

    /**
     * This creates the main window and all of the components within that window.
     */
    public GUI(PC model) {
        this.model = model;

        //Creating the main window and setting its properties
        mainMenu = new JFrame("CPU Processor");
        mainMenu.setSize(1000, 750);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.getContentPane().setBackground(new Color(230, 245, 255));
        main = mainMenu.getContentPane();
        main.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 0, 0);    //Padding between edge of window and top row

        //Creating the Status Label
        status = new JLabel();
        status.setText("System Starting...");
            //Adding the Status Label to main frame
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 10;
        main.add(status, c);

        //Creating the Button Panel and setting its properties
        buttonPanel = new JPanel(new GridLayout(1, 3, 45, 0));
        buttonPanel.setBackground(new Color(230, 245, 255));
        //Creating the individual buttons and adding them to the Button Panel
            //Creating the Start System Button and adding the action listener
        startSystem = new JButton("Start System");  //Start Button
        buttonPanel.add(startSystem);
            //Creating the action listener for the Start System button
            //Starts and runs the system when the button is pressed
        startSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Running");
            }
        });
            //Creating the Pause System Button and adding the action listener
        pauseSystem = new JButton("Pause System");  //Pause Button
        buttonPanel.add(pauseSystem);
            //Creating the action listener for the Pause System button
            //Pauses the whole system when the button is pressed
        pauseSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // If the pause button was clicked, interrupt the clock and cpus
                model.throwClockInterrupt();
                model.throwCPUInterrupt();
                status.setText("System Paused");
            }
        });

        //Adding the Button Panel to the main frame
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        main.add(buttonPanel, c);

        //Creating the Time Unit Entry field and the action listener for the text entry
        //NOTE: You MUST press Enter after typing the Time Unit for it to be updated in the system
        time = new JPanel(new FlowLayout());
        time.setBackground(new Color(230, 245, 255));
        timeunit = new JLabel();
        timeunit.setText("1 time unit = ");
        timeUnitField = new JTextField(Integer.toString(model.cpu1.getTimeScale()),10);
            //Action listener for the data entered for Time Unit
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
            //Adding the Time Panel to the main frame
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 10;
        c.weighty = 10;
        main.add(time, c);

        //Creating the CPU Panels and setting their properties
            //Creating CPU1 Panel and text fields using the cpu1 information
        cpuPanel1 = new JPanel(new GridLayout(3, 1));
        cpuPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel1.setPreferredSize(new Dimension(200, 100));
        cpuPanel1.setBackground(new Color(255, 204, 204));
            //Creating CPU1 Label
        cpu1 = new JLabel();  //CPU label
        cpu1.setText(model.cpu1.getName());
        cpuPanel1.add(cpu1);
            //Creating Execution Label
        exec1 = new JLabel(); //Current process executing
        execStatus1 = model.cpu1.getStatus();
        exec1.setText(execStatus1);
        cpuPanel1.add(exec1);
            //Creating Time Remaining Label
        tRemaining1 = model.cpu1.getRunThis().getServiceTime();
        timeRemaining1 = new JLabel();    //Time remaining for current process
        timeRemaining1.setText(" Time remaining: " + tRemaining1);
        cpuPanel1.add(timeRemaining1);
            //Adding CPU1 to the main frame
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel1, c);
            //Creating the action listener for the CPU1 Panel to update the CPU1
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateQueueTableView();
                updateFinishedList();
                exec1.setText("exec: " +model.cpu1.getRunThis().getProcessID());
                timeRemaining1.setText(" Time remaining: " + model.cpu1.getRunTime());
            }
        });

            //Creating CPU2 Panel and text fields using the cpu2 information
        cpuPanel2 = new JPanel(new GridLayout(3, 1));
        cpuPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cpuPanel2.setPreferredSize(new Dimension(200, 100));
        cpuPanel2.setBackground(new Color(255, 204, 204));
            //Creating CPU2 Label
        cpu2 = new JLabel();  //CPU label
        cpu2.setText(model.cpu2.getName());
        cpuPanel2.add(cpu2);
            //Creating Execution Label
        exec2 = new JLabel(); //Current process executing
        execStatus2 = model.cpu2.getStatus();
        exec2.setText(execStatus2);
        cpuPanel2.add(exec2);
            //Creating Time Remaining Label
        tRemaining2 = model.cpu2.getRunThis().getServiceTime();
        timeRemaining2 = new JLabel();
        timeRemaining2.setText(" Time remaining: " + tRemaining2);
        cpuPanel2.add(timeRemaining2);
            //Adding CPU2 to the main frame
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 10;
        c.weighty = 5;
        main.add(cpuPanel2, c);
            //Creating the action listener for the CPU2 Panel to update the CPU2
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateQueueTableView();
                updateFinishedList();
                exec2.setText("exec: " +model.cpu2.getRunThis().getProcessID());
                timeRemaining2.setText(" Time remaining: " + model.cpu2.getRunTime());
            }
        });

        //Creating the Tables Panel and setting its properties
        tables = new JPanel(new GridLayout(2,1,0,15));
        tables.setPreferredSize(new Dimension(500,550));
        tables.setBackground(new Color(230, 245, 255));
            //Creating Process Queue Scrollable Table
        String[] queueColumnNames = {"Process Name", "Service Time"};
        queueTableModel = new DefaultTableModel(0,2);
        queueTableModel.setColumnIdentifiers(queueColumnNames);
        queueTable = new JTable(queueTableModel);
//        queueTable.setModel(queueTableModel);
        queueScrollPane = new JScrollPane(queueTable);
        tables.add(queueScrollPane);
            //Creating Reports Area Scrollable Table
        String[] reportsColumnNames = {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"};
        reportsTableModel = new DefaultTableModel(0,6);
        reportsTableModel.setColumnIdentifiers(reportsColumnNames);
        reportsTable = new JTable(reportsTableModel);
//        reportsTable.setModel(reportsTableModel);
        reportsScrollPane = new JScrollPane(reportsTable);
        tables.add(reportsScrollPane);
            //Adding the Tables Panel to the main frame
        c.gridx = 0;
        c.gridy = 1;
        c.gridheight = 3;
        main.add(tables, c);

        //Creating the Current Throughput Field
        currentThroughput = new JLabel();

        // Initialize the throughput display
        throughput = model.cpu1.getThroughput();
        if ( Double.isNaN(throughput)){
            throughput = 0.0;
        }
        throughput = model.cpu1.getThroughput();currentThroughput.setText("Current Throughput: " + throughput + " process/unit of time");

        //Create a listener on CPU1 because it updates throughput on process finish
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                throughput = model.cpu1.getThroughput();
                throughput = model.cpu1.getThroughput();currentThroughput.setText("Current Throughput: " + throughput + " process/unit of time");

            }
        });

        //Create a listener on CPU2 because it updates throughput on process finish
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                throughput = model.cpu2.getThroughput();
                throughput = model.cpu2.getThroughput();currentThroughput.setText("Current Throughput: " + throughput + " process/unit of time");
            }
        });

        // Show it
        currentThroughput.setFont(currentThroughput.getFont().deriveFont(20.0f));
            //Adding the Current Throughput Label to main frame
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        main.add(currentThroughput, c);

        mainMenu.setVisible(true);

        //TODO: Does this automatically close the program?
        //Create the file input window
        // Error if the file could not be open
        try{
            result = JOptionPane.showInputDialog(mainMenu, "Enter the file path: ");
        } catch (HeadlessException e) {
            System.out.println("Error opening file.");
        }

        if (result == null) {
            return;
        }
//        reports.append("File Path entered: " + result + "\n");
    }

    //TODO: This function has nothing in it. Is this intended?
    public void run() {
    }

    /**
     * This function returns the filepath entered at the beginning of the program.
     */
    public String getFilePath() {
        return result;
    }

    /**
     * This function calls the loadQueueTableData function to update the displayed Process Queue Table.
     */
    public void updateQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                queueTableModel.setRowCount(0);
                loadQueueTableData();

            }
        });
    }

    /**
     * This function updates the Reports Area Table as the Finished List of processes changes.
     */
    public void updateFinishedList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                reportsTableModel.setRowCount(0);
                loadFinishedList();
            }
        });
    }

    /**
     * This function returns the Table Model for the Process Queue.
     */
    public DefaultTableModel getQueueTableModel() {
        return queueTableModel;
    }

    /**
     * This function returns the Start System button.
     */
    public JButton getStartSystem() {
        return startSystem;
    }

    /**
     * This function sets the Execution Status to the appropriate Process that is on the CPU.
     */
    public void setExecStatus(String s){
        this.execStatus1 = s;
    }

    /**
     * This function returns the Execution Status of the referenced CPU (i.e. what Process is on the CPU).
     */
    public String getExecStatus(){
        return this.execStatus1;
    }

    /**
     * This function sets the Time Remaining for the Process that is on the CPU.
     */
    public void setTRemaining(int i){
        this.tRemaining1 = i;
    }

    /**
     * This function returns the Time Remaining for the Process that is on the CPU.
     */
    public int gettRemaining(){
        return this.tRemaining1;
    }

    /**
     * This function updates the Process Queue Table as the Process Queue changes.
     */
    public void loadQueueTableData(){
        synchronized (model.processQueue){
            try {
                for (int i = 0; i < model.processQueue.size(); i++) {
                    queueTableModel.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()});
                }
                queueTable.setModel(queueTableModel);
            } catch (IndexOutOfBoundsException e){System.out.println("PQ Out of bounds");}
        }
    }

    /**
     * This function updates the Reports Area Table as the Finished List of Processes changes.
     */
    public void loadFinishedList() {
        try {
            for (int i = 0; i < model.cpu1.getFinishedList().size(); i++) {
                reportsTableModel.addRow(new Object[]{String.valueOf(model.cpu1.getFinishedList().get(i).getProcessID()),
                        model.cpu1.getFinishedList().get(i).getArrivalTime(), model.cpu1.getFinishedList().get(i).getServiceTime(),
                        model.cpu1.getFinishedList().get(i).getFinishTime(), model.cpu1.getFinishedList().get(i).getTat(),
                        model.cpu1.getFinishedList().get(i).getnTat()});
                //System.out.println("asdf:" + i);
            }
            reportsTable.setModel(reportsTableModel);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FinishedList Out of bounds");
        } catch (NullPointerException e) {
            System.out.println("FinishedList nullptr");
        }
        //System.out.println("flist size:" + model.cpu1.getFinishedList().size());
    }
}
