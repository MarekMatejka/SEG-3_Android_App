package com.seg.questionnaire.backend.json;

public class QuestionJSON 
{
	long id;
	
	String title;
	
	//0 = scale, 1 = choosemany, 2 = yes/no, 3 = text, 4 = chooseone, 5 = rank
	int type;
	
	boolean required;
	
	DetailsJSON details;
	
	DependentQuestionsJSON dependent_questions;
	
	public String toString()
	{	
		String s = "q id = "+id+" | q title = "+title+" | q type = "+type+" | q required = "+required;
		
		if (details != null)
			s += " | "+details.toString(type);
		
		if (dependent_questions != null)
			s += " | "+dependent_questions.toString();
		
		return s;
	}
}
