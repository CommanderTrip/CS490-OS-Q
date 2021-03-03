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
    private int timeScale = 100;
    private int runTime;

    public CPU(){
    }

    public CPU(String name, ArrayList<Process> processQueue){
        this.name = name;
        this.processQueue = processQueue;
        this.status = "idle";
        this.runThis = new Process(0, "empty", 0, 0 );
    }
    public void run(){
        while(!processQueue.isEmpty()){
            this.SelectProcess();
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
        Thread pt = new Thread(p);
        pt.start();
        try {
            this.setStatus("Running");
            System.out.println(this.name +" running " + p.getProcessID() + " for " + p.getServiceTime());
            pt.join();
            Thread.sleep((p.getServiceTime() * timeScale));
            this.setStatus("idle");
            this.setRunTime(0);

        } catch (InterruptedException e) {
            //Thread failed catch
            System.out.println("Process interrupted");
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
    public void setTimeScale(int i){this.timeScale = i;}
    public int getTimeScale(){return this.timeScale;}
    public Process getRunThis(){return this.runThis;}
    public void setRunThis(Process rt){this.runThis = rt;}
    public String getName(){return this.name;}
    public void setName(String s){this.name = s;}
    public int getRunTime(){return this.runTime;}
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }
}
