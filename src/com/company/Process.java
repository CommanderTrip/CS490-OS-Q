package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Process  {
    private int arrivalTime;
    private String processID;
    private int serviceTime;
    private int priority;
    private int finishTime;
    private float tat;
    private float nTat;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);

    //Constructor
    Process(){}
    Process(int arrivalTime, String processID, int serviceTime, int priority) {
        this.arrivalTime = arrivalTime;
        this.processID = processID;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }
    /**
     * This function returns the service time.
     */
    public int getServiceTime(){
        return this.serviceTime;
    }
    /**
     * This function sets the service time and fires a property change for its listeners.
     */
    public void setServiceTime(int newServiceTime){
        int oldServiceTime = this.serviceTime;
        this.serviceTime = newServiceTime;
        c.firePropertyChange("serviceTime", oldServiceTime, newServiceTime);
    }

    /*public void addPropertyChangeListener(PropertyChangeListener pcl){
        c.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl){
        c.removePropertyChangeListener(pcl);
    }
*/   /**
     * This function returns the name of the process.
     */
    public String getProcessID(){
        return this.processID;
    }
    /**
     * This function sets the name of the process.
     */
    public void setProcessID(String id){
        this.processID = id;
    }
    /**
     * This function returns the arrival time.
     */
    public int getArrivalTime(){return this.arrivalTime;}
    /**
     * This function sets the arrival time.
     */
    public void setArrivalTime(int i){this.arrivalTime = i;}
    /**
     * This function returns the process priority.
     */
    public int getPriority(){return this.priority;}
    /**
     * This function sets the process priority.
     * */
    public void setPriority(int i){this.priority = i;}
    /**
     * This function sets the turnaround time.
     */
    public float getTat() {
        return tat;
    }
    /**
     * This function returns the turnaround time.
     */
    public void setTat(float tat) {
        this.tat = tat;
    }
    /**
     * This function returns the normalized turnaround time.
     */
    public float getnTat() {
        return nTat;
    }
    /**
     * This function sets the normalized turnaround time.
     */
    public void setnTat(float nTat) {
        this.nTat = nTat;
    }
    /**
     * This function returns finish time.
     */
    public int getFinishTime() {
        return finishTime;
    }
    /**
     * This function sets the finish time.
     */
    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }
}
