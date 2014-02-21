package com.seg.questionnaire.backend.json;

import java.util.List;

/**
 * Class used as a template for Dependent Question object
 * in the JSON.
 * 
 * @author Marek Matejka
 *
 */
public class DependentQuestionsJSON 
{
	/**
	 * Condition attribute in the JSON.
	 */
	private String condition;
	
	/**
	 * Array of Question objects in the JSON.
	 */
	private List<QuestionJSON> questions;
	
	/**
	 * Returns the condition.
	 * 
	 * @return Condition assigned.
	 */
	public String getCondition() 
	{
		return condition;
	}

	/**
	 * Returns a list of questions assigned.
	 * 
	 * @return List of assigned questions.
	 */
	public List<QuestionJSON> getQuestions() 
	{
		return questions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String qs = "";
		for (QuestionJSON q : questions)
			qs += " ?? "+q.toString();
		
		return "dep condition = "+condition+qs;
	}
}