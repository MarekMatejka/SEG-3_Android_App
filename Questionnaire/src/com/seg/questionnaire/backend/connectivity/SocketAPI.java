package com.seg.questionnaire.backend.connectivity;

import java.util.concurrent.CountDownLatch;

import com.seg.questionnaire.activities.LoginActivity;

/**
 * Class providing the interface between the app and the server.
 * 
 * @author Marek Matejka
 *
 */
public class SocketAPI 
{		
	public static final String SOCKET_TIMEOUT_EXCEPTION = "Socket timeout";
	
	/**
	 * Calls 'FindPatient' method at the server side.
	 * 
	 * @param NHS Patient's NHS number.
	 * @return JSON String result.
	 */
	public static String findPatient(String NHS)
	{
		//TODO: fully implement in the PatientDetail screen
		return executeCommand("FindPatient: "+NHS);
	}

	/**
	 * Calls 'GetAllQuestionnairesForPatient' method at the server side.
	 * 
	 * @param NHS Patient's NHS number
	 * @return JSON String result.
	 */
	public static String getAllQuestionnairesForPatient(String NHS)
	{
		//TODO: implement in QuestionnaireScreen
		return executeCommand("GetAllQuestionnairesForPatient: "+NHS);
	}
	
	/**
	 * Calls 'GetQuestionnaireByName' method at the server side
	 * 
	 * @param questionnaireName Name of the questionnaire.
	 * @return JSON String result.
	 */
	public static String getQuestionnaireByName(String questionnaireID)
	{
		//TODO: implement in QuestionnaireScreen
		return executeCommand("GetQuestionnaireByID: "+questionnaireID);
	}
	
	/**
	 * Calls 'CheckPasscode' method at the server side.
	 * Format = { "result": true/false }
	 * 
	 * @param passcode Passcode
	 * @return TRUE String if the combination is correct, FALSE String otherwise.
	 */
	public static String checkPasscode(String passcode)
	{
		//TODO: implement in LoginScreen
		return executeCommand("CheckPasscode: "+passcode);
	}
	
	/**
	 * Calls 'SendAnswers' method at the server side.
	 * 
	 * @param answersJSON JSON formatted String of answers.
	 * @return TRUE if received correctly, FALSE otherwise.
	 */
	public static String sendAnswers(String answersJSON)
	{
		//TODO: implement in QuestionScreen
		return executeCommand("SendAnswers: "+answersJSON);
	}
	
	/**
	 * Calls 'Close' method at the server side.
	 */
	public static void close()
	{
		executeCommand("Close");
	}
	
	/**
	 * Method that runs a new Thread that retrieves the answer from the server.
	 * 
	 * @param serverIP IP of a server.
	 * @param command Command send to the server.
	 * @return String response of a server to the command.
	 */
	private static String executeCommand(String command)
	{
		//used to make sure that the result is not returned before the Thread received it.
		CountDownLatch l = new CountDownLatch(1);
		SocketThread t = new SocketThread(LoginActivity.getServerIP(), command, l); //create new custom Thread
		t.start(); //start the Thread
		
		try {
			l.await(); //turn on 'waiting mode' which is turned off only when the Thread receives the answer
		} catch (InterruptedException e) {e.printStackTrace();}
		return t.getResult(); //return the answer retrieved from the server
	}
}
