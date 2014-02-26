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

import android.app.Activity;
import android.os.Message;
import android.view.Gravity;
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
	public  static final int GUIDES_SEARCH = 1;
	
	private static final String GF_ROOT = "http://www.gamefaqs.com";
	private static final String GF_SEARCH = "/search/index.html?game=";
	
	// patterns:
	private static final String GF_BEST_MATCH_PATTERN = "Best Matches";
	private static final String GF_ITEM_PATTERN = "\\<td class=\"rtitle\"\\>";
	private static final String GF_TITLE_PATTERN = "(\\>)(.+)(\\<)/a\\>";
	private static final String GF_ITEM_URL_PATTERN = "(\"/)(.+)(/)(.+)(\")";
	private static final String GF_ITEM_PLATFORM_PATTERN = "(/)(.+)(/)";
	private static final String GF_FAQ_URL_PATTERN = "(/faqs/)([0-9]+)";
	
	private static final int GF_BEGIN_TITLE_OFFSET = 1;
	private static final int GF_END_TITLE_OFFSET = 4;
	private static final int GF_BEGIN_FAQ_NUM_OFFSET = 6;

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
			
		}
	}
	
	public void searchGameName() {
		
		int fileLength;
		String fileName;
		String htmlContents = "";
		BufferedOutputStream outStream;
		File outFile;
		FileOutputStream fileStream;
		Message msg;
		
		htmlContents = getCleanHTML();
		
		//make sure that there was a success, otherwise just return
		//since error has already been reported.
		if(htmlContents.equals("FAILURE")) { return; }
		
	}
	
	private String getCleanHTML() {
		String cleanHTML = "";
		int beginSection = 0, endSection = 0;
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
			Pattern bestMatchesPattern = Pattern.compile(GF_BEST_MATCH_PATTERN);
			Matcher htmlMatcher = bestMatchesPattern.matcher(htmlTemp);
			boolean foundBestMatches = false;
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
			}
			
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
					
					
					final Button titleButton = new Button(parentActivity);
					titleButton.setText(itemName + "\n<platform:" + platform +">");
					titleButton.setGravity(Gravity.LEFT);
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
