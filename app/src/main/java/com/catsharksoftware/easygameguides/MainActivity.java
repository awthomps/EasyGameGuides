package com.catsharksoftware.easygameguides;

import android.content.ContentProvider;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.catsharksoftware.easygameguides.R;


public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "com.catsharksoftware.easygameguides.MESSAGE";
	public final static String EasyGGFile = "EasyGG";

	private String QUERY_STRING_KEY = "queryStringKey";

	private String queryString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	/** Called when the user clicks the 'Search' button */

	public void submitSearch(View view) {
	    
	    EditText editText = (EditText) findViewById(R.id.edit_message);
	    
	    String message = editText.getText().toString();
	    
	    if(message == null || message.equals(""))
	    {
	    	String error = "Please enter non-empty search query.";
	    	editText.setHint(error);
	    }
	    else
	    {
			queryString = message;
	    	Intent intent = new Intent(this, DisplaySearchActivity.class);
		    intent.putExtra(EXTRA_MESSAGE, message);
		    startActivity(intent);
	    }
	}

	public void onPause() {
		super.onPause();
		SharedPreferences mainActivityData = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = mainActivityData.edit();
		editor.putString(QUERY_STRING_KEY, queryString);

		//commit
		editor.commit();
	}

	public void onResume() {
		super.onResume();
		SharedPreferences mainActivityData = getPreferences(MODE_PRIVATE);
		queryString = mainActivityData.getString(QUERY_STRING_KEY, "");
		EditText editText = (EditText) findViewById(R.id.edit_message);
		editText.setText(queryString);
	}

	
	public void openSavedGuides(View view) {
		Intent intent = new Intent(this, SavedGuidesActivity.class);
		startActivity(intent);
	}
	
	public void addALocalFile(View view){
		Intent intent = new Intent(this, AddLocalGuideActivity.class);
		startActivity(intent);
	}
	
	public void deleteALocalFile(View view){
		Intent intent = new Intent(this, DeleteLocalGuideActivity.class);
		startActivity(intent);
	}
	public void displayHowTo(View view){
		Intent intent = new Intent(this, HowToUseActivity.class);
		startActivity(intent);
	}
	public void displayAbout(View view){
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}

}
