package com.catsharksoftware.easygameguides;

import java.io.FileOutputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.catsharksoftware.easygameguides.R;

public class AddLocalGuideActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_local_guide);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_local_guide, menu);
		return true;
	}
	
	public void addNewGuide(View view)
	{
		EditText editText = (EditText) findViewById(R.id.edit_guide_name);
		TextView outputMessage = (TextView) findViewById(R.id.add_guide_messages);
		
		String fileName = editText.getText().toString();
		String data = "EasyGG here! Testing file output!";
		
		if(fileName.equals("") || fileName.equals(null))
	    {
	    	String error = "Please enter non-empty search query.";
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
				
				
				/*
				File path = new File(getFilesDir(), MainActivity.EasyGGFile);
				if(!path.exists())
				{
					path.createNewFile();
					path.mkdir();
				}
				File myPath = new File(path, fileName +".txt");
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(myPath));
				bw.write("EasyGG here! Testing file output!");
				bw.close();
				
				outputMessage.setText("New file at directory: " + path);
				*/
				
				/*
				@SuppressWarnings("deprecation")
				FileOutputStream fOut = openFileOutput(fileName +".txt", MODE_WORLD_READABLE);
				
				OutputStreamWriter osw = new OutputStreamWriter(fOut);
				
				osw.write("EasyGG here! Testing file output!");
				
				osw.flush();
				osw.close();
				*/
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
