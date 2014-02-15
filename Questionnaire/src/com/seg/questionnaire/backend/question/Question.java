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
	 * Map of all dependent questions to a question.
	 */
	protected Map<String, List<Question>> dependentQuestions = new HashMap<String, List<Question>>();

	/**
	 * Returns the run-time generated View (layout)
	 * for the question.
	 * 
	 * @param context Context of the Activity.
	 * @return View (layout) generated based on the type
	 * of the question.
	 */
	public abstract View getView(Context context);
	
	/**
	 * Reads answer(s) from the question and saves them in 
	 * the appropriate format in the Answer object of the
	 * question.
	 */
	protected abstract void readAnswer();
	
	/**
	 * Checks whether the question is answered.
	 * Based on whether the question is required
	 * and if the user inputed some answer.
	 * 
	 * @return TRUE if the question is not required or
	 * is answered, FALSE if question is required and 
	 * was not answered.
	 */
	protected abstract boolean isAnswered();
	
	/**
	 * Flag specifying whether the question's answer was 
	 * read already or not. 
	 */
	private boolean read = false;
	
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
			question += " *";
		return this.question;
	}
	
	/**
	 * Reads the answer currently specified
	 * for the question or NULL.
	 * Firstly checks whether the question is answered,
	 * then reads the answer and then returns it.
	 * 
	 * @return Answer for the question, if it is answered,
	 * NULL otherwise.
	 */
	public Object getAnswer()
	{
		if (isAnswered())
		{
			if (!read)
			{
				readAnswer();
				read = true;
			}
			return this.answer.getAnswer();
		}
		else
			return null;
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
}