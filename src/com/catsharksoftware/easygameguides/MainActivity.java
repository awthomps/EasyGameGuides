package com.catsharksoftware.easygameguides;

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
	    
	    if(message.equals("") || message.equals(null))
	    {
	    	String error = "Please enter non-empty search query.";
	    	editText.setHint(error);
	    }
	    else
	    {
	    	Intent intent = new Intent(this, DisplaySearchActivity.class);
		    intent.putExtra(EXTRA_MESSAGE, message);
		    startActivity(intent);
	    }
	}
	
	public void openSavedGuides(View view) {
		Intent intent = new Intent(this, SavedGuidesActivity.class);
		startActivity(intent);
	}
	
	public void addALocalFile(View view){
		Intent intent = new Intent(this, AddLocalGuideActivity.class);
		startActivity(intent);
	}

}
