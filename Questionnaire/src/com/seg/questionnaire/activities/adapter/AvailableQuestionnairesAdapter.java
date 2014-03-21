package com.seg.questionnaire.activities.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.json.QuestionnairePointerJSON;

public class AvailableQuestionnairesAdapter extends ArrayAdapter<QuestionnairePointerJSON>
{
	Context context;
	List<QuestionnairePointerJSON> pointers;
	
	public AvailableQuestionnairesAdapter(Context context, List<QuestionnairePointerJSON> pointers)
	{
		super(context, R.layout.layout_dynamic_list_view_row, pointers);
		this.pointers = pointers;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View view = inflater.inflate(R.layout.layout_dynamic_list_view_row, parent, false); 
				
		TextView pointer = (TextView) view.findViewById(R.id.dynamic_list_row);
		pointer.setText(pointers.get(position).getTitle());
		pointer.setTextColor(context.getResources().getColorStateList(R.color.text_color));
		
		view.setBackground(context.getResources().getDrawable(R.color.button_background_color));
		
	    return view;
	}
}

