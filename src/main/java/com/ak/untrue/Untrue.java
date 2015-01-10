package com.ak.untrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Untrue {
    public static void main( String[] args ) throws IOException {
    	boolean doExit = false;
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	String input;
    	while (!doExit) {
			System.out.print(">> ");
			input = br.readLine();
			switch(input) {
				case "e": {
					doExit=true; 
					System.out.println("Exiting REPL"); 
					break;
				}
				default: {
					String result = EvalEngine.evaluate(input);
					System.out.println(result);
				}
			}
    	}
    	br.close();
    }
}
