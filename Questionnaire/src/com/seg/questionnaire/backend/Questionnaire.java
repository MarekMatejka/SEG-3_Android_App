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
	 * Questionnaire's ID.
	 */
	private long id;
	
	/**
	 * List of all questions contained in the questionnaire.
	 */
	private List<Question> questions;
	
	/**
	 * Constructor which initializes the Questionnaire.
	 * Currently it also loads dummy data.
	 * 
	 * @param questions List of questions for the questionnaire.
	 */
	public Questionnaire(long id, List<Question> questions) 
	{
		this.id = id;
		this.questions = questions;
	}
	
	/**
	 * Returns questionnaire's ID.
	 * 
	 * @return Questionnaire's ID.
	 */
	public long getQuestionnaireID()
	{
		return this.id;
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
	 * Sets the questionnaire's list of questions to the the 
	 * given list of questions.
	 * 
	 * @param questions List of new questionnaire's questions.
	 */
	public void setQuestions(List<Question> questions)
	{
		this.questions = questions;
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
	 * UNUSED AT THE MOMENT!
	 * 
	 * Loads dummy data into the questionnaire.
	 */
	public void loadDummy()
	{
		//list of possible answers
		List <String> a = new LinkedList<String>();
		a.add("awesome");
		a.add("good");
		a.add("can be"); 
		a.add("nothing special");
		a.add("terrible");
		
		//add different questions
		questions.add(new TextQuestion("1", "1. Tell me how you are", false));
		
		//add also dependent questions
		YesNoQuestion y = new YesNoQuestion("2", "2. Are you good?", true);
		y.addDependentQuestion("Yes", new YesNoQuestion("3", "2.0 Feeling well?", true));
		y.addDependentQuestion("Yes", new SelectOneQuestion("4", "2.1 Mood?", a, true));
		y.addDependentQuestion("No", new SelectManyQuestion("5", "2.2 Mood? 2", a, false));
		questions.add(y);
		
		//some more questions and dependent questions
		SelectManyQuestion m = new SelectManyQuestion("6", "3. Pick your mood", a, true);
		m.addDependentQuestion("awesome", new YesNoQuestion("7", "3.1 Is it raining?", false));
		m.addDependentQuestion("nothing special", new YesNoQuestion("8", "3.2 Is it freezing?", false));
		questions.add(m);
		questions.add(new SelectOneQuestion("9", "4. What is your current mood?", a, true));
		RangeQuestion r = new RangeQuestion("10", "5. How good are you?", -10, 10, true);
		r.addDependentQuestion("10", new YesNoQuestion("11", "5.1 Is it snowing?", false));
		questions.add(r);
		
		questions.add(new RankQuestion("12", "Test", a, true));
		
	}
	
	/**
	 * Deletes the content of the Questionnaire.
	 */
	public void deleteQuestionnaire()
	{
		questions.clear();
	}
	
	/**
	 * Returns all the questions of the questionanire.
	 * 
	 * @return All questionnaire's questions.
	 */
	public List<Question> getQuestions()
	{
		return this.questions;
	}
	
	/**
	 * Removes a single question from the questionnaire.
	 * 
	 * @param question Question to be deleted.
	 */
	public void removeQuestion(Question question)
	{
		this.questions.remove(question);
	}
	
	/**
	 * Removes all questions from the list from the questionnaire.
	 * 
	 * @param questions List of questions to delete from the questionnaire.
	 */
	public void removeQuestions(List<Question> questions)
	{
		for (Question q : questions)
			this.questions.remove(q);
	}
	
	/**
	 * Checks whether all required questions are completed.
	 * 
	 * @return TRUE if all required questions are completed,
	 * FALSE otherwise.
	 */
	public boolean isCompleted()
	{
		for (Question q : questions)
			if (q.isRequired() && !q.isAnswered())
				return false;
		return true;
	}
	
	/**
	 * Finds the index of the first required question that
	 * needs to be completed.
	 * 
	 * @return Index of the first required question that needs to be
	 * completed or -1 if no such question is found.
	 */
	public int getFirstRequiredQuestionToComplete()
	{
		for (int i = 0; i < questions.size(); i++)
		{
			Question q = questions.get(i);
			if (q.isRequired() && !q.isAnswered())
				return i;
		}
		return -1;
	}
	
	/**
	 * Finds the index of the first non-required question that
	 * needs to be completed.
	 * 
	 * @return Index of the first non-required question that needs to be
	 * completed or -1 if no such question is found.
	 */
	public int getFirstNonRequiredQuestionToComplete()
	{
		for (int i = 0; i < questions.size(); i++)
		{
			Question q = questions.get(i);
			if (!q.isRequired() && !q.isAnswered())
				return i;
		}
		return -1;
	}
}
