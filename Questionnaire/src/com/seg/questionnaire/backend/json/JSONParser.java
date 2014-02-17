package com.seg.questionnaire.backend.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;

public class JSONParser 
{
	public static void parse(Context context)
	{
		 try {
			AssetManager assetManager = context.getAssets();
			BufferedReader in = new BufferedReader(new InputStreamReader(assetManager.open("test.json")));
			
			String s = "";
			String result = "";
			
			while ((s = in.readLine()) != null)
				result += s.trim();
			
			Log.e("DEBUG", result);
			
			Gson gson = new Gson();
			QuestionnaireJSON qj = gson.fromJson(result, QuestionnaireJSON.class);
			
			Log.e("DEBUG", ""+qj.questions.size());
			
			Log.e("DEBUG", qj.toString());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
