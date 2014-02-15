package com.seg.questionnaire.backend;

import java.util.LinkedList;
import java.util.List;

import com.seg.questionnaire.backend.question.Question;
import com.seg.questionnaire.backend.question.RangeQuestion;
import com.seg.questionnaire.backend.question.RankQuestion;
import com.seg.questionnaire.backend.question.SelectManyQuestion;
import com.seg.questionnaire.backend.question.SelectOneQuestion;
import com.seg.questionnaire.backend.question.TextQuestion;
import com.seg.questionnaire.backend.question.YesNoQuestion;

/**
 * Class representing a questionnaire.
 * 
 * @author Marek Matejka
 *
 */
public class Questionnaire 
{
	/**
	 * List of all questions contained in the questionnaire.
	 */
	private List<Question> questions;
	
	/**
	 * Constructor which initializes the Questionnaire.
	 * Currently it also loads dummy data.
	 */
	public Questionnaire() 
	{
		questions = new LinkedList<Question>();
	}
	
	/**
	 * Returns a question from the questionnaire
	 * based on the index (order).
	 * 
	 * @param index Index (order) of the question in the 
	 * questionnaire.
	 * @return Question found at a given position.
	 */
	public Question getQuestion(int index)
	{
		return this.questions.get(index);
	}
	
	/**
	 * Adds a question at the end of the questionnaire.
	 * 
	 * @param question Question to add.
	 */
	public void addQuestion(Question question)
	{
		this.questions.add(question);
	}
	
	/**
	 * Adds a question to the questionnaire at 
	 * one after a given position. So it makes it the
	 * next question after the currently selected, if
	 * <i>currentQuestion</i> is used.
	 * 
	 * @param question Question to add.
	 * @param index Position at which it should be added.
	 */
	public void addQuestion(Question question, int index)
	{
		this.questions.add(index+1, question);
	}
	
	/**
	 * Adds all questions from the list at one after
	 * a given position. So it makes them the next questions
	 * if <i>currentQuestion</i> is used.
	 * 
	 * @param questions List of Questions to add.
	 * @param index Position at which they should be added.
	 */
	public void addQuestions(List<Question> questions, int index)
	{
		//takes last question first so that I can keep adding them
		//to the same position and I still keep the original order
		for (int i = questions.size()-1; i >= 0; i--)
			this.questions.add(index+1, questions.get(i));
	}
	
	/**
	 * Returns the number of questions currently
	 * stored in the questionnaire.
	 * 
	 * @return Number of question in the questionnaire.
	 */
	public int getNumberOfQuestions()
	{
		return this.questions.size();
	}
	
	/**
	 * Loads dummy data into the questionnaire.
	 */
	public void loadDummy()
	{
		//list of possible answerss
		List <Object> a = new LinkedList<Object>();
		a.add("awesome");
		a.add("good");
		a.add("can be"); 
		a.add("nothing special");
		a.add("terrible");
		
		//add different questions
		questions.add(new TextQuestion("1. Tell me how you are", false));
		
		//add also dependent questions
		YesNoQuestion y = new YesNoQuestion("2. Are you good?", true);
		y.addDependentQuestion("Yes", new YesNoQuestion("2.0 Feeling well?", true));
		y.addDependentQuestion("Yes", new SelectOneQuestion("2.1 Mood?", a, true));
		y.addDependentQuestion("No", new SelectManyQuestion("2.2 Mood? 2", a, false));
		questions.add(y);
		
		//some more questions and dependent questions
		SelectManyQuestion m = new SelectManyQuestion("3. Pick your mood", a, true);
		m.addDependentQuestion("awesome", new YesNoQuestion("3.1 Is it raining?", false));
		m.addDependentQuestion("nothing special", new YesNoQuestion("3.2 Is it freezing?", false));
		questions.add(m);
		questions.add(new SelectOneQuestion("4. What is your current mood?", a, true));
		RangeQuestion r = new RangeQuestion("5. How good are you?", -10, 10, true);
		r.addDependentQuestion("10", new YesNoQuestion("5.1 Is it snowing?", false));
		questions.add(r);
		
		questions.add(new RankQuestion("Test", a, true));
		
	}
}
