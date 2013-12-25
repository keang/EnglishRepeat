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

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private List<String> titlesList;
	private List<String> lessonsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//initialize Parse
		Parse.initialize(this, "j0JtophPAGbWHkyMYkR1IyneNfQcKKxNsM2enwxM", "brta8dJ5olNrlxw4P96fWEsxt6ZMKirJs0rDeMXW"); 
		PushService.setDefaultPushCallback(this, MainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		//for parse tracking
		ParseAnalytics.trackAppOpened(getIntent()); 
		Log.i("init parse", "done");
		//load all lessons
		titlesList = new ArrayList<String>();
		lessonsList = new ArrayList<String>();
	    FileInputStream titleStream;
		try {
			titleStream = new FileInputStream(new File(path, "titles.txt"));
		    BufferedReader r = new BufferedReader(new InputStreamReader(titleStream));
		    String line;
		    while ((line = r.readLine()) != null) {
		    	lessonsList.add(line);
		    	titlesList.add(r.readLine());
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Display titles
		final ListView listview = (ListView) findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.title_list_item, titlesList);
		listview.setAdapter(adapter);
		//Toast.makeText(this, path.toString(), Toast.LENGTH_LONG).show();
		//Log.i("path", path.toString());
		//wire onclicks for titles
		listview.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
				  //start a lesson activity					
				  Intent startLesson = new Intent(getApplicationContext(), LessonActivity.class);
				  startLesson.putExtra("lesson", lessonsList.get(position));
				  startLesson.putExtra("title", titlesList.get(position));
				  startActivity(startLesson);
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
