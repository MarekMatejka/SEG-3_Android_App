package com.seg.questionnaire.backend.question;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;

import com.seg.questionnaire.backend.answer.Answer;

/**
 * Abstract class which defines methods for all questions.
 * 
 * @author Marek Matejka
 *
 */
public abstract class Question 
{
	/**
	 * Question's unique ID. 
	 */
	protected String id;
	
	/**
	 * Question text.
	 */
	protected String question;
	
	/**
	 * Answer for the question - Single/Multiple Answer.
	 */
	protected Answer answer;
		
	/**
	 * Flag specifying if the question is required.
	 */
	protected boolean required;
	
	/**
	 * Description of the question as specified by the doctor.
	 */
	private String description;
	
	/**
	 * Map of all dependent questions to a question.
	 */
	protected Map<String, List<Question>> dependentQuestions = new HashMap<String, List<Question>>();

	/**
	 * Returns the run-time generated View (layout)
	 * for the question.
	 * 
	 * @param context Context of the Activity.
	 * @param highContrastMode Defines whether highContrastMode is on or not.
	 * @return View (layout) generated based on the type
	 * of the question.
	 */
	public abstract View getView(Context context, boolean highContrastMode);
	
	/**
	 * Reads answer(s) from the question and saves them in 
	 * the appropriate format in the Answer object of the
	 * question.
	 */
	protected abstract void readAnswer();
	
	/**
	 * Checks whether the question's answer is ready 
	 * (has been selected or is ready to be saved).
	 * Based on whether the question is required
	 * and if the user inputed some answer.
	 * 
	 * @return TRUE if the question has answer that 
	 * is non-empty, FALSE otherwise.
	 */
	public abstract boolean isAnswerReady();
	
	/**
	 * Loads the current answer into the view.
	 */
	public abstract void loadAnswer();
	
	/**
	 * General constructor for Question object.
	 *  
	 * @param id Question ID.
	 * @param question Question text.
	 * @param required Is the question required?
	 * @param description Description of the question.
	 */
	public Question (String id, String question, boolean required, String description)
	{
		this.id = id;
		this.question = question;
		this.required = required;
		this.description = description;
	}
	
	/**
	 * Returns the question text.
	 * Adds an asterisk (*) to a question that
	 * is set to be required.
	 * 
	 * @return Question text.
	 */
	public String getQuestion()
	{
		if (required)
			return question +" *";
		return this.question;
	}
	
	/**
	 * Returns the currently selected answer.
	 * 
	 * @return Answer to the question.
	 */
	public String getAnswer()
	{
		return this.answer.getAnswer();
	}
	
	/**
	 * Saves the currently selected answer.
	 */
	public void saveAnswer()
	{
		//if there is some answer already, erase it
		if (!answer.getAnswer().equals(""))
			answer.clearAnswer();
		
		if (isAnswerReady())
			readAnswer();
	}
	
	/**
	 * Sets the question text.
	 * 
	 * @param question New question text.
	 */
	public void setQuestion(String question)
	{
		this.question = question;
	}
	
	/**
	 * Sets the answer.
	 * 
	 * @param answer New answer for the question.
	 */
	public void setAnswer(Answer answer)
	{
		this.answer = answer;
	}
		
	/**
	 * Checks whether the question is required or not.
	 * 
	 * @return TRUE if required, FALSE otherwise.
	 */
	public boolean isRequired()
	{
		return this.required;
	}
	
	/**
	 * Sets the required flag for a question.
	 * 
	 * @param required New value for the flag.
	 */
	public void setRequired(boolean required)
	{
		this.required = required;
	}
	
	/**
	 * Adds dependent question and its condition to
	 * a question.
	 * 
	 * @param condition Condition under which the dependent
	 * question will be displayed.
	 * @param question Dependent question displayed once the
	 * condition is met.
	 */
	public void addDependentQuestion(String condition, Question question)
	{		
		List<Question> list = getDependentQuestions(condition);
		list.add(question);
		dependentQuestions.put(condition, list);
	}
	
	/**
	 * Adds a list of dependent questions and their condition
	 * to a question.
	 * 
	 * @param condition Condition under which dependent 
	 * questions will be displayed.
	 * @param questions List of dependent questions displayed once the
	 * condition is met.
	 */
	public void addDependentQuestions(String condition, List<Question> questions)
	{
		for (int i = 0; i < questions.size(); i++)
			addDependentQuestion(condition, questions.get(i));
	}
	
	/**
	 * Returns the List of dependent question for a given condition.
	 * 
	 * @param condition Condition for which dependent questions are
	 * desired.
	 * @return List of dependent questions based on the given condition.
	 */
	public List<Question> getDependentQuestions(String condition)
	{
		if (this.dependentQuestions.get(condition) != null)
			return this.dependentQuestions.get(condition);
		return new LinkedList<Question>();
	}
	
	/**
	 * Checks whether the question has any dependent questions
	 * for a given condition.
	 * 
	 * @param condition Condition which is used to search for
	 * dependent questions.
	 * @return TRUE if dependent questions exists for a given 
	 * condition, FALSE otherwise.
	 */
	public boolean hasDependentQuestions(String condition)
	{
		if (getDependentQuestions(condition).size() > 0)
			return true;
		return false;
	}
	
	/**
	 * Returns the question's ID.
	 * 
	 * @return Question's ID.
	 */
	public String getID()
	{
		return this.id;
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object question)
	{
		Question q = (Question)question; //change Object to Question
		if (this.id == q.getID()) //compare based on ID
			return true;
		return false;
	}
	
	/**
	 * Says whether the question contains an answer or not.
	 * 
	 * @return TRUE if some answer was already selected,
	 * FALSE otherwise.
	 */
	public boolean isAnswered()
	{
		if (answer.getAnswer().equals(""))
			return false;
		return true;
	}
	
	/**
	 * Returns answers as a List.
	 * 
	 * @return List of answers.
	 */
	public List<String> getAnswerAsList()
	{
		return answer.getAnswersAsList();
	}
	
	/**
	 * Returns the description defined for this Question.
	 * 
	 * @return Description of the question.
	 */
	public String getDescription()
	{
		return this.description;
	}
}
