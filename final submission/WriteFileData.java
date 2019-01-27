package org.upb.music.artist.similarity.measures;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class WriteFileData 
{
	public void SaveData(String Data,String file_name)
	{

		File file = new File("C:\\LIMES\\limes-core\\"+file_name+"_Similarity.txt");

		PrintWriter printWriter = null;

		try
		{
			printWriter = new PrintWriter(new FileOutputStream(file, true));
			printWriter.println(Data);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if ( printWriter != null ) 
			{
				printWriter.close();
			}
		}
	}
	/*public static void main(String[] args) {
		WriteFileData w = new WriteFileData();
		w.SaveData("Test 123", "XYZ_FILE");
	}*/
}