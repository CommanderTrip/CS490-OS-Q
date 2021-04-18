package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * This class is an implementation of a CPU with the highest response ratio next algorithm implemented
 * HRRN is NON-PREEMPTIVE, so a process chosen will run until completion
 */
public class CPU_HRRN implements Runnable {

    private String cpuName;                 // The name of the CPU
    private String status;                  // CPU Status; idle, running, paused
    private int timeStep = 100;             // Time step of the system in MS
    private double throughput = 0.0;        // Current cpu throughput
    private int localStart;

    private Process currentRunning;         // Stores the current, running processes
    private int runTime;                    // The process's run time
    private ArrayList<Process> processQ;    // The list of all processes to be completed
    private ArrayList<Process> finishedQ;   // The list of all the finished processes
    private ArrayList<Process> waitQ;       // The list of processes that have entered the system but yet to run
    private PropertyChangeSupport c = new PropertyChangeSupport(this); // Helps monitor props for GUI
    private int time;
    double summednTAT = 0.0;
    private double avgnTAT;

    /**
     * Constructor for the HRRN CPU. Initializes values
     *
     * @param cpuName    The name of the CPU
     * @param processesQ The list of all processes to run
     * @param finishedQ  The list of the finished processes
     */
    public CPU_HRRN(String cpuName, ArrayList<Process> processesQ, ArrayList<Process> finishedQ) {
        this.cpuName = cpuName;
        status = "idle";
        this.processQ = processesQ;
        this.finishedQ = finishedQ;
        this.waitQ = new ArrayList<>();
        currentRunning = new Process(0, "empty", 0,0);
        time = 0;
    }

    /**
     * This is called when the thread starts
     */
    @Override
    public void run() {
        // Only run if the process queue is not empty
        if (!processQ.isEmpty()) {
            // See if any processes reached arrival time
            addToWaitQ();

            // Choose the next process according to HRRN
            Process nextProcess = selectNextProcesses();

            if (nextProcess != null) {
                // Fetch the process out of the process q so only this cpu can touch it
                synchronized (processQ) {
                    if (!processQ.isEmpty()) {
                        setProcess(nextProcess);
                        processQ.remove(nextProcess);
                        setStatus("Ready");
                        runTime = currentRunning.getServiceTime();
                    } else {
                        setStatus("idle");
                    }
                }

                // Run that processes
                runProcess(nextProcess);
            }
        }
    }

    /**
     * Selects the next process according by finding the next process in the wait queue with the highest response ratio
     *
     * @return The process in the wait queue with the highest response ratio
     */
    private Process selectNextProcesses() {
        double hrr = -1;
        Process next = null;

        //System.out.println("HRRN waitq is size: "+ waitQ.size() );
        // there is no process to select if the waitQ is empty
        if (waitQ.isEmpty()) {
            // Found out when the next process is
            int soonestArrival = 10000;
            synchronized (processQ) {
                for (Process p : processQ) {
                    if (p.getArrivalTime() < soonestArrival) {
                        soonestArrival = p.getArrivalTime() - time;
                    }
                }
            }

            // Sleep until just before the next process arrives
            try {
                System.out.println("HRRN sleeps for " + (soonestArrival-1) + " timesteps");
                Thread.sleep(((long) (soonestArrival-1) * timeStep) );
                time += soonestArrival;

                // Some inconsistencies when pausing. Sleep until just before next process and keep trying to add it
                while(waitQ.isEmpty()) {
                    addToWaitQ();
                }
            } catch (InterruptedException e) {
                System.out.println(cpuName + "'s sleep for next process was interrupted.");
                return null;
            }
        }

        // Find the process in the wait queue that has the highest response ratio
        // Response Ratio = (Wait time + Service Time)/Service Time
        System.out.println("HRRN selecting process at time: " + time);
        for (Process p : waitQ) {
            // Get the processes service time
            double s = p.getServiceTime();

            // Calculate the process's wait time = current time - arrival time
            int currentTime = time;
            double w = currentTime - p.getArrivalTime();

            // Calculate response ratio and compare to the highest response ratio
            double rr = (w + s) / s;
            System.out.println("HRRN RRs: \t"+p.getProcessID() + " " + "( " + w + " + " + s + " )" + "/" + s + " = " + rr);
            if (rr > hrr) {
                hrr = rr;
                next = p;
            }
        }

        // Remove the chosen process from the wait queue and return it
        assert next != null;
        waitQ.remove(next);
        return next;
    }

    /**
     * Runs a process an calculates all necessary information about the process
     * @param runningProcess The process to run
     */
    private void runProcess(Process runningProcess) {
        try {
            // Set CPU satus
            setStatus("Running");
            System.out.println("HRRN" + " running " + runningProcess.getProcessID() + " for " + runningProcess.getServiceTime());

            // Capture the local start time
            localStart = time;

            // Run the job
            Thread.sleep((long) runningProcess.getServiceTime() * timeStep);
            time += runningProcess.getServiceTime();

            // Get the job's finish time
            runningProcess.setFinishTime(time);

            // Calculate the turnaround time = finish time - arrival time
            runningProcess.setTat(runningProcess.getFinishTime() - runningProcess.getArrivalTime());

            // Calculate normalized turnaround time = tat / s
            runningProcess.setnTat(runningProcess.getTat() / runningProcess.getServiceTime());


            // Add process to the finish queue
            synchronized (finishedQ) {
                finishedQ.add(runningProcess);
            }
            summednTAT += runningProcess.getnTat();
            avgnTAT = summednTAT/finishedQ.size();

            if (Double.isNaN(avgnTAT)) {
                avgnTAT = 0.0;
            }
            // Update the CPU status and run again
            setStatus("idle");
            setRunTime(0);
            run();

        } catch (InterruptedException e) {
            synchronized (processQ) {
                // System was most likely paused

                // Helpful pause debug
                System.out.println("~~~~~~~~~~" + cpuName + " Process interrupted~~~~~~~~~~");

                // Increment the time for the process running
                time += runningProcess.getServiceTime();

                // Set the job's finish time
                runningProcess.setFinishTime(time);

                // Calculate the turnaround time = finish time - arrival time
                runningProcess.setTat(runningProcess.getFinishTime() - runningProcess.getArrivalTime());

                // Calculate normalized turnaround time = tat / s
                runningProcess.setnTat(runningProcess.getTat() / runningProcess.getServiceTime());

                // Add process to the finish queue
                synchronized (finishedQ) {
                    finishedQ.add(runningProcess);
                }
                summednTAT += runningProcess.getnTat();
                avgnTAT = summednTAT/finishedQ.size();

                if (Double.isNaN(avgnTAT)) {
                    avgnTAT = 0.0;
                }
                // Update the CPU status and run again
                setStatus("idle");
                setRunTime(0);
            }
        }
    }

    /**
     * Check to see if any processes needed to be added to the wait queue
     */
    private void addToWaitQ() {
        synchronized (processQ) {
            for (Process p : processQ) {
                // Check each process's arrival in the processQ against current time
                if (p.getArrivalTime() <= time) {
                    // check if the process is already in the waitQ
                    boolean inWaitQ = false;
                    for (Process w : waitQ) {
                        if (w.getProcessID().equals(p.getProcessID())) {
                            inWaitQ = true;
                            break;
                        }
                    }
                    // Add it to the waitQ if it's not there
                    if (!inWaitQ){
                        System.out.println("HRRN: adding " + p.getProcessID() + " to the WaitQ");
                        waitQ.add(p);
                    }
                }
            }
        }
    }

    /**
     * Helps the GUI listen for changes in specific properties
     *
     * @param pcl The listener
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        c.addPropertyChangeListener(pcl);
    }

    /**
     * This function sets the runtime and fires a property change to its listeners.
     */
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }

    /**
     * This function sets the status and fires a property change to its listeners.
     */
    public void setProcess(Process newP) {
        Process oldP = currentRunning;
        currentRunning = newP;
        c.firePropertyChange("runThis", oldP, newP);
    }

    public void setStatus(String newStat) {
        String oldStat = status;
        status = newStat;
        c.firePropertyChange("status", oldStat, newStat);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // BASIC GETTERS AND SETTERS
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public ArrayList<Process> getFinishedList() {
        return finishedQ;
    }

    public ArrayList<Process> getProcessQueue() {
        return processQ;
    }

    public void setProcessQueue(ArrayList<Process> processQ) {
        this.processQ = processQ;
    }

    public int getTimeScale() {
        return timeStep;
    }

    public void setTimeScale(int timeStep) {
        this.timeStep = timeStep;
    }

    public Process getCurrentRunning() {
        return currentRunning;
    }

    public void setCurrentRunning(Process currentRunning) {
        this.currentRunning = currentRunning;
    }

    public int getRunTime() {
        return this.runTime;
    }

    public String getName() {
        return cpuName;
    }

    public Process getRunThis() {
        return currentRunning;
    }

    public String getStatus() {
        return status;
    }

    public double getAvgnTAT() {
        return avgnTAT;
    }

    public void setAvgnTAT(double avgnTAT) {
        this.avgnTAT = avgnTAT;
    }
}