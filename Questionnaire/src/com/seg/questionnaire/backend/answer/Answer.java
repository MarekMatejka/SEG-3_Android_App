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
	 * Returns answer for a question to which it belongs.
	 * Answer is in comma separated format. 
	 * 
	 * @return Answer for a question to which it belongs.
	 */
	public abstract Object getAnswer();
	
	/**
	 * Adds/Sets the answer.
	 * 
	 * @param answer User's answer taken from the question.
	 */
	public abstract void addAnswer(Object answer);
}
