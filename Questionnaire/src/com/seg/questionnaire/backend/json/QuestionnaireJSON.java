package com.seg.questionnaire.backend.json;

import java.util.List;

public class QuestionnaireJSON 
{
	int questionnaire_id;
	
	String questionnaire_title;
	
	List<QuestionJSON> questions;

	public String toString()
	{
		String qs = "";
		for (QuestionJSON q : questions)
			qs += "\n /// "+q.toString();
		return "ques_id = "+questionnaire_id+" | ques_title = "+questionnaire_title+qs;
	}
}
