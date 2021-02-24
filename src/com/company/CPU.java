package com.company;

public class CPU {
    public int status; //running, idle, paused

    public void GetProcess(PC pc){
        while (!pc.processQueue.isEmpty()) {
            Process runThis = pc.processQueue.remove();
            Thread pt = new Thread(runThis);
            pt.start();
            System.out.println("Started thread.");
            try {
                pt.join();
            } catch (Exception e) {
                //Thread failed catch
            }
        }
    }
}
