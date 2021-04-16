package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class PC {
    private int status;
    public int startTime;
    public int currentTime;
    public ArrayList<Process> processQueue;
    public ArrayList<Process> finishedList;
    public CPU_HRRN cpu1;
    public CPU cpu2;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);
    private Thread clockThread;
    private Thread cpu1Thread;
    private Thread cpu2Thread;

    //Constructor
    public PC() {
        startTime = 0;
        currentTime = 0;
        ArrayList<Process> processQueue = new ArrayList<>();
        ArrayList<Process> finishedList = new ArrayList<>();
        cpu1 = new CPU_HRRN("CPU HRRN", processQueue, finishedList);
        cpu2 = new CPU("cpu2", processQueue, finishedList);

        // Initialize a clock thread
        Clock clock = Clock.getInstance();
        clockThread = getThread(clock);
        clockThread = new Thread(clock);

        // Initialized CPU Threads
        cpu1Thread = new Thread(cpu1);
        cpu2Thread = new Thread(cpu2);
        cpu1Thread.start();
        cpu2Thread.start();
    }

    // Throw interrupts to the clock
    public void throwClockInterrupt(){clockThread.interrupt();}

    // Throw interrupts to the CPU
    public void throwCPUInterrupt() {
        cpu1Thread.interrupt();
        cpu2Thread.interrupt();
    }

    // For creating new threads after interrupts are thrown
    public Thread getThread(Clock clock){
        clockThread = new Thread(clock);
        return clockThread;
    }

    public Thread getThreadCPU1(CPU_HRRN cpu){
        cpu1Thread = new Thread(cpu);
        return cpu1Thread;
    }

    public Thread getThreadCPU2(CPU cpu){
        cpu2Thread = new Thread(cpu);
        return cpu2Thread;
    }

    //Method to start running the system
    public void start(){
        //Sets the process queues for the cpus to avoid null ptr on start
        if (!cpu1.getProcessQueue().equals(processQueue)){cpu1.setProcessQueue(processQueue);}
        if (!cpu2.getProcessQueue().equals(processQueue)){cpu2.setProcessQueue(processQueue);}
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
            }
            catch(NullPointerException e){System.out.println("NullPTR during splitting");}
            //temp processes while reading file to be added to queue
            //System.out.println("Inside loop print: " + proc.getArrivalTime() + ", " + proc.getProcessID() + ", " + proc.getServiceTime() + ", " + proc.getPriority());
        }

        //Sorts the queue by arrival time
        q.sort(Comparator.comparing(p->p.getArrivalTime()));
        this.setProcessQueue(q);
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

}
