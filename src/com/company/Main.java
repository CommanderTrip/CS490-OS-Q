package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PC model = new PC();
                GUI view = new GUI(model);
                Controller controller = new Controller(model, view);
            }
        });
    }
}
