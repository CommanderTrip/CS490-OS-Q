package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Process implements Runnable {
    private int arrivalTime;
    private String processID;
    private int serviceTime;
    private int priority;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);

    Process(){}
    Process(int arrivalTime, String processID, int serviceTime, int priority) {
        this.arrivalTime = arrivalTime;
        this.processID = processID;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

    public void run() {
        try {
            //System.out.println(" Running process  " + this.getProcessID() + " for " + this.getServiceTime() + " seconds.");
            Thread.sleep(1);
            //System.out.println(" Process " + processID + " has finished execution.");
        } catch (InterruptedException ex) {
            //Catch this later?
        } catch (NullPointerException ex){
            System.out.println("no process");
        }
    }
    public int getServiceTime(){
        return this.serviceTime;
    }
    public void setServiceTime(int newServiceTime){
        int oldServiceTime = this.serviceTime;
        this.serviceTime = newServiceTime;
        c.firePropertyChange("serviceTime", oldServiceTime, newServiceTime);
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){
        c.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl){
        c.removePropertyChangeListener(pcl);
    }

    public String getProcessID(){
        return this.processID;
    }
    public void setProcessID(String id){
        this.processID = id;
    }
    public int getArrivalTime(){return this.arrivalTime;}
    public void setArrivalTime(int i){this.arrivalTime = i;}
    public int getPriority(){return this.priority;}
    public void setPriority(int i){this.priority = i;}
}
