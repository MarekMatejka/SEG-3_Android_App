package com.seg.questionnaire.backend.question;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.seg.questionnaire.backend.answer.MultipleAnswer;
import com.seg.questionnaire.backend.question.adapter.SpinnerAdapter;

/**
 * Class representing a RankQuestion.
 * 
 * @author Marek Matejka
 *
 */
public class RankQuestion extends Question implements OnItemSelectedListener
{
	/**
	 * List of all answer options.
	 */
	private List<String> answerOptions;
	
	/**
	 * List of all Spinners used.
	 */
	private List<Spinner> spinners;
	
	/**
	 * Flag specifying whether the user already tried to submit his answer.
	 * Used to evaluate the content only after the user already made his choices.
	 */
	private boolean submitted = false;

	/**
	 * Constructor for RankQuestion
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param answerOptions List of answer options.
	 * @param required Flag specifying whether the question is required or not.
	 */
	public RankQuestion(long id, String question, List<String> answerOptions, boolean required)
	{
		this.id = id;
		this.question = question;
		this.answer = new MultipleAnswer(id);
		this.answerOptions = answerOptions;
		this.required = required;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context) 
	{
		spinners = new LinkedList<Spinner>();
		
		//create and initialize the overall layout
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		
		for (int i = 0; i < answerOptions.size(); i++)
		{
			//create and initialize layout for a row
			LinearLayout inner = new LinearLayout(context);
			inner.setOrientation(LinearLayout.HORIZONTAL);
			
			//add answer option text
			TextView a = new TextView(context);
			a.setText(answerOptions.get(i).toString());
			
			//add Spinner with proper ID, Adapter, Tag and listener
			Spinner s = new Spinner(context);
			s.setId(i);
			s.setAdapter(new SpinnerAdapter(context, answerOptions.size()));
			s.setTag(answerOptions.get(i).toString());
			s.setOnItemSelectedListener(this);
			
			spinners.add(s); //add spinner to the list
			
			//add TextView and Spinner to row's layout
			inner.addView(a);
			inner.addView(s);
			
			l.addView(inner); //add row to overall layout
		}
		
		return l; //return overall layout
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		for (int i = 0; i < spinners.size(); i++)
			answer.addAnswer(spinners.get(i).getTag().toString()+" "+spinners.get(i).getSelectedItem().toString());
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswered()
	 */
	@Override
	protected boolean isAnswered() 
	{
		submitted = true; //user already tried to submit his answers
		if (!required || evaluate()) //not required or correctly filled.
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id) 
	{
		if (submitted) //only after attempted submission of answers
			evaluate();
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> parent) {}

	/**
	 * Checks whether there is no answer selected twice.
	 * If yes then appropriate Spinners are set red background.
	 * 
	 * @return If two Spinners has the same answer return FALSE,
	 * TRUE otherwise.
	 */
	private boolean evaluate()
	{
		boolean correct = true;
		for (Spinner s : spinners) //remove any background set previously
			s.setBackgroundColor(Color.TRANSPARENT);
		
		for (Spinner s : spinners) //check every Spinner against every Spinner
			for (Spinner s2 : spinners)
				if (s.getSelectedItemPosition() == s2.getSelectedItemPosition()	&&
					s.getId() != s2.getId()) //if the answer is the same and it is not the same Spinner
				{
					//set background of both Spinners to red
					s.setBackgroundColor(Color.RED);
					s2.setBackgroundColor(Color.RED);
					correct = false;
				}
		return correct;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getDependentQuestions(java.lang.String)
	 */
	@Override
	public List<Question> getDependentQuestions(String condition)
	{
		List<Question> list = new LinkedList<Question>();
		String[] a = split(condition);
		for (int i = 0; i < a.length; i++)
			if (this.dependentQuestions.get(a[i]) != null)
				list.addAll(this.dependentQuestions.get(a[i]));
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#hasDependentQuestions(java.lang.String)
	 */
	@Override
	public boolean hasDependentQuestions(String condition)
	{
		String[] a = split(condition);
		for (int i = 0; i < a.length; i++)
			if (this.dependentQuestions.containsKey(a[i]))
				return true;
		return false;
	}
	
	/**
	 * Splits the given String by comma (,).
	 * 
	 * @param s String to be split.
	 * @return Array of String containing
	 * parts of the initial String split by
	 * comma (,).
	 */
	private String[] split(String s)
	{
		String[] ss = s.split(",");
		for (int i = 0; i < ss.length; i++)
			ss[i] = ss[i].trim();
		return ss;
		
	}
}
