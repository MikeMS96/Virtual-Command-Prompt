package com.metacube.vcp;

/**
 * Driver class to start the command prompt
 * 
 * @author Mohit Sahu
 *
 */
public class Driver {
	public static void main(String args[]) {
		VirtualCommandPrompt commandPrompt = new VirtualCommandPrompt();
		commandPrompt.start();
	}
}
