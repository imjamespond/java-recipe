package com.james.rmi;

public final class QueueHeader {
    private String  que_name;   // name of this queue
    private WaitLists waitlist; // pointer to wait lists
    private int nbr_waitlists;  // number of wait lists
    private int nbr_wl_entries; // number of entries in a waitlist 
    private int wait_time;      // time to wait when no work
    private int nbr_threads;    // total threads for this queue 
    private QueueDetail[] details; // detail array of thread info
    // public application logic class for use by the Queue Thread
    public DemoCall to_call; // class to call
}
