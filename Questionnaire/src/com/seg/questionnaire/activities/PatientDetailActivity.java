package com.seg.questionnaire.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.json.PatientJSON;

/**
 * Activity which retrieves patient's data and displays them.
 * 
 * @author Marek Matejka
 *
 */
public class PatientDetailActivity extends Activity 
{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_detail);
	}

	/**
	 * Handles onClick calls.
	 * 
	 * @param v View that triggered the call.
	 */
	public void onClick(View v)
	{
		if (v.getId() == R.id.getPatient)
			validateNHS();
		else if (v.getId() == R.id.confirm)
			finish();
	}
	
	/**
	 * Validates the NHS number.
	 */
	private void validateNHS()
	{
		EditText e = (EditText)findViewById(R.id.patientNHS);
		String nhs = e.getText().toString(); //get NHS number
		
		if (nhs.length() != 10) //if it is too short
			e.setError(getString(R.string.incorrect_nhs_format));
		else
		{	
			fromFormToWaitingAnimation(true);//start animation
			
			//TODO: PatientJSON patient = gson.fromJSON(SocketAPI.findPatient(serverIP, nhs), PatientJSON.class);
			PatientJSON patient = findPatient(nhs); //parse JSON
			if (patient == null)
			{
				e.setError(getString(R.string.no_patient_found));
				fromFormToWaitingAnimation(false); //start animation
			}
			else
				fromWaitingToPatientDetailsAnimation(patient); //start animation
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
		
		TextView dob = (TextView)findViewById(R.id.patient_dob);
		dob.setText(patient.getDateOfBirth());
		
		TextView postcode = (TextView)findViewById(R.id.patient_postcode);
		postcode.setText(patient.getPostcode());
		
		TextView disability = (TextView)findViewById(R.id.patient_disability);
		disability.setText(patient.getDisability());
	}
	
	
	/**
	 * TEMPORARY METHOD.
	 * 
	 * @param NHS
	 * @return
	 */
	private PatientJSON findPatient(String NHS)
	{
		//TODO: DELETE METHOD
		Gson gson = new Gson();
		if (NHS.equals("1234567890"))
		{
			String p1 = "{\"nhsNumber\":\"1234567890\",\"first_name\":\"Marek\",\"middle_name\":\"\",\"surname\":\"Matejka\"," +
						"\"dateOfBirth\":\"15.08.1994\",\"postcode\":\"WC2R 2LS\",\"disability\":\"No dissability\"}";
			return gson.fromJson(p1, PatientJSON.class);
		}
		else if (NHS.equals("0987654321"))
		{
			String p1 = "{\"nhsNumber\":\"0987654321\",\"first_name\":\"James\",\"middle_name\":\"\",\"surname\":\"Bellamy\"," +
						"\"dateOfBirth\":\"14.01.1994\",\"postcode\":\"CT19 5JY\",\"disability\":\"No dissability\"}";
			return gson.fromJson(p1, PatientJSON.class);
		}
		else
			return null;
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
}
