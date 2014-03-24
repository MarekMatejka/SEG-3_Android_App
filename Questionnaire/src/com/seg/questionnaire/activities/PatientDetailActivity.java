package com.seg.questionnaire.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.connectivity.SocketAPI;
import com.seg.questionnaire.backend.json.PatientJSON;

/**
 * Activity which retrieves patient's data and displays them.
 * 
 * @author Marek Matejka
 *
 */
public class PatientDetailActivity extends Activity 
{
	private static String patientName = "";
	private static String patientNHS = "";

	private PatientDetailsTask task = null;
	private PatientJSON patient = null;
	private EditText nhs;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_detail);
		
		nhs = (EditText)findViewById(R.id.patientNHS);
	}

	/**
	 * Handles onClick calls.
	 * 
	 * @param v View that triggered the call.
	 */
	public void onClick(View v)
	{
		if (v.getId() == R.id.getPatient)
		{
			validateNHS();
			
			//hide keyboard
			InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		}
		else if (v.getId() == R.id.confirm)
		{
			startActivity(new Intent(this, AvailableQuestionnairesActivity.class));
			finish();
			getDisability(); //must be last to open Settings after the following page!
		}
		else if (v.getId() == R.id.newSearch)
			newSearch();
	}
	
	/**
	 * Validates the NHS number.
	 */
	private void validateNHS()
	{
		String NHS = nhs.getText().toString(); //get NHS number
		patientNHS = NHS;
		
		if (NHS.length() != 10) //if it is too short
			nhs.setError(getString(R.string.incorrect_nhs_format));
		else
		{	
			fromFormToWaitingAnimation(true); //start animation
			
			task = new PatientDetailsTask();
			task.execute(new String[] {NHS});
		}
	}
	
	/**
	 * Sets patient's details into appropriate fields on the screen.
	 * 
	 * @param patient Patient object from which data will be used.
	 */
	private void setPatientDetails(PatientJSON patient)
	{		
		TextView name = (TextView)findViewById(R.id.patient_name);
		name.setText(patient.getName());
		patientName = patient.getName();
		
		TextView dob = (TextView)findViewById(R.id.patient_dob);
		dob.setText(patient.getDateOfBirth());
		
		TextView postcode = (TextView)findViewById(R.id.patient_postcode);
		postcode.setText(patient.getPostcode());
	}
	
	
	/**
	 * Connects to the server and retrieves a patient object
	 * from the database for the given NHS number
	 * 
	 * @param NHS Patient's NHS number.
	 * @return Patient object or NULL if no such patient found.
	 */
	private PatientJSON findPatient(String NHS)
	{
		Gson gson = new Gson();
		String patient = SocketAPI.findPatient(NHS);
		
		if (patient.contains("'error_code': 666"))
			return null;
		return gson.fromJson(patient, PatientJSON.class);
	}
	
	/**
	 * Animate the transition from NHS number input to Waiting for response
	 * or the other way round
	 * 
	 * @param show TRUE if from NHS number input to Waiting for response,
	 * FALSE for the other way.
	 */
	private void fromFormToWaitingAnimation(final boolean show)
	{		
		//get animation time
		int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
		
		//loads the spinner animation
		ImageView spinner = (ImageView)findViewById(R.id.spinnerPatientDetail);
		spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));
				
		//animate waiting for response to appear
		final View loginStatusView = findViewById(R.id.login_status_patient);
		loginStatusView.setVisibility(View.VISIBLE);
		loginStatusView.animate().setDuration(animTime)
								 .alpha(show ? 1 : 0)
								 .setListener(new AnimatorListenerAdapter() 
								 {
									 @Override
									 public void onAnimationEnd(Animator animation) 
									 {
										 loginStatusView.setVisibility(show ? View.VISIBLE: View.GONE);
									 }
								 });

		//animate login form to disappear
		final View searchForm = findViewById(R.id.search_form);
		searchForm.setVisibility(View.VISIBLE);
		searchForm.animate().setDuration(animTime)
							   .alpha(show ? 0 : 1)
							   .setListener(new AnimatorListenerAdapter() 
							   {
								   @Override
								   public void onAnimationEnd(Animator animation) 
								   {
									   searchForm.setVisibility(show ? View.GONE: View.VISIBLE);
								   }
							   });
	}
	
	/**
	 * Animate the transition from Patient Details input to Patient Form.
	 */
	private void newSearch()
	{		
		//get animation time
		int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
				
		//animate patient details to disappear
		final View patientDetails = findViewById(R.id.patient_details);
		patientDetails.setVisibility(View.VISIBLE);
		patientDetails.animate().setDuration(animTime)
							    .alpha(0)
							    .setListener(new AnimatorListenerAdapter() 
							    {
								    @Override
								    public void onAnimationEnd(Animator animation) 
								    {
								 	    patientDetails.setVisibility(View.GONE);
								    }
							    });

		//animate waiting for response to appear
		final View loginFormView = findViewById(R.id.search_form);
		loginFormView.animate().setDuration(animTime)
								 .alpha(1)
								 .setListener(new AnimatorListenerAdapter() 
								 {
									 @Override
									 public void onAnimationEnd(Animator animation) 
									 {
										 loginFormView.setVisibility(View.VISIBLE);
									 }
								 });
	}
	
	/**
	 * Animate the transition from Waiting for response to Patient Details
	 * or the other way round
	 * 
	 * @param patient Patient who's info should be displayed.
	 */
	private void fromWaitingToPatientDetailsAnimation(final PatientJSON patient)
	{
		//get animation time
		int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

		//animate waiting for response to disappear
		final View loginStatusView = findViewById(R.id.login_status_patient);
		loginStatusView.setVisibility(View.VISIBLE);
		loginStatusView.animate().setDuration(animTime)
								 .alpha(0)
								 .setListener(new AnimatorListenerAdapter() 
								 {
									 @Override
									 public void onAnimationEnd(Animator animation) 
									 {
										 loginStatusView.setVisibility(View.GONE);
									 }
								 });

		//animate patient details to appear
		final View patientDetails = findViewById(R.id.patient_details);
		patientDetails.setVisibility(View.VISIBLE);
		patientDetails.animate().setDuration(animTime)
							    .alpha(1)
							    .setListener(new AnimatorListenerAdapter() 
							    {
								    @Override
								    public void onAnimationEnd(Animator animation) 
								    {
								 	    patientDetails.setVisibility(View.VISIBLE);
										setPatientDetails(patient);
								    }
							    });
	}
	
	/**
	 * Returns the currently active patient's name.
	 * 
	 * @return Name of the currently active patient.
	 */
	public static String getPatientName()
	{
		return patientName;
	}
	
	/**
	 * Gets the user selected disability and does appropriate steps to 
	 * ensure that the correct mode is applied through the application.
	 */
	private void getDisability()
	{
		RadioGroup g = (RadioGroup)findViewById(R.id.disability);
		switch (g.getCheckedRadioButtonId())
		{
			case R.id.no_dissability: return;
			case R.id.high_contrast_mode: QuestionActivity.setHighContrastMode(true); break;
			case R.id.advanced_accessibility_settings: startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)); onStop(); break;
			default: return;
		}
	}
	
	/**
	 * Returns the currently active patient's NHS number.
	 * 
	 * @return NHS number of the currently active patient.
	 */
	public static String getPatientNHS()
	{
		return patientNHS;
	}
	
	/**
	 * AsyncTask making sure that the animation is working correctly with the 
	 * data retrieval.
	 * 
	 * @author Marek Matejka
	 *
	 */
	public class PatientDetailsTask extends AsyncTask<String, Void, Void> 
	{
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(String... params) 
		{
			patient = findPatient(params[0]); //get patient
			return (Void)null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void v) 
		{
			if (patient == null) //if no patient retrieved
			{
				nhs.setError(getString(R.string.no_patient_found));
				fromFormToWaitingAnimation(false); //start animation
			}
			else
				fromWaitingToPatientDetailsAnimation(patient); //start animation
			
			task = null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() 
		{
			task = null;
			fromFormToWaitingAnimation(false);
		}
	}
}