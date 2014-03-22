package com.seg.questionnaire.backend.factories;

import java.util.LinkedList;
import java.util.List;

import com.seg.questionnaire.backend.Questionnaire;
import com.seg.questionnaire.backend.json.QuestionJSON;
import com.seg.questionnaire.backend.json.QuestionnaireJSON;
import com.seg.questionnaire.backend.question.Question;
import com.seg.questionnaire.backend.question.RangeQuestion;
import com.seg.questionnaire.backend.question.RankQuestion;
import com.seg.questionnaire.backend.question.SelectManyQuestion;
import com.seg.questionnaire.backend.question.SelectOneQuestion;
import com.seg.questionnaire.backend.question.TextQuestion;
import com.seg.questionnaire.backend.question.YesNoQuestion;

/**
 * Class that converts JSON parsed objects into
 * objects which could be used as the data.
 * 
 * @author Marek Matejka
 *
 */
public class QuestionnaireFactory 
{
	/**
	 * Creates a Questionnaire object from a parsed
	 * JSON questionnaire object.
	 * 
	 * @param json Parsed JSON Questionnaire object.
	 * @return Converted Questionnaire object.
	 */
	public static Questionnaire creatQuestionnaire(QuestionnaireJSON json)
	{
		return new Questionnaire(json.getQuestionnaireId(),
								 convertQuestions(json.getQuestions()),
								 json.getQuestionnaireTitle());
	}
	
	/**
	 * Converts a list of parsed JSON QuestionJSON objects to a
	 * list of Question objects.
	 * 
	 * @param questions List of parsed JSON Questions.
	 * @return List of converted Question objects.
	 */
	private static List<Question> convertQuestions(List<QuestionJSON> questions)
	{
		List<Question> q = new LinkedList<Question>();
		
		for (int i = 0; i < questions.size(); i++)
			q.add(createQuestionFromJSON(questions.get(i))); //add (converted) questions	
			
		return q;
	}
	
	/**
	 * Converts a question from QuestionJSON object to
	 * Question object.
	 * 
	 * @param json Parsed JSON object to convert.
	 * @return Converted object.
	 */
	private static Question createQuestionFromJSON(QuestionJSON json)
	{
		Question q;
		switch(json.getType()) //based on the question type
		{
			case 0: q = new RangeQuestion(json.getId(),
			/*Range Question*/			  json.getTitle(), 
										  json.getLowerBound(),
										  json.getUpperBound(),
										  json.isRequired());
										  break;
			case 1: q = new SelectManyQuestion(json.getId(),
			/*Select Many Question*/  	 	   json.getTitle(),
					   						   json.getAnswerOptions(),
											   json.isRequired());
					addDependentQuestions(json, q);
					break;
			case 2: q = new YesNoQuestion(json.getId(),
			/*Yes/No Question*/			  json.getTitle(), 
										  json.isRequired());
					addDependentQuestions(json, q);
					break;
			case 3: q = new TextQuestion(json.getId(),
			/*Text Question*/			 json.getTitle(), 
						 				 json.isRequired());
										 break;
			case 4: q = new SelectOneQuestion(json.getId(),
			/*Select One Question*/			  json.getTitle(),
											  json.getAnswerOptions(),
											  json.isRequired());
					addDependentQuestions(json, q);
					break;
			case 5: q = new RankQuestion(json.getId(),
			/*Rank Question*/			 json.getTitle(), 
										 json.getAnswerOptions(),
										 json.isRequired());
										 break;
			default: q = null; //Unknown error
		}
		
		return q; //return question
	}
	
	private static void addDependentQuestions(QuestionJSON json, Question q)
	{
		List<QuestionJSON> l; //list of all dependent questions
		for (String key : json.getAnswerOptions())
			if ((l = json.getDependentQuestions(key)) != null) // if there are any
				q.addDependentQuestions(key, convertQuestions(l)); //add them to the question
	}
}
