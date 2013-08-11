package com.kakadadroid.englishrepeat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private MediaPlayer mp;
	private Button buttonPlay;
	private Button buttonNext;
	private Button buttonBack;
	private TextView textView;
	private int curID; 
	private String lesson;
	private SoundPool soundPool;
	private List<Integer> soundID;
	private List<String> text;
	private boolean loaded;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lesson = "frying";
		
		// Load the sound
	    soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	    soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
	      @Override
	      public void onLoadComplete(SoundPool soundPool, int sampleId,
	          int status) {
	        loaded = true;
	        Log.i("Soundpool", "loaded");
	      }
	    });
	    int id;
	    soundID = new ArrayList<Integer>();

	    
	    curID=0;
		
	    // Load the text
	    text = new ArrayList<String>();
	    FileInputStream textStream;
		try {
			textStream = new FileInputStream(new File(path, lesson+".txt"));
		
		    BufferedReader r = new BufferedReader(new InputStreamReader(textStream));
		    StringBuilder total = new StringBuilder();
		    String line;
		    id=1;
		    while ((line = r.readLine()) != null) {
		    	text.add(line);
		    	
		    	//load a sound for each line
		    	soundID.add(soundPool.load(path+"/"+lesson+id+".mp3", 0));
		    	id++;
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Display first line
		textView = (TextView)findViewById(R.id.textView1);
		textView.setText(text.get(curID));
		
	    //wiring the buttons
	    buttonPlay = (Button) findViewById(R.id.button_replay);
		buttonPlay.setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {     
		        new Thread(){
		            public void run(){   
		                soundPool.play(soundID.get(curID), 1, 1, 1, 0, 1.0f);
		            }
		        }.start();
		    }
		});
		
		buttonNext = (Button) findViewById(R.id.button_next);
		buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//changing the sound
				curID++;
				soundPool.play(soundID.get(curID), 1, 1, 1, 0, 1.0f);
				
				//changing the text
				textView.setText(text.get(curID));
			}
		});
		
		buttonBack = (Button) findViewById(R.id.button_previous);
		buttonBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				//changing the sound
				curID--;
				soundPool.play(soundID.get(curID), 1, 1, 1, 0, 1.0f);
				//changing the text
				textView.setText(text.get(curID));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
