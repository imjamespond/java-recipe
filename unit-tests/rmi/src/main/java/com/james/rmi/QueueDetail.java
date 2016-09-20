package com.james.rmi;

public final class QueueDetail {
    private int   status;       // status of this entry
    private String tname;       // name of this thread
    private int  totl_proc;     // total requests processed
    private int  totl_new;      // total times instantiated
    private FrameWorkBase fwb;    // base storage
    private QueueHeader   AH;     // Queue this belongs to
    private QueThread     thread; // queue thread
}
