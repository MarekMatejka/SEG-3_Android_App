package com.seg.questionnaire.backend.question;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.seg.questionnaire.backend.answer.SingleAnswer;

/**
 * Class defining Free Text Question.
 * 
 * @author Marek Matejka
 *
 */
public class TextQuestion extends Question
{
	/**
	 * EditText for the answer.
	 */
	private EditText et;
	
	/**
	 * Constructor of the TextQuestion.
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param required Flag defining whether the question
	 * is required or not.
	 */
	public TextQuestion(long id, String question, boolean required)
	{
		this.id = id;
		this.question = question;
		this.answer = new SingleAnswer(id);
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context) 
	{	
		//Initialize and prepare EditText
		et = new EditText(context);
		et.setEms(10);
		et.requestFocus();
		
		return et;//return the EditText
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		this.answer.addAnswer(et.getText());
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswered()
	 */
	@Override
	protected boolean isAnswered() 
	{
		if (!required || !et.getText().toString().trim().equals(""))
			return true;
		return false;
	}

}
