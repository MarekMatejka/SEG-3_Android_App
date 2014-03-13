package com.seg.questionnaire.backend.question;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.answer.MultipleAnswer;
import com.seg.questionnaire.backend.question.rankquestion.DynamicListView;
import com.seg.questionnaire.backend.question.rankquestion.StableArrayAdapter;

/**
 * Class representing a RankQuestion.
 * 
 * @author Marek Matejka
 *
 */
public class RankQuestion extends Question
{
	private StableArrayAdapter adapter;
	private DynamicListView list;
	
	/**
	 * List of all answer options.
	 */
	private List<String> answerOptions;

	/**
	 * Constructor for RankQuestion
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param answerOptions List of answer options.
	 * @param required Flag specifying whether the question is required or not.
	 */
	public RankQuestion(String id, String question, List<String> answerOptions, boolean required)
	{
		super(id, question, required);
		this.answer = new MultipleAnswer(id);
		this.answerOptions = answerOptions;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context)
	{
		list = new DynamicListView(context);
		adapter = new StableArrayAdapter(context, R.layout.text_view, answerOptions);
		
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setContentList(answerOptions);
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		for (int i = 0; i < answerOptions.size(); i++)
			answer.addAnswer(adapter.getItem(i).toString());
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswered()
	 */
	@Override
	public boolean isAnswerReady() 
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getDependentQuestions(java.lang.String)
	 */
	@Override
	public List<Question> getDependentQuestions(String condition)
	{
		List<Question> list = new LinkedList<Question>();
		String[] a = split(condition);
		for (int i = 0; i < a.length; i++)
			if (this.dependentQuestions.get(a[i]) != null)
				list.addAll(this.dependentQuestions.get(a[i]));
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#hasDependentQuestions(java.lang.String)
	 */
	@Override
	public boolean hasDependentQuestions(String condition)
	{
		String[] a = split(condition);
		for (int i = 0; i < a.length; i++)
			if (this.dependentQuestions.containsKey(a[i]))
				return true;
		return false;
	}
	
	/**
	 * Splits the given String by comma (,).
	 * 
	 * @param s String to be split.
	 * @return Array of String containing
	 * parts of the initial String split by
	 * comma (,).
	 */
	private String[] split(String s)
	{
		String[] ss = s.split(",");
		for (int i = 0; i < ss.length; i++)
			ss[i] = ss[i].trim();
		return ss;
		
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#loadAnswer()
	 */
	@Override
	public void loadAnswer() 
	{		
		//do nothing, updated automatically by adapter
	}
}
