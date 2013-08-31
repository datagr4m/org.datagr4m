package org.datagr4m.utils;

public interface IParserHelper {	
	static String space = "\\s+";
	static String space_or_not = "\\s*";
    static String spnt = space_or_not;
	static String non_space = "\\S+";
	static String any = ".*";
	static String no_greedy_any = ".*?";
	static String word = "\\w+";
    static String word_list = "[\\w|,]+";
	static String number = "\\d+";
	static String antislash = "\\\\";
	static String slash = "\\/";
    static String path = "[\\w|\\d|\\-|\\/\\\\|\\.]+";
	static String start_with = "^";
    static String end_with = "$";
}
