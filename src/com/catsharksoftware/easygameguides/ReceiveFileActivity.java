package com.catsharksoftware.easygameguides;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ReceiveFileActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_file);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get info display
		TextView infoText = (TextView) findViewById(R.id.file_receive_info);
		infoText.setTextSize(20);
		
		
		// Get intent, action and MIME type:
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		
		if(Intent.ACTION_SEND.equals(action) && type != null) {
			if("text/plain".equals(type)) {
				handleSendText(intent);
			}
		}
		else if(Intent.ACTION_MAIN.equals(action)){
		    //app has been launched directly, not through sharing
			infoText.setText("No text file was shared. Return to" +"" +
		    "main menu or exit app.");
		}
	}

	private void handleSendText(Intent intent) {
		Uri textUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if(textUri != null) {
			//TODO: implement file creation after receiving file
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
		getMenuInflater().inflate(R.menu.receive_file, menu);
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
