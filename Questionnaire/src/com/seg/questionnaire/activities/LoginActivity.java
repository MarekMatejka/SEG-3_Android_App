package com.seg.questionnaire.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.seg.questionnaire.R;
import com.seg.questionnaire.backend.connectivity.SocketAPI;
import com.seg.questionnaire.backend.file.AnswerFile;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 * 
 * @author Android, adapted by Marek Matejka
 */
public class LoginActivity extends Activity 
{		
	/**
	 * Tag used to define whether the app should go the activity that called it or to PatientDetail.
	 */
	public static final String RETURN_TO_THE_SAME_ACTIVITY = "RTTSA";
	
	/**
	 * Tag used to define whether the no connection dialog should be opened or not.
	 */
	public static final String SHOW_NO_CONNECTION_DIALOG = "SNCD";
	
	private static String serverIP = "";
	
	private Context context = this;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask authTask = null;

	// Values for password at the time of the login attempt.
	private String password;

	// UI references.
	//private EditText usernameView;
	private EditText passwordView;
	private EditText serverIPView;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Set up the login form.
		//usernameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);
		serverIPView = (EditText) findViewById(R.id.serverIPAddress);
		serverIPView.setText(serverIP);
		

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				attemptLogin();
				//hide the keyboard
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); 
			}
		});
	}
	
	private boolean activityRunning = false;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		if (!activityRunning)
		{
			Log.e("LA", "onStart - "+getIntent().getBooleanExtra(RETURN_TO_THE_SAME_ACTIVITY, false)+" "+getIntent().getBooleanExtra(SHOW_NO_CONNECTION_DIALOG, false));
			if (getIntent().getBooleanExtra(SHOW_NO_CONNECTION_DIALOG, false))
				showNoConnectionDialog();
			activityRunning = true;
		}
		
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid username, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() 
	{
		if (authTask != null) //validation in progress
			return;

		// Reset errors.
		//usernameView.setError(null);
		passwordView.setError(null);
		serverIPView.setError(null);

		// Store values at the time of the login attempt.
		//username = usernameView.getText().toString();
		password = passwordView.getText().toString();
		serverIP = serverIPView.getText().toString();


		if (validatePassword() && validateIP())
		{
			// Show a progress spinner, and start a background task to perform the user login attempt.
			TextView mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			
			showProgress(true);
			
			authTask = new UserLoginTask();
			authTask.execute((Void) null);
		}
	}
	
	/**
	 * Checks whether the password field is not empty and
	 * if the password is long enough.
	 * 
	 * @return TRUE if not empty and of correct length, FALSE otherwise.
	 */
	private boolean validatePassword()
	{
		if (TextUtils.isEmpty(password)) 
		{
			passwordView.setError(getString(R.string.error_field_required));
			passwordView.requestFocus();
			return false;
		} 
		else if (password.length() < 4 || password.length() > 12) 
		{
			passwordView.setError(getString(R.string.error_invalid_password));
			passwordView.requestFocus();
			return false;
		}
		return true;
	}
	
	/**
	 * Checks whether the username field is not empty,
	 * 
	 * @return TRUE if not empty, FALSE otherwise.
	 */
	/*private boolean validateUsername()
	{
		if (TextUtils.isEmpty(username)) 
		{
			usernameView.setError(getString(R.string.error_field_required));
			usernameView.requestFocus();
			return false;
		} 
		return true;
	}*/
	
	private boolean validateIP()
	{
		if (TextUtils.isEmpty(serverIP)) 
		{
			serverIPView.setError(getString(R.string.error_field_required));
			serverIPView.requestFocus();
			return false;
		} 
		return true;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	private void showProgress(final boolean show) 
	{
		//get animation time
		int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);
		
		//loads the spinner animation
		ImageView spinner = (ImageView)findViewById(R.id.spinnerLogin);
		spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));
		
		//animate logining in view to appear
		final View loginStatusView = findViewById(R.id.login_status);
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
		final View loginFormView = findViewById(R.id.login_form);
		loginFormView.setVisibility(View.VISIBLE);
		loginFormView.animate().setDuration(animTime)
		             .alpha(show ? 0 : 1)
					 .setListener(new AnimatorListenerAdapter() 
					 {
						 @Override
						 public void onAnimationEnd(Animator animation) 
						 {
							 loginFormView.setVisibility(show ? View.GONE: View.VISIBLE);
						 }
					 });
	}
	
	/**
	 * Returns the currently stored server IP.
	 * 
	 * @return Server IP.
	 */
	public static String getServerIP()
	{
		return serverIP;
	}
	
	/**
	 * Displays a dialog saying that the app could not connect to the server.
	 * 
	 * @param context Activity's context.
	 */
	private void showNoConnectionDialog()
	{
		final AlertDialog dialog;
		
		Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage(context.getResources().getString(R.string.no_server_connection));
	    builder.setCancelable(false);
	    builder.setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
	    {	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		}); 

	    dialog = builder.create();
	    dialog.show();
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> 
	{
		@Override
		protected Boolean doInBackground(Void... params) 
		{
			String s = SocketAPI.checkPasscode(password);
			if (s.equals(SocketAPI.SOCKET_TIMEOUT_EXCEPTION))
			{
				//must be like this, because it must run on UI thread and not the background (Async) thread!
				new Thread(){ 
					public void run()
					{
						LoginActivity.this.runOnUiThread(new Runnable() 
						{
							@Override
							public void run() 
							{
								showNoConnectionDialog();
							}
						});
					}
				}.start();
				return false;
			}
			return s.contains("true") ? true : false;
		}

		@Override
		protected void onPostExecute(final Boolean success) 
		{
			authTask = null;
			showProgress(false);

			if (success) 
			{
				if (!getIntent().getBooleanExtra(RETURN_TO_THE_SAME_ACTIVITY, false))
				{
					Log.e("LA", "NOT return to the same activity");
					startActivity(new Intent(LoginActivity.this, PatientDetailActivity.class));
				}
				else
					Log.e("LA", "return to the same activity");
				finish();
				activityRunning = false;
				AnswerFile.readAndSendFile(context); //check if there is a file that needs to be send, if yes send it
			}
			else 
			{
				passwordView.setError(getString(R.string.error_incorrect_password));
				passwordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() 
		{
			authTask = null;
			showProgress(false);
		}
	}
}
