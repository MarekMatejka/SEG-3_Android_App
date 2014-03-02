package com.seg.questionnaire;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.question.Question;
import com.seg.questionnaire.backend.question.RangeQuestion;
import com.seg.questionnaire.backend.question.RankQuestion;
import com.seg.questionnaire.backend.question.SelectManyQuestion;
import com.seg.questionnaire.backend.question.SelectOneQuestion;
import com.seg.questionnaire.backend.question.TextQuestion;
import com.seg.questionnaire.backend.question.YesNoQuestion;

/**
 * Class taking care of the tutorial
 * 
 * @author Marek Matejka
 *
 */
public class TutorialActivity extends Activity
{
	/**
	 * Questionnaire used.
	 */
	private Questionnaire ques;
	
	/**
	 * Takes count of which step is currently being completed.
	 */
	private int tutorialStep = 0;
	
	/**
	 * Takes count of which question is currently being answered.
	 */
	private int currentQuestion = 0;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("serial")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		//initialize the questionnaire
		ques = new Questionnaire(0, new LinkedList<Question>());
		
		//add Yes/No question
		ques.addQuestion(new YesNoQuestion(0, "Is the weather nice?", true));
		
		//add Select Many Question
		ques.addQuestion(new SelectManyQuestion(1, "What do you need to prepare ham and eggs?", 
												new LinkedList<String>() {{add("Ham"); 
																		   add("Honey"); 
																		   add("Lemon"); 
																		   add("Eggs"); 
																		   add("Apples");}}, 
												true));
		
		//add Select One Question
		SelectOneQuestion q1 = new SelectOneQuestion(2, "Which month is the shortest?",
													new LinkedList<String>() {{add("January"); 
													add("February"); 
													add("March"); 
													add("April"); 
													add("May");}}, 
													true);
		//add its dependent Question
		q1.addDependentQuestion("February", new SelectManyQuestion(3, "How many days can it have?",
																   new LinkedList<String>() {{add("28"); 
																   add("29"); 
																   add("30"); 
																   add("31");}}, 
																   true));
		ques.addQuestion(q1);
		
		//Add Range Question
		ques.addQuestion(new RangeQuestion(4, "Which month in a year is February? Select on a scale from 1 to 12, where 1 is January and 12 is December.", 
										   1, 12, true));
		//Add Non-Required Question
		ques.addQuestion(new RangeQuestion(5, "Which month in a year is August? Select on a scale from 1 to 12, where 1 is January and 12 is December.", 
				   1, 12, false));
		
		//Add Text Question
		ques.addQuestion(new TextQuestion(6, "Please fill in your name", true));
		
		//Add Rank Question
		ques.addQuestion(new RankQuestion(7, "Rank the following cities based on how much you like them", 
											 new LinkedList<String>() {{add("London"); 
											 							add("Paris"); 
											 							add("Bratislava"); 
											 							add("New York");}},
											 true));
		
		//load first question and first tutorial step
		loadQuestion(ques.getQuestion(currentQuestion));
		loadTutorialStep(tutorialStep);
	}
	
	
	private void loadQuestion(Question q)
	{	
		updateProgress();
		
		RelativeLayout answer = (RelativeLayout)findViewById(R.id.answer);
		answer.removeAllViews();
		answer.addView(q.getView(this));
		
		TextView question = (TextView)findViewById(R.id.question);
		question.setText(q.getQuestion());
	}
	
	private void updateProgress()
	{
		TextView outOf = (TextView) findViewById(R.id.outOf);
		outOf.setText(""+(currentQuestion+1)+"/"+ques.getNumberOfQuestions());
		
		ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
		p.setMax(ques.getNumberOfQuestions());
		p.setProgress(currentQuestion+1);
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
	
	public void onClick(View v)
	{
		currentQuestion++;
		
		if (currentQuestion < ques.getNumberOfQuestions())
			loadQuestion(ques.getQuestion(currentQuestion));
		else
			finish();
	}
}
