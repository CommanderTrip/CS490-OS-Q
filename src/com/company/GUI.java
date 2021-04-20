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
    private JFrame mainMenu;    //Main window
    private Container main; //Container of the main window to hold the components of the GUI
    private GridBagConstraints c;   //Constraints for the GridBagLayout

    //Status Panel variables
    private JPanel statusPanel; //Panel that holds the start and pause buttons, the status label, and the time unit field
    private JLabel status;  //Displays the system status (starting, running, or paused)
    public JButton startSystem; //Button to start the system execution of processes
    private JButton pauseSystem;    //Button to pause the system
    private JPanel time;    //Used to layout the time entry field
    private JLabel timeunit;    //Displays "1 time unit ="
    private JLabel timeunit2;   //Displays "ms"
    private JTextField timeUnitField;   //Text field to hold the user's time unit input

    //Queue and Reports column names for both RR and HRRN
    private String[] queueColumnNames = {"Process Name", "Service Time"};   //Column names for the Waiting Queue tables
    private String[] reportsColumnNames = {"Process Name", "Arrival Time", "Service Time", "Finish Time", "TAT", "nTAT"};   //Column names for the Finished Queue tables

    //HRRN Panel variables
        //CPU1 variables
    private JPanel cpuPanel1;   //Container representing CPU1 (HRRN)
    private JLabel cpu1;    //Displays CPU name: "CPU HRRN"
    private JLabel exec1;   //Displays the execution status of CPU HRRN (idle or process name)
    private JLabel timeRemaining1;  //Displays the time remaining of the current process on CPU HRRN
    String execStatus1; //Variable that contains the execution status to be displayed
    int tRemaining1;    //Variable that contains the time remaining of the current process on CPU HRRN
        //Table variables
    private JPanel hrrnTables;  //Container that holds the Waiting and Finished Queue tables for CPU HRRN
        //Process Queue Table variables
    private DefaultTableModel hrrnQueueTableModel;  //The table model used for the waiting queue for CPU HRRN
    private JTable hrrnQueueTable;  //The table that displays the waiting queue for CPU HRRN
    private JScrollPane hrrnQueueScrollPane;    //Scollpane for the CPU HRRN waiting queue table if more processes are in the queue than can be displayed in one frame
        //Reports Area Table variables
    private DefaultTableModel hrrnReportsTableModel;    //The table model used for the finished queue for CPU HRRN
    private JTable hrrnReportsTable;    //The table that displays the finished queue for CPU HRRN
    private JScrollPane hrrnReportsScrollPane;  //Scrollpane for the CPU HRRN finished queue table if more processes are in the finished queue than can be displayed in one frame
        //nTAT variables
    private JLabel hrrnCurrentnTAT; //Displays the current nTAT for CPU HRRN
    private double hrrnnTAT;    //Variable that contains the current nTAT value for CPU HRRN to be displayed

    //RR Panel variables
        //CPU2 variables
    private JPanel cpuPanel2;   //Container representing CPU2 (RR)
    private JLabel cpu2;    //Displays CPU name: "CPU RR"
    private JLabel exec2;   //Displays the execution status of CPU RR (idle or process name)
    private JLabel timeRemaining2;  //Displays the time remaining of the current process on CPU RR
    String execStatus2; //Variable that contains the execution status to be displayed
    int tRemaining2;    //Variable that contains the time remaining of the current process on CPU RR
    private JPanel timeSlice;   //Container used to format the time slice length entry field
    private JLabel timeSliceLength; //Displays "Round Robin Time Slice Length"
    private JTextField timeSliceField;  //Text field to hold the user's time slice length input
        //Table variables
    private JPanel rrTables;    //Container that holds the Waiting and Finished Queue tables for CPU RR
        //Process Queue Table variables
    private DefaultTableModel rrQueueTableModel;    //The table model used for the waiting queue for CPU RR
    private JTable rrQueueTable;    //The table that displays the waiting and ready queues for CPU RR
    private JScrollPane rrQueueScrollPane;  //Schollpane for the CPU RR waiting/ready queue table if more processes are in the queue than can be displayed in one frame
        //Reports Area Table variables
    private DefaultTableModel rrReportsTableModel;  //The table model used for the finished queue for CPU RR
    private JTable rrReportsTable;  //The table that displays the finished queue for CPU RR
    private JScrollPane rrReportsScrollPane;    //Scrollpane for the CPU RR finished queue table if more processes are in the finished queue than can be displayed in one frame
        //Average nTAT variables
    private JLabel rrCurrentnTAT;   //Displays the current nTAT for CPU RR
    private double rrnTAT;  //Variable that contains the current nTAT value for CPU RR to be displayed

    //Filepath Input variables
    String result;  //Variable that holds the filepath entered by the user

    //PC model variable
    PC model;   //Initializes an instance of the PC class


    /**
     * This creates the main window and all of the components within that window.
     */
    public GUI(PC model) {
        this.model = model;

        //Creating the main window and setting its properties
        mainMenu = new JFrame("CPU Processor"); //Initializes the mainMenu window
        mainMenu.setSize(1250, 750);    //Sets the preferred size of mainMenu
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //Sets the close operation to exit the program
        mainMenu.getContentPane().setBackground(new Color(230, 245, 255));  //Sets the background color
        main = mainMenu.getContentPane();   //Connects the content pane to the mainMenu frame
        main.setLayout(new GridBagLayout());    //Sets the layout of the content pane
        c = new GridBagConstraints();   //Initializes the constraints for the GridBagLayout
        c.insets = new Insets(15, 0, 0, 0);    //Padding between edge of window and top row

        //Creating the Button Panel and setting its properties
        statusPanel = new JPanel(new GridLayout(1, 4, 45, 0));  //Initializes the status panel
        statusPanel.setBackground(new Color(230, 245, 255));    //Sets the background color

        //Creating the individual buttons and adding them to the Button Panel
            //Creating the Start System Button and adding the action listener
        startSystem = new JButton("Start System");  //Initializes the Start Button
        statusPanel.add(startSystem);   //Adds the start button to the statusPanel
            //Creating the action listener for the Start System button
        startSystem.addActionListener(new ActionListener() {
            //Starts and runs the system when the button is pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                status.setText("System Running");   //Updates the status label
            }
        });
            //Creating the Pause System Button and adding the action listener
        pauseSystem = new JButton("Pause System");  //Initializes the Pause Button
        statusPanel.add(pauseSystem);   //Adds the pause button to the statusPanel
            //Creating the action listener for the Pause System button
        pauseSystem.addActionListener(new ActionListener() {
            //Pauses the whole system when the button is pressed
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the pause button was clicked, interrupt the cpu
                model.throwCPUInterrupt();  //creates the interrupt to pause the system
                status.setText("System Paused");    //Updates the status label
            }
        });

        //Creating the Status Label
        status = new JLabel();  //Initializes the status label
        status.setText("System Starting...");   //Sets the status text to "System Starting..." when the program starts
        statusPanel.add(status);    //Adds the status label to the statusPanel

        //Creating the Time Unit Entry field and the action listener for the text entry
            //NOTE: You MUST press Enter after typing the Time Unit for it to be updated in the system
        time = new JPanel(new FlowLayout());    //Initializes the time panel with a flow layout
        time.setBackground(new Color(230, 245, 255));   //Sets the background color
        timeunit = new JLabel();    //Initializes the timeunit label
        timeunit.setText("1 time unit = "); //Sets the text to "1 time unit = " when the program starts
        timeUnitField = new JTextField(Integer.toString(model.cpu1.getTimeScale()),10); //Initializes the entry field and the function getTimeScale
            //Action listener for the data entered for Time Unit
        timeUnitField.addActionListener(new ActionListener() {
            //Updates the time scale for CPU1 (HRRN) and CPU2 (RR)
            @Override
            public void actionPerformed(ActionEvent e) {
                model.cpu1.setTimeScale(Integer.parseInt(timeUnitField.getText())); //Updates the time scale for CPU1 (HRRN)
                model.cpu2.setTimeScale(Integer.parseInt(timeUnitField.getText())); //Updates the time scale for CPU2 (RR)
            }
        });
        timeunit2 = new JLabel();   //Initializes the second timeunit field that holds "ms"
        timeunit2.setText("ms");    //Sets the text to display "ms"
        time.add(timeunit); //Adds the "1 time unit =" label to the time panel
        time.add(timeUnitField);    //Adds the time unit entry field to the time panel
        time.add(timeunit2);    //Adds the "ms" label to the time panel
        statusPanel.add(time);  //Adds the time panel to the status panel

        //Adding the Button Panel to the main frame
        //Sets the grid bag constraints for adding the status panel to the mainMenu container
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.weightx = 1;
        main.add(statusPanel, c);   //Adds the status panel to the mainMenu window

        //Creating CPU HRRN section
            //Creating CPU HRRN Panel and text fields using the cpu1 information
        cpuPanel1 = new JPanel(new GridLayout(3, 1));   //Initializes the CPU HRRN panel with a grid layout
        cpuPanel1.setBorder(BorderFactory.createLineBorder(Color.BLACK));   //Sets the border to black
        cpuPanel1.setPreferredSize(new Dimension(200, 100));    //Sets the preferred size of the CPU HRRN panel
        cpuPanel1.setBackground(new Color(255, 204, 204));  //Sets the background color
            //Creating CPU1 Label
        cpu1 = new JLabel();  //Initializes the CPU HRRN label
        cpu1.setText(model.cpu1.getName()); //Sets the text to the name of CPU1 (CPU HRRN) using the getName function
        cpuPanel1.add(cpu1);    //Adds the CPU HRRN label to the CPU HRRN panel
            //Creating Execution Label
        exec1 = new JLabel(); //Initializes the CPU HRRN execution status
        execStatus1 = model.cpu1.getStatus();   //Sets the execution status of CPU HRRN using the getStatus function
        exec1.setText(execStatus1); //Sets the text of the execution status label to the text returned by the getStatus function
        cpuPanel1.add(exec1);   //Adds the execution status label to the CPU HRRN panel
            //Creating Time Remaining Label
        tRemaining1 = model.cpu1.getRunThis().getServiceTime(); //Sets the time remaining of the current process using the getServiceTime function
        timeRemaining1 = new JLabel();    //Initializes the process time remaining
        timeRemaining1.setText(" Time remaining: " + tRemaining1);  //Sets the text of the time remaining label to the value returned by getServiceTime
        cpuPanel1.add(timeRemaining1);  //Adds the time remaining label to the CPU HRRN panel
            //Creating the action listener for the CPU1 Panel to update the CPU1
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            //Updates the waiting and finished queues when a process enters/exits the CPU
            //And sets the execution status and time remaining to that of the running process
            public void propertyChange(PropertyChangeEvent evt) {
                updateHrrnQueueTableView(); //Updates the waiting queue
                updateHrrnFinishedList();   //Updates the finished queue
                exec1.setText("exec: " +model.cpu1.getRunThis().getProcessID());    //Updates the execution status
                timeRemaining1.setText(" Time remaining: " + model.cpu1.getRunTime());  //Updates the time remaining
            }
        });
        //Sets the grid bag constraints for adding the CPU HRRN panel to the mainMenu container
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridheight = 1;
        main.add(cpuPanel1, c); //Adds the CPU HRRN panel to the mainMenu window


        //Creating the HRRN Tables Panel and setting its properties
        hrrnTables = new JPanel(new GridLayout(2,1,0,15));  //Initializes the HRRN tables panel with a grid layout
        hrrnTables.setPreferredSize(new Dimension(500,450));    //Sets the preferred size of the panel
        hrrnTables.setBackground(new Color(230, 245, 255)); //Sets the background color
            //Creating Process Queue Scrollable Table
        hrrnQueueTableModel = new DefaultTableModel(0,2);   //Initializes the waiting queue table model to have 2 columns
        hrrnQueueTableModel.setColumnIdentifiers(queueColumnNames); //Sets the column names to "Process Name" and "Service Time"
        hrrnQueueTable = new JTable(hrrnQueueTableModel);   //Initializes the HRRN waiting queue table using the hrrnQueueTableModel
        hrrnQueueScrollPane = new JScrollPane(hrrnQueueTable);  //Initializes the scroll pane for the waiting queue table display window
        hrrnTables.add(hrrnQueueScrollPane);    //Adds the scroll pane to the HRRN waiting queue table
            //Creating Reports Area Scrollable Table
        hrrnReportsTableModel = new DefaultTableModel(0,6); //Initalizes the finished queue table model to have 6 columns
        hrrnReportsTableModel.setColumnIdentifiers(reportsColumnNames); //Sets the column names to the name stored in reportsColumnNames (see variables section)
        hrrnReportsTable = new JTable(hrrnReportsTableModel);   //Initializes the HRRN finished queue table using the hrrnReportsTableModel
        hrrnReportsScrollPane = new JScrollPane(hrrnReportsTable);  //Initializes the scroll pane for the finished queue table display window
        hrrnTables.add(hrrnReportsScrollPane);  //Adds the scroll pane to the HRRN finished queue table
        //Sets the grid bag constraints for adding the HRRN tables panel to the mainMenu container
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        c.gridheight = 3;
        c.gridwidth = 2;
        main.add(hrrnTables, c);    //Adds the hrrnTables panel to the mainMenu window

        //Creating the Current nTAT Field
        hrrnCurrentnTAT = new JLabel(); //Initializes the HRRN current nTAT label

        hrrnnTAT = model.cpu1.getAvgnTAT(); //Sets the hrrnnTAT using the HRRN function getAvgnTAT
        if ( Double.isNaN(hrrnnTAT)){   //Initializes to 0.0 if the hrrnnTAT is not a number
            hrrnnTAT = 0.0;
        }
        hrrnCurrentnTAT.setText(String.format("HRRN Current average nTAT: %.3f", hrrnnTAT)); //Displays the HRRN current nTAT
            //Create a listener on CPU1 because it updates nTAT on process finish
        model.cpu1.addPropertyChangeListener(new PropertyChangeListener() {
            //Updates the current nTAT for HRRN when a process finishes
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                hrrnnTAT = model.cpu1.getAvgnTAT(); //Returns the new nTAT
                hrrnCurrentnTAT.setText(String.format("HRRN Current average nTAT: %.3f", hrrnnTAT));    //Displays the new HRRN current nTAT

            }
        });
        hrrnCurrentnTAT.setFont(hrrnCurrentnTAT.getFont().deriveFont(16.0f));   //Sets the font of the hrrnCurrentnTAT label
        //Sets the grid bag constraints for adding HRRN current nTAT label to the mainMenu container
        c.gridx = 0;
        c.gridy = 5;
        c.gridheight = 1;
        c.weightx = 0.5;
        main.add(hrrnCurrentnTAT, c);   //Adds the HRRN current nTAT label to the mainMenu window
    //End of CPU HRRN section

        //Creating CPU RR section
        //Creating CPU2 RR Panel and text fields using the cpu2 information
        cpuPanel2 = new JPanel(new GridLayout(3, 1));   //Initializes the CPU RR panel with a grid layout
        cpuPanel2.setBorder(BorderFactory.createLineBorder(Color.BLACK));   //Sets the border to black
        cpuPanel2.setPreferredSize(new Dimension(200, 100));    //Sets the preferred size of the CPU RR panel
        cpuPanel2.setBackground(new Color(255, 204, 204));  //Sets the background color
            //Creating CPU2 Label
        cpu2 = new JLabel();    //Initializes the CPU RR label
        cpu2.setText(model.cpu2.getName()); //Sets the text to the name of CPU1 (CPU RR) using the getName function
        cpuPanel2.add(cpu2);    //Adds the CPU RR label to the CPU RR panel
            //Creating Execution Label
        exec2 = new JLabel();   //Initializes the CPU RR execution status
        execStatus2 = model.cpu2.getStatus();   //Sets the execution status of CPU RR using the getStatus function
        exec2.setText(execStatus2); //Sets the text of the execution status label to the text returned by the getStatus function
        cpuPanel2.add(exec2);   //Adds the execution status label to the CPU RR panel
            //Creating Time Remaining Label
        //tRemaining2 = model.cpu2.getRunThis().getServiceTime();
        timeRemaining2 = new JLabel();  //Initializes the process time remaining
        timeRemaining2.setText(" Time remaining: " + tRemaining2);  //Sets the text of the time remaining label to the value returned by getServiceTime
        cpuPanel2.add(timeRemaining2);  //Adds the time remaining label to the CPU RR panel
            //Creating the action listener for the CPU2 Panel to update the CPU2
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            //Updates the waiting and finished queues when a process enters/exits the CPU
            //And sets the execution status and time remaining to that of the running process
            public void propertyChange(PropertyChangeEvent evt) {
                updateRrQueueTableView();   //Updates the waiting queue
                updateRrFinishedList(); //Updates the finished queue
                exec2.setText("exec: " +model.cpu2.getRunThis().getProcessID());    //Updates the execution status
                timeRemaining2.setText(" Time remaining: " + model.cpu2.getRunTime());  //Updates the time remaining
            }
        });
        //Sets the grid bag constraints for adding the CPU RR panel to the mainMenu container
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridheight = 1;
        main.add(cpuPanel2, c); //Adds the CPU RR panel to the mainMenu window

        //Creating the Time Slice Length Entry field and the action listener for the text entry
            //NOTE: You MUST press Enter after typing the Time Slice Length for it to be updated in the system
        timeSlice = new JPanel(new FlowLayout());   //Initializes the timeSlice panel with a flow layout
        timeSlice.setBackground(new Color(230, 245, 255));  //Sets the background color
        timeSliceLength = new JLabel(); //Initializes the timeSliceLength label
        timeSliceLength.setText("Round Robin Time Slice Length");   //Sets the text to "Round Robin Time Slice Length" when the program starts
        timeSliceField = new JTextField(Integer.toString(model.cpu2.getTimeQuantum()),10);  //Initializes the entry field and the function getTimeQuantum
        timeSliceField.setColumns(4);
            //Action listener for the data entered for Time Slice Length
        timeSliceField.addActionListener(new ActionListener() {
            //Updates the time slice length for the CPU2 (RR)
            @Override
            public void actionPerformed(ActionEvent e) {
                model.cpu2.setTimeQuantum(Integer.parseInt(timeSliceField.getText()));  //Updates the time slice length for CPU2 (RR)
            }
        });
        timeSlice.add(timeSliceLength); //Adds the timeSliceLength label to the timeSlice panel
        timeSlice.add(timeSliceField);  //Adds the timeSliceLength text entry field to the timeSlice panel
        //Sets the grid bag constraints for adding the RR Time Slice Length panel to the mainMenu container
        c.gridx = 3;
        c.gridy = 1;
        c.weightx = 0.1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(0,0,0,50);    //Adds padding between the RR Time Slice Length panel and the edge of the mainMenu window
        main.add(timeSlice, c); //Adds the timeSlice panel to the mainMenu window


        //Creating the RR Tables Panel and setting its properties
        rrTables = new JPanel(new GridLayout(2,1,0,15));    //Initializes the RR tables panel with a grid layout
        rrTables.setPreferredSize(new Dimension(500,450));  //Sets the preferred size of the panel
        rrTables.setBackground(new Color(230, 245, 255));   //Sets the background color
            //Creating Process Queue Scrollable Table
        rrQueueTableModel = new DefaultTableModel(0,2); //Initializes the waiting queue table model to have 2 columns
        rrQueueTableModel.setColumnIdentifiers(queueColumnNames); //Sets the column names to "Process Name" and "Service Time"
        rrQueueTable = new JTable(rrQueueTableModel);   //Initializes the RR waiting queue table using the rrQueueTableModel
        rrQueueScrollPane = new JScrollPane(rrQueueTable);  //Initializes the scroll pane for the waiting queue table display window
        rrTables.add(rrQueueScrollPane);    //Adds the scroll pane to the RR waiting queue table
            //Creating Reports Area Scrollable Table
        rrReportsTableModel = new DefaultTableModel(0,6);   //Initalizes the finished queue table model to have 6 columns
        rrReportsTableModel.setColumnIdentifiers(reportsColumnNames); //Sets the column names to the name stored in reportsColumnNames (see variables section)
        rrReportsTable = new JTable(rrReportsTableModel);   //Initializes the RR finished queue table using the rrReportsTableModel
        rrReportsScrollPane = new JScrollPane(rrReportsTable);  //Initializes the scroll pane for the finished queue table display window
        rrTables.add(rrReportsScrollPane);  //Adds the scroll pane to the RR finished queue table
        //Sets the grid bag constraints for adding the RR tables panel to the mainMenu container
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 0.5;
        c.gridheight = 3;
        c.gridwidth = 2;
        c.insets = new Insets(0,0,0,0); //Resets the padding to 0
        main.add(rrTables, c);  //Adds the rrTables panel to the mainMenu window

        //Creating the Current nTAT Field
        rrCurrentnTAT = new JLabel();   //Initializes the RR current nTAT label
        rrnTAT = model.cpu2.getAvgnTAT();   //Sets the rrnTAT using the RR function getAvgnTAT
        if ( Double.isNaN(rrnTAT)){ //Initializes to 0.0 if the rrnTAT is not a number
            rrnTAT = 0.0;
        }
        rrCurrentnTAT.setText(String.format("RR Current average nTAT: %.3f", rrnTAT));  //Displays the RR current nTAT
        //Create a listener on CPU2 because it updates nTAT on process finish
        model.cpu2.addPropertyChangeListener(new PropertyChangeListener() {
            //Updates the current nTAT for RR when a process finishes
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                rrnTAT = model.cpu2.getAvgnTAT();   //Returns the new nTAT
                rrCurrentnTAT.setText(String.format("RR Current average nTAT: %.3f", rrnTAT));  //Displays the new RR current nTAT
            }
        });
        rrCurrentnTAT.setFont(rrCurrentnTAT.getFont().deriveFont(16.0f));   //Sets the font of the rrCurrentnTAT label
        //Sets the grid bag constraints for adding RR current nTAT label to the mainMenu container
        c.gridx = 2;
        c.gridy = 5;
        c.gridheight = 1;
        c.weightx = 0.5;
        main.add(rrCurrentnTAT, c); //Adds the RR current nTAT label to the mainMenu window

        mainMenu.setVisible(true);  //Displays the mainMenu window

        //Create the filepath input window
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
        return result;  //Returns the filepath
    }

    /**
     * This function calls the loadQueueTableData function to update the displayed HRRN Process Queue Table.
     * This occurs when a process is added or removed from the HRRN Process Queue.
     */
    public void updateHrrnQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                hrrnQueueTableModel.setRowCount(0); //Resets the table model length
                loadHrrnQueueTableData();   //Updates the HRRN waiting queue

            }
        });
    }

    /**
     * This function calls the loadQueueTableData function to update the displayed Process Queue Table.
     * This occurs when a process is added or removed from the RR Process Queue.
     */
    public void updateRrQueueTableView() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                rrQueueTableModel.setRowCount(0);   //Resets the table model length
                loadRrReadyQueueTableData();    //Updates the ready queue within the RR process queue table
                //loadRrQueueTableData(); //Updates the waiting queue within the RR process queue table
            }
        });
    }

    /**
     * This function updates the HRRN Reports Area Table as the HRRN Finished List of processes changes.
     * This occurs when a process is added to the HRRN finished process list.
     */
    public void updateHrrnFinishedList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                hrrnReportsTableModel.setRowCount(0);   //Resets the table model length
                loadHrrnFinishedList(); //Updates the HRRN finished list
            }
        });
    }
    /**
     * This function updates the Reports Area Table as the Finished List of processes changes.
     * This occurs when a process is added to the RR finished process list.
     */
    public void updateRrFinishedList() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                rrReportsTableModel.setRowCount(0); //Reset the table model length
                loadRrFinishedList();   //Updates the RR finished list
            }
        });
    }

    /**
     * This function updates the Process Queue Table as the Process Queue changes.
     * This is called by the updateHrrnQueueTableView function.
     */
    public void loadHrrnQueueTableData(){
        synchronized (model.processQueue){
            try {
                for (int i = 0; i < model.processQueue.size(); i++) {
                    hrrnQueueTableModel.addRow(new Object[]{String.valueOf(model.processQueue.get(i).getProcessID()), model.processQueue.get(i).getServiceTime()}); //Add each row of data to the table model
                }
                hrrnQueueTable.setModel(hrrnQueueTableModel);   //Update the HRRN waiting queue table with the new table model
            } catch (IndexOutOfBoundsException e){System.out.println("PQ Out of bounds");}
        }
    }

    /**
     * This function updates the Process Queue Table as the Process Queue changes.
     * This is called by the updateRrQueueTableView function.
     */
    public void loadRrQueueTableData(){
        synchronized (model.processQueue2){
            try {
                //if(!model.processQueue2.isEmpty()){rrQueueTableModel.addRow(new Object[]{"--Waiting Process Queue--", "--"});}
                for (int i = 0; i < model.processQueue2.size(); i++) {
                    rrQueueTableModel.addRow(new Object[]{String.valueOf(model.processQueue2.get(i).getProcessID()), model.processQueue2.get(i).getServiceTime()}); //Add each row of data to the table model
                }
                rrQueueTable.setModel(rrQueueTableModel);   //Update the RR waiting queue table with the new table model
            } catch (IndexOutOfBoundsException e){System.out.println("PQ Out of bounds");}
        }
    }

    /**
     * This function updates the Process Queue Table as the Process Queue changes.
     * This is called by the updateRrQueueTableView function.
     */
    public void loadRrReadyQueueTableData(){
        synchronized (model.cpu2.getReadyQueue()){
            try {
                //if(!model.cpu2.getReadyQueue().isEmpty()){rrQueueTableModel.addRow(new Object[]{"--Ready Queue--", "--"});}
                for (int i = 0; i < model.cpu2.getReadyQueue().size(); i++) {
                    rrQueueTableModel.addRow(new Object[]{String.valueOf(model.cpu2.getReadyQueue().get(i).getProcessID()), model.cpu2.getReadyQueue().get(i).getRunTimeRemaining()});  //Add each row of data to the table model
                }
                rrQueueTable.setModel(rrQueueTableModel);   //Update the RR waiting queue table with the new table model
            } catch (IndexOutOfBoundsException e){System.out.println("RR RQ Out of bounds");}
        }
    }

    /**
     * This function updates the Reports Area Table as the Finished List of Processes changes.
     * This is called by the updateHrrnFinishedList function.
     */
    public void loadHrrnFinishedList() {
        try {
            for (int i = 0; i < model.cpu1.getFinishedList().size(); i++) {
                hrrnReportsTableModel.addRow(new Object[]{String.valueOf(model.cpu1.getFinishedList().get(i).getProcessID()),
                        model.cpu1.getFinishedList().get(i).getArrivalTime(), model.cpu1.getFinishedList().get(i).getServiceTime(),
                        model.cpu1.getFinishedList().get(i).getFinishTime(), model.cpu1.getFinishedList().get(i).getTat(),
                        model.cpu1.getFinishedList().get(i).getnTat()});    //Add each row of data to the table model
                //System.out.println("asdf:" + i);
            }
            hrrnReportsTable.setModel(hrrnReportsTableModel);   //Update the HRRN finished table with the new table model
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FinishedList Out of bounds");
        } catch (NullPointerException e) {
            System.out.println("FinishedList nullptr");
        }
        //System.out.println("flist size:" + model.cpu1.getFinishedList().size());
    }
    /**
     * This function updates the Reports Area Table as the Finished List of Processes changes.
     * This is called by the updateRrFinishedList function.
     */
    public void loadRrFinishedList() {
        try {
            for (int i = 0; i < model.cpu2.getFinishedList().size(); i++) {
                rrReportsTableModel.addRow(new Object[]{String.valueOf(model.cpu2.getFinishedList().get(i).getProcessID()),
                        model.cpu2.getFinishedList().get(i).getArrivalTime(), model.cpu2.getFinishedList().get(i).getServiceTime(),
                        model.cpu2.getFinishedList().get(i).getFinishTime(), model.cpu2.getFinishedList().get(i).getTat(),
                        model.cpu2.getFinishedList().get(i).getnTat()});    //Add each row of data to the table model
                //System.out.println("asdf:" + i);
            }
            rrReportsTable.setModel(rrReportsTableModel);   //Update the RR finished table with the new table model
        } catch (IndexOutOfBoundsException e) {
            System.out.println("FinishedList Out of bounds");
        } catch (NullPointerException e) {
            System.out.println("FinishedList nullptr");
        }
        //System.out.println("flist size:" + model.cpu2.getFinishedList().size());
    }

    /**
     * This function returns the Table Model for the HRRN Process Queue.
     */
    public DefaultTableModel getHrrnQueueTableModel() {
        return hrrnQueueTableModel; //Returns the HRRN table model
    }

    /**
     * This function returns the Table Model for the RR Process Queue.
     */
    public DefaultTableModel getRrQueueTableModel() {
        return rrQueueTableModel; //Returns the RR table model
    }

    /**
     * This function returns the Start System button.
     */
    public JButton getStartSystem() {
        return startSystem; //Returns the start system button
    }

    /**
     * This function sets the Execution Status to the appropriate Process that is on the CPU HRRN.
     */
    public void setHrrnExecStatus(String s){
        this.execStatus1 = s;   //Sets the HRRN execution status
    }

    /**
     * This function sets the Execution Status to the appropriate Process that is on the CPU RR.
     */
    public void setRrExecStatus(String s){
        this.execStatus2 = s;   //Sets the RR execution status
    }

    /**
     * This function returns the Execution Status of the referenced HRRN CPU (i.e. what Process is on the CPU HRRN).
     */
    public String getHrrnExecStatus(){
        return this.execStatus1;    //Returns the HRRN execution status
    }

    /**
     * This function returns the Execution Status of the referenced RR CPU (i.e. what Process is on the CPU RR).
     */
    public String getRrExecStatus(){
        return this.execStatus2;    //Returns the RR execution status
    }

    /**
     * This function sets the Time Remaining for the Process that is on the CPU HRRN.
     */
    public void setHrrnTRemaining(int i){
        this.tRemaining1 = i;   //Sets the HRRN time remaining
    }

    /**
     * This function sets the Time Remaining for the Process that is on the CPU RR.
     */
    public void setRrTRemaining(int i){
        this.tRemaining2 = i;   //Sets the RR time remaining
    }

    /**
     * This function returns the Time Remaining for the Process that is on the CPU HRRN.
     */
    public int getHrrntRemaining(){
        return this.tRemaining1;    //Returns the HRRN time remaining
    }

    /**
     * This function returns the Time Remaining for the Process that is on the CPU RR.
     */
    public int getRrtRemaining(){
        return this.tRemaining2;    //Returns the RR time remaining
    }
}

