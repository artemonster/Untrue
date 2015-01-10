package com.ak.untrue.reducers;

import java.util.Deque;

import com.ak.untrue.Expression;
import com.ak.untrue.Expression.Type;

public class BetaReducer {
	public static Expression getNew(Expression toRewrite) {
		//sanity check
		assert toRewrite.getType() == Type.SYMBOL : "WUT?";
		String operation = toRewrite.getValue();
		Deque<Expression> args = toRewrite.getArgs();
		switch(operation) {
			case "hd": {
				if (args.isEmpty()) {
					return new Expression("", 0, Type.NIL);
				}
				Expression newAp = args.pop();
				while (!newAp.getArgs().isEmpty()) {
					newAp.getArgs().pop();
				}
				while (!args.isEmpty()) {
					newAp.getArgs().addLast(args.pop());
				}
				return newAp;
			}			
			case "tl": {
				if (args.isEmpty()) {
					return new Expression("", 0, Type.NIL);
				}
				Expression listhead = args.pop();
				if (listhead.getArgs().isEmpty()) {
					return new Expression("", 0, Type.NIL);
				}
				Expression newAp = listhead.getArgs().pop();
				while (!listhead.getArgs().isEmpty()) {
					newAp.addChildLinkParent(listhead.getArgs().pop());
				}
				return newAp;
			}		
			case "if": {
				Expression predicate = args.removeFirst();
				Expression forTrue = args.removeFirst();
				Expression forFalse = args.removeFirst();
				if (predicate.getValue().equals("#t")) {
					return forTrue;
				} else if (predicate.getValue().equals("#f")) {
					return forFalse;
				} else {
					System.out.println("Predicate has not returned a boolean!");
					return null;
				}
			}
			case "define": {
				Expression symbolDef = args.removeFirst(); //this is a list (funName arg1...argN)
				Expression body = args.removeFirst();
				toRewrite.addToEnv(symbolDef.getValue(), body);
				return new Expression("#t",0,Type.LITERAL_BOOL);
			}
			default: {
				Expression newBody = toRewrite.find(toRewrite.getValue());
				if (newBody != null) {
					//substitute args
				}
				return newBody;
			}
		}
	}
}
