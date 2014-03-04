package com.seg.questionnaire.backend.connectivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Custom implementation of Thread object which opens a new 
 * socket and connects to the server for every request.
 * 
 * @author Marek Matejka
 *
 */
public class SocketThread extends Thread 
{
	/**
	 * Port at which the app and the server communicates
	 */
	private final int PORT = 4000;
	
	/**
	 * String marking the end of a the stream from the server.
	 */
	private final String END = "END";
	
	/**
	 * Flag marking whether the Thread is currently active or not.
	 */
	private boolean connected;
	
	/**
	 * Command being send to the server.
	 */
	private String command;
	
	/**
	 * IP of a server.
	 */
	private String serverIP;
	
	/**
	 * Server's response to the command.
	 */
	private String result;
	
	/**
	 * Reference to a CountDownLatch which notifies the main thread 
	 * once a response was received.
	 */
	private CountDownLatch latch;

	/**
	 * Public constructor for SocketThread.
	 * 
	 * @param serverIP IP of a server to which we want to connect.
	 * @param command Command we want to send to the server.
	 * @param latch Reference to a latch which we want to notify once the 
	 * response from server is received.
	 */
	public SocketThread(String serverIP, String command, CountDownLatch latch) 
	{
		this.serverIP = serverIP;
		this.command = command;
		this.latch = latch;
		this.connected = false;
	}
	
	/**
	 * Checks whether the thread is active or not.
	 * 
	 * @return TRUE if connected (active), FALSE otherwise.
	 */
	public boolean isConnected()
	{
		return this.connected;
	}
	
	/**
	 * Returns the result received from the server.
	 * 
	 * @return The server's response. 
	 */
	public String getResult()
	{
		return this.result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		try {
			//initialize the connection
			InetAddress serverAddr = InetAddress.getByName(serverIP);
			Socket socket = new Socket(serverAddr, PORT); //open connection
			connected = true;
			try {				
				//prepare a stream to send command
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);

				out.println(command); //send the command to the server

				//prepare a stream to receive response
				BufferedReader in = new BufferedReader(new InputStreamReader(
														socket.getInputStream()));
				
				//temporary variables
				String result = "";
				String temp = "";

				//while we still have something to read
				while (!((temp = in.readLine()).endsWith(END)))
					result += temp; //read and append

				result += temp.substring(0, temp.length() - END.length()); //get rid of the END

				this.result = result; //save result;
								
				latch.countDown(); //notify that the result was received

			} catch (IOException ioe) {ioe.printStackTrace();}

			socket.close(); //close socket connection
			connected = false;
			
		} catch (Exception e) {e.printStackTrace(); connected = false;}
	}
}