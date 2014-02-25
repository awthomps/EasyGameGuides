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
	
	private static final int DL_BUFFER_LENGTH = 4096;
	private static final int GAME_NAME_SEARCH = 0;
	private static final int GUIDES_SEARCH = 1;
	private static final String GF_ROOT = "http://www.gamefaqs.com/";
	private static final String GF_SEARCH = "search/index.html?game=";
	private static final String GF_TITLE_PATTERN = "<td class=\"rtitle\"\n<";

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
		URL url;
		URLConnection connection;
		int fileLength;
		String fileName;
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		File outFile;
		FileOutputStream fileStream;
		Message msg;
	}
	
	public void searchGameName() {
		URL url;
		URLConnection connection;
		int fileLength;
		String fileName;
		String htmlContents;
		BufferedInputStream inStream;
		BufferedOutputStream outStream;
		File outFile;
		FileOutputStream fileStream;
		Message msg;
		
		try {
			url = new URL(GF_ROOT + GF_SEARCH + query);
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
			htmlContents = new String(baf.buffer());
			//TODO: pass through and consume all tabs and newlines
			//then, use http://docs.oracle.com/javase/tutorial/essential/regex/test_harness.html
			// to access desired data
			
			
			
		} catch (MalformedURLException e) {
			TextView error = new TextView(parentActivity);
			error.setText("This URL was malformed! Please contact support!");
			resultsView.addView(error);
			e.printStackTrace();
			return;
		} catch (IOException e) {
			TextView error = new TextView(parentActivity);
			error.setText("Could not make a connection! Are you online?");
			resultsView.addView(error);
			e.printStackTrace();
			return;
		}
		
	}
	
}
