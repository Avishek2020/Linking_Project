package org.upb.music.artist.similarity.measures;

import java.util.ArrayList;
import java.util.List;

public class RegexStopperRemoval {
	
	public  String stopperremoval(String text) 
	{
		String v_text ;
		String v_processText=null;
		
		v_text = text.toLowerCase();
		String stopWords[]= {"are","you","how","it",":","is","a","by","e","the","but","to"};
		String[] splitString = (v_text.split("\\s+"));
		System.out.println(splitString.length);//  
		
		for (String string : splitString) 
		{
			System.out.println("string 1"+string);
			for(int i =0;i<stopWords.length;i++) 
			{
				 System.out.println("stopWords[i] -"+stopWords[i]);
				if(!string.equals(stopWords[i]))
				{
					string = string.replaceAll(stopWords[i], "");
					//string = string.replaceAll("\\d", "");
					
					v_processText =string;
					System.out.println("inner loopstring  --"+string);
				}
			}
           System.out.println("v_processText  "+string);
		}

		return v_processText;
	}
	public static void main(String[] args) {
		RegexStopperRemoval rr = new RegexStopperRemoval();
		System.out.println(rr.stopperremoval("2010 hello how are you."));

	}
}

