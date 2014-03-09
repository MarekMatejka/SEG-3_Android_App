package com.seg.questionnaire.backend.json;

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
	
	/**
	 * Details of the question - lower/upper bound, answer options.
	 */
	private DetailsJSON details;
	
	/**
	 * List of dependent questions to this question.
	 */
	private List<DependentQuestionsJSON> dependent_questions;
		
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

	/**
	 * Returns further details about the question.
	 * Details include lower/upper bound and answer options.
	 * 
	 * @return Details about the question.
	 */
	public DetailsJSON getDetails() 
	{
		return details;
	}

	/**
	 * Returns dependent questions for this question.
	 * 
	 * @return Dependent questions for this question.
	 */
	public List<DependentQuestionsJSON> getDependentQuestions() 
	{
		return dependent_questions;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{	
		String s = "q id = "+id+" | q title = "+title+" | q type = "+type+" | q required = "+required;
		
		if (details != null)
			s += " | "+details.toString(type);
		
		if (dependent_questions != null)
			for (DependentQuestionsJSON d : dependent_questions)
				s += " | "+d.toString();
		
		return s;
	}
}
