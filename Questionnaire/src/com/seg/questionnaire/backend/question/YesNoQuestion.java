package com.seg.questionnaire.backend.question;

import java.util.ArrayList;
import java.util.List;

/**
 * Class defining Yes/No Question.
 * 
 * @author Marek Matejka
 *
 */
public class YesNoQuestion extends SelectOneQuestion
{		
	/**
	 * List of answer options containing only 'Yes' and 'No' answers.
	 */
	@SuppressWarnings("serial")
	private static final List<String> answerOptions = new ArrayList<String>() {{add("Yes"); add("No");}};
	
	/**
	 * Constructor defining question text and if the 
	 * question is required or not.
	 * Automatically, sets answer options.
	 * 
	 * @param question Question text.
	 * @param required Defines whether the question
	 * is required or not.
	 */
	public YesNoQuestion(String question, boolean required)
	{
		super(question, answerOptions, required);
	}
}
