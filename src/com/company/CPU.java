package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CPU implements Runnable{
    private String name;
    private String status; //running, idle, paused
    private Process runThis;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);
    private ArrayList<Process> processQueue;
    private ArrayList<Process> finishedList;
    private int timeScale = 100;
    private int runTime;
    private double throughput;

    public CPU(){
    }

    public CPU(String name, ArrayList<Process> processQueue, ArrayList<Process> finishedList){
        this.name = name;
        this.processQueue = processQueue;
        this.finishedList = finishedList;
        this.status = "idle";
        this.runThis = new Process(0, "empty", 0, 0 );
    }
    public void run(){
        // Only run if the process queue is not empty
        if(!processQueue.isEmpty()) {
            // Grab the next process
            this.SelectProcess();

            // Run that process
            this.RunProcess(runThis);
        }
    }
    public void SelectProcess() {
        //Ensures only one thread will access the process queue at a time
        synchronized (processQueue) {
            if (!processQueue.isEmpty()) {
                this.setProcess(processQueue.remove(0));
                this.setStatus("Ready");
                this.runTime = runThis.getServiceTime();
                //update cpu panel on gui
            }
            if (processQueue.isEmpty()) {
                this.setStatus("idle");
            }
        }
    }
    public void RunProcess(Process p){
        try {
            // Set CPU status
            this.setStatus("Running");
            System.out.println(this.name +" running " + p.getProcessID() + " for " + p.getServiceTime());

            // Print arrival time
            p.setArrivalTime(Clock.getInstance().getTime());
            //System.out.println(this.name + " " + p.getProcessID() + " arrival time: " +  p.getArrivalTime());

            // Let the job complete
            //processThread.join();
            Thread.sleep(((long) p.getServiceTime() * timeScale));

            // Print the Finish time
            p.setFinishTime(Clock.getInstance().getTime());
            //System.out.println(this.name + " " + p.getProcessID() + " finish time: " +  p.getFinishTime());

            // Turnaround time
            p.setTat(p.getFinishTime() - p.getArrivalTime());
            //System.out.println(this.name + " " + p.getProcessID() + " TAT: " +  p.getTat() );

            // Normalized Turnaround time
            p.setnTat(p.getTat() / p.getServiceTime());
            //System.out.println(this.name + " " + p.getProcessID() + " nTAT: " +  p.getnTat());

            // Current Throughput
            throughput = finishedList.size() / (float) Clock.getInstance().getTime();
            throughput = Math.round(throughput*1000.0) / 1000.0;
            if ( Double.isNaN(throughput)){
                throughput = 0.0;
            }
            //System.out.println("Throughput: "+ throughput);

            //When a process completes it is added to the finished list
            synchronized (finishedList){
                finishedList.add(p);
                //System.out.println("flist size:" + finishedList.size());
            }
            this.setStatus("idle");
            this.setRunTime(0);
            run();
        } catch (InterruptedException e) {
            synchronized (processQueue){
            // System was most likely paused
            // Adjust the current process's service time
            p.setServiceTime(p.getServiceTime() - (Clock.getInstance().getTime() - p.getArrivalTime()));
            processQueue.add(0,p);
            System.out.println(p.getProcessID() + " added back to queue");
            // State that the CPU was paused
            System.out.println("~~~~~~~~~~CPU Process interrupted~~~~~~~~~~");
        }
        }
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){c.addPropertyChangeListener(pcl);}
    public void removePropertyChangeListener(PropertyChangeListener pcl){c.removePropertyChangeListener(pcl);}

    /**
     * This function sets runThis to a new process and fires a property change to its listeners.
     */
    public void setProcess(Process newProcess){
        Process oldProcess = this.runThis;
        this.runThis = newProcess;
        c.firePropertyChange("runThis", oldProcess, newProcess);
    }
    /**
     * This function changes the name of a process and fires a property change to its listeners.
     */
    public void setStatus(String newString){
        String oldString = this.status;
        this.status = newString;
        c.firePropertyChange("status", oldString, newString);
    }
    /**
     * This function returns the status of the process.
     */
    public String getStatus(){return this.status;}
    /**
     * This function returns the run time of the current process.
     */
    public int getTime(){return runThis.getServiceTime();}
    /**
     * This function sets the process queue.
     */
    public void setProcessQueue(ArrayList<Process> pq){this.processQueue = pq;}
    /**
     * This function returns the process queue.
     */
    public ArrayList<Process> getProcessQueue(){ return this.processQueue;}
    /**
     * This function sets the finished list.
     */
    public void setFinishedList(ArrayList<Process> fl){this.finishedList = fl;}
    /**
     * This function returns the finished list.
     */
    public ArrayList<Process> getFinishedList(){ return this.finishedList;}
    /**
     * This function sets the time step.
     */
    public void setTimeScale(int i){this.timeScale = i;}
    /**
     * This function returns the time step.
     */
    public int getTimeScale(){return this.timeScale;}
    /**
     * This function returns the running process.
     */
    public Process getRunThis(){return this.runThis;}
    /**
     * This function sets the running process.
     */
    public void setRunThis(Process rt){this.runThis = rt;}
    /**
     * This function returns the name of the CPU.
     */
    public String getName(){return this.name;}
    /**
     * This function sets the name of the CPU.
     */
    public void setName(String s){this.name = s;}
    /**
     * This function returns the run time.
     */
    public int getRunTime(){return this.runTime;}
    /**
     * This function returns throughput of the CPU.
     */
    public double getThroughput(){return this.throughput;}
    /**
     * This function sets the runtime and fires a property change to its listeners.
     */
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }
}
