package com.seg.questionnaire.backend.factories;

import com.google.gson.Gson;
import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.json.AnswerJSON;
import com.seg.questionnaire.backend.json.AnswersJSON;
import com.seg.questionnaire.backend.question.Question;

/**
 * Class that converts Questionnaire object into JSON template objects. 
 * 
 * @author Marek Matejka
 *
 */
public class AnswersFactory 
{
	/**
	 * Converts the whole Questionnaire object into AnswersJSON object
	 * and parses it into JSON String.
	 * 
	 * @param q Questionnaire object to convert.
	 * @return Converted Questionnaire into AnswersJSON formatted JSON String.
	 */
	public static String createJSON(Questionnaire q)
	{
		AnswersJSON a = new AnswersJSON();
		a.setPatient_id(1);
		a.setQuestionnaire_id(q.getQuestionnaireID());
		
		for (Question qq : q.getQuestions())
			a.addAnswer(getAnswerJSON(qq));
		
		Gson gson = new Gson();
		return gson.toJson(a);
	}
	
	/**
	 * Converts a Question object into AnswerJSON object.
	 * 
	 * @param q Question object to be converted.
	 * @return Converted AnswerJSON object.
	 */
	private static AnswerJSON getAnswerJSON(Question q)
	{
		AnswerJSON a = new AnswerJSON();
		a.setQuestion_id(q.getID());
		a.setAnswer(q.getAnswerAsList());
		return a;
	}

}
