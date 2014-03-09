package com.seg.questionnaire.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.seg.questionnaire.R;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 * 
 * @author Android, adapted by Marek Matejka
 */
public class LoginActivity extends Activity 
{
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {"Marek:marek"};

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask authTask = null;

	// Values for username and password at the time of the login attempt.
	private String username;
	private String password;

	// UI references.
	private EditText usernameView;
	private EditText passwordView;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		usernameView = (EditText) findViewById(R.id.username);
		passwordView = (EditText) findViewById(R.id.password);

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				attemptLogin();
			}
		});
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
		usernameView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		username = usernameView.getText().toString();
		password = passwordView.getText().toString();


		if (validateUsername() && validatePassword())
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
		else if (password.length() < 4) 
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
	private boolean validateUsername()
	{
		if (TextUtils.isEmpty(username)) 
		{
			usernameView.setError(getString(R.string.error_field_required));
			usernameView.requestFocus();
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
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> 
	{
		@Override
		protected Boolean doInBackground(Void... params) 
		{
			// TODO: attempt authentication against a network service.
			try {
				// Simulate network access.
				//SocketAPI.checkUser(serverIP, username, password);
				Thread.sleep(3000);
			} catch (InterruptedException e) {return false;}

			// TODO: remove this when real connection is available
			for (String credential : DUMMY_CREDENTIALS) 
			{
				String[] pieces = credential.split(":");
				if (pieces[0].equals(username)) 
					return pieces[1].equals(password); // Account exists, return true if the password matches.
			}

			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) 
		{
			authTask = null;
			showProgress(false);

			if (success) 
				finish();
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
