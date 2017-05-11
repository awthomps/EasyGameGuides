package com.catsharksoftware.easygameguides;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
 * To find files, this class relies heavily on the following:
 * 
 *  "http://www.gamefaqs.com/search/index.html?game="
 * 
 */

/*
 * Possibly helpful links:
 * http://code.google.com/p/hassanpurcom/source/browse/#svn%2Ftrunk%2Fandroid%2FAndroidFileDownloaderExample%2Fsrc%2Fcom%2Fhassanpur%2Ftutorials%2Fandroid
 * http://www.helloandroid.com/tutorials/how-download-fileimage-url-your-device
 * 
 */



public class WebDownloadThread extends Thread {
	
	public static final int GAME_NAME_SEARCH = 0;
	public static final int GUIDES_SEARCH = 1;
	public static final int ONLINE_GUIDE_DISPLAY = 2;
	
	private static final String GF_ROOT = "https://www.gamefaqs.com";
	private static final String GF_SEARCH = "/search/index.html?game=";
	
	// patterns:
	private static final String GF_BEST_MATCH_PATTERN = "Best Matches";
	private static final String GF_ITEM_PATTERN = "\\<td class=\"rtitle\"\\>";
	private static final String GF_TITLE_PATTERN = "(\\>)(.+)(\\<)/a\\>";
	private static final String GF_ITEM_URL_PATTERN = "(\"/)(.+)(/)(.+)(\")";
	private static final String GF_ITEM_PLATFORM_PATTERN = "(/)(.+)(/)";
	private static final String GF_FAQ_URL_PATTERN = "(/faqs/)(\\d+)";
	private static final String GF_FAQ_SPAN_BEGIN_A = "\\<span id=\"faqspan-";
	private static final String GF_FAQ_SPAN_BEGIN_B = "\"\\>";
	private static final String GF_FAQ_SPAN_END = "\\<\\span\\>";
	private static final int GF_BEGIN_TITLE_OFFSET = 1;
	private static final int GF_END_TITLE_OFFSET = 4;
	private static final int GF_BEGIN_FAQ_NUM_OFFSET = 6;
	private static final int GF_REMOVE_FAQ_FROM_URL = 5;
	
	private static final String MIME_TEXT_HTML = "text/html";

	private Activity parentActivity;
	private String query;
	private LinearLayout resultsView;
	private int type;
	
	/**
	 * Instatiate a WebDownloaderThread
	 * @param parentActivity: reference to parent activity
	 * @param requestedURL: the url to be downloaded
	 */
	public WebDownloadThread(Activity parentActivity, String query, int type, LinearLayout desiredView) {
		this.query = "";
		resultsView = desiredView;
		if(query != null) {
			//parse unwanted values:
			this.query = query.replace(' ', '-');
		}
		this.type = type;
		this.parentActivity = parentActivity;
	}
	
	public void run() {
		
		if(type == GAME_NAME_SEARCH) {
			searchGameName();
		}
		else if(type == GUIDES_SEARCH) {
			displayGuidesFromTitle();
		}
		else if(type == ONLINE_GUIDE_DISPLAY) {
			displayOnlineGuide();
		}
	}


	private void downloadGuideToTextFile() {
		String htmlData = "";
		URL url;
		BufferedInputStream inStream;
		URLConnection connection;

		try {
			String urlString = query;
			url = new URL(urlString);
			connection = url.openConnection();
			connection.setUseCaches(false);

			// define input stream to read from the URLConnection
			inStream = new BufferedInputStream((connection.getInputStream()));

			//read bytes to the buffer until there is no more to add;
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int currentByte = 0;
			while((currentByte = inStream.read()) != -1) {
				baf.append((byte) currentByte);
			}

			//create string from bytes
			String htmlTemp = new String(baf.buffer());

			String cleanedText = "";
			int i = 1;
			while(htmlTemp.contains(GF_FAQ_SPAN_BEGIN_A + i + GF_FAQ_SPAN_BEGIN_B)) {
				/*
				TODO: USE REGEX TO PARSE OUT ALL THE 'IMPORTANT' TEXT FROM THE ONLINE GUIDE AND THEN SAVE IT TO A TEXT FILE.

				 */
			}


		} catch (MalformedURLException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("This URL was malformed! Please contact support!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});

			e.printStackTrace();
			return;
		} catch (IOException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("Could not make a connection! Are you online?");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			e.printStackTrace();
			return;
		}
	}


	@SuppressLint("SetJavaScriptEnabled")
	private void displayOnlineGuide() {
		String htmlData = "";
		URL url;
		BufferedInputStream inStream;
		URLConnection connection;
		
		try {
			String urlString = query;
			url = new URL(urlString);
			connection = url.openConnection();
			connection.setUseCaches(false);
			
			// define input stream to read from the URLConnection
			inStream = new BufferedInputStream((connection.getInputStream()));
			
			//read bytes to the buffer until there is no more to add;
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int currentByte = 0;
			while((currentByte = inStream.read()) != -1) {
				baf.append((byte) currentByte);
			}
			
			//create string from bytes
			String htmlTemp = new String(baf.buffer());
			
			//final WebView guideDisplayView = (WebView) parentActivity.findViewById(R.id.online_guide_view);
			//allow for javascript execution
			//guideDisplayView.post(new Runnable() {
			resultsView.post(new Runnable() {
				
				//TODO: figure out why this displays nothing!
				public void run() {
					WebView guideDisplayView = new WebView(parentActivity);
					guideDisplayView.getSettings().setJavaScriptEnabled(true);
					guideDisplayView.loadUrl(query);
					resultsView.addView(guideDisplayView);
					guideDisplayView.requestLayout();
					resultsView.requestLayout();
					resultsView.getParent().requestLayout();
				}
			});
			resultsView.post(new Runnable() {
				public void run() {
					
				}
			});
			
			/*guideDisplayView.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					parentActivity.setProgress(progress * 1000);
				}
			});*/
			
			/*resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(guideDisplayView);
				}
			});*/
			
			
		} catch (MalformedURLException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("This URL was malformed! Please contact support!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			
			e.printStackTrace();
			return;
		} catch (IOException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("Could not make a connection! Are you online?");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			e.printStackTrace();
			return;
		}
	}

	private void displayGuidesFromTitle() {
		String cleanHTML = "";
		URL url;
		BufferedInputStream inStream;
		URLConnection connection;
		
		
		try {
			String urlString = query;
			url = new URL(urlString);
			connection = url.openConnection();
			connection.setUseCaches(false);
			
			// define input stream to read from the URLConnection
			inStream = new BufferedInputStream((connection.getInputStream()));
			
			//read bytes to the buffer until there is no more to add;
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int currentByte = 0;
			while((currentByte = inStream.read()) != -1) {
				baf.append((byte) currentByte);
			}
			
			//create string from bytes
			String htmlTemp = new String(baf.buffer());
			
			
			//find all of the text guides:
			Pattern guidesPattern = Pattern.compile(GF_FAQ_URL_PATTERN);
			Matcher guidesMatcher = guidesPattern.matcher(htmlTemp);
			boolean foundGuides = false;
			while (guidesMatcher.find()) {
				final String onlineGuideURL = urlString.substring(0,urlString.length() - GF_REMOVE_FAQ_FROM_URL) + guidesMatcher.group();
				final Button guide = new Button(parentActivity);
				guide.setText(guidesMatcher.group());
				guide.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						//TODO: implement what happens when this is clicked
						//get data from the regex where the url of the guide is!
						Intent intent = new Intent(parentActivity, DisplayOnlineGuideActivity.class);
					    intent.putExtra(DisplaySearchActivity.ONLINE_GUIDE_URL, onlineGuideURL);
					    parentActivity.startActivity(intent);
					}

				});
				//add the button of the guide to the view
				resultsView.post(new Runnable() {
					public void run() {
						resultsView.addView(guide);
					}
				});
				foundGuides = true;
			}
			if(!foundGuides) {
				final TextView message = new TextView(parentActivity);
				message.setText("Sorry, there seems to be no guides for this game!");
				resultsView.post(new Runnable() {
					public void run() {
						resultsView.addView(message);
					}
				});
			}
			
		} catch (MalformedURLException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("This URL was malformed! Please contact support!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			
			e.printStackTrace();
			return;
		} catch (IOException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("Could not make a connection! Are you online?");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			e.printStackTrace();
			return;
		}
	}

	public void searchGameName() {
		String htmlContents = "";
		htmlContents = performGameNameSearch();
		
		//make sure that there was a success, otherwise just return
		//since error has already been reported.
		if(htmlContents.equals("FAILURE")) { return; }
		
	}
	
	private String performGameNameSearch() {
		String cleanHTML = "";
		int beginSection = 0;
		URL url;
		BufferedInputStream inStream;
		URLConnection connection;
		
		try {
			String urlString = GF_ROOT + GF_SEARCH + query;
			url = new URL(urlString);
			connection = url.openConnection();
			connection.setUseCaches(false);
			
			// define input stream to read from the URLConnection
			inStream = new BufferedInputStream((connection.getInputStream()));
			
			//read bytes to the buffer until there is no more to add;
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int currentByte = 0;
			while((currentByte = inStream.read()) != -1) {
				baf.append((byte) currentByte);
			}
			
			//create string from bytes
			String htmlTemp = new String(baf.buffer());
			//TODO: use http://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
			// to access desired data
			
			//Cut off the beginning of the html file string
			Pattern bestMatchesPattern = Pattern.compile(GF_FAQ_URL_PATTERN);
			Matcher htmlMatcher = bestMatchesPattern.matcher(htmlTemp);
			boolean foundBestMatches = false;
			/*
			while (htmlMatcher.find()) {
				final TextView message = new TextView(parentActivity);
				message.setText(GF_BEST_MATCH_PATTERN + ":");
				resultsView.post(new Runnable() {
					public void run() {
						resultsView.addView(message);
					}
				});
				beginSection = htmlMatcher.start();
				foundBestMatches = true;
			}
			if(!foundBestMatches) {
				final TextView message = new TextView(parentActivity);
				message.setText("Results not found! Try another search.");
				resultsView.post(new Runnable() {
					public void run() {
						resultsView.addView(message);
					}
				});
			}*/
			
			//search through the items:
			cleanHTML = htmlTemp.substring(beginSection);
			Pattern itemsPattern = Pattern.compile(GF_ITEM_PATTERN);
			Matcher itemsMatcher = itemsPattern.matcher(cleanHTML);
			boolean foundItems = false;
			while(itemsMatcher.find()) {
				
				String itemStart = cleanHTML.substring(itemsMatcher.start());
				
				
				Pattern currentItemPattern = Pattern.compile(GF_TITLE_PATTERN);
				Matcher currentItemMatcher = currentItemPattern.matcher(itemStart);
				//find first instance of title:
				if(currentItemMatcher.find()) {
					String platform = "";
					String titleURL = "";
					String itemName = currentItemMatcher.group().substring(GF_BEGIN_TITLE_OFFSET, 
							currentItemMatcher.group().length() - GF_END_TITLE_OFFSET);
					String itemSubString = itemStart.substring(0,currentItemMatcher.end());
					
					//find url:
					Pattern currentItemURLPattern = Pattern.compile(GF_ITEM_URL_PATTERN);
					Matcher currentItemURLMatcher = currentItemURLPattern.matcher(itemSubString);
					if(currentItemURLMatcher.find()) {
						//get the url and remove the quotations:
						titleURL = currentItemURLMatcher.group().substring(1,currentItemURLMatcher.group().length()-1);
					}
					//find platform (first part of url):
					Pattern currentItemPlatformPattern = Pattern.compile(GF_ITEM_PLATFORM_PATTERN);
					Matcher currentItemPlatformMatcher = currentItemPlatformPattern.matcher(titleURL);
					if(currentItemPlatformMatcher.find()) {
						//get the platform and remove the quotations from it:
						platform = currentItemPlatformMatcher.group().substring(1,currentItemPlatformMatcher.group().length()-1);
					}
					
					//set up the button for this game title
					final String resultsForText = "Guides for \"" + itemName + "\"\n<platform:" + platform +">:";
					final Button titleButton = new Button(parentActivity);
					final String buttonTitleURL = titleURL;
					titleButton.setText(itemName + "\n<platform:" + platform +">");
					titleButton.setGravity(Gravity.LEFT);
					titleButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							//clear the views for the guides:
							resultsView.post(new Runnable() { 
								public void run() {
									//clear the views for the search query
									resultsView.removeAllViews();
								}
							});
							
							//create a way to go back and re-query:
							final Button backButton = new Button(parentActivity);
							backButton.setText("Back to search.");
							backButton.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									String prefix = "Results for: ";
									final TextView resultsForView = new TextView(parentActivity);
									resultsForView.setTextSize(16);
									resultsForView.setText(prefix + "\"" + query + "\":");
								    resultsView.post(new Runnable() { 
										public void run() {
											//clear the views for the search query
											resultsView.removeAllViews();
											resultsView.addView(resultsForView);
										}
									});
									
									Thread homeThread = new WebDownloadThread(parentActivity, query, GAME_NAME_SEARCH, resultsView);
									homeThread.start();
								}
								
							});
							resultsView.post(new Runnable() { 
								public void run() {
									//clear the views for the search query
									resultsView.addView(backButton);
								}
							});
							
							//remind user what the results are for:
							final TextView titleView = new TextView(parentActivity);
							titleView.setTextSize(16);
							titleView.setText(resultsForText);
							resultsView.post(new Runnable() { 
								public void run() {
									resultsView.addView(titleView);
								}
							});
							
							Thread currentGuides = new WebDownloadThread(parentActivity, GF_ROOT + buttonTitleURL + "/faqs", GUIDES_SEARCH, resultsView);
							currentGuides.start();
						}
					});
					
					//Thread search = new WebDownloadThread(this, message, WebDownloadThread.GAME_NAME_SEARCH, searchLayout);
					//search.start();
					
					resultsView.post(new Runnable() {
						public void run() {
							resultsView.addView(titleButton);
						}
					});
					
					
					/*
					String faqsStart = itemStart.substring(currentItemsMatcher.start());
					
					Pattern currentFAQsPattern = Pattern.compile(GF_FAQ_URL_PATTERN);
					Matcher currentFAQsMatcher = currentFAQsPattern.matcher(faqsStart);
					*/
				}
				else
				{
					//could not find currentItem
				}
				
				foundItems = true;
			}
			if(!foundItems) {
				/*
				final TextView message = new TextView(parentActivity);
				message.setText("Could not find " + GF_ITEM_PATTERN);
				resultsView.post(new Runnable() {
					public void run() {
						resultsView.addView(message);
					}
				});*/
			}
			
			
			/*
			final TextView message = new TextView(parentActivity);
			message.setText("Successfully connected to gamefaqs.com!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(message);
				}
			});*/
			
			
		} catch (MalformedURLException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("This URL was malformed! Please contact support!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			
			e.printStackTrace();
			return "FAILURE";
		} catch (IOException e) {
			final TextView error = new TextView(parentActivity);
			error.setText("Could not make a connection! Are you online?");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(error);
				}
			});
			e.printStackTrace();
			return "FAILURE";
		}
		return cleanHTML;
	}
	
}
