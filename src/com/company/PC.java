package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class PC {
    private int status; //running, idle, paused
    private int startTime;
    private int currentTime;
    public ArrayList<Process> processQueue;
    public CPU cpu1;
    //private String filePath;

    public PC() {
        startTime = 0;
        currentTime = 0;
        Queue<Process> processQueue = new LinkedList<>();
    }


    //Method to start running the system
    public void start(){
        cpu1 = new CPU();
        cpu1.GetProcess(this);
    }

    //Method to pause the system when it is running
    public void pause(){

    }

    //Method to stop the system when it is running
    public void stop(){

    }

    //Method to populate linked list of processes from CSV file
    public ArrayList<Process> ReadFromFile(String path) {
        ArrayList<Process> q = new ArrayList<>();
        BufferedReader fileReader;
        String fromFile = null;
        try {
            fileReader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file.");
            return q;
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
            q.add(proc);
            System.out.println("Inside loop print: " + proc.arrivalTime + ", " + proc.processID + ", " + proc.serviceTime + ", " + proc.priority);
        }
        System.out.println(q.size() + " processes added to the queue.");
        return q;
    }
    /*public String GetUserPath(){
        //Get user inputted file name
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String filePath = userInput.nextLine();
        System.out.println("File path entered: " + filePath);
        return filePath;
    }
    public void setFilePath(String path){
        filePath = path;
    }
    public String getFilePath(){
        return filePath;
    }*/
}
