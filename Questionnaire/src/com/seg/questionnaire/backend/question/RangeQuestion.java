package com.seg.questionnaire.backend.question;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.seg.questionnaire.backend.answer.SingleAnswer;

/**
 * Class defining Range Question.
 * 
 * @author Marek Matejka
 *
 */
public class RangeQuestion extends Question 
{
	/**
	 * Lower and upper bounds of the range.
	 */
	private int lowerBound, upperBound;
	
	/**
	 * SeekBar shown to user, used to gain user's input.
	 */
	private SeekBar b;
	
	/**
	 * Constructor for a RangeQuestion.
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param lowerBound Lower bound of the range.
	 * @param upperBound Upper bound of the range.
	 * @param required Flag defining is the question is
	 * required or not.
	 */
	public RangeQuestion(long id, String question, int lowerBound, int upperBound, boolean required)
	{
		this.id = id;
		this.question = question;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.answer = new SingleAnswer(id);
		this.required = required;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context) 
	{			
		//create a linear layout with basic parameters
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		l.setLayoutParams(getLayoutParams());
	
		//create a TextView used to show the currently
		//selected value from the range (SeekBar) and apply
		//basic layout parameters
		final TextView t = new TextView(context);
		t.setText(""+(lowerBound+upperBound)/2);
		t.setLayoutParams(getLayoutParams());
		t.setGravity(Gravity.CENTER_HORIZONTAL);
		
		//create and initialize the SeekBar
		b = new SeekBar(context);
		b.setMax(upperBound-lowerBound);
		b.incrementProgressBy(1);
		b.setProgress((lowerBound+upperBound)/2-lowerBound);
		b.setLayoutParams(getLayoutParams());
		b.setOnSeekBarChangeListener(new OnSeekBarChangeListener() 
		{	
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			/* (non-Javadoc)
			 * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar, int, boolean)
			 */
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
			{
				t.setText(""+(progress+lowerBound));
			}
		});
		
		//add SeekBar and TextView to the layout
		l.addView(b);
		l.addView(t);
		return l; //return layout (View)
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#readAnswer()
	 */
	@Override
	protected void readAnswer() 
	{
		this.answer.addAnswer(Integer.toString(b.getProgress()+lowerBound));
	}

	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#isAnswered()
	 */
	@Override
	protected boolean isAnswered() 
	{
		return true;
	}
	
	/**
	 * Returns Layout Parameters used to properly
	 * define the view.
	 * 
	 * @return Layout Parameters defining the view.
	 */
	private final static LayoutParams getLayoutParams()
	{
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		return lp;
	}

}
