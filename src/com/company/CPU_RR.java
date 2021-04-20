package com.company;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
/**This CPU employs round robin scheduling to ensure all processes get a fair amount
 * of processing time.  When processes arrive at the CPU from the system's process
 * queue they are placed into the CPU's ready queue in a first come first serve manner.
 * Each process spends the selected length of time on the CPU and is then placed back
 * into the ready queue if execution is not complete.
 *
 * If the system is paused, the CPU will complete execution of the current cycle and
 * then pause.
 * */
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

    /**If the system process queue is not empty the CPU will add arriving processes to
     * its' ready queue.  When there is at least one process in the ready queue, the
     * CPU will run that process for the desired time slice.*/
    public void run() {
        if (!processQueue.isEmpty()) {
            populateReadyQueue();
        }
        if (!readyQueue.isEmpty()) {
            this.SelectProcess();
            this.RunProcess(runThis);
        }
    }

    /**This method to select a process will remove the first item from the process
     * queue and begin execution on that process.  Here the remaining service time
     * for the process is updated and any new arriving processes are added to the
     * ready queue.*/
    public void SelectProcess() {
        if (!this.readyQueue.isEmpty()) {
            this.setProcess(this.readyQueue.remove(0));
            this.setStatus("Ready");
            this.setRunTime(runThis.getRunTimeRemaining());
            populateReadyQueue();
        }
    }

    /**This method runs the selected process.  The thread sleeps for the time slice
     * giving the feel of real-time execution.  If the process has more service time
     * remaining it is added back to the wait queue.  If the process is done executing
     * during this time slice, it will update the process' finish time and compute
     * the turnaround time.*/
    public void RunProcess(Process p) {
        try {
            if (p.getRunTimeRemaining() < timeQuantum && p.getRunTimeRemaining() > 0) {
                Thread.sleep((long) timeScale * p.getRunTimeRemaining());
                time += p.getRunTimeRemaining();
            } else {
                Thread.sleep((long) timeQuantum * timeScale);
                time += timeQuantum;
            }

            // Set CPU status
            this.setStatus("Running");
            p.setRunTimeRemaining(p.getRunTimeRemaining() - timeQuantum);

            if (p.getRunTimeRemaining() <= 0) {
                p.setFinishTime(time);
                p.setTat(p.getFinishTime() - p.getArrivalTime());
                p.setnTat(p.getTat() / p.getServiceTime());
                finishedList.add(p);
            } else {
                readyQueue.add(p);
                //printRQ();
                //System.out.println(runThis.getProcessID() + " added back to RQ at " + time);
            }

            //Current average nTAT
            summednTAT += p.getnTat();
            avgnTAT = summednTAT/finishedList.size();
            if (Double.isNaN(avgnTAT)) {
                avgnTAT = 0.0;
            }

            this.setStatus("idle");
            if (readyQueue.isEmpty()) {
                this.setRunTime(0);
            }
            run();
        } catch (InterruptedException e) {
            /**If the process is interrupted the current cycle completes and the process
             * is added back to the ready queue or to the finished queue*/
            time += timeQuantum;
            synchronized (this.readyQueue) {
                // System was most likely paused
                // Finish current time slice and adjust runtimes
                if (p.getRunTimeRemaining() > timeQuantum) {
                    p.setRunTimeRemaining(p.getRunTimeRemaining() - timeQuantum);
                    readyQueue.add(p);
                    System.out.println(p.getProcessID() + " added back to ready queue");
                } else if (p.getRunTimeRemaining() <= timeQuantum) {
                    p.setFinishTime(time);
                    p.setTat(p.getFinishTime() - p.getArrivalTime());
                    p.setnTat(p.getTat() / p.getServiceTime());
                    finishedList.add(p);
                    summednTAT += p.getnTat();
                    avgnTAT = summednTAT/finishedList.size();

                    System.out.println("RR finished " + p.getProcessID() + " at " + time);
                    //System.out.println(p.getProcessID() + " added to finished list");
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
        return this.time;
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
     * This function sets the runtime and fires a property change to its listeners.
     */
    public void setRunTime(int newRunTime) {
        int oldRunTime = this.runTime;
        this.runTime = newRunTime;
        c.firePropertyChange("runTime", oldRunTime, newRunTime);
    }

    /**This method populates the ready queue with processes that have arrived from the
     * process queue.  If no processes are available and there are still processes to arrive,
     * the CPU will sleep for the time slice.*/
    public void populateReadyQueue() {
        if(!processQueue.isEmpty()) {
            for (int i = 0; i < processQueue.size(); i++)
                //System.out.println("Still in PQ: " + processQueue.size() + " " + processQueue.get(0).getProcessID());
            if (processQueue.get(0).getArrivalTime() <= time) {
                //System.out.println(processQueue.get(0).getProcessID() + " added from PQ at " + time);
                readyQueue.add(processQueue.remove(0));
            }
            if(readyQueue.isEmpty() && this.runTime == 0){
                try {
                    //System.out.println("RQ empty");
                    Thread.sleep(timeQuantum * timeScale);
                    time += timeQuantum;
                    run();
                } catch (InterruptedException e){}
            }
        }
    }

    /**
     * This function returns the time slice of the CPU.
     */
    public int getTimeQuantum() {
        return this.timeQuantum;
    }

    /**
     * This function sets the time slice of the CPU.
     */
    public void setTimeQuantum(int i) {
        this.timeQuantum = i;
    }

    public void printRQ(){
        for (int i = 0; i < readyQueue.size(); i++){
            //System.out.print("RQ: " + readyQueue.get(i).getProcessID() + "/" + readyQueue.get(i).getRunTimeRemaining() + " -- ");
        }
        //System.out.print("\n");
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