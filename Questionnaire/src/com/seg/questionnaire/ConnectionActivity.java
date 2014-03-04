package com.seg.questionnaire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.seg.questionnaire.backend.connectivity.SocketAPI;

/**
 * Activity which provides the GUI for connecting to the server.
 * 
 * @author Marek Matejka
 * 
 */
public class ConnectionActivity extends Activity 
{
	private TextView result;
	private EditText ip, params;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		ip = (EditText) findViewById(R.id.server_ip);
		params = (EditText) findViewById(R.id.parameters);
		result = (TextView) findViewById(R.id.result);
	}

	/**
	 * Responds to onClick events from the GUI.
	 * 
	 * @param v View that triggered the onClick event.
	 */
	public void onClick(View v) 
	{
		String serverIP = ip.getText().toString(); //read IP
		String parameters = params.getText().toString(); //read Parameters

		if (parameters.isEmpty() || serverIP.isEmpty()) //if both fields are NOT filled
		{
			result.setText("IP - "+serverIP+" | param - "+parameters+" | NOT ALL FIELD FILLED");
			return;
		}

		String[] params = parameters.split(","); //split parameters by comma
		switch (v.getId()) //based on the View call proper API function and print the result on the screen
		{
			case R.id.findPatient:
				result.setText(SocketAPI.findPatient(serverIP, params[0], params[1])); 
				break;
			case R.id.getQuesForPatient:
				result.setText(SocketAPI.getAllQuestionnairesForPatient(serverIP, params[0]));
				break;
			case R.id.getQuesByID:
				result.setText(SocketAPI.getQuestionnaireByName(serverIP, params[0]));
				break;
			case R.id.checkUser:
				result.setText(SocketAPI.checkUser(serverIP, params[0], params[1]));
				break;
		}
	}

}
