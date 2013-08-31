package org.datagr4m.utils;

public class StringUtils {
	public static String blanks(int length){
		String b = "";
		for (int i = 0; i < length; i++)
			b += " ";
		return b;
	}
	
	public static String commonPrefix(String st1, String st2){
        int max = Math.min(st1.length(), st2.length());
        
        int k = -1;
        for (int i = 0; i < max; i++) {
            if(st1.charAt(i)==st2.charAt(i))
                k=i;
            else
                break;
        }
        
        if(k==-1)
            return null;
        else
            return st1.substring(0, k);
    }
}
