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

import com.seg.questionnaire.R;
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
	 * TextView showing the current value.
	 */
	private TextView t;
	
	/**
	 * Constructor for a RangeQuestion.
	 * 
	 * @param id Question's unique ID.
	 * @param question Question text.
	 * @param lowerBound Lower bound of the range.
	 * @param upperBound Upper bound of the range.
	 * @param required Flag defining is the question is
	 * required or not.
	 * @param description Description of the question.
	 */
	public RangeQuestion(String id, String question, int lowerBound, int upperBound, boolean required, String description)
	{
		super(id, question, required, description);
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.answer = new SingleAnswer(id);
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#getView(android.content.Context)
	 */
	@Override
	public View getView(Context context, boolean highContrastMode) 
	{			
		//create a linear layout with basic parameters
		LinearLayout l = new LinearLayout(context);
		l.setOrientation(LinearLayout.VERTICAL);
		l.setLayoutParams(getLayoutParams());
	
		//create a TextView used to show the currently
		//selected value from the range (SeekBar) and apply
		//basic layout parameters
		t = new TextView(context);
		t.setId(11);
		t.setText(""+(lowerBound+upperBound)/2);
		t.setLayoutParams(getLayoutParams());
		t.setGravity(Gravity.CENTER_HORIZONTAL);
		if (highContrastMode)
			t.setTextColor(context.getResources().getColor(R.color.black));
		else
			t.setTextColor(context.getResources().getColor(R.color.white));
		t.setTextSize(context.getResources().getDimension(R.dimen.normal_text_size));
		
		//create and initialize the SeekBar
		b = new SeekBar(context);
		b.setId(10);
		b.setMax(upperBound-lowerBound);
		if (highContrastMode)
		{
			b.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_style_bw));
			b.setThumb(context.getResources().getDrawable(R.drawable.seekbar_thumb_bw));
		}
		else
		{
			b.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_style));
			b.setThumb(context.getResources().getDrawable(R.drawable.seekbar_thumb));
		}
		b.setPadding(30, 0, 30, 0);
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
	
	@Override
	protected void setAccessibilityFocuses()
	{
		//add accessibility focuses
		t.setFocusable(true);
		t.setNextFocusDownId(R.id.outOf);
		t.setNextFocusUpId(b.getId());
		
		b.setFocusable(true);
		b.setNextFocusDownId(t.getId());
		b.setNextFocusUpId(R.id.description);
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
	public boolean isAnswerReady() 
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.seg.questionnaire.backend.question.Question#loadAnswer()
	 */
	@Override
	public void loadAnswer()
	{
		if (!answer.getAnswer().equals(""))
			b.setProgress(Integer.parseInt(answer.getAnswer())-lowerBound);
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
