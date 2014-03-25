package com.seg.questionnaire.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.seg.questionnaire.R;

/**
 * ThankYou screen.
 * 
 * @author Marek Matejka
 *
 */
public class ThankYouActivity extends Activity
{
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);
		
		//set thank you text
		TextView text = (TextView)findViewById(R.id.thankYou);
		text.setText(getResources().getString(R.string.thank_you_1)+" "+
					 PatientDetailActivity.getPatientName()+
					 getResources().getString(R.string.thank_you_2));
		
		//set initial count down text
		final TextView autoClose = (TextView)findViewById(R.id.autoClose);
		autoClose.setText(getString(R.string.auto_close_1)+" 10 "+getString(R.string.auto_close_2));
		
		new CountDownTimer(9100, 1000) 
		{	
			/* (non-Javadoc)
			 * @see android.os.CountDownTimer#onTick(long)
			 */
			@Override
			public void onTick(long millisUntilFinished) 
			{
				autoClose.setText(getString(R.string.auto_close_1)+" "+
								  (millisUntilFinished/1000)+" "+
								  getString(R.string.auto_close_2));
			}
			
			/* (non-Javadoc)
			 * @see android.os.CountDownTimer#onFinish()
			 */
			@Override
			public void onFinish() 
			{
				startActivity(new Intent(ThankYouActivity.this, LoginActivity.class));
				finish();
				PatientDetailActivity.clearPatientData(); //wipe patient data
			}
		}.start();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		//do nothing, don't allow to exit the screen.
	}
	

}
