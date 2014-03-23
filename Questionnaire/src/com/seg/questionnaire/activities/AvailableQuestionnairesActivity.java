package com.seg.questionnaire.activities;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.activities.adapter.AvailableQuestionnairesAdapter;
import com.seg.questionnaire.backend.connectivity.SocketAPI;
import com.seg.questionnaire.backend.json.QuestionnairePointerJSON;
import com.seg.questionnaire.backend.json.QuestionnairePointersJSON;

public class AvailableQuestionnairesActivity extends Activity 
{
	private List<QuestionnairePointerJSON> pointers;
	private AvailableQuestionnaireTask task;
	private static String questionnaireID = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_available_questionnaires);
		
		//loads the spinner animation
		ImageView spinner = (ImageView)findViewById(R.id.spinnerAvailableQuestionnaires);
		spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));
		
		task = new AvailableQuestionnaireTask();
		task.execute();
	}
	
	private List<QuestionnairePointerJSON> getAllAvailableQuestionnaires(String NHS)
	{
		Gson gson = new Gson();
		
		String response = SocketAPI.getAllQuestionnairesForPatient(NHS);
		Log.e("response", response);
		response = "{\"pointers\":"+response+"}";
		
		return gson.fromJson(response, QuestionnairePointersJSON.class).getPointers();
	}
	
	public void close(View v)
	{
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
	
	private void noQuestionnairesFoundAnimation()
	{
		//get animation time
		int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
				
		//animate waiting for response to disappear
		final View waiting = findViewById(R.id.waitAvailableQuestionnaires);
		waiting.setVisibility(View.VISIBLE);
		waiting.animate().setDuration(animTime)
								 .alpha(0)
								 .setListener(new AnimatorListenerAdapter() 
								 {
									 @Override
									 public void onAnimationEnd(Animator animation) 
									 {
										 waiting.setVisibility(View.GONE);
									 }
								 });

		//animate No Questionnaires found text to appear
		final View noQuestionnaires = findViewById(R.id.noQuestionnaireAvailable);
		noQuestionnaires.setVisibility(View.VISIBLE);
		noQuestionnaires.animate().setDuration(animTime)
							.alpha(1)
		 				    .setListener(new AnimatorListenerAdapter() 
 						    {
		 				    	@Override
								public void onAnimationEnd(Animator animation) 
								{
		 				    		noQuestionnaires.setVisibility(View.VISIBLE);
								}
							});
		
		TextView t = (TextView)findViewById(R.id.noQuestionnaires);
		t.setText(getResources().getString(R.string.no_available_questionnaires)+" "+
										   PatientDetailActivity.getPatientName());
	}
	
	private void questionnairesFoundAnimation()
	{
		//get animation time
				int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
						
				//animate waiting for response to disappear
				final View waiting = findViewById(R.id.waitAvailableQuestionnaires);
				waiting.setVisibility(View.VISIBLE);
				waiting.animate().setDuration(animTime)
										 .alpha(0)
										 .setListener(new AnimatorListenerAdapter() 
										 {
											 @Override
											 public void onAnimationEnd(Animator animation) 
											 {
												 waiting.setVisibility(View.GONE);
											 }
										 });

				//animate List of Questionnaires found to appear
				final View questionnaireAvailable = findViewById(R.id.questionnairesAvailable);
				questionnaireAvailable.setVisibility(View.VISIBLE);
				questionnaireAvailable.animate().setDuration(animTime)
									  .alpha(1)
				 				      .setListener(new AnimatorListenerAdapter() 
		 						      {
				 				    	  @Override
										  public void onAnimationEnd(Animator animation) 
										  {
				 				    		  questionnaireAvailable.setVisibility(View.VISIBLE);
										  }
									  });
				
				TextView t = (TextView) findViewById(R.id.availableQuestionnairesTV);
				t.setText(getResources().getString(R.string.questionnaires_available)+" "+
												   PatientDetailActivity.getPatientName());
				
				Log.e("DEBUG", "size = "+pointers.size());
				
				ListView list = (ListView) findViewById(R.id.availableQuestionnairesList);
				list.setAdapter(new AvailableQuestionnairesAdapter(this, pointers));
				list.setOnItemClickListener(new OnItemClickListener() 
				{

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
					{
						Log.e("DEBUG", ""+pointers.get(position).getId());
						questionnaireID = ""+pointers.get(position).getId();
						startActivity(new Intent(AvailableQuestionnairesActivity.this, VideoActivity.class));
						finish();
					}
				});
	}
		
	/**
	 * Returns the currently selected questionnaire ID.
	 * 
	 * @return Currently selected questionnaire ID.
	 */
	public static String getQuestionnaireID()
	{
		return questionnaireID;
	}
	
	/**
	 * AsyncTask making sure that the animation is working correctly with the 
	 * data retrieval.
	 * 
	 * @author Marek Matejka
	 *
	 */
	public class AvailableQuestionnaireTask extends AsyncTask<Void, Void, Void> 
	{
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(Void... params) 
		{
			pointers = getAllAvailableQuestionnaires(PatientDetailActivity.getPatientNHS()); //get pointers
			return (Void)null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void v) 
		{
			if (pointers.size() == 0) //no questionnaire available
				noQuestionnairesFoundAnimation();
			else
				questionnairesFoundAnimation();
			
			task = null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() 
		{
			//task = null;
			//fromFormToWaitingAnimation(false);
		}
	}
}
