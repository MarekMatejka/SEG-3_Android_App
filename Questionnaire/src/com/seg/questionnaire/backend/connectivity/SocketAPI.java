package com.seg.questionnaire.backend.connectivity;

import java.util.concurrent.CountDownLatch;

/**
 * Class providing the interface between the app and the server.
 * 
 * @author Marek Matejka
 *
 */
public class SocketAPI 
{	
	/**
	 * Calls 'FindPatient' method at the server side.
	 * 
	 * @param serverIP IP of a server
	 * @param surname Patient's surname
	 * @param dob Patient's Date of Birth
	 * @return JSON String result.
	 */
	public static String findPatient(String serverIP, String NHS)
	{
		return executeCommand(serverIP, "FindPatient : "+NHS);
	}

	/**
	 * Calls 'GetAllQuestionnairesForPatient' method at the server side.
	 * 
	 * @param serverIP IP of a server
	 * @param NHSNumber Patient's NHS number
	 * @return JSON String result.
	 */
	public static String getAllQuestionnairesForPatient(String serverIP, String NHSNumber)
	{
		return executeCommand(serverIP, "GetAllQuestionnairesForPatient : "+NHSNumber);
	}
	
	/**
	 * Calls 'GetQuestionnaireByName' method at the server side
	 * 
	 * @param serverIP IP of a server
	 * @param questionnaireName Name of the questionnaire.
	 * @return JSON String result.
	 */
	public static String getQuestionnaireByName(String serverIP, String questionnaireName)
	{
		return executeCommand(serverIP, "GetQuestionnaireByName: "+questionnaireName);
	}
	
	/**
	 * Calls 'CheckUser' method at the server side.
	 * 
	 * @param serverIP IP of a server
	 * @param username Username
	 * @param password Password
	 * @return TRUE String if the combination is correct, FALSE String otherwise.
	 */
	public static String checkUser(String serverIP, String username, String password)
	{
		return executeCommand(serverIP, "CheckUser : "+username+","+password);
	}
	
	/**
	 * Calls 'SendAnswers' method at the server side.
	 * 
	 * @param serverIP IP of a server.
	 * @param answersJSON JSON formatted String of answers.
	 * @return TRUE if received correctly, FALSE otherwise.
	 */
	public static String sendAnswers(String serverIP, String answersJSON)
	{
		return executeCommand(serverIP, "SendAnswers: "+answersJSON);
	}
	
	/**
	 * Method that runs a new Thread that retrieves the answer from the server.
	 * 
	 * @param serverIP IP of a server.
	 * @param command Command send to the server.
	 * @return String response of a server to the command.
	 */
	private static String executeCommand(String serverIP, String command)
	{
		//used to make sure that the result is not returned before the Thread received it.
		CountDownLatch l = new CountDownLatch(1);
		
		SocketThread t = new SocketThread(serverIP, command, l); //create new custom Thread
		t.start(); //start the Thread
		try {
			l.await(); //turn on 'waiting mode' which is turned off only when the Thread receives the answer
		} catch (InterruptedException e) {e.printStackTrace();}
		return t.getResult(); //return the answer retrieved from the server
	}
}
