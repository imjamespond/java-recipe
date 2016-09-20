package com.james.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public final class FrameWorkImpl extends UnicastRemoteObject implements
		FrameWorkInterface {
	// instance field (base of common memory)
	private FrameWorkBase fwb;

	// constructor
	public FrameWorkImpl(FrameWorkBase reference_to_fwb) throws RemoteException {

		// set common memory reference
		fwb = reference_to_fwb;
	}

	public Object[] syncRequest(FrameWorkParm in) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] asyncRequest(FrameWorkParm in) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String shutRequest() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
