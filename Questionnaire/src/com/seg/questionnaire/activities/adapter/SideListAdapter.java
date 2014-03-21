package com.seg.questionnaire.activities.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.question.Question;

/**
 * Adapter that is used to take care of the side list in the {@link #QuestionActivity}.
 * 
 * @author Marek Matejka
 *
 */
public class SideListAdapter extends ArrayAdapter<Question>
{
	private Context context;
	
	private List<Question> questions;
	
	/**
	 * Public constructor.
	 * 
	 * @param context Context of the Activity.
	 * @param questions List of questions in the questionnaire.
	 */
	public SideListAdapter(Context context, List<Question> questions)
	{
		super(context, R.layout.layout_side_list_row, questions);
		this.context = context;
		this.questions = questions;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{		
		//inflate layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View row = inflater.inflate(R.layout.layout_side_list_row, parent, false); //get row of the list
			
		Question q = questions.get(position); //get question
		
		TextView user = (TextView) row.findViewById(R.id.sideListRow);
		user.setText(q.getQuestion()); //set text of textview in the layout
		if (q.isAnswered()) //if the question is answered
			user.setBackgroundColor(Color.GREEN); //set its background green

	    return row; //return the row
	}
}
