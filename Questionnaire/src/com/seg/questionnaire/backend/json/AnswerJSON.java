package com.seg.questionnaire.backend.json;

import java.util.List;

/**
 * JSON template class for Answer object. 
 * 
 * @author Marek Matejka
 *
 */
public class AnswerJSON 
{
	/**
	 * Question ID field.
	 */
	@SuppressWarnings("unused")
	private String question_id;
	
	/**
	 * Answer field.
	 */
	@SuppressWarnings("unused")
	private List<String> answer;

	/**
	 * Sets the question_id field with given value.
	 * 
	 * @param question_id Value of the field.
	 */
	public void setQuestion_id(String question_id) 
	{
		this.question_id = question_id;
	}

	/**
	 * Sets the answer field with the given value.
	 * 
	 * @param answer Value of the field.
	 */
	public void setAnswer(List<String> answer) 
	{
		this.answer = answer;
	}

}
