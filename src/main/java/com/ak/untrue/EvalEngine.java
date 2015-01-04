package com.ak.untrue;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.ak.untrue.Expression.Type;
import com.ak.untrue.reducers.BetaReducer;
import com.ak.untrue.reducers.DeltaReducer;
/**
 * In this version the evaluator is unbounded. Every tick all nodes that are ready to be rewritten
 * are pushed to the queues (for beta and delta redexes) and both queues are emptied in 1 tick, thus
 * simulating totally unbounded parallel machine. After that everything that is ready to be rewritten
 * is marked again and the process repeats itself, until nothing can be rewritten.
 * 
 * In future versions the worker queues (they represent the amount of memory ports and available ALU
 * units in the virtual processor) can be bounded.
 * 
 * After each evaluation the statistical counters are renewed.
 * @author artemonster
 */
public final class EvalEngine {
	private EvalEngine() {};
	//private static Map<String, Expression> lookup;
	
	private static int lastEvalTicks = 0;
	private static int lastEvalMaxDeltas = 0;
	private static int lastEvalMaxBetas = 0;
	private static int lastEvalMaxWorkerLen = 0;
	
	public static String evaluate(String in) {
		return evaluate(Parser.parseString(in));
	}
	public static void updateEnv(String in) {
		
	}
	private static void pushReadyToQueues(Deque<Expression> deltaRedexes,
			Deque<Expression> betaRedexes, 
			Expression worker) {
		int thisWorkerLength = 0;
		for(Expression exp : worker) {
			if (exp.isReady()) {
				Type expType = exp.getType();
				if (expType.equals(Type.ALU_OP)) {
					deltaRedexes.push(exp);
				} else if (expType.equals(Type.SYMBOL)) {
					betaRedexes.push(exp);
				}
			}
			thisWorkerLength++;
		}	
		lastEvalMaxWorkerLen = (lastEvalMaxWorkerLen > thisWorkerLength) ? lastEvalMaxWorkerLen : thisWorkerLength;
	}
	
	private static Type matchType(String symbol) {
		if (symbol.matches("[0-9]+")) {
			return Type.LITERAL_NUM;
		} else if (symbol.matches("#f|#t")){
			return Type.LITERAL_BOOL;
		} else if (symbol.matches("\\*|\\+|-|/|and|or|xor|not")){
			return Type.ALU_OP;
		} else {
			return Type.SYMBOL;
		}
	}
	
	private static int matchArity(String symbol, Type type) {
		if (type.equals(Type.ALU_OP)) {
			if (symbol.matches("\\*|\\+|-|/|and|or|xor")) {
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
	
	public static Expression compileExpression(AbstractSyntaxTree<String> ast, int arity) {
		if (ast.getVal() == null) { //top-entry is a list, again. curry?
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
	
	public static String evaluate(AbstractSyntaxTree<String> ast) {
		lastEvalTicks = 0;
		lastEvalMaxWorkerLen = 0;
		lastEvalMaxDeltas = 0;
		lastEvalMaxBetas = 0;
		Deque<Expression> deltaRedexes = new LinkedList<>(); 
		Deque<Expression> betaRedexes = new LinkedList<>();
		Expression worker = compileStringAST(ast);
		pushReadyToQueues(deltaRedexes, betaRedexes, worker);
		while (!deltaRedexes.isEmpty() || !betaRedexes.isEmpty()) {
			lastEvalMaxDeltas = (lastEvalMaxDeltas > deltaRedexes.size()) ? lastEvalMaxDeltas : deltaRedexes.size();
			lastEvalMaxBetas = (lastEvalMaxBetas > betaRedexes.size()) ? lastEvalMaxBetas : betaRedexes.size();
			while (!deltaRedexes.isEmpty()) {
				Expression toRewrite = deltaRedexes.removeFirst();
				Expression result = DeltaReducer.getNew(toRewrite);
				toRewrite.rewrite(result);
			}
			while (!betaRedexes.isEmpty()) {
				Expression toRewrite = deltaRedexes.removeFirst();
				Expression result = BetaReducer.getNew(toRewrite);
				toRewrite.rewrite(result);
			}
			pushReadyToQueues(deltaRedexes, betaRedexes, worker);
			lastEvalTicks++;
		}
		return worker.prettyPrint();
	}

	public static int getTotalTicks() {
		return lastEvalTicks;
	}
}
