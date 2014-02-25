package com.seg.questionnaire.backend.answer;


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
	private Object answer;
	
	/**
	 * Constructor which sets the answer to default value.
	 * 
	 * @param id ID of a Question to which this Answer belongs.
	 */
	public SingleAnswer(long id)
	{
		this.id = id;
		this.answer = "";
	}
	
	/**
	 * Constructor which sets the answer to a given value.
	 * 
	 * @param answer New value of the answer.
	 */
	public SingleAnswer(Object answer)
	{
		this.answer = answer;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#getAnswer()
	 */
	@Override
	public Object getAnswer() 
	{
		return answer;
	}

	/**
	 * Sets the answer to the given value.
	 * 
	 * @param answer New value of the answer.
	 */
	public void setAnswer(Object answer)
	{
		this.answer = answer;
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#addAnswer(java.lang.Object)
	 */
	@Override
	public void addAnswer(Object answer) 
	{
		this.answer = answer;
	}
}
