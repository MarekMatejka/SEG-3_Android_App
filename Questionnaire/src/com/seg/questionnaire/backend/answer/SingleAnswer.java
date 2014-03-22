package com.seg.questionnaire.backend.answer;

import java.util.LinkedList;
import java.util.List;


/**
 * Answer that contains only a single answer.
 * Used with RangeQuestion, SelectOneQuestion, TextQuestion and YesNoQuestion.
 * 
 * @author Marek Matejka
 *
 */
public class SingleAnswer extends Answer
{
	/**
	 * Currently set answer.
	 */
	private String answer;
	
	/**
	 * Constructor which sets the answer to default value.
	 * 
	 * @param id ID of a Question to which this Answer belongs.
	 */
	public SingleAnswer(String id)
	{
		super(id);
		this.answer = "";
	}
	
	/**
	 * Constructor which sets the answer to a given value.
	 * 
	 * @param answer New value of the answer.
	 */
	public SingleAnswer(String id, String answer)
	{
		super(id);
		this.answer = answer;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#getAnswer()
	 */
	@Override
	public String getAnswer() 
	{
		return answer;
	}

	/**
	 * Sets the answer to the given value.
	 * 
	 * @param answer New value of the answer.
	 */
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#addAnswer(java.lang.Object)
	 */
	@Override
	public void addAnswer(String answer) 
	{
		this.answer = answer;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#clearAnswer()
	 */
	@Override
	public void clearAnswer()
	{
		this.answer = "";
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#getAnswersAsArray()
	 */
	@Override
	public List<String> getAnswersAsList() 
	{
		List<String> l = new LinkedList<String>();
		l.add(answer);
		return l;
	}
}
