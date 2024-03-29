package com.seg.questionnaire.backend.question;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.seg.questionnaire.R;
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
	 * @param description Description of the question.
	 */
	public SelectManyQuestion(String id, String question, List<String> answerOptions, boolean required, String description)
	{
		super(id, question, required, description);
		this.answer = new MultipleAnswer(id);
		this.answerOptions = answerOptions;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context, boolean highContrastMode) 
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
			c.setId(i);
			c.setText(answerOptions.get(i).toString());
			if (highContrastMode)
			{
				c.setButtonDrawable(context.getResources().getDrawable(R.drawable.check_box_style_bw));
				c.setTextColor(context.getResources().getColor(R.color.black));
			}
			else
			{
				c.setButtonDrawable(context.getResources().getDrawable(R.drawable.check_box_style));
				c.setTextColor(context.getResources().getColor(R.color.white));
			}
			c.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));
			
			//add the CheckBox to options List and to the LinearLayout
			options.add(c);
			l.addView(c);
		}
		
		setAccessibilityFocuses();
		
		return l; //return filled LinearLayout
	}

	@Override
	protected void setAccessibilityFocuses()
	{
		for (CheckBox cb : options)
		{
			cb.setFocusable(true);
			if (options.size() > 1)
			{
				if (cb.getId() == 0)
				{
					cb.setNextFocusDownId(1);
					cb.setNextFocusUpId(R.id.description);
				}
				else if (cb.getId() == options.size()-1)
				{
					cb.setNextFocusDownId(R.id.outOf);
					cb.setNextFocusUpId(options.size()-2);
				}
				else
				{
					cb.setNextFocusDownId(cb.getId()+1);
					cb.setNextFocusUpId(cb.getId()-1);
				}
			}
		}
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
				this.answer.addAnswer(c.getText().toString());
		}
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswerReady()
	 */
	@Override
	public boolean isAnswerReady() 
	{
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

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#loadAnswer()
	 */
	@Override
	public void loadAnswer() 
	{
		String a = answer.getAnswer();
		if (!a.equals(""))
			for (CheckBox b : options)
				if (a.contains(b.getText().toString()))
					b.setChecked(true);
	}
}
