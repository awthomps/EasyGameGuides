package com.catsharksoftware.easygameguides;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.catsharksoftware.easygameguides.R;

public class DisplaySearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_search);
		
		LinearLayout searchLayout = (LinearLayout) findViewById(R.id.search_layout);
		
		String prefix = "Results for: ";
		
		// Get the message from the intent
		Intent intent = getIntent();
	    String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
	    
	    // Create the text view
	    TextView textView = new TextView(this);
	    textView.setTextSize(16);
	    textView.setText(prefix + "\"" + message + "\":");
	    
	    // Set the text view as the activity layout
	    searchLayout.addView(textView);
	    
	    Thread search = new WebDownloadThread(this, message, WebDownloadThread.GAME_NAME_SEARCH, searchLayout);
	    search.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_search, menu);
		return true;
	}

}
