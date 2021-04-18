package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class CPU_RR implements Runnable {
    private String name;
    private String status; //running, idle, paused
    private Process runThis;
    private PropertyChangeSupport c = new PropertyChangeSupport(this);
    private ArrayList<Process> processQueue;
    private ArrayList<Process> finishedList;
    private int timeScale = 100;
    private int runTime;
    private double throughput;
    private int timeQuantum;
    private int time = 0;
    private ArrayList<Process> readyQueue;
    private double avgnTAT;
    double summednTAT;


    public CPU_RR() {
    }

    public CPU_RR(String name, ArrayList<Process> processQueue, ArrayList<Process> finishedList) {
        this.name = name;
        this.processQueue = processQueue;
        this.finishedList = finishedList;
        this.status = "idle";
        this.runThis = new Process(0, "empty", 0, 0);
        this.timeQuantum = 2;
        this.readyQueue = new ArrayList<>();
        double summednTAT = 0.0;
    }

    public void run() {
        // Only run if the process queue is not empty
        if (!processQueue.isEmpty()) {
            populateReadyQueue();
        }
        if (!readyQueue.isEmpty()) {
            // Grab the next process
            this.SelectProcess();
            // Run that process
            this.RunProcess(runThis);
        }
    }

    public void SelectProcess() {
        //Ensures only one thread will access the process queue at a time
        //synchronized (processQueue) {
        if (!this.readyQueue.isEmpty()) {
                /*if (this.time < this.readyQueue.get(0).getArrivalTime()) {
                    Process p = this.readyQueue.remove(0);
                    this.readyQueue.add(p);
                    return;
                }*/
            this.setProcess(this.readyQueue.remove(0));
            this.setStatus("Ready");
            this.runTime = runThis.getRunTimeRemaining();

            //update cpu panel on gui
        }
        if (readyQueue.isEmpty()) {
            this.setStatus("idle");
        }
        //}
    }

    public void RunProcess(Process p) {
        try {
            //p.setStartTime(Clock.getInstance().getTime());
            if (p.getRunTimeRemaining() < timeQuantum && p.getRunTimeRemaining() > 0) {
                Thread.sleep((long) timeScale * p.getRunTimeRemaining());
                //Thread.sleep(1);
                time += p.getRunTimeRemaining();
            } else {
                Thread.sleep((long) timeQuantum * timeScale);
                time += timeQuantum;

            }
            //System.out.println(Clock.getInstance().getTime());

            // Set CPU status
            this.setStatus("Running");
            //System.out.println(this.name + " running " + p.getProcessID() + " for " + p.getServiceTime());

            p.setRunTimeRemaining(p.getRunTimeRemaining() - timeQuantum);
            //System.out.println("clock instance: " + Clock.getInstance().getTime());
            //System.out.println("curr time: " + time);

            if (p.getRunTimeRemaining() <= 0) {

                p.setFinishTime(time);
                //p.setFinishTime(Clock.getInstance().getTime());
                p.setTat(p.getFinishTime() - p.getArrivalTime());
                p.setnTat(p.getTat() / p.getServiceTime());
                finishedList.add(p);
                populateReadyQueue();
                System.out.println("RR finished " + p.getProcessID() + " at " + time
                        + "/" + Clock.getInstance().getTime());
                //System.out.println("flist size:" + finishedList.size());

            } else {
                populateReadyQueue();
                readyQueue.add(p);
                printRQ();
                System.out.println(runThis.getProcessID() + " added back to RQ at " + time);

            }
            //Current average nTAT
            summednTAT += p.getnTat();
            avgnTAT = summednTAT/finishedList.size();

            if (Double.isNaN(avgnTAT)) {
                avgnTAT = 0.0;
            }
            // Current Throughput
            //throughput = finishedList.size() / (float) Clock.getInstance().getTime();
            if(time != 0){throughput = finishedList.size() / time;}

            throughput = Math.round(throughput * 1000.0) / 1000.0;
            if (Double.isNaN(throughput)) {
                throughput = 0.0;
            }
            //System.out.println("Throughput: "+ throughput);

            this.setStatus("idle");
            if (readyQueue.isEmpty()) {
                this.setRunTime(0);
            }
            run();
        } catch (InterruptedException e) {
            synchronized (this.readyQueue) {
                // System was most likely paused
                // Adjust the current process's service time
                //p.setRunTimeRemaining(p.getRunTimeRemaining() - (Clock.getInstance().getTime() - p.getStartTime()));
                if (p.getRunTimeRemaining() > timeQuantum) {
                    p.setRunTimeRemaining(p.getRunTimeRemaining() - timeQuantum);
                    readyQueue.add(p);
                    System.out.println(p.getProcessID() + " added back to ready queue");
                } else if (p.getRunTimeRemaining() <= timeQuantum) {
                    finishedList.add(p);

                    summednTAT += p.getnTat();
                    avgnTAT = summednTAT/finishedList.size();

                    System.out.println("RR finished " + p.getProcessID() + " at " + time
                            + "/" + Clock.getInstance().getTime());
                    System.out.println(p.getProcessID() + " added to finished list");
                }

                // State that the CPU was paused
                System.out.println("~~~~~~~~~~CPU Process interrupted~~~~~~~~~~");
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        c.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        c.removePropertyChangeListener(pcl);
    }

    /**
     * This function sets runThis to a new process and fires a property change to its listeners.
     */
    public void setProcess(Process newProcess) {
        Process oldProcess = this.runThis;
        this.runThis = newProcess;
        c.firePropertyChange("runThis", oldProcess, newProcess);
    }

    /**
     * This function changes the name of a process and fires a property change to its listeners.
     */
    public void setStatus(String newString) {
        String oldString = this.status;
        this.status = newString;
        c.firePropertyChange("status", oldString, newString);
    }

    /**
     * This function returns the status of the process.
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * This function returns the run time of the current process.
     */
    public int getTime() {
        return runThis.getServiceTime();
    }

    /**
     * This function sets the process queue.
     */
    public void setProcessQueue(ArrayList<Process> pq) {
        this.processQueue = pq;
    }

    /**
     * This function returns the process queue.
     */
    public ArrayList<Process> getProcessQueue() {
        return this.processQueue;
    }

    /**
     * This function sets the finished list.
     */
    public void setFinishedList(ArrayList<Process> fl) {
        this.finishedList = fl;
    }

    /**
     * This function returns the finished list.
     */
    public ArrayList<Process> getFinishedList() {
        return this.finishedList;
    }

    /**
     * This function sets the time step.
     */
    public void setTimeScale(int i) {
        this.timeScale = i;
    }

    /**
     * This function returns the time step.
     */
    public int getTimeScale() {
        return this.timeScale;
    }

    /**
     * This function returns the running process.
     */
    public Process getRunThis() {
        return this.runThis;
    }

    /**
     * This function sets the running process.
     */
    public void setRunThis(Process rt) {
        this.runThis = rt;
    }

    /**
     * This function returns the name of the CPU.
     */
    public String getName() {
        return this.name;
    }

    /**
     * This function sets the name of the CPU.
     */
    public void setName(String s) {
        this.name = s;
    }

    /**
     * This function returns the run time.
     */
    public int getRunTime() {
        return this.runTime;
    }

    /**
     * This function returns throughput of the CPU.
     */
    public double getThroughput() {
        return this.throughput;
    }

    /**
     * This function sets the runtime and fires a property change to its listeners.
     */
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }

    public void populateReadyQueue() {
        //try{
        for (int i = 0; i < processQueue.size(); i++)
            if (processQueue.get(0).getArrivalTime() <= Clock.getInstance().getTime()) {
                System.out.println(processQueue.get(0).getProcessID() + " added from PQ at " + time);
                readyQueue.add(processQueue.remove(0));

                //  }
                //} catch (Exception e){System.out.println("populate ready queue exception");}
            }
    }

    /**
     * This function returns the name of the CPU.
     */
    public int getTimeQuantum() {
        return this.timeQuantum;
    }

    /**
     * This function sets the name of the CPU.
     */
    public void setTimeQuantum(int i) {
        this.timeQuantum = i;
    }

    public void printRQ(){
        for (int i = 0; i < readyQueue.size(); i++){
            System.out.print("RQ: " + readyQueue.get(i).getProcessID() + "/" + readyQueue.get(i).getRunTimeRemaining() + " -- ");
        }
        System.out.print("\n");
    }
    /**
     * This function sets the process queue.
     */
    public void setReadyQueue(ArrayList<Process> rq) {
        this.readyQueue = rq;
    }

    /**
     * This function returns the process queue.
     */
    public ArrayList<Process> getReadyQueue() {
        return this.readyQueue;
    }

    public double getAvgnTAT() {
        return avgnTAT;
    }

    public void setAvgnTAT(double avgnTAT) {
        this.avgnTAT = avgnTAT;
    }
}