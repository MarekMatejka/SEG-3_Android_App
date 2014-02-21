package com.seg.questionnaire.backend.json;

import java.util.List;

/**
 * Class used as a template for Details object
 * in the JSON.
 * 
 * @author Marek Matejka
 *
 */
public class DetailsJSON 
{
	/**
	 * Upper bound, defined only for Scale question. 
	 */
	private int upper_bound;
	
	/**
	 * Lower bound, defined only for Scale question.
	 */
	private int lower_bound;
	
	/**
	 * Answer options, defined for SelectManyQuestions, 
	 * SelectOneQuestion and Rank Question.
	 */
	private List<String> choices;
	
	/**
	 * Returns an upper bound for Scale question.
	 * 
	 * @return Upper bound for a Scale question.
	 */
	public int getUpperBound() 
	{
		return upper_bound;
	}

	/**
	 * Returns a lower bound for Scale question.
	 * 
	 * @return Lower bound for Scale question.
	 */
	public int getLowerBound() 
	{
		return lower_bound;
	}

	/**
	 * Returns answer options, for
	 * SelectManyQuestion, SelectOneQuestion and RankQuestion.
	 * 
	 * @return Answer options.
	 */
	public List<String> getChoices() 
	{
		return choices;
	}

	/**
	 * Returns Details object in String.
	 * 
	 * @param type Question type.
	 * @return Details object as a String.
	 */
	public String toString(int type)
	{
		switch(type)
		{
			case 0: return "low bound = "+lower_bound+" | up bound = "+upper_bound;
			case 2: return " | no details allowed | ";
			case 3: return " | no details allowed | ";
			default: 
				String s = "choices = ";
				for (String ss : choices)
					s += ss+", ";
				return s;
		}
	}
}
