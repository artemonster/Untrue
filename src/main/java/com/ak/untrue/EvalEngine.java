package com.ak.untrue;

import java.util.Deque;
import java.util.LinkedList;

import com.ak.untrue.Expression.Type;
/**
 * In this version the evaluator is unbounded. Every tick all nodes that are ready to be rewritten
 * are pushed to the queues (for beta and delta redexes) and both queues are emptied in 1 tick, thus
 * simulating totally unbounded parallel machine. After that everything that is ready to be rewritten
 * is marken again and the process repeats itself, until nothing can be rewriten.
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
	private static void pushReadyToQueues(Deque<AbstractSyntaxTree<Expression>> deltaRedexes,
			Deque<AbstractSyntaxTree<Expression>> betaRedexes, 
			AbstractSyntaxTree<Expression> worker) {
		int thisWorkerLength = 0;
		for(AbstractSyntaxTree<Expression> expr : worker) {
			Expression exp = expr.getVal();
			if (exp.isReady()) {
				Type expType = exp.getType();
				if (expType.equals(Type.ALU_OP)) {
					deltaRedexes.push(expr);
				} else if (expType.equals(Type.SYMBOL)) {
					betaRedexes.push(expr);
				}
			}
			thisWorkerLength++;
		}	
		lastEvalMaxWorkerLen = (lastEvalMaxWorkerLen > thisWorkerLength) ? lastEvalMaxWorkerLen : thisWorkerLength;
	}
	
	public static String evaluate(AbstractSyntaxTree<String> ast) {
		lastEvalTicks = 0;
		lastEvalMaxWorkerLen = 0;
		lastEvalMaxDeltas = 0;
		lastEvalMaxBetas = 0;
		Deque<AbstractSyntaxTree<Expression>> deltaRedexes = new LinkedList<>(); 
		Deque<AbstractSyntaxTree<Expression>> betaRedexes = new LinkedList<>();
		AbstractSyntaxTree<Expression> worker = new AbstractSyntaxTree<>();
		
		//TODO: transpose String ast to Expression ast
		
		pushReadyToQueues(deltaRedexes, betaRedexes, worker);
		while (!deltaRedexes.isEmpty() || !betaRedexes.isEmpty()) {
			lastEvalMaxDeltas = (lastEvalMaxDeltas > deltaRedexes.size()) ? lastEvalMaxDeltas : deltaRedexes.size();
			lastEvalMaxBetas = (lastEvalMaxBetas > betaRedexes.size()) ? lastEvalMaxBetas : betaRedexes.size();
			while (!deltaRedexes.isEmpty()) {
				deltaRedexes.removeFirst().getVal().rewrite();
			}
			while (!betaRedexes.isEmpty()) {
				betaRedexes.removeFirst().getVal().rewrite();
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
