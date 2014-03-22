package com.seg.questionnaire.backend.json;

import java.util.HashMap;
import java.util.List;

/**
 * Class used as a template for Question object
 * in the JSON.
 * 
 * @author Marek Matejka
 *
 */
public class QuestionJSON 
{
	/**
	 * Question's id.
	 */
	private String id;
	
	/**
	 * Question's title.
	 */
	private String title;
	
	/**
	 * Question's type.
	 * 0 = scale, 1 = choosemany, 2 = yes/no, 3 = text, 4 = chooseone, 5 = rank
	 */
	private int type;
	
	/**
	 * Flag is question is required or not.
	 */
	private boolean required;
	
	private String description;
	
	private HashMap<String, List<QuestionJSON>> dependentQuestions;
	
	private int upperBound, lowerBound;
	
	private List<String> answerOptions;
		
	/**
	 * Return question's id.
	 * 
	 * @return Question's id.
	 */
	public String getId() 
	{
		return id;
	}

	/**
	 * Returns question's title.
	 * 
	 * @return Question's title.
	 */
	public String getTitle() 
	{
		return title;
	}

	/**
	 * Returns question type.
	 * 0 = scale, 1 = choosemany, 2 = yes/no, 3 = text, 4 = chooseone, 5 = rank
	 * 
	 * @return Question type.
	 */
	public int getType() 
	{
		return type;
	}

	/**
	 * Defines whether the question is required or not.
	 * 
	 * @return TRUE if the question is required, FALSE otherwise.
	 */
	public boolean isRequired() 
	{
		return required;
	}

	public String getDescription() {
		return description;
	}
	
	public HashMap<String, List<QuestionJSON>> getDependentQuestions() {
		return dependentQuestions;
	}
	
	public List<QuestionJSON> getDependentQuestions(String key)
	{
		return dependentQuestions.get(key);
	}
	
	public int getUpperBound() {
		return upperBound;
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public List<String> getAnswerOptions() {
		return answerOptions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{	
		String s = "q id = "+id+" | q title = "+title+" | q type = "+type+" | q required = "+required;
		return s;
	}
	
	
}
