package com.seg.questionnaire.backend.json;

import java.util.LinkedList;
import java.util.List;

/**
 * Class used as template for JSON parsing.
 * 
 * @author Marek Matejka
 * 
 */
public class AnswersJSON 
{
	/**
	 * Questionnaire ID field.
	 */
	@SuppressWarnings("unused")
	private long questionnaire_id;
	
	/**
	 * Patient ID field.
	 */
	@SuppressWarnings("unused")
	private String patient_id;
	
	/**
	 * List of Answers.
	 */
	private List<AnswerJSON> answers;

	/**
	 * Sets the questionnaire_id field value.
	 * 
	 * @param questionnaire_id Value given to this field.
	 */
	public void setQuestionnaire_id(long questionnaire_id) 
	{
		this.questionnaire_id = questionnaire_id;
	}

	/**
	 * Sets the patient_id field value.
	 * 
	 * @param patient_id Value given to the field.
	 */
	public void setPatient_id(String patient_id) 
	{
		this.patient_id = patient_id;
	}

	/**
	 * Adds AnswerJSON object to the list of answers.
	 * 
	 * @param answer Object to be added.
	 */
	public void addAnswer(AnswerJSON answer)
	{
		if (answers == null)
			answers = new LinkedList<AnswerJSON>();
		answers.add(answer);
	}
	
	

}
