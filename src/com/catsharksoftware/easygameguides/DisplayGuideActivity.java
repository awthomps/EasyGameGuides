package com.catsharksoftware.easygameguides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import com.catsharksoftware.easygameguides.R;

public class DisplayGuideActivity extends Activity {
	
	private LinearLayout toolbar;
	private LinearLayout layout, infoDisplay;
	private ArrayList<View> guideTextViews;
	private ScrollView scrollBar;
	private String guideName;
	private int guideIndex, textBlockIndex;
	private TextView pageLoc;
	
	private boolean isFirstTime;
	private int sessionScrollLocation;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// Get the message from the intent
		Intent intent = getIntent();
		guideName = intent.getStringExtra(SavedGuidesActivity.NAME);
		setTitle(guideName);
		
		setContentView(R.layout.activity_display_guide);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Initialize layout
		try
		{
			layout = (LinearLayout) findViewById(R.id.guide_text);
			toolbar = (LinearLayout) findViewById(R.id.guide_toolbar);
			infoDisplay = (LinearLayout) findViewById(R.id.info_bar);
			infoDisplay.setVisibility(View.GONE); //does not update percentage so hide
			scrollBar = (ScrollView) findViewById(R.id.scroll_bar);
			guideTextViews = new ArrayList<View>();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		guideIndex = 0;
		textBlockIndex = 0;
		isFirstTime = true;
		
		
		/*
		 * TODO: get this to work so that when the orientation changes, the scroll is not lost
		 * 
		 * get sessionScrollLocation if it has been saved from a previous
		 * instance of this particular activity
		 */
		
		@SuppressWarnings("deprecation")
		final Object location = getLastNonConfigurationInstance();
		if(location != null && (location instanceof Integer))
		{
			sessionScrollLocation = (Integer) location;
		}
		else
		{
			sessionScrollLocation = 0;
		}
		
		setupToolbar(false, false, false);
		loadFile(guideName);
		//setupInfoDisplay();
	}

	/**
	 * activity resumes
	 * scrolls to the last location the user was in the ScrollView
	 */
	public void onResume(Bundle savedInstanceState)
	{
		scrollBar.scrollTo(0, sessionScrollLocation);
	}
	
	/**
	 * activity pauses
	 * saves the last location the user was in the ScrollView
	 */
	public void onPause(Bundle savedInstanceState)
	{
		ScrollView scrollBar = (ScrollView) findViewById(R.id.scroll_bar);
		sessionScrollLocation = scrollBar.getScrollY();
	}
	
	/**
	 * onRetainNonConfigurationInstance
	 * saves current scroll location if activity is killed.
	 */
	public Object onRetainNonConfigurationInstance()
	{
		final int location = sessionScrollLocation;
		return location;
	}
	
	/**
	 * set up the search functionality for the game guide
	 */

	private void setupToolbar(boolean bringUpSearch, boolean bringUpNav, boolean bringUpBookmarks) {
		
		//toolbar openers
		Button openSearch = (Button) findViewById(R.id.open_search);
		Button openNav = (Button) findViewById(R.id.open_nav);
		Button openBookmarks = (Button) findViewById(R.id.open_bookmarks);
		
		//toolbar hider
		Button hide = (Button) findViewById(R.id.hide);
		
		//searh toolbar
		Button prevWord = (Button) findViewById(R.id.prev_word);
		Button nextWord = (Button) findViewById(R.id.next_word);
		EditText searchBar = (EditText) findViewById(R.id.search_bar);
		
		
		//integrate navigation and bookmarks
		Button upGuide = (Button) findViewById(R.id.up_guide);
		Button downGuide = (Button) findViewById(R.id.down_guide);
		
		TextView numMarks = (TextView) findViewById(R.id.num_of_marks);
		Button prevMark = (Button) findViewById(R.id.prev_mark);
		Button makeMark = (Button) findViewById(R.id.make_mark);
		Button nextMark = (Button) findViewById(R.id.next_mark);
		
		
		
		//If first time this is called, initiate setup
		if(isFirstTime)
		{
			//open search button
			openSearch.setText("Search");
			openSearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				setupToolbar(true, false, false);
				}
			});
			
			//open navigation button
			//TODO: setup navigation functionality (divide the guide into ten parts and go up and down the guide ten percent)
			openNav.setText("Navigate");
			openNav.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				setupToolbar(false, true, false);
				}
			});
			
			//open bookmarks button
			//TODO: setup bookmarks functionality (save the scroll location of a desired bookmark to local memory)
			openBookmarks.setText("Bookmarks");
			openBookmarks.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
				setupToolbar(false, false, true);
				}
			});
			
			//hide bar button
			hide.setText("Hide");
			hide.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					setupToolbar(false, false, false);
				}
			});
			
			//The previous button
			prevWord.setText("<");
			prevWord.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
					//Find the searchBar again
					EditText searchBar = (EditText) findViewById(R.id.search_bar);
					if(searchBar.getText().equals(""))
					{
						searchBar.setText("Please enter nonempty query");
					}
					else
					{
						searchGuide(searchBar, false);
					}
				}
			});
			
			//The forward button
			nextWord.setText(">");
			nextWord.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
					//Find the searchBar again
					EditText searchBar = (EditText) findViewById(R.id.search_bar);
					if(searchBar.getText().equals(""))
					{
						searchBar.setText("Please enter nonempty query");
					}
					else
					{
						searchGuide(searchBar, true);
					}
				}
			});
			
			
			//The up page button
			upGuide.setText("Up ^");
			upGuide.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					scrollBy(-10);
					
					
				}
			});
			
			//The down page button
			downGuide.setText("Down v");
			downGuide.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					scrollBy(10);
				}
			});
			
			
			
			//The number of bookmarks page button
			numMarks.setText("Number");
			numMarks.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
				}
			});
			
			//Go back a bookmark button
			prevMark.setText("<");
			prevMark.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
				}
			});
			
			
			//set current scrollview scrollTo to this bookmark
			makeMark.setText("Bookmark location");
			makeMark.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
				}
			});
			//Go to next boormark button
			nextMark.setText(">");
			nextMark.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//TODO: set up searching
					
				}
			});
			
			isFirstTime = false;
		}
		
		
		if(bringUpSearch)
		{
			//hide toolbar menu
			openSearch.setVisibility(View.GONE);
			openNav.setVisibility(View.GONE);
			openBookmarks.setVisibility(View.GONE);
			hide.setVisibility(View.VISIBLE);
			
			//Bring up the search toolbar
			prevWord.setVisibility(View.VISIBLE);
			nextWord.setVisibility(View.VISIBLE);
			
			//The search bar
			searchBar.setHint("Search guide");
			searchBar.setVisibility(View.VISIBLE);
		}
		else if(bringUpNav)
		{
			//hide toolbar menu
			openSearch.setVisibility(View.GONE);
			openNav.setVisibility(View.GONE);
			openBookmarks.setVisibility(View.GONE);
			hide.setVisibility(View.VISIBLE);
			
			//bring up navigation bar
			upGuide.setVisibility(View.VISIBLE);
			downGuide.setVisibility(View.VISIBLE);
		}
		else if(bringUpBookmarks)
		{
			//hide toolbar menu
			openSearch.setVisibility(View.GONE);
			hide.setVisibility(View.VISIBLE);
			openNav.setVisibility(View.GONE);
			openBookmarks.setVisibility(View.GONE);
			
			//bring up bookmarks bar
			numMarks.setVisibility(View.VISIBLE);
			prevMark.setVisibility(View.VISIBLE);
			makeMark.setVisibility(View.VISIBLE);
			nextMark.setVisibility(View.VISIBLE);
		}
		else
		{
			//Bring up the regular toolbar menu
			// Hide non funtional features
			openSearch.setVisibility(View.GONE);
			openNav.setVisibility(View.VISIBLE);
			openBookmarks.setVisibility(View.GONE);
			
			hide.setVisibility(View.GONE);
			prevWord.setVisibility(View.GONE);
			nextWord.setVisibility(View.GONE);
			searchBar.setVisibility(View.GONE);
			
			upGuide.setVisibility(View.GONE);
			downGuide.setVisibility(View.GONE);
			
			numMarks.setVisibility(View.GONE);
			prevMark.setVisibility(View.GONE);
			makeMark.setVisibility(View.GONE);
			nextMark.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Find the next instance of whatever is contained in the EditText view
	 * @param searchBar
	 * @param forward
	 * @return
	 */
	private void searchGuide(EditText searchBar, boolean forward)
	{	
		//private ArrayList<View> guideTextViews;
		//private int guideIndex;
		//private int textBlockIndex;
		
		String query = searchBar.getText().toString().toLowerCase(AlgorithmContainer.CURRENT_LOCALE);
		
		while(guideIndex >= 0 && guideIndex < guideTextViews.size())
		{
			View view = guideTextViews.get(guideIndex);
			TextView block = null;
			String textBlock = null;
			
			if(view instanceof TextView)
			{
				block = (TextView) view;
			}
			else
			{
				searchBar.setHint("An error has occurred");
				return;
			}
			
			textBlock = (String) block.getText().toString().toLowerCase(AlgorithmContainer.CURRENT_LOCALE);
			
			
			//Search the given block of text
			while(textBlockIndex < textBlock.length())
			{
				textBlockIndex = textBlock.indexOf(query,textBlockIndex);
				if(textBlockIndex == -1)
				{
					textBlockIndex = 0;
					break;
				}
				else
				{
					int loc[] = {0,1};
					block.getLocationInWindow(loc);
					scrollBar.scrollTo(loc[0],loc[1]);
					if(forward)
					{	searchBar.clearFocus();
						block.requestFocus(View.FOCUS_DOWN);
						return;
					}
					else
					{
						searchBar.clearFocus();
						block.requestFocus(View.FOCUS_UP);
						return;
					}
					
				}
			}
			textBlockIndex = 0;
			if(forward)
			{
				++guideIndex;
			}
			else
			{
				--guideIndex;
			}
		}
		if(guideIndex >= guideTextViews.size() || guideIndex < 0)
		{
			searchBar.setText("Reached end for \"" + query + "\"");
			if(forward)
			{
				guideIndex = 0;
			}
			else
			{
				guideIndex = guideTextViews.size() - 1;
			}
		}
		
		return;
	}



	private void loadFile(final String name) {
		/*
		 * TODO:
		 * Tutorial for reading/displaying text in a file:
		 * http://people.bridgewater.edu/~arn002/csci440/android-tutorial.htm
		 * 
		 * 
		 */	
		final Context thisContext = this;
		
		final String loading = "Please wait, loading guide \"" + name + "\"...";
		final TextView loadingView = new TextView(this);
		loadingView.setTextSize(24);
		loadingView.setPadding(0,0,0,15);
		loadingView.setFocusable(true);
		loadingView.setText(loading);
		layout.addView(loadingView);
		
		new Thread(new Runnable() {
			final ArrayList<String> guideText = new ArrayList<String>();
			public void run() {
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
					
				
				
				for(int i = 0; i < guideText.size(); ++i)
				{
					try
					{
						// Create the text view
						// String text = "";
						while(i < guideText.size() && !guideText.get(i).equals(""))
						{
							//text += guideText.get(i);
							final int percent = (int) (((double) (i * 100))/ ((double) guideText.size()));
							final String lineOfText = guideText.get(i);
							TextView lineOfTextView = new TextView(thisContext);
							lineOfTextView.setTextSize(16);
							lineOfTextView.setText(lineOfText);
							lineOfTextView.setFocusable(true);
							lineOfTextView.setTypeface(Typeface.MONOSPACE);
							
							//Add Guide to data structure
							guideTextViews.add(lineOfTextView);
							layout.post(new Runnable() {
								public void run() {
									TextView lineOfTextView = new TextView(thisContext);
									lineOfTextView.setTextSize(16);
									lineOfTextView.setText(lineOfText);
									lineOfTextView.setFocusable(true);
									lineOfTextView.setTypeface(Typeface.MONOSPACE);
									loadingView.setText("Loading %" + percent + "\nPlease wait a few moments...");
									layout.addView(lineOfTextView);
								}
							});
							++i;
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				layout.post(new Runnable() {
					public void run() {
						layout.removeView(loadingView);
					}
				});
			}
		}).start();
	}
	

	
	private void setupInfoDisplay() {
		final Activity thisActivity = this;
		final ScrollView scrollBar = (ScrollView) findViewById(R.id.scroll_bar);
		TextView test = new TextView(this);
		test.setText("%0");
		infoDisplay.addView(test);
	    new Thread(new Runnable() {
	        public void run() {
	            final TextView percentDisplay = new TextView(thisActivity);
	            infoDisplay.addView(percentDisplay);
	            percentDisplay.post(new Runnable() {
	                public void run() {
	                    while(true)
	                    {
	                    	percentDisplay.setText("%" + ((double) scrollBar.getScrollY())/scrollBar.getChildAt(0).getHeight());
	                    }
	                }
	            });
	        }
	    }).start();	
	}
	
	private void scrollBy(double amount) {
		//scrollBar.computeScroll();
		double currentScroll = scrollBar.getScrollY();
		double maxScroll = scrollBar.getChildAt(0).getHeight();//.getMaxScrollAmount();
		double fractionLoc = currentScroll/maxScroll;
		
		//check if under %amount
		if(amount < 0 && (fractionLoc * 100.0 < -amount))
		{
			scrollBar.scrollTo(scrollBar.getScrollX(), 0);
			//scrollBar.scrollBy(0, (int) (amount/maxScroll));
		}
		else if(amount > 0 && (100.0 - (fractionLoc*100.0)) < amount )
		{
			scrollBar.scrollTo(scrollBar.getScrollX(), (int)maxScroll);
			//scrollBar.scrollBy(0, (int) (amount/maxScroll));
		}
		else
		{
			int newLocation = (int) (currentScroll + (amount*maxScroll/100));
			scrollBar.scrollTo(scrollBar.getScrollX(), newLocation);
			//scrollBar.scrollBy(0,(int)amount);
		}
		
		TextView maxScrollV = new TextView(this);
        infoDisplay.addView(maxScrollV);
        //maxScrollV.setText("       "+maxScroll);
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
