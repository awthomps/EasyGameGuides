/**
 * Due to the need to reuse algorithm functions, AlgorithmContainer's purpose is to take a data structure
 * that I need processed from any activity. An activity only needs to create an AlgorithmContainer and
 * the processed data will be returned.
 */

package com.catsharksoftware.easygameguides;

public class AlgorithmContainer {
	
	//AlgorithmContainer Constructor
	public AlgorithmContainer()
	{
		
	}
	
	
	public String[] mergeSort(String[] files)
	{
		int size = files.length;
		//The base case of the sort has been reached
		if(size <= 1)
		{
			return files;
		}
		else
		{
			//Initialize two new file arrays
			String[] filesA = new String[size/2];
			String[] filesB = new String[size - size/2];
			
			//Populate filesA array:
			for(int i = 0; i < filesA.length; ++i)
			{
				filesA[i] = files[i];
			}
			//Populate filesB array starting from where fileA left off:
			for(int i = 0; i < filesB.length; ++i)
			{
				filesB[i] = files[filesA.length + i];
			}
			
			//Begin the sorting process:
			filesA = mergeSort(filesA);
			filesB = mergeSort(filesB);
			
			//Finish up this level's sorting process:
			int a = 0, b = 0, c = 0;
			while(c < size)
			{
				if(a >= filesA.length)
				{
					files[c] = filesB[b];
					++b;
				}
				else if(b >= filesB.length)
				{
					files[c] = filesA[a];
					++a;
				}
				else if(filesA[a].compareTo(filesB[b]) < 0)
				{
					files[c] = filesA[a];
					++a;
				}
				else
				{
					files[c] = filesB[b];
					++b;
				}
				++c;
			}
			return files;
		}
	}

}
