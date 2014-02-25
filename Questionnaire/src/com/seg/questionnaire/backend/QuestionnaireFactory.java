package com.seg.questionnaire.backend;

import java.util.LinkedList;
import java.util.List;

import com.seg.questionnaire.backend.json.DependentQuestionsJSON;
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
		return new Questionnaire(convertQuestions(json.getQuestions()));
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
										  json.getDetails().getLowerBound(),
										  json.getDetails().getUpperBound(),
										  json.isRequired());
										  break;
			case 1: q = new SelectManyQuestion(json.getId(),
			/*Select Many Question*/  	 	   json.getTitle(),
					   						   json.getDetails().getChoices(),
											   json.isRequired());
											   break;
			case 2: q = new YesNoQuestion(json.getId(),
			/*Yes/No Question*/			  json.getTitle(), 
										  json.isRequired());
										  break;
			case 3: q = new TextQuestion(json.getId(),
			/*Text Question*/			 json.getTitle(), 
						 				 json.isRequired());
										 break;
			case 4: q = new SelectOneQuestion(json.getId(),
			/*Select One Question*/			  json.getTitle(),
											  json.getDetails().getChoices(), 
											  json.isRequired());
											  break;
			case 5: q = new RankQuestion(json.getId(),
			/*Rank Question*/			 json.getTitle(), 
										 json.getDetails().getChoices(),
										 json.isRequired());
			default: q = null; //Unknown error
		}
				
		List<DependentQuestionsJSON> l; //list of all dependent questions
		if ((l = json.getDependentQuestions()) != null) // if there are any
			for (DependentQuestionsJSON d : l) //loop through them
				q.addDependentQuestions(d.getCondition(), convertQuestions(d.getQuestions())); //add them to the question
		
		return q; //return question
	}
}