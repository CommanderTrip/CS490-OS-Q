package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class PC {
    public int status; //running, idle, paused
    public int startTime;
    public int currentTime;
    public Queue<Process> processQueue = new LinkedList<>();



    //Method to start running the system
    public void start(){
        CPU cpu1 = new CPU();
        cpu1.GetProcess(this);
    }

    //Method to pause the system when it is running
    public void pause(){

    }

    //Method to stop the system when it is running
    public void stop(){

    }

    //Method to populate linked list of processes from CSV file
    public void ReadFromFile(String filePath) {
        BufferedReader fileReader = null;
        String fromFile = null;
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file.");
        }
        while (true) {
            try {
                if ((fromFile = fileReader.readLine()) == null) break;
            } catch (IOException e) {
                System.out.println("Reached end of file.");
            }
            //Split each line by ", " -- space after the comma is important here and in text file must match
            String[] string = fromFile.split(", ");
            //temp processes while reading file to be added to queue
            Process proc = new Process(Integer.parseInt(string[0]), string[1], Integer.parseInt(string[2]), Integer.parseInt(string[3]));
            processQueue.add(proc);
            System.out.println("Inside loop print: " + proc.arrivalTime + ", " + proc.processID + ", " + proc.serviceTime + ", " + proc.priority);
        }
        System.out.println(processQueue.size() + " processes added to the queue.");

    }
}
