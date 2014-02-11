package com.catsharksoftware.easygameguides;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ReceiveFileActivity extends Activity {

	private LinearLayout buttonArray;
	private TextView infoText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_file);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Get info display
		infoText = (TextView) findViewById(R.id.file_receive_info);
		buttonArray = (LinearLayout) findViewById(R.id.file_receive_button_array);
		infoText.setTextSize(20);
		
		
		// Get intent, action and MIME type:
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		
		if(Intent.ACTION_VIEW.equals(action) && type != null) {
			if("text/plain".equals(type)) {
				infoText.setText("Text file received!\n Open now?");
				runDecision(intent);
				handleSendText(intent);
				TextView test = new TextView(this);
				test.setWidth(buttonArray.getWidth());
				buttonArray.addView(test);
				/*TODO: figure out how to get the text data from the intent
				String textUri = intent.getStringExtra(Intent.EXTRA_TEXT);
				test.setText(textUri);*/
			}
		}
		else if(Intent.ACTION_MAIN.equals(action)){
		    //app has been launched directly, not through sharing
			infoText.setText("No text file was shared. Return to" +"" +
		    "main menu or exit app.");
		}
	}

	private void runDecision(Intent intent) {
		Button yesButton = new Button(this);
		yesButton.setWidth(buttonArray.getWidth());
		yesButton.setHeight(20);
		yesButton.setText("YES!");
		buttonArray.addView(yesButton);
		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		Button noButton = new Button(this);
		noButton.setWidth(buttonArray.getWidth());
		noButton.setHeight(20);
		noButton.setText("No thanks, not right now...");
		buttonArray.addView(noButton);
		noButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
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
