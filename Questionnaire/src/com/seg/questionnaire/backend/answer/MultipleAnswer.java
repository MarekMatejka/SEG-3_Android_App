package com.seg.questionnaire.backend.answer;

import java.util.LinkedList;
import java.util.List;

/**
 * Answer that contains multiple answers to a given question.
 * Used with SelectManyQuestion.
 * 
 * @author Marek Matejka
 *
 */
public class MultipleAnswer extends Answer
{
	/**
	 * Answers for a given question.
	 */
	List<Object> answers;
	
	/**
	 * Constructor that sets the answer to be empty.
	 */
	public MultipleAnswer()
	{
		this.answers = new LinkedList<Object>();
	}
	
	/**
	 * Constructor that sets the List of answers as answers
	 * to a given question.
	 * 
	 * @param answers Initial answers to a question.
	 */
	public MultipleAnswer(List<Object> answers)
	{
		this.answers = answers;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#getAnswer()
	 */
	@Override
	public Object getAnswer() 
	{
		if (answers.isEmpty())
			return "";
		
		String answer = answers.get(0).toString();
		for (int i = 1; i < answers.size(); i++)
			answer += ", "+answers.get(i).toString();
		return answer;
	}
	
	/**
	 * Sets answers for the question.
	 * 
	 * @param answers New List of answers for a question.
	 */
	public void setAnswers(List<Object> answers)
	{
		this.answers = answers;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.answer.Answer#addAnswer(java.lang.Object)
	 */
	@Override
	public void addAnswer(Object answer)
	{
		this.answers.add(answer);
	}
	
	/**
	 * Removes a given answer from current answers.
	 * 
	 * @param answer Answer to be removed.
	 */
	public void removeAnswer(Object answer)
	{
		this.answers.remove(answer);
	}
}