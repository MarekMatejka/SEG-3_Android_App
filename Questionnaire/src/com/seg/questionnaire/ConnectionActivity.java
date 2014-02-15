package com.seg.questionnaire;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity that can connect to the server, NOT FINISHED!
 * Very provisional, but works.
 * 
 * @author Marek Matejka
 *
 */
public class ConnectionActivity extends Activity {

	 private EditText serverIp;
	 
	 private Button connect;
	
	 private String serverIpAddress = "";
	 
	 private boolean connected = false;
	 	 
	 private TextView result;
	 	 
	 private Handler handler = new Handler();
	 	 
	 private EditText command;
	 
	 private String commandS = "";
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_connection);
	 
	        serverIp = (EditText) findViewById(R.id.server_ip);
	        connect = (Button) findViewById(R.id.connect);
	        result = (TextView) findViewById(R.id.result);
	        connect.setOnClickListener(connectListener);
	        command = (EditText) findViewById(R.id.command);
	    }
	 
	    private OnClickListener connectListener = new OnClickListener() {
	 
	        @Override
	        public void onClick(View v) {
	        	commandS = command.getText().toString();
	            if (!connected) {
	                serverIpAddress = serverIp.getText().toString();
	                if (!serverIpAddress.equals("")) {
	                    Thread cThread = new Thread(new ClientThread());
	                    cThread.start();
	                }
	            }
	        }
	    };
	    
	    public class ClientThread implements Runnable {
	 
	        public void run() {
	            try {
	                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
	                Log.e("ClientActivity", "C: Connecting...");
	                Socket socket = new Socket(serverAddr, 4000);
	                connected = true;
	                    try {
	                        Log.e("ClientActivity", "C: Sending command.");
	                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
	                            // WHERE YOU ISSUE THE COMMANDS
	                            out.println(commandS);
	                             
	                            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                            final String s = in.readLine();
	                            Log.e("ClientActivity", "C: Sent. "+s);
	                            
	                            handler.post(new Runnable() {
									
									@Override
									public void run() {
										result.setText(s);
									}
								});
	                            	                            
	                    } catch (Exception e) {
	                        Log.e("ClientActivity", "S: Error", e);
	                }
	                socket.close();
	                connected = false;
	                Log.e("ClientActivity", "C: Closed.");
	            } catch (Exception e) {
	                Log.e("ClientActivity", "C: Error", e);
	                connected = false;
	            }
	        }
	    }

}
