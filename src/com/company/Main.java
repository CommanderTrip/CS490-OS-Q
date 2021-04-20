package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PC model = new PC();    //Initializes the PC
                GUI view = new GUI(model);  //Initializes the GUI
                Controller controller = new Controller(model, view);    //Initializes the controller
            }
        });
    }
}
