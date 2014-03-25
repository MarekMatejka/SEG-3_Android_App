package com.seg.questionnaire.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.factories.QuestionnaireFactory;
import com.seg.questionnaire.backend.json.QuestionnaireJSON;
import com.seg.questionnaire.backend.question.Question;

/**
 * Class taking care of the tutorial
 * 
 * @author Marek Matejka
 *
 */
public class TutorialActivity extends QuestionActivity
{
	/**
	 * Takes count of which step is currently being completed.
	 */
	private int tutorialStep = 0;
	
	@Override
	protected Questionnaire getQuestionnaire()
	{
		Gson gson = new Gson();
		return QuestionnaireFactory.creatQuestionnaire(gson.fromJson(readTutorialQuestionnaire(), 
																	 QuestionnaireJSON.class));
	}
	
	@Override
	protected void sendAnswers()
	{
		startActivity(new Intent(this, QuestionActivity.class));
		finish();
	}
	
	/**
	 * Reads a JSON file containing the tutorial.
	 * 
	 * @param context Activity's context.
	 * @return JSON parsed QuestionnaireJSON object.
	 */
	private String readTutorialQuestionnaire()
	{
		String result = "";
		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(getAssets().open("tutorial_questionnaire.json")));
			
			String s = "";

			while ((s = in.readLine()) != null)
				result += s.trim();
	
			in.close();
			
		} catch (IOException e) {e.printStackTrace();}
		 
		return result;
	}
	
	private void loadTutorialStep(int tutorialStep)
	{
		switch(tutorialStep)
		{
			case 0: showDialog("Welcome to the tutorial!");
		}
	}
	
	private void showDialog(String text)
	{
		final AlertDialog dialog;
		
		Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(text);
	    builder.setCancelable(false);
	    builder.setPositiveButton("Let's try it!", new DialogInterface.OnClickListener() 
	    {	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		}); 

	    dialog = builder.create();
	    dialog.show();
	}
}
