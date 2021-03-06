package com.ak.untrue;

import java.util.Deque;
import java.util.LinkedList;

import com.ak.untrue.Expression.Type;
import com.ak.untrue.reducers.BetaReducer;
import com.ak.untrue.reducers.DeltaReducer;
import com.ak.untrue.util.Parser;
import com.ak.untrue.util.Translator;
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
	
	private static int lastEvalTicks = 0;
	private static int lastEvalMaxDeltas = 0;
	private static int lastEvalMaxBetas = 0;
	private static int lastEvalMaxWorkerLen = 0;
	
	public static String evaluate(String in) {
		return evaluate(Parser.parseString(in));
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

	public static String evaluate(AbstractSyntaxTree<String> ast) {
		lastEvalTicks = 0;
		lastEvalMaxWorkerLen = 0;
		lastEvalMaxDeltas = 0;
		lastEvalMaxBetas = 0;
		Deque<Expression> deltaRedexes = new LinkedList<>(); 
		Deque<Expression> betaRedexes = new LinkedList<>();
		Expression worker = Translator.compileStringAST(ast);
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
				Expression toRewrite = betaRedexes.removeFirst();
				Expression result = BetaReducer.getNew(toRewrite);
				toRewrite.rewrite(result);
			}
			worker = worker.returnUpdated();
			pushReadyToQueues(deltaRedexes, betaRedexes, worker);
			lastEvalTicks++;
		}
		return worker.toString();
	}

	public static int getTotalTicks() {
		return lastEvalTicks;
	}
}
