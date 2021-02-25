package com.company;

public class Process implements Runnable {
    public int arrivalTime;
    public String processID;
    public int serviceTime;
    public int priority;

    Process(int arrivalTime, String processID, int serviceTime, int priority) {
        this.arrivalTime = arrivalTime;
        this.processID = processID;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

    public void run() {
        //System.out.println(" Running process  " + processID + " for " + serviceTime + " seconds.");
        try {
            //Currently running 10x faster
            Thread.sleep((serviceTime * 100));
        } catch (InterruptedException ex) {
            //Catch this later?
        }
        //System.out.println(" Process " + processID + " has finished execution.");
    }
}
