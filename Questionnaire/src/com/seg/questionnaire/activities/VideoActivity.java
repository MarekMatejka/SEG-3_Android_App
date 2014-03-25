package com.seg.questionnaire.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.seg.questionnaire.R;

/**
 * Activity playing a video file.
 * 
 * @author Marek Matejka
 *
 */
public class VideoActivity extends Activity 
{
	/**
	 * Final String defining the path to sdcard/Movies folder.
	 */
	private final String VIDEO_FILE_DIRECTORY = Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_MOVIES;
	
	/**
	 * Name and extension of the video file.
	 */
	private static String videoFileName = "MediQ.mp4";
	
	private boolean wasHere = false;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		File f = new File(VIDEO_FILE_DIRECTORY+"/"+videoFileName);
		if (!f.isFile()) 
			goToTutorial();
		
		VideoView video = (VideoView) findViewById(R.id.videoView1); //find view
        video.setVideoPath(f.getAbsolutePath()); //set video file
        video.setMediaController(new MediaController(this)); //TODO: WILL BE REMOVED
        video.start(); //start playing
        video.setOnCompletionListener(new OnCompletionListener() //when video ends
        {	
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				//TODO: startActivity(new Intent(VideoActivity.this, TutorialActivity.class));
				if (!wasHere)
				{
					wasHere = true;
					goToTutorial();
				}
			}
		});
	}
	
	/**
	 * Changes the Activity to TutorialActivity.
	 */
	private void goToTutorial()
	{
		startActivity(new Intent(VideoActivity.this, TutorialActivity.class));
		finish(); //close the Activity
	}

	/**
	 * Sets the video file name and extension.
	 * 
	 * @param videoName Name and extension of the video.
	 */
	public static void setVideoFileName(String videoName)
	{
		videoFileName = videoName;
	}
	
}
