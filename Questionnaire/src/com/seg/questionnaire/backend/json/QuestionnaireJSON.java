package com.seg.questionnaire.backend.json;

import java.util.List;

/**
 * Class used as a template for Questionnaire object
 * in the JSON.
 * 
 * @author Marek Matejka
 */
public class QuestionnaireJSON 
{
	/**
	 * Questionnaire_id in JSON.
	 */
	private int questionnaire_id;

	/**
	 * Questionnaire_title in JSON.
	 */
	private String questionnaire_title;
	
	/**
	 * Array of questions in the questionnaire.
	 */
	private List<QuestionJSON> questions;

	/**
	 * Returns questionnaire id.
	 * 
	 * @return Questionnaire ID.
	 */
	public int getQuestionnaireId() 
	{
		return questionnaire_id;
	}

	/**
	 * Returns questionnaire title.
	 * 
	 * @return Questionnaire title.
	 */
	public String getQuestionnaireTitle() 
	{
		return questionnaire_title;
	}

	/**
	 * Returns a list of questions for the questionnaire.
	 * 
	 * @return List of questionnaire's questions.
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
			qs += "\n/// "+q.toString();
		return "ques_id = "+questionnaire_id+" | ques_title = "+questionnaire_title+qs;
	}
}
