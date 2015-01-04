package com.ak.untrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Everything that can be evaluated (reduced) - will be evaluated. If function has arity 0 -> eval.
 * If function has arity 1 - it is not evaluated until argument is there (useful for sequencing).
 * If fn has arity >1 - it is evaluated as soon as at least 1 argument is present.
 * 
 * (((lambda x (lambda y (x y))) 2) 3) _> (2 3)
 * The rule of thumb is that the outermost .\ goes with the innermost argument. 
 * 
 * (dist fast_sq(5) fact(11))
 * (dist 25 11*fact(10)) -> (sqrt(+ (* 11*10*fact(9) _thunk)) (* 25 25)))
 * 
 * (map (+ fact(150)) (1 2 3 4 5))
 * 
 * (define repeat (lambda (f) (lambda (x) (f (f x)))))
 * (define twice (lambda (x) (* 2 x)))
 * ((repeat twice) 10)
 * 
 * Expression:
 * 	list of addresses to rewrite a thunk (relative?)
 *	arity (number of required args)
 *	
 *(define (factorial n)
 (f-aux n 1))
(define (f-aux n a)
 (if (= n 0)
     a        ; tail-recursive
     (f-aux (- n 1) (* n a))))
 */
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
					AbstractSyntaxTree<String> ast = Parser.parseString(input);
					System.out.println("Echoing " + ast.prettyPrint());
				}
			}
    	}
    	br.close();
    }
}
