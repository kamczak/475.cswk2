package com.acmetelecom.call;

public interface Callable {

	public void callInitiated(String caller, String callee);

	public void callCompleted(String caller, String callee);
	
}
