package com.seg.questionnaire.backend.question;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.answer.SingleAnswer;

/**
 * Class defining Select One Question (Radio Button question).
 * 
 * @author Marek Matejka
 *
 */
public class SelectOneQuestion extends Question 
{
	/**
	 * List of answer options.
	 */
	private List<String> answerOptions;
	
	/**
	 * Array of RadioButtons used as answer options. 
	 */
	private SparseArray<RadioButton> options;
	
	/**
	 * RadioGroup grouping all RadioButtons.
	 */
	private RadioGroup rg;

	/**
	 * Constructor for SelecOneQuestion.
	 * 
	 * @param id Question's unique id.
	 * @param question Question text.
	 * @param answerOptions List of answer options.
	 * @param required Flag defining whether the question
	 * is required or not.
	 */
	public SelectOneQuestion(String id, String question, List<String> answerOptions, boolean required)
	{
		super(id, question, required);
		this.answer = new SingleAnswer(id);
		this.answerOptions = answerOptions;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context) 
	{
		//create linear layout and set its orientation
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		
		//Initialize the RadioGroup
		rg = new RadioGroup(context);
		
		//initialize options
		options = new SparseArray<RadioButton>();
		
		//for all answerOptions
		for (int i = 0; i < answerOptions.size(); i++)
		{
			//create new RadioButton and set its text
			RadioButton rb = new RadioButton(context);
			rb.setText(answerOptions.get(i).toString());
			rb.setTextColor(context.getResources().getColor(R.color.white));
			rb.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));
			
			//add the RadioButton to the RadioGroup and options list.
			rg.addView(rb);
			options.put(rb.getId(), rb);
		}
				
		//add RadioGroup with all RadioButtons to the linear layout
		l.addView(rg);
		return l; //return the linear layout (View)
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		RadioButton rb = options.get(rg.getCheckedRadioButtonId());
		if (rb != null)
			this.answer.addAnswer(rb.getText().toString());
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswered()
	 */
	@Override
	public boolean isAnswerReady() 
	{
		if (rg.getCheckedRadioButtonId() != -1)
			return true;			
		return false;
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#loadAnswer()
	 */
	@Override
	public void loadAnswer() 
	{	
		String a = answer.getAnswer();
		if (!a.equals(""))
			for (int i = 0; i < options.size(); i++)
				if (options.get(options.keyAt(i)).getText().toString().equals(a))
				{
					options.get(options.keyAt(i)).setChecked(true);
					break;
				}
	}
}
