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
    synchronized public void SelectProcess(){
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
    public void RunProcess(Process p){
        try {
            // Set CPU status
            this.setStatus("Running");
            System.out.println(this.name +" running " + p.getProcessID() + " for " + p.getServiceTime());

            // Print arrival time
            p.setArrivalTime(Clock.getInstance().getTime());
            System.out.println(this.name + " " + p.getProcessID() + " arrival time: " +  p.getArrivalTime());

            // Let the job complete
            //processThread.join();
            Thread.sleep(((long) p.getServiceTime() * timeScale));

            // Print the Finish time
            p.setFinishTime(Clock.getInstance().getTime());
            System.out.println(this.name + " " + p.getProcessID() + " finish time: " +  p.getFinishTime());

            // Turnaround time
            p.setTat(p.getFinishTime() + 1 - p.getArrivalTime());
            System.out.println(this.name + " " + p.getProcessID() + " TAT: " +  p.getTat() );

            // Normalized Turnaround time
            p.setnTat(p.getTat() / p.getServiceTime());
            System.out.println(this.name + " " + p.getProcessID() + " nTAT: " +  p.getnTat());

            // Current Throughput
            throughput = finishedList.size() / (float) Clock.getInstance().getTime();
            if ( Double.isNaN(throughput)){
                throughput = 0.0;
            }
            System.out.println("Throughput: "+ throughput);


            finishedList.add(p);
            System.out.println("flist size:" + finishedList.size());
            this.setStatus("idle");
            this.setRunTime(0);
            run();
        } catch (InterruptedException e) {
            // System was most likely paused
            // Adjust the current process's service time
            p.setServiceTime(p.getServiceTime() - (Clock.getInstance().getTime() - p.getArrivalTime()));
            processQueue.add(0,p);
            // State that the CPU was paused
            System.out.println("~~~~~~~~~~CPU Process interrupted~~~~~~~~~~");
        }
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){c.addPropertyChangeListener(pcl);}
    public void removePropertyChangeListener(PropertyChangeListener pcl){c.removePropertyChangeListener(pcl);}

    public void setProcess(Process newProcess){
        Process oldProcess = this.runThis;
        this.runThis = newProcess;
        c.firePropertyChange("runThis", oldProcess, newProcess);
    }
    public void setStatus(String newString){
        String oldString = this.status;
        this.status = newString;
        c.firePropertyChange("status", oldString, newString);
    }
    public String getStatus(){return this.status;}
    public int getTime(){return runThis.getServiceTime();}
    public void setProcessQueue(ArrayList<Process> pq){this.processQueue = pq;}
    public ArrayList<Process> getProcessQueue(){ return this.processQueue;}
    public void setFinishedList(ArrayList<Process> fl){this.finishedList = fl;}
    public ArrayList<Process> getFinishedList(){ return this.finishedList;}
    public void setTimeScale(int i){this.timeScale = i;}
    public int getTimeScale(){return this.timeScale;}
    public Process getRunThis(){return this.runThis;}
    public void setRunThis(Process rt){this.runThis = rt;}
    public String getName(){return this.name;}
    public void setName(String s){this.name = s;}
    public int getRunTime(){return this.runTime;}
    public double getThroughput(){return this.throughput;}
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }
}
