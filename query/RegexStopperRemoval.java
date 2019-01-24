package org.upb.music.artist.similarity;

import java.util.ArrayList;
import java.util.List;

public class RegexStopperRemoval {
	static String v_processText = null;
	public static String stopperremoval(String text) 
	{
		String v_text ;
		
		v_text = text.toLowerCase();
		String stopWords[]= {"it",":","is","a","by","e","the","but","to"};
		String[] splitString = (v_text.split("\\s+"));
		System.out.println(splitString.length);//  
		for (String string : splitString) 
		{
			for(int i =0;i<stopWords.length;i++) 
			{
				if(string.equals(stopWords[i])) 
				{
					string = string.replaceAll(stopWords[i], "");
					//string = string.replaceAll("\\d", "");
					v_processText =string;
				}
			}
           System.out.println("v_processText  "+string);
		}

		return v_processText;
	}
	/*public static void main(String[] args) {
		RegexStopperRemoval rr = new RegexStopperRemoval();
		System.out.println(rr.stopperremoval("\"2010: Odyssey Two is a 1982 science fiction novel by Arthur C. Clarke. It is the sequel to the 1968 novel 2001: A Space Odyssey, but;"));

	}*/
}

