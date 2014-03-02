package com.seg.questionnaire.backend.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

/**
 * Class responsible for parsing of a JSON input.
 * 
 * @author Marek Matejka
 *
 */
public class JSONParser 
{
	/**
	 * Parses a JSON file into QuestionnaireJSON object.
	 * 
	 * @param context Activity's context.
	 * @return JSON parsed QuestionnaireJSON object.
	 */
	public static QuestionnaireJSON parse(Context context)
	{
		QuestionnaireJSON qj = null;
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader in = new BufferedReader(new InputStreamReader(assetManager.open("test.json")));
			
			String s = "";
			String result = "";
			
			while ((s = in.readLine()) != null)
				result += s.trim();
			
			Gson gson = new Gson();
			qj = gson.fromJson(result, QuestionnaireJSON.class);
			
			in.close();
			
		} catch (IOException e) {e.printStackTrace();}
		 
		return qj;
	}
	
	/**
	 * Parses the AnswersJSON file into JSON String.
	 * 
	 * @param a Object to parse.
	 * @return Parsed JSON String
	 */
	public static String toJSON(AnswersJSON a)
	{
		Gson gson = new Gson();
		return gson.toJson(a);
	}

}
