package com.catsharksoftware.easygameguides;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.os.Message;
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
	//
	private static final String GF_TITLE_PATTERN = "\\<td class=\"rtitle\"\\<";

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
			this.query = query;
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
			//TODO: pass through and consume all tabs and newlines
			//then, use http://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
			// to access desired data
			
			for(int i = 0; i < htmlTemp.length(); ++i) {
				char currChar = htmlTemp.charAt(i);
				if(currChar != '\n' && currChar != '\t') {
					cleanHTML += currChar;
				}
				else {
					/*
					final TextView message = new TextView(parentActivity);
					message.setText("found " + currChar);
					resultsView.post(new Runnable() {
						public void run() {
							resultsView.addView(message);
						}
					});*/
				}
			}
			
			final TextView message = new TextView(parentActivity);
			message.setText("Successfully connected to gamefaqs.com!");
			resultsView.post(new Runnable() {
				public void run() {
					resultsView.addView(message);
				}
			});
			
			
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
