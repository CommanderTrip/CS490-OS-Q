package com.company;

public class Process {
    public int arrivalTime;
    public String processID;
    public int serviceTime;
    public int priority;

    Process(int arrivalTime,String processID, int serviceTime, int priority){
        this.arrivalTime = arrivalTime;
        this.processID = processID;
        this.serviceTime = serviceTime;
        this.priority = priority;
    }

}
