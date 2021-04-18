package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class PC {
    public ArrayList<Process> processQueue;
    public ArrayList<Process> finishedList;
    public ArrayList<Process> processQueue2;
    public ArrayList<Process> finishedList2;
    public CPU_HRRN cpu1;
    public CPU_RR cpu2;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);
    private Thread cpu1Thread;
    private Thread cpu2Thread;

    //Constructor
    public PC() {
        ArrayList<Process> processQueue = new ArrayList<>();
        ArrayList<Process> finishedList = new ArrayList<>();
        ArrayList<Process> processQueue2 = new ArrayList<>();
        ArrayList<Process> finishedList2 = new ArrayList<>();
        cpu1 = new CPU_HRRN("CPU HRRN", processQueue, finishedList);
        cpu2 = new CPU_RR("CPU RR", processQueue2, finishedList2);

    // Initialized CPU Threads
        cpu1Thread = new Thread(cpu1);
        cpu2Thread = new Thread(cpu2);
    }

    // Throw interrupts to the CPU
    public void throwCPUInterrupt() {
        cpu1Thread.interrupt();
        cpu2Thread.interrupt();
    }

    public Thread getThreadCPU1(CPU_HRRN cpu){
        cpu1Thread = new Thread(cpu);
        return cpu1Thread;
    }

    public Thread getThreadCPU2(CPU_RR cpu){
        cpu2Thread = new Thread(cpu);
        return cpu2Thread;
    }

    //Method to start running the system
    public void start(){
        //Sets the process queues for the cpus to avoid null ptr on start
        if (!cpu1.getProcessQueue().equals(processQueue)){cpu1.setProcessQueue(processQueue);}
        if (!cpu2.getProcessQueue().equals(processQueue2)){cpu2.setProcessQueue(processQueue2);}
    }

    //Method to populate linked list of processes from CSV file
    //Creates 2 of the same process queue, one for each CPU.
    //Saved time over implementing Clonable
    public void ReadFromFile(String path) {
        ArrayList<Process> q = new ArrayList<>();
        ArrayList<Process> q2 = new ArrayList<>();
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
                Process proc2 = new Process(Integer.parseInt(string[0]), string[1], Integer.parseInt(string[2]), Integer.parseInt(string[3]));
                q.add(proc);
                q2.add(proc2);
            }
            catch(NullPointerException e){System.out.println("NullPTR during splitting");}
            //temp processes while reading file to be added to queue
            //System.out.println("Inside loop print: " + proc.getArrivalTime() + ", " + proc.getProcessID() + ", " + proc.getServiceTime() + ", " + proc.getPriority());
        }

        //Sorts the queue by arrival time
        q.sort(Comparator.comparing(p->p.getArrivalTime()));
        q2.sort(Comparator.comparing(p->p.getArrivalTime()));
        this.setProcessQueue(q);
        this.setProcessQueue2(q2);
        System.out.println(this.processQueue.size() + " processes added to the queue.");
    }
    public void setProcessQueue(ArrayList<Process> pq){this.processQueue = pq;}
    public void setProcessQueue2(ArrayList<Process> pq){this.processQueue2 = pq;}
}
