package com.seg.questionnaire.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.seg.questionnaire.R;

/**
 * TEMPORARY ACTIVITY USED AS MENU TO ALLOW EASY ACCESS TO 
 * VARIOUS PARTS OF THE APP.
 * 
 * @author Marek Matejka
 *
 */
public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public void onClick(View v)
	{
		switch(v.getId())
		{
			case R.id.questionnaire_button: startActivity(new Intent(this, QuestionActivity.class)); onStop(); break;
			case R.id.tutorial_button: startActivity(new Intent(this, TutorialActivity.class)); onStop(); break;
			case R.id.connection_button: startActivity(new Intent(this, ConnectionActivity.class)); onStop(); break;
			case R.id.video_button: startActivity(new Intent(this, VideoActivity.class)); onStop(); break;
			case R.id.login_button: startActivity(new Intent(this, LoginActivity.class)); onStop(); break;
			case R.id.thank_you_button: startActivity(new Intent(this, ThankYouActivity.class)); onStop(); break;
		}
	}

}
