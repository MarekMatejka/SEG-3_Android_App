package com.seg.questionnaire.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.activities.adapter.SideListAdapter;
import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.connectivity.SocketAPI;
import com.seg.questionnaire.backend.factories.AnswersFactory;
import com.seg.questionnaire.backend.factories.QuestionnaireFactory;
import com.seg.questionnaire.backend.json.QuestionnaireJSON;
import com.seg.questionnaire.backend.question.Question;

/**
 * Class responsible for displaying all questions from a 
 * questionnaire and handling the whole questionnaire.
 * 
 * @author Marek Matejka
 *
 */
public class QuestionActivity extends Activity 
{		
	/**
	 * Defines the current question
	 */
	private int currentQuestion = 0;
		
	/**
	 * Current questionnaire being used.
	 */
	protected Questionnaire ques;
		
	/**
	 * Context of this Activity
	 */
	private Context context = this;
	
	private static boolean highContrastMode = false;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		if (highContrastMode)
			highContrastMode();
		
		//initialize questionnaire
		ques = getQuestionnaire();
		
		//set questionnaire's title
		TextView questionnaireTitle = (TextView)findViewById(R.id.questionnaireTitle);
		questionnaireTitle.setText(ques.getQuestionnaireTitle());
		
		Log.e("size", ""+ques.getNumberOfQuestions());
		if (ques.getNumberOfQuestions() == 0)
		{
			startActivity(new Intent(QuestionActivity.this, ThankYouActivity.class));
			finish();
			return;
		}
		
		//load first question
		loadQuestion(ques.getQuestion(currentQuestion));
	}
	
	protected Questionnaire getQuestionnaire()
	{
		Gson gson = new Gson();
		String questionnaire = SocketAPI.getQuestionnaireByName(AvailableQuestionnairesActivity.getQuestionnaireID());
		Log.e("ques", questionnaire);
		return QuestionnaireFactory.creatQuestionnaire(gson.fromJson(questionnaire, QuestionnaireJSON.class));
	}
	
	/**
	 * Responds to click event on a button.
	 * 
	 * @param v View on which the event happened.
	 */
	public void changeQuestion(View v)
	{
		if (v.getId() == R.id.next)
			changeQuestion(currentQuestion+1);
		else if (v.getId() == R.id.previous)
			changeQuestion(currentQuestion-1);
		else //v.getId() == R.id.skip
			skipQuestion(currentQuestion+1);
		
		hideKeyboard();
	}
	
	/**
	 * Does all the necessary steps to change the question.
	 * 
	 * @param changeTo Position to which the question should be moved
	 */
	private void changeQuestion(int changeTo)
	{		
		//take current question and answer
		Question q = ques.getQuestion(currentQuestion);
		String lastAnswer = q.getAnswer();
		q.saveAnswer();
		String answer = q.getAnswer();
				
		if (answer.equals("")) //if no answer
			Toast.makeText(this, getString(R.string.answer_first), Toast.LENGTH_SHORT).show(); //ask for it
		else
		{
			checkDependentQuestions(q, answer, lastAnswer); //deal with dependent questions
			
			Toast.makeText(this, "answer = '"+answer+"'", Toast.LENGTH_SHORT).show(); //show the answer
					
			currentQuestion = changeTo; //change the current position pointer value
			
			if (currentQuestion == ques.getNumberOfQuestions()) //if we are at the end of the questionnaire
			{
				if (ques.isCompleted()) //if all required questions has been answered
				{
					int index = ques.getFirstNonRequiredQuestionToComplete(); //get index of the first non-required non-answered question
					if (index == -1) //there are no non-required non-answered questions
						sendAnswers(); //send answers to server
					else
						showOptionDialog(index); //show dialog to the user
				}
				else //not all required questions has been answered
				{
					Toast.makeText(this, R.string.answer_question, Toast.LENGTH_SHORT).show(); //tell user what happened
					skipQuestion((currentQuestion = ques.getFirstRequiredQuestionToComplete())); //send him to the first required non-answered question
				}
			}
			else
				loadQuestion(ques.getQuestion(currentQuestion)); //load next question
		}
		
	}
	
	/**
	 * Sends the data to the server and closes the Activity.
	 * Next Activity is ThankYouActivity.
	 */
	protected void sendAnswers()
	{
		Log.e("DEBUG", AnswersFactory.createJSON(ques));
		
		Log.e("answer", SocketAPI.sendAnswers(AnswersFactory.createJSON(ques)));
		SocketAPI.close();
		startActivity(new Intent(this, ThankYouActivity.class));
		finish();
		ques.deleteQuestionnaire(); //wipe questionnaire data
	}
	
	/**
	 * Displays the option dialog (if user wants to complete also 
	 * non-required questions) and handles user's response.
	 * 
	 * @param index Index of the first non-answered non-required question.
	 */
	private void showOptionDialog(final int index)
	{
		AlertDialog.Builder b = new Builder(this);
		b.setCancelable(false);
		b.setMessage(R.string.option_dialog);
		b.setNegativeButton(R.string.no, new OnClickListener() 
		{	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				sendAnswers();
				dialog.cancel();
			}
		});
		b.setPositiveButton(R.string.yes, new OnClickListener() 
		{	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				skipQuestion((currentQuestion = index));
				dialog.cancel();
			}
		});
		AlertDialog d = b.create();
		d.show();
		
	}
	
	/**
	 * Skips question (i.e. does not save any answer for the question).
	 * 
	 * @param skipTo Index of the question to which the questionnaire should go next.
	 */
	private void skipQuestion(int skipTo)
	{
		Question q  = ques.getQuestion(currentQuestion);
		Toast.makeText(this, "answer = '"+q.getAnswer()+"'", Toast.LENGTH_SHORT).show(); //show the answer
			
		currentQuestion = skipTo; //change the current position pointer value
		loadQuestion(ques.getQuestion(currentQuestion)); //load next question
		
		hideKeyboard();
	}
	
	/**
	 * Loads a given question and displays it to the user.
	 * 
	 * @param q Question to be loaded and displayed.
	 */
	private void loadQuestion(Question q)
	{	
		updateProgress();
		
		//find the RelativeLayout
		RelativeLayout answer = (RelativeLayout)findViewById(R.id.answer);
		
		//make it empty
		answer.removeAllViews(); 
		
		//add the View generated by the Question
		answer.addView(q.getView(this, highContrastMode));
		
		//find the question TextView and set it with question text for the question
		TextView question = (TextView)findViewById(R.id.question);
		question.setText(q.getQuestion());
		
		//find the question TextView and set it with question text for the question
		TextView description = (TextView)findViewById(R.id.description);
		description.setText(q.getDescription());
		
		q.loadAnswer(); //load answer for the question
		
		//set buttons visible or gone based on some basic conditions
		Button skip = (Button)findViewById(R.id.skip);
		if (q.isRequired() || currentQuestion == ques.getNumberOfQuestions()-1)
			skip.setVisibility(View.GONE);
		else
			skip.setVisibility(View.VISIBLE);
		
		Button previous = (Button)findViewById(R.id.previous);
		if (currentQuestion == 0)
			previous.setVisibility(View.GONE);
		else
			previous.setVisibility(View.VISIBLE);
		
		Button next = (Button)findViewById(R.id.next);
		if (currentQuestion == ques.getNumberOfQuestions()-1)
			next.setText(getResources().getString(R.string.submit_answers));
		else
			next.setText(getResources().getString(R.string.next_question));
	}
	
	/**
	 * Updates progress visually to the user.
	 */
	private void updateProgress()
	{
		//find a TextView and set it with current values
		TextView outOf = (TextView) findViewById(R.id.outOf);
		outOf.setText(""+(currentQuestion+1)+"/"+ques.getNumberOfQuestions());
		
		//find a ProgressBar and set it to current value
		ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
		p.setMax(ques.getNumberOfQuestions());
		p.setProgress(currentQuestion+1);
		
		updateList();
	}
	
	/**
	 * Updates the side list and handles question changes when question is
	 * directly selected from the list.
	 */
	private void updateList()
	{
		ListView list = (ListView)findViewById(R.id.questionsList);
		list.setAdapter(new SideListAdapter(this, ques.getQuestions()));
		list.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				Question q = ques.getQuestions().get(currentQuestion);
				if (!q.isRequired())
					skipQuestion(position);
				else
				{	
					if (q.getAnswer().equals(""))
						Toast.makeText(context, getString(R.string.answer_first), Toast.LENGTH_SHORT).show();
					else
						changeQuestion(position);
				}
			}

		});
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		if (currentQuestion > 0)
			changeQuestion(currentQuestion-1); //change to previous question
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	public void onStop()
	{
		super.onStop();
		if (currentQuestion != ques.getNumberOfQuestions())
		{
			Intent i = new Intent(this, LoginActivity.class);
			i.putExtra(LoginActivity.COMES_FROM_QUESTION_ACTIVITY, true);
			startActivity(i);
		}
	}
	
	/**
	 * Deals with dependent questions and makes sure that if the
	 * answer changed, dependent questions will be adjusted accordingly.
	 * 
	 * @param question Question for which dependent questions should be adjusted.
	 * @param answer The  current answer for the question
	 * @param lastAnswer The last answer for the question
	 */
	private void checkDependentQuestions(Question question, String answer, String lastAnswer)
	{
		if (!answer.equals(lastAnswer)) //if the last answer and the new answer does not equal
		{
			if(question.hasDependentQuestions(lastAnswer)) //if there are any dependent questions for last answer
				ques.removeQuestions(question.getDependentQuestions(lastAnswer)); //remove them from the questionnaire
			
			if (question.hasDependentQuestions(answer)) //if there are some dependent questions for a given answer
				ques.addQuestions(question.getDependentQuestions(answer.toString()), currentQuestion); //add them to the questionnaire
		}
	}
	
	/**
	 * Turns the whole UI into high contrast mode.
	 */
	private void highContrastMode()
	{
		Resources r = getResources();
		findViewById(R.id.questionRoot).setBackgroundColor(r.getColor(R.color.white));
		((TextView)findViewById(R.id.questionnaireTitle)).setTextColor(r.getColor(R.color.black));
		findViewById(R.id.poweredBy).setVisibility(View.GONE);
		findViewById(R.id.poweredByBW).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.description)).setTextColor(r.getColor(R.color.black));
		((TextView)findViewById(R.id.question)).setTextColor(r.getColor(R.color.black));
		((Button)findViewById(R.id.previous)).setTextColor(r.getColor(R.color.text_color_bw));
		((Button)findViewById(R.id.previous)).setBackground(r.getDrawable(R.color.button_background_color_bw));
		((Button)findViewById(R.id.skip)).setTextColor(r.getColor(R.color.text_color_bw));
		((Button)findViewById(R.id.skip)).setBackground(r.getDrawable(R.color.button_background_color_bw));
		((Button)findViewById(R.id.next)).setTextColor(r.getColor(R.color.text_color_bw));
		((Button)findViewById(R.id.next)).setBackground(r.getDrawable(R.color.button_background_color_bw));
		((ProgressBar)findViewById(R.id.progressBar1)).setProgressDrawable(r.getDrawable(R.drawable.progress_bar_style_bw));
		((TextView)findViewById(R.id.outOf)).setTextColor(r.getColor(R.color.black));
	}
	
	/**
	 * Hides keyboard.
	 */
	private void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * Checks if the high contrast mode is turned on or off.
	 * 
	 * @return TRUE if high contrast mdoe is on, FALSE otherwise.
	 */
	public static boolean isHighContrastMode()
	{
		return highContrastMode;
	}
	
	/**
	 * Sets the high contrast mode on or off.
	 * 
	 * @param newHighContrastMode TRUE to set the high contrast mode on,
	 * FALSE to turn it off.
	 */
	public static void setHighContrastMode(boolean newHighContrastMode)
	{
		highContrastMode = newHighContrastMode;
	}
}
