package com.seg.questionnaire.backend.answer;

/**
 * Abstract class defining basic method for all Answers.
 * 
 * @author Marek Matejka
 *
 */
public abstract class Answer 
{
	/**
	 * ID of a question to which this Answer belongs.
	 */
	protected String id;
	
	/**
	 * Returns answer for a question to which it belongs.
	 * Answer is in comma separated format. 
	 * 
	 * @return Answer for a question to which it belongs.
	 */
	public abstract String getAnswer();
	
	/**
	 * Adds/Sets the answer.
	 * 
	 * @param answer User's answer taken from the question.
	 */
	public abstract void addAnswer(String answer);
	
	/**
	 * Clears the content of the answer.
	 */
	public abstract void clearAnswer();
	
	/**
	 * Returns an ID of a Question which it belongs.
	 * 
	 * @return ID of a Question to which it belongs.
	 */
	public String getID()
	{
		return this.id;
	}
}
