package com.catsharksoftware.easygameguides;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.catsharksoftware.easygameguides.R;

public class AddLocalGuideActivity extends Activity {
	
	private ArrayList<File> fileSystem;
	private LinearLayout currentDirectory;
	private AlgorithmContainer algorithm;
	
	
	//TODO: fix functionality of back button press at the top of the AddLocalGuideActivity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_local_guide);
		
		//Initialize fileSystem and layout for displaying file system
		fileSystem = new ArrayList<File>();
		currentDirectory = (LinearLayout) findViewById(R.id.current_directory);
		
		algorithm = new AlgorithmContainer();
		
		displayDirectory();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_local_guide, menu);
		return true;
	}
	
	//TODO: remove this functionality of the adding of dummy files
	public void addNewGuide(View view)
	{
		EditText editText = (EditText) findViewById(R.id.edit_guide_name);
		TextView outputMessage = (TextView) findViewById(R.id.add_guide_messages);
		
		String fileName = editText.getText().toString();
		String data = "EasyGG here! Testing file output!";
		
		if(fileName.equals("") || fileName.equals(null))
	    {
	    	String error = "Oops, you didn't enter anything!";
	    	editText.setHint(error);
	    }
		else
		{
			try
			{
				FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
				fos.write(data.getBytes());
				fos.close();
				
				outputMessage.setText("File \"" + fileName + "\" successfully created!");
				
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	private void displayDirectory()
	{
		/*
		 * TODO: add ability to find and add a local file:
		 * http://stackoverflow.com/questions/4108881/android-programming-where-to-start-for-creating-a-simple-file-browser
		 * 
		 * http://stackoverflow.com/questions/5751335/using-file-listfiles-with-filenameextensionfilter
		 */
		
		String externalState = Environment.getExternalStorageState();
		
		if(!externalState.equals(Environment.MEDIA_MOUNTED) && !externalState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			//External media could not be found
		}
		else
		{
			//If this is the first time displayDirectory is called
			if(fileSystem.size() == 0)
			{
				File sd = Environment.getExternalStorageDirectory();
				
				//Add the SD card to the base of the fileSystem ArrayList
				fileSystem.add(sd);
			}
			
			File workingDirectory = fileSystem.get(fileSystem.size() - 1);
			
			TextView cDTextView = new TextView(this);
			cDTextView.setText((String) workingDirectory.getName() + ":");
			cDTextView.setTextSize(20);
			currentDirectory.addView(cDTextView);
			
			//TODO: add a file button to return to the previous directory
			
			
			File[] sdDirList = workingDirectory.listFiles();
			String listOfFiles[] = listFileNames(sdDirList);
			
			for(String currentFile : listOfFiles)
			{
				Button b = new Button(this);
				File fileFromList = getFileFromFileList(currentFile);
				
				if(fileFromList != null)
				{
					if(!fileFromList.isDirectory())
					{
						//TODO: Differentiate the files from the directories much better than how it is now
						b.setBackgroundColor(0x88000000);
						b.setPadding(2, 2, 2, 2);
					}
					
					b.setText(currentFile);
					b.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							selectFile(v);
						}
					});
					currentDirectory.addView(b);
				}
				else
				{
					reportMessage("\"" + currentFile + "\" does not exist");
				}
			}
			
		}
	}
	
	/**
	 * Handle selecting the file or directory
	 * @param View v
	 */
	
	private void selectFile(View v)
	{
		//Get file based on the name of the button
		if(v instanceof Button)
		{
			Button b = (Button) v;
			File selectedFile = getFileFromFileList((String) b.getText());
			if(selectedFile != null)
			{
				if(!selectedFile.isDirectory())
				{
					//Add the file to internal memory
					if(!addFileToInternalMemory(selectedFile))
					{
						b.setText(b.getText() + " does not exist");
					}
					else
					{
						b.setText(b.getText() + "has been successfully added!");
					}
				}
				else
				{
					fileSystem.add(selectedFile);
					currentDirectory.removeAllViews();
					displayDirectory();
				}
			}
			else
			{
				b.setText(b.getText() + " does not exist");
			}
		}
	}
	
	
	/**
	 * Finds and returns the file in the current directory by name
	 * returns null if no such file exists
	 * @param String name
	 * @return File file
	 */
	private File getFileFromFileList(String name)
	{
		File[] directoryFiles =  fileSystem.get(fileSystem.size()-1).listFiles();
		
		for(int i = 0; i < directoryFiles.length; ++i)
		{
			String currentName = (String) directoryFiles[i].getName();
			
			if(currentName.equals(name))
			{
				return directoryFiles[i];
			}
		}
		return null;
	}
	
	private boolean addFileToInternalMemory(File file)
	{
		FileChannel sourceFile = null;
		FileChannel destinationFile = null;
		
		FileOutputStream newFOS = null;
		FileInputStream oldFIS = null;
		try
		{
			//Create channel for new file
			newFOS = openFileOutput(file.getName(), Context.MODE_PRIVATE);			
			destinationFile = newFOS.getChannel();
			
			//Create channel for old file
			oldFIS = new FileInputStream(file);
			sourceFile = oldFIS.getChannel();
			
			//Copy the files bytes
			destinationFile.transferFrom(sourceFile, 0, sourceFile.size());
			
			//Close the file streams
			if(newFOS != null)
			{
				newFOS.close();
			}
			if(oldFIS != null)
			{
				oldFIS.close();
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Function to populate the string array with the file names
	 * @param names
	 * @param files
	 */
	private String[] listFileNames(File[] files)
	{
		String names[] = new String[files.length];
		for(int i = 0; i < files.length; ++i)
		{
			names[i] = files[i].getName();
		}
		if(algorithm == null)
		{
			algorithm = new AlgorithmContainer();
		}
		return algorithm.mergeSort(names);
	}
	
	/**
	 * Report a message using the currentDirectory LinearLayout
	 * @param error
	 */
	private void reportMessage(String error)
	{
		TextView messageText = new TextView(this);
		messageText.setText(error);
		currentDirectory.removeAllViews();
		currentDirectory.addView(messageText);
	}

}
