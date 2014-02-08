package com.catsharksoftware.easygameguides;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class HowToUseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_how_to_use);
		// Show the Up button in the action bar.
		//setupActionBar();
		
		//Set up tutorial:
		TextView helpText = (TextView) findViewById(R.id.help_text);
		String text = new String(
				"To view game guides or other text files, simply download any \".txt\" "+
		"file to your android device. Open up \"Easy Game Guides\" and select \"Add a local guide!\". "+
		"Afterward, return to the main screen and select \"Saved Guides\" and your new guide or text "+
		"file should appear on screen. To delete an unwanted guide, from the main screen select "+
		"\"Delete a local guide...\" and you should be able to select the guide you want to remove from "+
		"the displayed buttons."
				);
		helpText.setTextSize(20);
		helpText.setPadding(0,0,0,15);
		helpText.setText(text);
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
		getMenuInflater().inflate(R.menu.how_to_use, menu);
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
