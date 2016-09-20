package com.james.rmi;

public final class FrameWorkServer { 
	// The base for all persistent processing 
	private static FrameWorkBase fwb = null; 
	
	// Start up Server 
	public static void main(java.lang.String[] args){ 
	
		// the base for all processing 
		fwb = new FrameWorkBase(); 
		
		// now, after initializing the other FrameWorkBase fields 
		// including the application queues and threads, 
		// do the Implementation class with a ref to FrameWorkBase
		FrameWorkImpl fwi = new FrameWorkImpl(fwb);
	}
}
