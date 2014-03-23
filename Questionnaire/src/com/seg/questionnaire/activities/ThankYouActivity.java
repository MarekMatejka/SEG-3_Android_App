package com.seg.questionnaire.activities;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.TextView;

import com.seg.questionnaire.R;

/**
 * @author Marek Matejka, parts of code based on code by Gupta Avinash
 * @see http://www.codeproject.com/Articles/655709/Using-Text-to-Speech-TTS-engine-in-an-Android-appl
 *
 */
public class ThankYouActivity extends Activity implements OnInitListener
{
	private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);
		
		tts  = new TextToSpeech(this, this);
		
		final TextView autoClose = (TextView)findViewById(R.id.autoClose);
		autoClose.setText(getString(R.string.auto_close_1)+" 10 "+getString(R.string.auto_close_2));
		
		new CountDownTimer(9100, 1000) 
		{	
			@Override
			public void onTick(long millisUntilFinished) 
			{
				autoClose.setText(getString(R.string.auto_close_1)+" "+
								  (millisUntilFinished/1000)+" "+
								  getString(R.string.auto_close_2));
			}
			
			@Override
			public void onFinish() 
			{
				startActivity(new Intent(ThankYouActivity.this, LoginActivity.class));
				finish();
			}
		}.start();
	}

	@Override
	public void onInit(int status) 
	{
		tts.setLanguage(Locale.UK);
		tts.speak(getString(R.string.thank_you), TextToSpeech.QUEUE_ADD, null);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		tts.shutdown();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed()
	{
		//do nothing
	}
	

}
