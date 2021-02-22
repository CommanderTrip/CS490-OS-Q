package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //gui main menu
        //File parser
        //create processes from parse
        //display to console the processes
        //Gui display processes

        //Get user inputted file name
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String filePath = userInput.nextLine();
        System.out.println("File path entered: " + filePath);

        //Create queue to add processes
        Queue<Process> processQueue = new LinkedList<>();
        ReadFromFile(filePath, processQueue);
        //System.out.println("Making sure we can read process queue size after returning from function call: " + processQueue.size());
        while (!processQueue.isEmpty()) {
            Process runThis = processQueue.remove();
            Thread pt = new Thread(runThis);
            pt.start();
            System.out.println("Started thread.");
            try {
                pt.join();
            } catch (Exception e) {
                //Thread failed catch
            }
        }
        System.out.println("Main program exiting.");
        System.exit(0);
    }

    //Read line by line from file -- who knows where this will live
    public static void ReadFromFile(String filePath, Queue processQueue) {
        BufferedReader fileReader = null;
        String fromFile = null;
        try {
            fileReader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file.");
        }
        while (true) {
            try {
                if (!((fromFile = fileReader.readLine()) != null)) break;
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
