package com.seg.questionnaire.backend.json;

import java.util.List;

public class DependentQuestionsJSON 
{
	String condition;
	
	List<QuestionJSON> questions;
	
	public String toString()
	{
		String qs = "";
		for (QuestionJSON q : questions)
			qs += " ?? "+q.toString();
		
		return "dep condition = "+condition+qs;
	}
}