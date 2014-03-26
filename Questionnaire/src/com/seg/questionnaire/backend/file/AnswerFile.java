package com.seg.questionnaire.backend.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.seg.questionnaire.backend.connectivity.SocketAPI;
import com.seg.questionnaire.backend.connectivity.SocketThread;

/**
 * Class that is responsible for reading and writing Answer file.
 * It takes care of handling answers stored due to malfunctioning 
 * network (SocketTimeout Exception).
 * 
 * @author Marek Matejka
 *
 */
public class AnswerFile 
{
	private static final String FILENAME = "answers.mq";
	
	private static Context context = null;
	
	/**
	 * Reads and sends file to the server.
	 * 
	 * @param context Application's/Activity's context.
	 */
	public static void readAndSendFile(Context context)
	{
		AnswerFile.context = context;
		String content = readFile(context);
		if (!content.equals(""))
		{
			SendAnswerFile f = new SendAnswerFile();
			f.execute(content);
		}
	}
	
	/**
	 * Reads the content of the file. Result will be decrypted String.
	 * 
	 * @param context Application's/Activity's content.
	 * @return Content of the file
	 */
	private static String readFile(Context context)
	{
		if (!fileExists(context))
			return "";
		
		String result = "";
		try {
			DataInputStream in = new DataInputStream(context.openFileInput(FILENAME));
			
			result = in.readUTF();
			
			if (in != null)
				in.close();
			
		}catch (Exception e){e.printStackTrace(); return "";}	
		
		return result;
	}
	
	/**
	 * Checks whether the file exists.
	 * 
	 * @param context Activtity's context.
	 * @return TRUE if file exists, FALSE otherwise.
	 */
	private static boolean fileExists(Context context)
	{
		File file = context.getFileStreamPath(FILENAME);
		return file.exists() ? true : false;
	}
	
	
	/**
	 * Creates a file and stores the <i>content</i> in it.
	 * This method will encrypt the data.
	 * 
	 * @param context Application's/Activity's context.
	 * @param content Content of the file.
	 */
	public static void writeFile(Context context, String content)
	{
		try {	       
			context.deleteFile(FILENAME);
			DataOutputStream out = new DataOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
			
			out.writeUTF(SocketThread.encrypt(content));
			
			if (out != null)
				out.close();
			
		}catch (IOException e) {createEmptyFile(context, FILENAME); 
								writeFile(context, content);
								e.printStackTrace();} 
	}
	
	/**
	 * Creates empty file.
	 * 
	 * @param context Context of the <code>Activity</code> from which the method is called.
	 * @param filename String name of the path and name of the file.
	 */
	private static void createEmptyFile(Context context, String filename)
	{
		try {
			DataOutputStream out = new DataOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			out.close();
		}catch (IOException e) {e.printStackTrace();}
	}
	
	/**
	 * AsyncTask responsible for sending data to server
	 * that could not be sent before.
	 * 
	 * @author Marek Matejka
	 *
	 */
	private static class SendAnswerFile extends AsyncTask<String, Void, String>
	{
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params) 
		{
			return SocketAPI.sendAnswers(SocketThread.decrypt(params[0]));
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		public void onPostExecute(String result)
		{
			if (result.contains("true")) //if sent successfully
				context.deleteFile(FILENAME);
		}
		
	}
}
