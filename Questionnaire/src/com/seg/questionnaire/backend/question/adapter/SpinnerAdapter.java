package com.seg.questionnaire.backend.question.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * A custom SpinnerAdapter to be used with Spinner in 
 * RankQuestion which fills the Spinner with numbers from
 * 1 to <i>amount</i> with appropriate order suffices.
 * 
 * @author Marek Matejka
 *
 */
public class SpinnerAdapter extends ArrayAdapter<String>
{	
	/**
	 * Constructor for SpinnerAdapter.
	 * 
	 * @param context Context of the Activity.
	 * @param amount How many inputs should it have.
	 */
	public SpinnerAdapter(Context context, int amount)
	{
		super(context, android.R.layout.test_list_item);
		addAll(getContent(amount));
	}
	
	/**
	 * Returns a list of inputs for the Spinner.
	 * These are order formatted numbers from 1 
	 * to <i>amount</i>.
	 * 
	 * @param amount The last number.
	 * @return List of order formatted numbers between
	 * 1 and <i>amount</i>.
	 */
	private List<String> getContent(int amount)
	{
		List<String> l = new LinkedList<String>();
		
		for (int i = 1; i <= amount; i++)
			l.add(getOrder(i));
		
		return l;
	}
	
	/**
	 * Returns the order suffix for a given number.
	 * 1 -> 1st, 2 -> 2nd, etc.
	 * 
	 * @param num The number to get the suffix for.
	 * @return Number with proper order suffix.
	 */
	private String getOrder(int num)
	{
		if (num == 11 || num == 12 || num == 13)
			return num+"th";
		else
		{
			switch (num%10) 
			{
				case 1: return num+"st";
				case 2: return num+"nd";
				case 3: return num+"rd";
				default: return num+"th";
			}
		}
	}
}
