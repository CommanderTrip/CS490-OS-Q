package com.company;


/**
 * Global singleton system clock that tracks time
 */
public final class Clock implements Runnable {
    private int time;
    private static int timeStep;
    private static final Clock INSTANCE = new Clock();

    private Clock() {
        time = 0;
        timeStep = 0;
    }

    public static Clock getInstance() {
        return INSTANCE;
    }

    public void incrementTime() {
        time += 1;
    }

    public int getTime() {
        return time;
    }

    public void setTimeStep(int timeStep) { Clock.timeStep = timeStep;}


    public void run(){
        try {
            Thread.sleep(timeStep);
            incrementTime();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        run();
    }
}