package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CPU_RR implements Runnable {
    private String name;
    private String status; //running, idle, paused
    private Process runThis;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);
    private ArrayList<Process> processQueue;
    private ArrayList<Process> finishedList;
    private int timeScale = 100;
    private int runTime;
    private double throughput;
    private int timeQuantum;
    private int time = 0;
    //private ArrayList<Process> readyQueue;

    public CPU_RR() {
    }

    public CPU_RR(String name, ArrayList<Process> processQueue, ArrayList<Process> finishedList) {
        this.name = name;
        this.processQueue = processQueue;
        this.finishedList = finishedList;
        this.status = "idle";
        this.runThis = new Process(0, "empty", 0, 0);
        this.timeQuantum = 2;
        //this.readyQueue = new ArrayList<Process>();
    }

    public void run() {
        // Only run if the process queue is not empty
        if (!processQueue.isEmpty()) {
            // Grab the next process
            this.SelectProcess();
            // Run that process
            this.RunProcess(runThis);
        }
    }

    public void SelectProcess() {
        //Ensures only one thread will access the process queue at a time
        //synchronized (processQueue) {
            if (!this.processQueue.isEmpty()) {
                if (this.time < this.processQueue.get(0).getArrivalTime()) {
                    Process p = this.processQueue.remove(0);
                    this.processQueue.add(p);
                    return;
                }
                this.setProcess(this.processQueue.remove(0));
                this.setStatus("Ready");
                this.runTime = runThis.getRunTimeRemaining();

                //update cpu panel on gui
            }
            if (processQueue.isEmpty()) {
                this.setStatus("idle");
            }
        //}
    }

    public void RunProcess(Process p) {
        try {
            //p.setStartTime(Clock.getInstance().getTime());
            if(p.getRunTimeRemaining() < timeQuantum && p.getRunTimeRemaining() > 0){
                Thread.sleep((long) timeScale * p.getRunTimeRemaining());
                //Thread.sleep(1);
                time += p.getRunTimeRemaining();
            }
            else{
                Thread.sleep((long) timeQuantum * timeScale);
                time += timeQuantum;

            }
            //System.out.println(Clock.getInstance().getTime());

            // Set CPU status
            this.setStatus("Running");
            //System.out.println(this.name + " running " + p.getProcessID() + " for " + p.getServiceTime());

            p.setRunTimeRemaining(p.getRunTimeRemaining() - timeQuantum);
            System.out.println("clock instance: " + Clock.getInstance().getTime());
            System.out.println("curr time: " + time);

            if (p.getRunTimeRemaining() <= 0) {

                    p.setFinishTime(time);
                    //p.setFinishTime(Clock.getInstance().getTime());
                    p.setTat(p.getFinishTime() - p.getArrivalTime());
                    p.setnTat(p.getTat() / p.getServiceTime());
                    finishedList.add(p);
                    //System.out.println("flist size:" + finishedList.size());

            } else {
                processQueue.add(p);
            }

            // Current Throughput
            //throughput = finishedList.size() / (float) Clock.getInstance().getTime();
            if(time != 0){throughput = finishedList.size() / time;}

            throughput = Math.round(throughput * 1000.0) / 1000.0;
            if (Double.isNaN(throughput)) {
                throughput = 0.0;
            }
            //System.out.println("Throughput: "+ throughput);

            this.setStatus("idle");
            if(processQueue.isEmpty()) {
                this.setRunTime(0);
            }
            run();
        } catch (InterruptedException e) {
            synchronized (this.processQueue) {
                // System was most likely paused
                // Adjust the current process's service time
                //p.setRunTimeRemaining(p.getRunTimeRemaining() - (Clock.getInstance().getTime() - p.getStartTime()));
                processQueue.add(p);
                System.out.println(p.getProcessID() + " added back to queue");
                // State that the CPU was paused
                System.out.println("~~~~~~~~~~CPU Process interrupted~~~~~~~~~~");
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        c.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        c.removePropertyChangeListener(pcl);
    }

    /**
     * This function sets runThis to a new process and fires a property change to its listeners.
     */
    public void setProcess(Process newProcess) {
        Process oldProcess = this.runThis;
        this.runThis = newProcess;
        c.firePropertyChange("runThis", oldProcess, newProcess);
    }

    /**
     * This function changes the name of a process and fires a property change to its listeners.
     */
    public void setStatus(String newString) {
        String oldString = this.status;
        this.status = newString;
        c.firePropertyChange("status", oldString, newString);
    }

    /**
     * This function returns the status of the process.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * This function returns the run time of the current process.
     */
    public int getTime() {
        return runThis.getServiceTime();
    }

    /**
     * This function sets the process queue.
     */
    public void setProcessQueue(ArrayList<Process> pq) {
        this.processQueue = pq;
    }

    /**
     * This function returns the process queue.
     */
    public ArrayList<Process> getProcessQueue() {
        return this.processQueue;
    }

    /**
     * This function sets the finished list.
     */
    public void setFinishedList(ArrayList<Process> fl) {
        this.finishedList = fl;
    }

    /**
     * This function returns the finished list.
     */
    public ArrayList<Process> getFinishedList() {
        return this.finishedList;
    }

    /**
     * This function sets the time step.
     */
    public void setTimeScale(int i) {
        this.timeScale = i;
    }

    /**
     * This function returns the time step.
     */
    public int getTimeScale() {
        return this.timeScale;
    }

    /**
     * This function returns the running process.
     */
    public Process getRunThis() {
        return this.runThis;
    }

    /**
     * This function sets the running process.
     */
    public void setRunThis(Process rt) {
        this.runThis = rt;
    }

    /**
     * This function returns the name of the CPU.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This function sets the name of the CPU.
     */
    public void setName(String s) {
        this.name = s;
    }

    /**
     * This function returns the run time.
     */
    public int getRunTime() {
        return this.runTime;
    }

    /**
     * This function returns throughput of the CPU.
     */
    public double getThroughput() {
        return this.throughput;
    }

    /**
     * This function sets the runtime and fires a property change to its listeners.
     */
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }
}