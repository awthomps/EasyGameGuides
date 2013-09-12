package com.catsharksoftware.easygameguides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import com.catsharksoftware.easygameguides.R;

public class DisplayGuideActivity extends Activity {
	
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// Get the message from the intent
		Intent intent = getIntent();
		String name = intent.getStringExtra(SavedGuidesActivity.NAME);
		setTitle(name);
		
		setContentView(R.layout.activity_display_guide);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Initialize layout
		try
		{
			layout = (LinearLayout) findViewById(R.id.guide_text);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		loadFile(name);
		 
	}

	private void loadFile(String name) {
		/*
		 * TODO:
		 * Tutorial for reading/displaying text in a file:
		 * http://people.bridgewater.edu/~arn002/csci440/android-tutorial.htm
		 * 
		 * 
		 */	
		ArrayList<String> guideText = new ArrayList<String>();
		try
		{
			FileInputStream guide = openFileInput(name);
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(guide));
			String line = null;
			while((line = fileReader.readLine()) != null)
			{
				guideText.add(line);
			}
			fileReader.close();
			guide.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//Test the scrolling capabilities:
		for(int i = 1; i <= 100; ++i)
		{
			guideText.add(""+i);
		}
		for(String text : guideText)
		{
			try
			{
			// Create the text view
			// TODO, in the future have a text view for each paragraph
			TextView textView = new TextView(this);
			textView.setTextSize(16);
			textView.setTypeface(Typeface.MONOSPACE);
			textView.setText(text);
			// Set the text view as the activity layout
			layout.addView(textView);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_guide, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
