package com.seg.questionnaire.backend.connectivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.spec.KeySpec;
import java.util.concurrent.CountDownLatch;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * Custom implementation of Thread object which opens a new 
 * socket and connects to the server for every request.
 * 
 * @author Marek Matejka
 *
 */
public class SocketThread extends Thread 
{
    private static SecretKeySpec secret;

    private static IvParameterSpec iv;
    private static Cipher decryptor;
    private static Cipher encryptor;
    private static Socket socket;
	
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
			Socket socket = new Socket();
			InetAddress serverAddr = InetAddress.getByName(serverIP);
			socket.connect(new InetSocketAddress(serverAddr, PORT), 4000); //4000 is timeout not Port
			connected = true;
			
			PrintWriter out = null;
			try {				
				//prepare a stream to send command
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
				
				String temps = encrypt(command);
				temps = temps.replace("\n", "");
				temps = temps.replace("\r", "");				
				temps += "END";				
				out.println(temps); //send the command to the server

				//prepare a stream to receive response
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				//temporary variables
				String result = "";
				String temp = "";

				//while we still have something to read
				while (!(temp = in.readLine()).endsWith("END"))
				{
					result += temp; //read and append
				}
				
				result += temp.substring(0, temp.length() - END.length()); //get rid of the END
				
				this.result = decrypt(result); //save result;				
			} catch (IOException ioe) {ioe.printStackTrace();}
			finally
			{
				if (out != null)
				{
					String temps = encrypt("Close");
					temps = temps.replace("\n", "");
					temps = temps.replace("\r", "");				
					temps += "END";
					out.println(temps);
					out.close();
				}
				latch.countDown(); //notify that the communication is over
				socket.close(); //close socket connection
				connected = false;
			}
			
		} catch (SocketTimeoutException se) {se.printStackTrace(); this.result = "Socket timeout";}
		catch (Exception e) {e.printStackTrace();}	
		finally{
			if (socket != null && !socket.isClosed())
			{
				try {
					socket.close();
					connected = false;
					latch.countDown();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
	}
	
	//ENCRYTION AND DECRYPTION
    private static void createSecret()
    {
        try
        {
            char[] password = new char[]{'f','x','5','6','x','o','r'};
            byte[] salt = new byte[]{1,2,3,4,5,6,7,8};

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(password, salt, 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void createIV()
    {
        byte[] ivSpec = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        iv = new IvParameterSpec(ivSpec);
    }

    private static void createEncryptor()
    {
        try
        {
            encryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptor.init(Cipher.ENCRYPT_MODE, secret, iv);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void createDecryptor()
    {
        try
        {
            decryptor = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptor.init(Cipher.DECRYPT_MODE, secret, iv);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String decrypt(String message)
    {
    	if(secret == null)
        {
            createSecret();
            createIV();
            createEncryptor();
            createDecryptor();
        }
        try
        {
            byte[] encrypted = Base64.decode(message.getBytes("UTF-8"), Base64.DEFAULT); //DatatypeConverter.parseBase64Binary(message);            
            byte[] decrypted = decryptor.doFinal(encrypted);
            String s = new String(decrypted, "UTF-8");
            return s;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "hello";
    }

    private static String encrypt(String message)
    {
        if(secret == null)
        {
            createSecret();
            createIV();
            createEncryptor();
            createDecryptor();
        }
        try
        {
            byte[] encrypted = encryptor.doFinal(message.getBytes("UTF-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT); //DatatypeConverter.printBase64Binary(encrypted);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}