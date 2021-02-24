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
        
        //Get user inputted file name
        Scanner userInput = new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String filePath = userInput.nextLine();
        System.out.println("File path entered: " + filePath);

        //Create PC and read data into it's process queue
        PC pc1 = new PC();
        pc1.ReadFromFile(filePath);
        pc1.start();

        System.out.println("Main program exiting.");
        System.exit(0);
    }

}
