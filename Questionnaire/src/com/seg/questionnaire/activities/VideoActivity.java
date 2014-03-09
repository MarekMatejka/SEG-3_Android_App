package com.seg.questionnaire.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
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
	private static String videoFileName = "Helicopter.mp4";

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		VideoView video = (VideoView) findViewById(R.id.videoView1); //find view
        video.setVideoPath(VIDEO_FILE_DIRECTORY+"/"+videoFileName); //set video file
        video.setMediaController(new MediaController(this)); // WILL BE REMOVED
        video.start(); //start playing
        video.setOnCompletionListener(new OnCompletionListener() //when video ends
        {	
			@Override
			public void onCompletion(MediaPlayer mp) 
			{
				finish(); //close the Activity
			}
		});
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
