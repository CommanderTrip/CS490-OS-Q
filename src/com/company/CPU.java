package com.company;

public class CPU {
    public int status; //running, idle, paused
    public Process runThis;

    public void GetProcess(PC pc){
        if (!pc.processQueue.isEmpty()) {
            runThis = pc.processQueue.remove(0);
            Thread pt = new Thread(runThis);
            pt.start();
            //System.out.println("Started thread.");
            try {
                pt.join();
            } catch (Exception e) {
                //Thread failed catch
            }
        }
    }
}
