package com.catsharksoftware.easygameguides;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import com.catsharksoftware.easygameguides.R;

public class SavedGuidesActivity extends Activity {
	
	public final static String NAME = "com.catsharksoftware.easygameguides.NAME";
	private ArrayList<Button> buttons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_guides);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Initialize button search list.
		buttons = new ArrayList<Button>();
		loadLocalGuides();
		
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
		getMenuInflater().inflate(R.menu.saved_guides, menu);
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
	
	private void loadLocalGuides() {
		
		LinearLayout layout =  (LinearLayout) findViewById(R.id.guide_list);
		String listOfFiles[] = fileList();
		
		//TODO: Sort files before displaying
		listOfFiles = mergeSort(listOfFiles);	
		
		for(String file : listOfFiles)
		{
			Button b = new Button(this);
			b.setText(file);
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openGuide(v);
					
				}
			});
			layout.addView(b);
			buttons.add(b);
		}	
	}
	
	public void openGuide(View view)
	{
		Button button = null;
		if(view instanceof Button)
		{
			button = (Button) view;
			
			
			
			//Begin the DisplayGuide Activity:
			Intent intent = new Intent(this, DisplayGuideActivity.class);
		    intent.putExtra(NAME,button.getText());
		    startActivity(intent);
		}
	}
	/*
	private void sortFiles(ArrayList<Button> files)
	{
		int numberOfFiles = files.size();
		//Convert to array since faster to sort
		Button[] filesArray = new Button[numberOfFiles];
		for(int i = 0; i < numberOfFiles; ++i)
		{
			filesArray[i] = files.get(i);
		}
		
		filesArray = mergeSort(filesArray);
		
		//Convert array back to ArrayList
		files.clear();
		for(int i = 0; i < numberOfFiles; ++i)
		{
			files.add(filesArray[i]);
		}
	}*/
	
	private String[] mergeSort(String[] files)
	{
		int size = files.length;
		//The base case of the sort has been reached
		if(size <= 1)
		{
			return files;
		}
		else
		{
			//Initialize two new file arrays
			String[] filesA = new String[size/2];
			String[] filesB = new String[size - size/2];
			
			//Populate filesA array:
			for(int i = 0; i < filesA.length; ++i)
			{
				filesA[i] = files[i];
			}
			//Populate filesB array starting from where fileA left off:
			for(int i = 0; i < filesB.length; ++i)
			{
				filesB[i] = files[filesA.length + i];
			}
			
			//Begin the sorting process:
			filesA = mergeSort(filesA);
			filesB = mergeSort(filesB);
			
			//Finish up this level's sorting process:
			int a = 0, b = 0, c = 0;
			while(c < size)
			{
				if(a >= filesA.length)
				{
					files[c] = filesB[b];
					++b;
				}
				else if(b >= filesB.length)
				{
					files[c] = filesA[a];
					++a;
				}
				else if(filesA[a].compareTo(filesB[b]) < 0)
				{
					files[c] = filesA[a];
					++a;
				}
				else
				{
					files[c] = filesB[b];
					++b;
				}
				++c;
			}
			return files;
		}
	}

}
