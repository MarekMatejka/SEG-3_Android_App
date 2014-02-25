package com.seg.questionnaire.backend.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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
			
			Log.e("DEBUG", result);
			
			Gson gson = new Gson();
			qj = gson.fromJson(result, QuestionnaireJSON.class);
			
			Log.e("DEBUG", ""+qj.getQuestions().size());
			
			Log.e("DEBUG", qj.toString());
			
			in.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		 return qj;
	}

}
