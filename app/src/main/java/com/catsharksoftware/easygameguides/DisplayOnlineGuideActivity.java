package com.catsharksoftware.easygameguides;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class DisplayOnlineGuideActivity extends Activity {

	private String onlineGuideURL;
	private LinearLayout displayGuideLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_online_guide);
		// Show the Up button in the action bar.
		setupActionBar();
		
		displayGuideLayout = (LinearLayout) this.findViewById(R.id.online_guide_layout);
		
		Intent intent = getIntent();
		String onlineGuideURL = intent.getStringExtra(DisplaySearchActivity.ONLINE_GUIDE_URL);
		Thread displayOnlineGuideThread = new WebDownloadThread(this, onlineGuideURL, WebDownloadThread.ONLINE_GUIDE_DISPLAY, displayGuideLayout);
		displayOnlineGuideThread.start();
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
		getMenuInflater().inflate(R.menu.display_online_guide, menu);
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

	private boolean downloadGuide(String url) {
		//GUIDES IN HTML ARE IN THE FORM faqspan-1 - faqspan-n
		return true;
	}

}
