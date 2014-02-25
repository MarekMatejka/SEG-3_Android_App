package com.seg.questionnaire.backend.question;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.seg.questionnaire.backend.answer.MultipleAnswer;

/**
 * Class defining SelectManyQuestion.
 * 
 * @author Marek Matejka
 *
 */
public class SelectManyQuestion extends Question
{
	/**
	 * List of possible answers.
	 */
	private List<String> answerOptions;
	
	/**
	 * List of CheckBoxes with answers.
	 */
	private List<CheckBox> options;
	
	/**
	 * Constructor of SelectManyQuestion.
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param answerOptions List of possible answers.
	 * @param required Flag defining whether the question
	 * is required or not.
	 */
	public SelectManyQuestion(long id, String question, List<String> answerOptions, boolean required)
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
		//initialize options list
		options = new LinkedList<CheckBox>();
		
		//declare and initialize LinearLayout
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		
		//for all answer options
		for (int i = 0; i < answerOptions.size(); i++)
		{
			//create a CheckBox and set its text to one of the answers
			CheckBox c = new CheckBox(context);
			c.setText(answerOptions.get(i).toString());
			
			//add the CheckBox to options List and to the LinearLayout
			options.add(c);
			l.addView(c);
		}
		
		return l; //return filled LinearLayout
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		for (int i = 0; i < options.size(); i++)
		{
			CheckBox c = options.get(i);
			if (c.isChecked())
				this.answer.addAnswer(c.getText());
		}
	}

	@Override
	protected boolean isAnswered() 
	{
		if (!required)
			return true;
		else
			for (int i = 0; i < options.size(); i++)
				if (options.get(i).isChecked())
					return true;
		return false;
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
