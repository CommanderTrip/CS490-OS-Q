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

    //Status Panel variables
    private JPanel statusPanel;
    private JLabel status;
    public JButton startSystem;
    private JButton pauseSystem;
    private JPanel time;
    private JLabel timeunit;
    private JLabel timeunit2;
    private JTextField timeUnitField;

    //Queue and Reports column names for both RR and HRRN
    private String[] queueColumnNames = {"Process Name", "Service Time"};
    private String[] reportsColumnNames = {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"};

    //HRRN Panel variables
    //CPU1 variables
    private JPanel cpuPanel1;
    private JLabel cpu1;
    private JLabel exec1;
    private JLabel timeRemaining1;
    String execStatus1;
    int tRemaining1;
    //Table variables
    private JPanel hrrnTables;
    //Process Queue Table variables
    private DefaultTableModel hrrnQueueTableModel;
    private JTable hrrnQueueTable;
    private JScrollPane hrrnQueueScrollPane;
    //Reports Area Table variables
    private DefaultTableModel hrrnReportsTableModel;
    private JTable hrrnReportsTable;
    private JScrollPane hrrnReportsScrollPane;
    //Throughput variables
    private JLabel hrrnCurrentThroughput;
    private double hrrnThroughput;

    //RR Panel variables
    //CPU2 variables
    private JPanel cpuPanel2;
    private JLabel cpu2;
    private JLabel exec2;
    private JLabel timeRemaining2;
    String execStatus2;
    int tRemaining2;
    private JPanel timeSlice;
    private JLabel timeSliceLength;
    private JTextField timeSliceField;
    //Table variables
    private JPanel rrTables;
    //Process Queue Table variables
    private DefaultTableModel rrQueueTableModel;
    private JTable rrQueueTable;
    private JScrollPane rrQueueScrollPane;
    //Reports Area Table variables
    private DefaultTableModel rrReportsTableModel;
    private JTable rrReportsTable;
    private JScrollPane rrReportsScrollPane;
    //Throughput variables
    private JLabel rrCurrentThroughput;
    private double rrThroughput;

    //Filepath Input variables
    String result;

    //PC model variable
    PC model;


    /**
     * This creates the main window and all of the components within that window.
     */
    public GUI(PC model) {
        this.model = model;

        //Creating the main window and setting its properties
        mainMenu = new JFrame("CPU Processor");
        mainMenu.setSize(1250, 750);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.getContentPane().setBackground(new Color(230, 245, 255));
        main = mainMenu.getContentPane();
        main.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(15, 0, 0, 0);    //Padding between edge of window and top row

        //Creating the Button Panel and setting its properties
        statusPanel = new JPanel(new GridLayout(1, 4, 45, 0));
        statusPanel.setBackground(new Color(230, 245, 255));
        //Creating the individual buttons and adding them to the Button Panel
            //Creating the Start System Button and adding the action listener
        startSystem = new JButton("Start System");  //Start Button
        statusPanel.add(startSystem);
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
        statusPanel.add(pauseSystem);
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
        //Creating the Status Label
        status = new JLabel();
        status.setText("System Starting...");
        statusPanel.add(status);

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
                //TODO: IS THE BELOW LINE SUPPOSED TO BE COMMENTED?
                //model.cpu2.setTimeScale(Integer.parseInt(timeUnitField.getText()));
                Clock clock = Clock.getInstance();
                clock.setTimeStep(Integer.parseInt(timeUnitField.getText()));
            }
        });
        timeunit2 = new JLabel();
        timeunit2.setText("ms");
        time.add(timeunit);
        time.add(timeUnitField);
        time.add(timeunit2);
        statusPanel.add(time);

        //Adding the Button Panel to the main frame
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.weightx = 1;
        main.add(statusPanel, c);

        //Creating CPU HRRN Panel
        //Creating CPU HRRN Panel and text fields using the cpu1 information
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
        //Creating the action listener for the CPU1 Panel to update the CPU1
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateHrrnQueueTableView();
                updateHrrnFinishedList();
                exec1.setText("exec: " +model.cpu1.getRunThis().getProcessID());
                timeRemaining1.setText(" Time remaining: " + model.cpu1.getRunTime());
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridheight = 1;
        main.add(cpuPanel1, c);


        //Creating the Tables Panel and setting its properties
        hrrnTables = new JPanel(new GridLayout(2,1,0,15));
        hrrnTables.setPreferredSize(new Dimension(500,450));
        hrrnTables.setBackground(new Color(230, 245, 255));
        //Creating Process Queue Scrollable Table
        hrrnQueueTableModel = new DefaultTableModel(0,2);
        hrrnQueueTableModel.setColumnIdentifiers(queueColumnNames);
        hrrnQueueTable = new JTable(hrrnQueueTableModel);
        //queueTable.setModel(queueTableModel);
        hrrnQueueScrollPane = new JScrollPane(hrrnQueueTable);
        hrrnTables.add(hrrnQueueScrollPane);
        //Creating Reports Area Scrollable Table
        hrrnReportsTableModel = new DefaultTableModel(0,6);
        hrrnReportsTableModel.setColumnIdentifiers(reportsColumnNames);
        hrrnReportsTable = new JTable(hrrnReportsTableModel);
        //reportsTable.setModel(reportsTableModel);
        hrrnReportsScrollPane = new JScrollPane(hrrnReportsTable);
        hrrnTables.add(hrrnReportsScrollPane);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.gridheight = 3;
        c.gridwidth = 2;
        main.add(hrrnTables, c);

        //Creating the Current Throughput Field
        hrrnCurrentThroughput = new JLabel();
        // Initialize the throughput display
        hrrnThroughput = model.cpu1.getThroughput();
        if ( Double.isNaN(hrrnThroughput)){
            hrrnThroughput = 0.0;
        }
        hrrnThroughput = model.cpu1.getThroughput();
        hrrnCurrentThroughput.setText("Current Throughput: " + hrrnThroughput + " process/unit of time");
        //Create a listener on CPU1 because it updates throughput on process finish
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                hrrnThroughput = model.cpu1.getThroughput();
                hrrnThroughput = model.cpu1.getThroughput();
                hrrnCurrentThroughput.setText("Current Throughput: " + hrrnThroughput + " process/unit of time");

            }
        });
        // Show it
        hrrnCurrentThroughput.setFont(hrrnCurrentThroughput.getFont().deriveFont(16.0f));
        //Adding the Current Throughput Label to main frame
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.weightx = 0.5;
        main.add(hrrnCurrentThroughput, c);




        //Creating CPU RR Panel
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
        //tRemaining2 = model.cpu2.getRunThis().getServiceTime();
        timeRemaining2 = new JLabel();
        timeRemaining2.setText(" Time remaining: " + tRemaining2);
        cpuPanel2.add(timeRemaining2);
        //Creating the action listener for the CPU2 Panel to update the CPU2
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                updateRrQueueTableView();
                updateRrFinishedList();
                exec2.setText("exec: " +model.cpu2.getRunThis().getProcessID());
                timeRemaining2.setText(" Time remaining: " + model.cpu2.getRunTime());
            }
        });
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridheight = 1;
        main.add(cpuPanel2, c);

        //Creating the Time Unit Entry field and the action listener for the text entry
        //NOTE: You MUST press Enter after typing the Time Unit for it to be updated in the system
        timeSlice = new JPanel(new FlowLayout());
        timeSlice.setBackground(new Color(230, 245, 255));
        timeSliceLength = new JLabel();
        timeSliceLength.setText("Round Robin Time Slice Length");
        timeSliceField = new JTextField(Integer.toString(model.cpu2.getTimeQuantum()),10);
        timeSliceField.setColumns(4);
        //Action listener for the data entered for Time Slice Length
        timeSliceField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.cpu2.setTimeQuantum(Integer.parseInt(timeSliceField.getText()));
                //System.out.println(model.cpu2.getTimeQuantum());
            }
        });
        timeSlice.add(timeSliceLength);
        timeSlice.add(timeSliceField);
        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0,0,0,50);
        main.add(timeSlice, c);


        //Creating the Tables Panel and setting its properties
        rrTables = new JPanel(new GridLayout(2,1,0,15));
        rrTables.setPreferredSize(new Dimension(500,450));
        rrTables.setBackground(new Color(230, 245, 255));
        //Creating Process Queue Scrollable Table
        rrQueueTableModel = new DefaultTableModel(0,2);
        rrQueueTableModel.setColumnIdentifiers(queueColumnNames); //Uses same column names as hrrn
        rrQueueTable = new JTable(rrQueueTableModel);
        //queueTable.setModel(queueTableModel);
        rrQueueScrollPane = new JScrollPane(rrQueueTable);
        rrTables.add(rrQueueScrollPane);
        //Creating Reports Area Scrollable Table
        rrReportsTableModel = new DefaultTableModel(0,6);
        rrReportsTableModel.setColumnIdentifiers(reportsColumnNames); //Uses same column names as hrrn
        rrReportsTable = new JTable(rrReportsTableModel);
        //reportsTable.setModel(reportsTableModel);
        rrReportsScrollPane = new JScrollPane(rrReportsTable);
        rrTables.add(rrReportsScrollPane);
        //Creating the Current Throughput Field
        rrCurrentThroughput = new JLabel();
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0.5;
        c.gridheight = 3;
        c.gridwidth = 2;
        c.insets = new Insets(0,0,0,0);
        main.add(rrTables, c);

        // Initialize the throughput display
        rrThroughput = model.cpu2.getThroughput();
        if ( Double.isNaN(rrThroughput)){
            rrThroughput = 0.0;
        }
        rrThroughput = model.cpu2.getThroughput();
        rrCurrentThroughput.setText("Current Throughput: " + rrThroughput + " process/unit of time");
        //Create a listener on CPU2 because it updates throughput on process finish
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                rrThroughput = model.cpu2.getThroughput();
                rrThroughput = model.cpu2.getThroughput();
                rrCurrentThroughput.setText("Current Throughput: " + rrThroughput + " process/unit of time");
            }
        });

        // Show it
        rrCurrentThroughput.setFont(rrCurrentThroughput.getFont().deriveFont(16.0f));
        //Adding the Current Throughput Label to main frame
        c.gridx = 2;
        c.gridy = 5;
        c.gridheight = 1;
        c.weightx = 0.5;
        main.add(rrCurrentThroughput, c);

        mainMenu.setVisible(true);

        //Create the file input window
        //Exits program if the file could not be open
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

    /**
     * This function returns the filepath entered at the beginning of the program.
     */
    public String getFilePath() {
        return result;
    }

    /**
     * This function calls the loadQueueTableData function to update the displayed Process Queue Table.
     */
    public void updateHrrnQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                hrrnQueueTableModel.setRowCount(0);
                loadHrrnQueueTableData();

            }
        });
    }
    /**
     * This function calls the loadQueueTableData function to update the displayed Process Queue Table.
     */
    public void updateRrQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                rrQueueTableModel.setRowCount(0);
                loadRrFinishedList();

            }
        });
    }

    /**
     * This function updates the Reports Area Table as the Finished List of processes changes.
     */
    public void updateHrrnFinishedList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                hrrnReportsTableModel.setRowCount(0);
                loadHrrnFinishedList();
            }
        });
    }
    /**
     * This function updates the Reports Area Table as the Finished List of processes changes.
     */
    public void updateRrFinishedList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                rrReportsTableModel.setRowCount(0);
                loadRrFinishedList();
            }
        });
    }

    /**
     * This function returns the Table Model for the Process Queue.
     */
    public DefaultTableModel getHrrnQueueTableModel() {
        return hrrnQueueTableModel;
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
    public void loadHrrnQueueTableData(){
        synchronized (model.processQueue){
            try {
                for (int i = 0; i < model.processQueue.size(); i++) {
                    hrrnQueueTableModel.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()});
                }
                hrrnQueueTable.setModel(hrrnQueueTableModel);
            } catch (IndexOutOfBoundsException e){System.out.println("PQ Out of bounds");}
        }
    }

    /**
     * This function updates the Process Queue Table as the Process Queue changes.
     */
    public void loadRrQueueTableData(){
        synchronized (model.processQueue2){
            try {
                for (int i = 0; i < model.processQueue2.size(); i++) {
                    rrQueueTableModel.addRow(new Object[]{String.valueOf(model.processQueue2.get(i).getProcessID()), model.processQueue2.get(i).getServiceTime()});
                }
                rrQueueTable.setModel(rrQueueTableModel);
            } catch (IndexOutOfBoundsException e){System.out.println("PQ Out of bounds");}
        }
    }
    /**
     * This function updates the Reports Area Table as the Finished List of Processes changes.
     */
    public void loadHrrnFinishedList() {
        try {
            for (int i = 0; i < model.cpu1.getFinishedList().size(); i++) {
                hrrnReportsTableModel.addRow(new Object[]{String.valueOf(model.cpu1.getFinishedList().get(i).getProcessID()),
                        model.cpu1.getFinishedList().get(i).getArrivalTime(), model.cpu1.getFinishedList().get(i).getServiceTime(),
                        model.cpu1.getFinishedList().get(i).getFinishTime(), model.cpu1.getFinishedList().get(i).getTat(),
                        model.cpu1.getFinishedList().get(i).getnTat()});
                //System.out.println("asdf:" + i);
            }
            hrrnReportsTable.setModel(hrrnReportsTableModel);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FinishedList Out of bounds");
        } catch (NullPointerException e) {
            System.out.println("FinishedList nullptr");
        }
        //System.out.println("flist size:" + model.cpu1.getFinishedList().size());
    }
    /**
     * This function updates the Reports Area Table as the Finished List of Processes changes.
     */
    public void loadRrFinishedList() {
        try {
            for (int i = 0; i < model.cpu2.getFinishedList().size(); i++) {
                rrReportsTableModel.addRow(new Object[]{String.valueOf(model.cpu2.getFinishedList().get(i).getProcessID()),
                        model.cpu2.getFinishedList().get(i).getArrivalTime(), model.cpu2.getFinishedList().get(i).getServiceTime(),
                        model.cpu2.getFinishedList().get(i).getFinishTime(), model.cpu2.getFinishedList().get(i).getTat(),
                        model.cpu2.getFinishedList().get(i).getnTat()});
                //System.out.println("asdf:" + i);
            }
            rrReportsTable.setModel(rrReportsTableModel);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FinishedList Out of bounds");
        } catch (NullPointerException e) {
            System.out.println("FinishedList nullptr");
        }
        //System.out.println("flist size:" + model.cpu2.getFinishedList().size());
    }
}
