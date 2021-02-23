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
        GUI mainGUI = new GUI();
        mainGUI.run();

        //File parser
        //create processes from parse
        //display to console the processes
        //Gui display processes


        //Get user inputted file name
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String filePath = userInput.nextLine();
        System.out.println("File path entered: " + filePath);
        PC pc1 = new PC();
        pc1.ReadFromFile(filePath);
        CPU cpu1 = new CPU();
        cpu1.GetProcess(pc1);

        System.out.println("Main program exiting.");
        System.exit(0);
    }

}
