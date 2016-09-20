package com.james.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FrameWorkInterface extends Remote {

	public Object[] syncRequest(FrameWorkParm in) throws RemoteException;

	public Object[] asyncRequest(FrameWorkParm in) throws RemoteException;

	public String shutRequest() throws RemoteException;
}