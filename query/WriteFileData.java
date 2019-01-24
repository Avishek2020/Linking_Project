package org.upb.music.artist.similarity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteFileData 
{
   // public static void main(String[] args) 
	public void SaveData(String Data)
    {

        File file = new File("C:\\LIMES\\limes-core\\Similarityfile.txt");

        PrintWriter printWriter = null;

        try
        {
            printWriter = new PrintWriter(file);
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
}