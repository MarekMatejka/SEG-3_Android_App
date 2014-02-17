package com.seg.questionnaire.backend.json;

import java.util.List;

public class DetailsJSON 
{
	int upper_bound;
	
	int lower_bound;
	
	List<String> choices;
	
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
