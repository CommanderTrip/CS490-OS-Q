package com.company;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Global singleton system clock that tracks time.
 * The clock will be stored into its own thread.
 */
public final class Clock implements Runnable {
    private int time;   // Maintains the current system time
    private static int timeStep;    // Maintains the system time step
    private static final Clock INSTANCE = new Clock(); // Holds the current instance of the system clock
    Timer t = new Timer();


    /**
     * Private so only one clock can be made for the system
     * Initializes time to the beginning
     * Initializes the time step to the default 100ms
     */
    private Clock() {
        time = 0;
        timeStep = 100;
    }

    /**
     * Called whenever the clock needs to be access
     * @return the current system clock
     */
    public static Clock getInstance() {
        return INSTANCE;
    }

    /**
     * time always increments by one unit
     */
    public void incrementTime() {
        time += 1;
    }

    public int getTime() { return time; }
    public Timer getTimer(){return t;}
    public void setTimeStep(int timeStep) { Clock.timeStep = timeStep;}

    /**
     * The main loop for the clock
     * Recursively increments time until an interrupt is thrown.
     */
    public void run(){
        /*try {
            //System.out.println("Clock running...");
            while(true) {
                Thread.sleep(timeStep);
                incrementTime();
            }
            // For debugging
            // System.out.println(getTime());
            //run();
        } catch (InterruptedException e)
        {
            System.out.println("~~~~~~~~~~Clock Sleep Interrupted~~~~~~~~~~");
        }*/
        t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                incrementTime();
            }
        };
        t.scheduleAtFixedRate(task, 0, timeStep);
    }
}