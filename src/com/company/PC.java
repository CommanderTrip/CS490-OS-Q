package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PC {
    private int status;
    public int startTime;
    public int currentTime;
    public ArrayList<Process> processQueue;
    public ArrayList<Process> finishedList;
    public CPU cpu1;
    //public int timeScale;
    public CPU cpu2;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);

    //Constructor
    public PC() {
        startTime = 0;
        currentTime = 0;
        ArrayList<Process> processQueue = new ArrayList<>();
        ArrayList<Process> finishedList = new ArrayList<>();
        cpu1 = new CPU("cpu1", processQueue, finishedList);
        cpu2 = new CPU("cpu2", processQueue, finishedList);
    }


    //Method to start running the system
    public void start(){
        if (!cpu1.getProcessQueue().equals(processQueue)){cpu1.setProcessQueue(processQueue);}
        if (!cpu2.getProcessQueue().equals(processQueue)){cpu2.setProcessQueue(processQueue);}
        //if (!cpu1.getFinishedList().equals(finishedList)){cpu1.setFinishedList(finishedList);}
       // if (!cpu2.getFinishedList().equals(finishedList)){cpu2.setFinishedList(finishedList);}

        //currentTime = currentTime + cpu1.runThis.serviceTime;
        Thread thread1 = new Thread(cpu1);
        Thread thread2 = new Thread(cpu2);
        thread1.start();
        thread2.start();
    }

    //Method to pause the system when it is running
    public void pause(){

    }

    //Method to stop the system when it is running
    public void stop(){

    }

    //Method to populate linked list of processes from CSV file
    public void ReadFromFile(String path) {
        ArrayList<Process> q = new ArrayList<>();
        BufferedReader fileReader;
        String fromFile = null;
        try {
            fileReader = new BufferedReader(new FileReader(path));
        } catch (Exception e) {
            System.out.println("Error opening file.");
            System.exit(-1);
            return;
        }
        while (true) {
            try {
                if ((fromFile = fileReader.readLine()) == null) break;
            } catch (IOException e) {
                System.out.println("Reached end of file.");
            }
            //Split each line by ", " -- space after the comma is important here and in text file must match
            try{
                String[] string = fromFile.split(", ");
                Process proc = new Process(Integer.parseInt(string[0]), string[1], Integer.parseInt(string[2]), Integer.parseInt(string[3]));
                q.add(proc);
                this.setProcessQueue(q);
            }
            catch(NullPointerException e){System.out.println("NullPTR during splitting");}
            //temp processes while reading file to be added to queue
            //System.out.println("Inside loop print: " + proc.getArrivalTime() + ", " + proc.getProcessID() + ", " + proc.getServiceTime() + ", " + proc.getPriority());
        }
        System.out.println(this.processQueue.size() + " processes added to the queue.");
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){
        c.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl){
        c.removePropertyChangeListener(pcl);
    }
    public void setProcessQueue(ArrayList<Process> pq){this.processQueue = pq;}
    public ArrayList<Process> getProcessQueue(){return this.processQueue;}
    public void setFinishedList(ArrayList<Process> fl){this.finishedList = fl;}
    public ArrayList<Process> getFinishedList(){return this.finishedList;}
    //public void setTimeScale(int i){this.timeScale = i;}
    //public int getTimeScale(){return this.timeScale;}
}
