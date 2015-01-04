package com.ak.untrue.util;

import java.util.Deque;
import java.util.LinkedList;

import com.ak.untrue.AbstractSyntaxTree;
import com.ak.untrue.Expression;
import com.ak.untrue.Expression.Type;
/**
 * Utility class to translate string based AST to expression graph with additional metadata:
 * operation type, arity, etc.
 * @author artemonster
 */
public final class Translator {
	private Translator() {}
	
	private static Type matchType(String symbol) {
		if (symbol.matches("[0-9]+")) {
			return Type.LITERAL_NUM;
		} else if (symbol.matches("#f|#t")){
			return Type.LITERAL_BOOL;
		} else if (symbol.matches("\\*|\\+|-|/|and|or|xor|not|=|<|>")){
			return Type.ALU_OP;
		} else {
			return Type.SYMBOL;
		}
	}
	
	private static int matchArity(String symbol, Type type) {
		if (type.equals(Type.ALU_OP)) {
			if (symbol.matches("\\*|\\+|-|/|and|or|xor|=|<|>")) {
				return 2;
			} else if (symbol.matches("not")) {
				return 1;
			} else {
				return 999; //TODO: log unknown alu op
			}
		} else if (type.equals(Type.SYMBOL))  {
			return -1; //do not check arity of symbols at this point
		}else {
			return 0; //literals have arity 0!
		}	
	}	
	
	private static Expression compileExpression(AbstractSyntaxTree<String> ast, int arity) {
		if (ast.getVal() == null) { //top-entry is a list, again. curry?
			//TODO: figure out how to curry things.
			return null;
		} else { //its a symbol!
			String symbol = ast.getVal();
			Type type = matchType(symbol); //match symbol to get a type
			if (matchArity(symbol, type) >= 0) {
				assert matchArity(symbol, type)  == arity : "Arity mismatch in "+ast.getVal();
			}
			return new Expression(symbol, arity, type);
		}			
	}
	/**
	 * Translates string abstract syntax tree to an expression graph, also setting proper
	 * "applicative" orders, matching expression types, etc.
	 * @param ast to be compiled
	 * @return Expression worker
	 */
	public static Expression compileStringAST(AbstractSyntaxTree<String> ast) {
		if (ast.getVal() == null) { //top-entry is a list, treat first element as a funcall
			Deque<AbstractSyntaxTree<String>> args = new LinkedList<>();
			args.addAll(ast.getNodes());
			AbstractSyntaxTree<String> sExpr = args.removeFirst();
			Expression funcall = compileExpression(sExpr, args.size());
			while (!args.isEmpty()) {
				AbstractSyntaxTree<String> arg = args.removeFirst();
				funcall.addChildLinkParent(compileStringAST(arg));
			}
			return funcall;			
		} else { //its a symbol in arg, with no args
			assert ast.getNodes().size() == 0 : "Argument "+ast.getVal()+" has nodes!"; //sanity check
			String symbol = ast.getVal();
			Type type = matchType(symbol); //match symbol to get a type
			return new Expression(symbol, 0, type);
		}	
	}
}
