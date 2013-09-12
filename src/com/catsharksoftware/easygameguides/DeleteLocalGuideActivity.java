package com.catsharksoftware.easygameguides;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;

public class DeleteLocalGuideActivity extends Activity {
	private ArrayList<Button> buttons;
	private AlgorithmContainer algorithm;
	private Button currentToBeDeleted;
	
	
	/*
	 * Create AlertDialog to prompt user whether they are sure they
	 * want to delete a certain guide or not.
	 */
	private DialogInterface.OnClickListener deleteConfirm = new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int choice)
		{
			switch(choice)
			{
				case DialogInterface.BUTTON_POSITIVE:
					deleteGuide();
				break;
				
				case DialogInterface.BUTTON_NEGATIVE:
					
				break;
			}
		}
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_local_guide);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Initialize button search list and AlgorithmContainer.
		algorithm = new AlgorithmContainer();
		buttons = new ArrayList<Button>();
		loadLocalGuides();
		currentToBeDeleted = null;
	}
	
	/**
	 * Load the list of local guides on the internal memory of the device
	 */
	private void loadLocalGuides() {
		
		LinearLayout layout =  (LinearLayout) findViewById(R.id.delete_guide_list);
		String listOfFiles[] = fileList();
		
		listOfFiles = algorithm.mergeSort(listOfFiles);	
		
		for(String file : listOfFiles)
		{
			Button b = new Button(this);
			b.setText(file);
			b.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					createDeleteDialog(v);
					
				}
			});
			layout.addView(b);
			buttons.add(b);
		}	
	}
	
	/**
	 * Function to call when button corresponding to a certain local guide is selected.
	 * This prompts the user to ensure that they selected the correct guide to delete.
	 * Takes in a view object which must be a button.
	 * @param view
	 */
	public void createDeleteDialog(View view)
	{
		if(view instanceof Button)
		{
			currentToBeDeleted = (Button) view;
			String name = (String) currentToBeDeleted.getText();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete \"" + name + "\"")
				.setPositiveButton("Yes", deleteConfirm)
				.setNegativeButton("No", deleteConfirm).show();
		}
	}
	
	
	/**
	 * Method which actually deletes the guide from internal memory.
	 */
	public void deleteGuide()
	{
		boolean success = false;	
		String deleteName = (String) currentToBeDeleted.getText();
		try
		{
			File directory = getFilesDir();
			File deleteFile = new File(directory, deleteName);
			success = deleteFile.delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(success)
		{
			currentToBeDeleted.setText("\"" + currentToBeDeleted.getText() + "\" successfully deleted!");
		}
		else
		{
			currentToBeDeleted.setText("\"" + currentToBeDeleted.getText() + "\" could not be deleted");
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
		getMenuInflater().inflate(R.menu.delete_local_guide, menu);
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
