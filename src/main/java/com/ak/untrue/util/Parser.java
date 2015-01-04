package com.ak.untrue.util;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import com.ak.untrue.AbstractSyntaxTree;
/**
 * Utility class to parse strings to AST of strings.
 * @author artemonster
 */
public final class Parser {
	private Parser() {}
	
	/**
	 * An input string is parsed by dropping comments, stripping newlines and tokenizing lists
	 * by adding spaces around parentheses.
	 * @param in string to be parsed
	 * @return AST representation of the program
	 */
	public static AbstractSyntaxTree<String> parseString(String in) {
		String formatted = in
				.replaceAll(";.*\\n", "") //drop everything between ; and \n
				.replace("(", " ( ")
				.replace(")", " ) ")
				.replaceAll("\\n", "");
		Deque<String> tokens = new LinkedList<>(Arrays.asList(formatted.split("\\s+", 0)));
		if (tokens.getFirst().equals("")) {
			tokens.removeFirst();//drop an empty leading substring produced by split
		}
		return parseTokens(tokens);
	}
	
	private static AbstractSyntaxTree<String> parseTokens(Deque<String> tokens) {
		while (!tokens.isEmpty()) {
			String token = tokens.removeFirst();
			if (token.equals("(")) {		
				AbstractSyntaxTree<String> newlist = new AbstractSyntaxTree<String>();
				while (!tokens.isEmpty() && !tokens.getFirst().equals(")")) {
					newlist.add(parseTokens(tokens));	
				}
				if (tokens.isEmpty()) {
					System.out.println("Error: Where is a fucking ')'?");
				}
				tokens.removeFirst();
				return newlist;
			} else if (token.equals(")")) {
				System.out.println("Unexpected ')'");
				return null; 
			} else {
				return new AbstractSyntaxTree<String>(token);
			}
		}
		return null;
	}
}
