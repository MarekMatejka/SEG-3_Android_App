package com.seg.questionnaire.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.factories.QuestionnaireFactory;
import com.seg.questionnaire.backend.json.QuestionnaireJSON;

/**
 * Class taking care of the tutorial
 * 
 * @author Marek Matejka
 *
 */
public class TutorialActivity extends QuestionActivity
{	
	private boolean notShowed = true;
	
	@Override
	public void onResume()
	{
		super.onResume();
		if (notShowed)
		{
			showDialog(getString(R.string.step_start));
			notShowed = false;
		}
	}
	
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
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.activities.QuestionActivity#validateAnswer(java.lang.String)
	 */
	@Override
	protected boolean validateAnswer(String answer)
	{
		String id = ques.getQuestion(currentQuestion).getID();
		
		if (id.equals("tq2") && !answer.equals("Ham, Eggs"))
			return false;
		else if (id.equals("tq3") && !answer.equals("February"))
			return false;
		else if (id.equals("tq4") && !answer.equals("28, 29"))
			return false;	
		else if (id.equals("tq5") && !answer.equals("2"))
			return false;
		else if (id.equals("tq6") && ques.getQuestion(currentQuestion).isAnswered() && 
				 !answer.equals("8"))
			return false;
		else if (id.equals("tq7") && !answer.equals("January, March, May, August, December"))
			return false;
		else if (id.equals("tq8") && !answer.toLowerCase(Locale.getDefault()).
									 equals(PatientDetailActivity.getPatientName().toLowerCase(Locale.getDefault())))
			return false;
		else
			return true;
	}
	
	@Override
	protected void showTutorialStep()
	{
		switch (currentQuestion) 
		{
			case 0: showDialog(getString(R.string.step_0)); break;
			case 1: showDialog(getString(R.string.step_1)); break;
			case 2: showDialog(getString(R.string.step_2)); break;
			case 3: showDialog(getString(R.string.step_3)); break;
			case 4: showDialog(getString(R.string.step_4)); break;
			case 5: showDialog(getString(R.string.step_5)); break;
			case 6: showDialog(getString(R.string.step_6)); break;
			case 7: showDialog(getString(R.string.step_7)); break;
			case 8: showDialog(getString(R.string.step_end)); break;
			default: break;
		}
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
	
	private void showDialog(String text)
	{
		final AlertDialog dialog;
		
		Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage(text);
	    builder.setCancelable(false);
	    builder.setPositiveButton(getString(R.string.ok_let_me_try), new DialogInterface.OnClickListener() 
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
